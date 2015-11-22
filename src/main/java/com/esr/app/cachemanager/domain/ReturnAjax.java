package com.esr.app.cachemanager.domain;

import java.util.List;

/**
 * Object used to Ajax Pagination
 * @author Enrique Sanchez
 *
 */
public class ReturnAjax {
	private Integer draw;
	private Integer recordsTotal;
	private Integer recordsFiltered;
	private List data;
	
	public Integer getDraw() {
		return draw;
	}
	public void setDraw(Integer draw) {
		this.draw = draw;
	}
	public Integer getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public Integer getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	public List getData() {
		return data;
	}
	public void setData(List data) {
		this.data = data;
	}
}
