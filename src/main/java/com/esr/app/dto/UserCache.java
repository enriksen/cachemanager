package com.esr.app.dto;

import java.io.Serializable;

/**
 * User Object to store in cache
 * @author Enrique Sanchez
 *
 */
public class UserCache implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4237463227013431924L;
	private String name;
	private String phone;
	private String company;
	private String iban;
	
	public UserCache(String name, String phone, String company, String iban) {
		super();
		this.name = name;
		this.phone = phone;
		this.company = company;
		this.iban = iban;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
		
}
