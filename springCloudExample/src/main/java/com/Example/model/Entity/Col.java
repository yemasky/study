package com.Example.model.Entity;

import java.util.List;

import org.bson.types.ObjectId;

public class Col {
	private ObjectId _id = null;
	private String title;
	private String description;
	private String by;
	private String url;
	private List<?> tags;
	private double likes;
	
	public ObjectId get_id() {
		return _id;
	}
	public void set_id(ObjectId _id) {
		this._id = _id;
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
	public String getBy() {
		return by;
	}
	public void setBy(String by) {
		this.by = by;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public List<?> getTags() {
		return tags;
	}
	public void setTags(List<?> tags) {
		this.tags = tags;
	}
	public double getLikes() {
		return likes; 
	}
	public void setLikes(double likes) {
		this.likes = likes;
	}
	
}
