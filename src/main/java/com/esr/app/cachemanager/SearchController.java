package com.esr.app.cachemanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.esr.app.cachemanager.domain.Response;
import com.esr.app.cachemanager.domain.ReturnAjax;
import com.esr.app.cachemanager.form.SearchForm;
import com.esr.app.dto.UserCache;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.SqlPredicate;

/**
 * Controller to search Users info in cache 
 * @author Enrique Sanchez
 *
 */
@Controller
public class SearchController {

	private static final Logger LOGGER = Logger.getLogger(SearchController.class);
	private static String USERS_MAP = "users";
	
	/**
	 * Hazelcast client instance
	 */
	static HazelcastInstance client;

	/**
	 * Show Cache Search Form
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String searchForm(Model model) {
		LOGGER.info("Show search form...");
		model.addAttribute("searchform", new SearchForm());
		return "search";
	}

	/**
	 * Get search form's info and show result page
	 * @param session
	 * @param form SearchForm
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String searchSubmit(HttpSession session, @ModelAttribute SearchForm form, Model model) {
		LOGGER.info("Submit search...");
		long start = System.nanoTime();

		Collection<UserCache> users = null;

		//Get Cache instance
		HazelcastInstance client = getHazelcastInstance();
		IMap<Integer, UserCache> mapUsers = client.getMap(USERS_MAP);

		String query = composeQuery(form);

		//Filter elements in cache
		if (query != null && !"".equals(query)) {
			users = mapUsers.values(new SqlPredicate(query));
		} else {
			users = mapUsers.values();
		}

		//Store information in session to avoid further querys during pagination
		session.setAttribute("users", users);

		long end = System.nanoTime();
		model.addAttribute("time", (end - start) / 1000000 + "ms");
		model.addAttribute("cachesize", mapUsers.size());
		model.addAttribute("searchform", form);
		
		LOGGER.info("Show results...");
		return "result";
	}
	
	/**
	 * Method to response Ajax request from Result's page
	 * @param session
	 * @param request
	 * @return JSON data
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/pagination", method = RequestMethod.GET)
	public @ResponseBody ReturnAjax pagination(HttpSession session, HttpServletRequest request) {

		LOGGER.info("Paginating results ...");
		String draw = request.getParameter("draw");

		int start = Integer.parseInt(request.getParameter("start"));
		int length = Integer.parseInt(request.getParameter("length"));
		Collection<UserCache> users;
		List<UserCache> uC = new ArrayList<UserCache>();
		Object[] u = new Object[0];
		
		try{
			//Get data from Session
			users = (Collection<UserCache>) session.getAttribute("users");
	
			if (users != null) {
				u = users.toArray();
	
				int from = start;
				int to = start + length;
	
				for (int i = from; i < to && i < u.length; i++) {
					uC.add((UserCache) u[i]);
				}
			}
		
		}catch(Exception e ){
			LOGGER.error("Error getting users from session ...", e);
		}
		
		ReturnAjax r = new ReturnAjax();
		if (draw != null)
			r.setDraw(Integer.parseInt(draw));
		r.setRecordsFiltered(u.length);
		r.setRecordsTotal(u.length);
		r.setData(uC);

		return r;
	}

	

	/**
	 * Create HazelCast SQL query to search info 
	 * @param form SearchForm
	 * @return String
	 */
	private String composeQuery(SearchForm form) {
		String query = "";
		boolean first = true;
		if (form.getName() != null && !"".equals(form.getName())) {
			query += " name ILIKE  '%" + form.getName() + "%'";
			first = false;
		}
		if (form.getCompany() != null && !"".equals(form.getCompany())) {
			if (!first)
				query += " AND ";
			query += " company ILIKE  '%" + form.getCompany() + "%'";
			first = false;
		}
		if (form.getPhone() != null && !"".equals(form.getPhone())) {
			if (!first)
				query += " AND ";
			query += " phone ILIKE  '%" + form.getPhone() + "%'";
			first = false;
		}
		if (form.getIban() != null && !"".equals(form.getIban())) {
			if (!first)
				query += " AND ";
			query += " iban ILIKE  '%" + form.getIban() + "%'";
		}

		return query;
	}



	
	/**
	 * REST method to get search's requests and response JSON data 
	 * @param name Name filter
	 * @param phone Phone filter
	 * @param company Company filter
	 * @param iban IBAN filter
	 * 
	 * @return JSON Response
	 */
	@RequestMapping(value="/search/users", produces={"application/json"})
	public @ResponseBody Response searchUsers(@RequestParam(value="name", required=false) String name, @RequestParam(value="phone", required=false) String phone, @RequestParam(value="company", required=false) String company, @RequestParam(value="iban", required=false) String iban){

		LOGGER.info("Get Users Request  ...");
		long start = System.nanoTime();

		Collection<UserCache> users = null;
		Response response=null;
		try{
			HazelcastInstance client = getHazelcastInstance();
			IMap<Integer, UserCache> mapUsers = client.getMap("users");
	
			SearchForm f=new SearchForm(name, phone, company, iban);
			
			String query = composeQuery(f);
	
			//Filter elements in cache
			if (query != null && !"".equals(query)) {
				users = mapUsers.values(new SqlPredicate(query));
			} else {
				users = mapUsers.values();
			}
	
	
			long end = System.nanoTime();
			
			response=new Response(Response.OK, (end - start) / 1000000 + "ms","Num. registers: "+users.size());
			response.setData(new ArrayList<UserCache>(users));
		} catch (Exception e) {
			LOGGER.error("Fail get users from cache ...", e);
			response = new Response(Response.ERROR, null, e.getStackTrace().toString());
		}

		return response;
	}
	
	/**
	 * Get Hazelcast Instance
	 * @return HazelcastInstance
	 */
	public static HazelcastInstance getHazelcastInstance() {
		if (client == null) {
			ClientConfig clientConfig = new ClientConfig();
			client = HazelcastClient.newHazelcastClient(clientConfig);
		}

		return client;
	}

}
