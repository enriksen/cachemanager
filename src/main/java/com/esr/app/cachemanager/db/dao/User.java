package com.esr.app.cachemanager.db.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * User POJO
 * @author Enrique Sanchez
 *
 */
@Entity
public class User {

	
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Id
    private int id;
    public User(String name, String phone, String company, String iban) {
		super();
		this.name = name;
		this.phone = phone;
		this.company = company;
		this.iban = iban;
	}

	private String name;
    private String phone;
    private String company;
    private String iban;
    
    protected User() {}
    
    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, name='%s', phone='%s', company='%s', iban='%s']",
                id, name, phone, company, iban);
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
