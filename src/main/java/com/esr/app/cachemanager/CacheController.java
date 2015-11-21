package com.esr.app.cachemanager;

import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.esr.app.cachemanager.domain.Response;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.HazelcastInstanceFactory;

/**
 * Controller with operations to admin cache instances
 * @author Enrique Sanchez
 *
 */
@RestController
public class CacheController {

	private static final Logger LOGGER = Logger.getLogger(CacheController.class);
	
	/**
	 * Create N cache's nodes (default:1)
	 * @param n Number of cache nodes to create
	 * 
	 * @return JSON Operation info
	 */
	@RequestMapping(value = "/cache/add", produces = { "application/json" })
	public @ResponseBody Response add(@RequestParam(value = "n", required = false) Integer n) {

		
		LOGGER.info("Adding cache nodes...");
		
		Response response;
		try {

			if (n == null) {
				n = 1;
			}
			
			long startTime = System.nanoTime();
			Config cfg = new Config();
			StringBuilder sb = new StringBuilder("Cache nodes created: ");
			
			for (int i = 0; i < n; i++) {
				HazelcastInstance instance = HazelcastInstanceFactory.newHazelcastInstance(cfg);
				sb.append(instance.getLocalEndpoint().toString()).append(" ");
			}
			long endTime = System.nanoTime();
			response = new Response(Response.OK, (endTime - startTime) / 1000000 + "ms", sb.toString());

			LOGGER.info("Created "+n+" new cache nodes...");
		} catch (Exception e) {
			LOGGER.error("Fail creating cache nodes ...", e);
			response = new Response(Response.ERROR, null, e.getStackTrace().toString());
		}

		return response;
	}
	
	/**
	 * Remove a cache cluster node
	 * @return JSON Operation info
	 */
	@RequestMapping(value = "/cache/remove", produces = { "application/json" })
	public @ResponseBody Response remove() {

		LOGGER.info("Removing cache...");
		Response response;
		try {
			long startTime = System.nanoTime();
			Iterator<HazelcastInstance> it = HazelcastInstanceFactory.getAllHazelcastInstances().iterator();
			String cacheName = null;
			if (it.hasNext()) {
				HazelcastInstance instance = it.next();
				cacheName = instance.getLocalEndpoint().toString();
				instance.shutdown();
			}

			long endTime = System.nanoTime();
			response = new Response(Response.OK, (endTime - startTime) / 1000000 + "ms", "Cache " + cacheName + " deleted...");
			LOGGER.info("Remove cache"+ cacheName);
		} catch (Exception e) {
			LOGGER.error("Fail removing cache ...", e);
			response = new Response(Response.ERROR, null, e.getStackTrace().toString());
		}

		return response;
	}
	
	/**
	 * Shutdown all cache nodes
	 * @return JSON Operation info
	 */
	@RequestMapping(value = "/cache/shutdown", produces = { "application/json" })
	public @ResponseBody Response shutdown() {

		LOGGER.info("Shuttingdown all cache nodes...");
		Response response;
		try {
			long startTime = System.nanoTime();
			Iterator<HazelcastInstance> it = HazelcastInstanceFactory.getAllHazelcastInstances().iterator();
			StringBuilder sb= new StringBuilder("Deleted caches: ");
			while (it.hasNext()) {
				HazelcastInstance instance = it.next();
				sb.append(instance.getLocalEndpoint().toString()).append(" ");
				instance.shutdown();
			}

			long endTime = System.nanoTime();
			response = new Response(Response.OK, (endTime - startTime) / 1000000 + "ms", sb.toString());
			LOGGER.info("Shutdown all cache nodes...");
		} catch (Exception e) {
			LOGGER.error("Fail shuttingdown all cache nodes...", e);
			response = new Response(Response.ERROR, null, e.getStackTrace().toString());
		}

		return response;
	}

	/**
	 * Return number of cache nodes
	 * @return JSON Operation info
	 */
	@RequestMapping(value = "/cache/status", produces = { "application/json" })
	public @ResponseBody Response status() {

		LOGGER.info("Checking cache status ...");
		Response response;
		try {

			long startTime = System.nanoTime();

			StringBuilder sb = new StringBuilder("Active Caches:");

			Set<HazelcastInstance> instances = HazelcastInstanceFactory.getAllHazelcastInstances();
			for (HazelcastInstance hI : instances) {
				sb.append(hI.getLocalEndpoint().toString()).append(" ");
			}

			long endTime = System.nanoTime();
			response = new Response(Response.OK, (endTime - startTime) / 1000000 + "ms", sb.toString());

		} catch (Exception e) {
			LOGGER.error("Fail checking cache status ...", e);
			response = new Response(Response.ERROR, null, e.getStackTrace().toString());
		}

		return response;
	}

}
