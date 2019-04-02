package com.Example.model.Entity;

import java.sql.Timestamp;

public class Test {
	private int id;
	private String title;
	private String description;
	private Timestamp add_datetime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getAdd_datetime() {
		return add_datetime;
	}
	public void setAdd_datetime(Timestamp add_datetime) {
		this.add_datetime = add_datetime;
	}
	
	
}
