package com.esr.app.cachemanager;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.esr.app.cachemanager.db.UserRepository;
import com.esr.app.cachemanager.db.dao.User;
import com.esr.app.cachemanager.domain.Response;
import com.esr.app.dto.UserCache;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

/**
 * Controller used to load info at Cache
 * 
 * @author Enrique Sanchez
 *
 */
@RestController
public class LoaderController {

	private static final Logger LOGGER = Logger.getLogger(LoaderController.class);

	private static String USERS_MAP = "users";
	
	/**
	 * DataBase User Repository
	 */
	@Autowired
	UserRepository userRep;

	/**
	 * Load all data from DataBase to Cache. Divide the work in N threads
	 * (default:5).
	 * 
	 * @param t Number of threads to use
	 * 
	 * @return JSON Operation's info
	 */
	@RequestMapping(value = "/loader/run", produces = { "application/json" })
	public @ResponseBody Response run(@RequestParam(value = "t", required = false) Integer t) {

		LOGGER.info("Starting to load info to cache...");

		Response response;
		try {
			if (t == null || t == 0) {
				// Default 5 threads
				t = 5;
			}

			long startTime = System.nanoTime();
			ExecutorService executor = Executors.newCachedThreadPool();

			long numRows = userRep.count();
			LOGGER.info("Total rows in database: " + numRows);

			int pageSize = (int) Math.ceil((float) numRows / t);
			LOGGER.info("Initializing " + t + " threads...");

			//Launch SendToCacheThreads
			for (int i = 0; i < t; i++) {
				//Each thread find and send a part of DataBase
				executor.execute(new SendToCacheThread(i, pageSize));
			}

			executor.shutdown();

			//Wait until thread finish
			while (!executor.isTerminated()) {

			}

			long endTime = System.nanoTime();

			long duration = (endTime - startTime);

			response= new Response(Response.OK, duration / 1000000 + "ms", "Load " + numRows + " registers to cache...");

			LOGGER.info("Loaded " + numRows + " registers to cache...");
		} catch (Exception e) {
			LOGGER.error("Fail at loader process ...", e);
			response = new Response(Response.ERROR, null, e.getStackTrace().toString());
		}

		return response;
	}

	/**
	 * Runnable class to read database information and load cache
	 * 
	 * @author Enrique Sanchez
	 *
	 */
	public class SendToCacheThread implements Runnable {
		private int start, end;
		IMap<Integer, UserCache> map;

		public SendToCacheThread(int start, int end) {
			this.start = start;
			this.end = end;
		}

		public void run() {
			try {
				Pageable p = new PageRequest(start, end);
				Page<User> page = userRep.findAll(p);

				ClientConfig clientConfig = new ClientConfig();
				HazelcastInstance cacheClient = HazelcastClient.newHazelcastClient(clientConfig);

				IMap<Integer, UserCache> mapUsers = cacheClient.getMap(USERS_MAP);
				Iterator<User> it = page.iterator();
				while (it.hasNext()) {
					User u = it.next();
					mapUsers.putAsync(u.getId(), new UserCache(u.getName(), u.getPhone(), u.getCompany(), u.getIban()));
				}
			} catch (Exception e) {
				LOGGER.error("Fail at SendToCacheThread ...", e);
			}

		}
	}

}
