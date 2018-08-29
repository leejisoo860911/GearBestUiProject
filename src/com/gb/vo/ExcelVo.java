package com.gb.vo;

import com.gb.utils.StringUtil;

public class ExcelVo {

	String url;
	String title;
	String coupon;
	String price;
	String count;
	String imgUrl;
	String strDtm;
	String endDtm;
	
	public ExcelVo(String str) {
		String []arr = str.split("\t");
		title	= arr.length >= 1 ? arr[0].trim() : "";
		url		= arr.length >= 2 ? arr[1].trim() : "";
		price   = arr.length >= 3 ? arr[2].trim() : "";
		coupon  = arr.length >= 4 ? arr[3].trim() : "";
		count   = arr.length >= 5 ? arr[4].trim() : "";
		strDtm   = arr.length >= 6 ? arr[5].trim() : "";
		endDtm   = arr.length >= 7 ? arr[6].trim() : "";
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCoupon() {
		return StringUtil.isEmpty(coupon) ? null : coupon;
	}
	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getCount() {
		return StringUtil.isEmpty(count) ? null : count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getStrDtm() {
		return strDtm;
	}
	public void setStrDtm(String strDtm) {
		this.strDtm = strDtm;
	}
	public String getEndDtm() {
		return endDtm;
	}
	public void setEndDtm(String endDtm) {
		this.endDtm = endDtm;
	}
}
