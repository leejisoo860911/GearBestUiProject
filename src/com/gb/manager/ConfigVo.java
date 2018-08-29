package com.gb.manager;

import java.util.ArrayList;
import java.util.List;

public class ConfigVo {
	
	private String title;
	
	private Boolean hasCategory;
	
	private Integer day;
	
	private List<String> list;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Boolean isHasCategory() {
		return hasCategory;
	}
	public void setHasCategory(boolean hasCategory) {
		this.hasCategory = hasCategory;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	
	public String getListString() {
		if(list==null || list.size()==0) return "";
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < list.size() ; i++) {
			if(i == list.size()-1) {
				sb.append(list.get(i).trim());
			}
			else {
				sb.append(list.get(i).trim()).append(",");
			}
		}
		return sb.toString();
	}
	
	public void setListString(String str) {
		if(str == null || "".equals(str.trim())) {
			return;
		}
		
		if(list == null) {
			list = new ArrayList<String>();
		}
		
		list.clear();
		String[] arr = str.split(",");
		for(int i = 0 ; i < arr.length ; i++) {
			list.add(arr[i].trim());
		}
		
	}
	
	@Override
	public String toString() {
		return title + "@" + hasCategory + "@" + day + "@" +  getListString();
	}
}
