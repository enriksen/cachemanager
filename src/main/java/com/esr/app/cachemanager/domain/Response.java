package com.esr.app.cachemanager.domain;

/**
 * Object with response info.
 * <ul>
 * <li>status: OK, ERROR</li>
 * <li>time: Processing time</li>
 * <li>info: Operation's info, messages, ...</li>
 * </ul>
 * 
 * @author Enrique Sanchez
 *
 */
public class Response {

	public static String OK="OK";
	public static String ERROR="OK";
	
	private String status;
	private String time;
	private String info;
	
	public Response(String status, String time, String info) {
		super();
		this.status = status;
		this.time = time;
		this.info = info;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
}
