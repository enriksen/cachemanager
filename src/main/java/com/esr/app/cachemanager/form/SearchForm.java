package com.esr.app.cachemanager.form;

/**
 * Users Serach Form
 * @author Enrique Sanchez
 *
 */
public class SearchForm {
	
	private String name;
	private String phone;
	private String company;
	private String iban;
	
	private Integer page;
	
	public SearchForm(){
		super();
	}
	
	public SearchForm(String name, String phone, String company, String iban) {
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

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
}
