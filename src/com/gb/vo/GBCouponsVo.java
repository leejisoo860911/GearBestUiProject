package com.gb.vo;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.gb.utils.HttpConnectionUtil;
import com.gb.utils.StringUtil;

/**
 * https://affiliate.gearbest.com/api/coupon/list-coupons?api_key=[APIkey]&time=[time]&language=[language]&category=[category]&page=[page]&sign=[Signature]&lkid=[lkid]
 * 
 * @author leejisoo
 *
 */
public class GBCouponsVo {
	private String productName;
	private String couponName;
	private String category;
	private String couponCode;
	private String startTime;
	private String endTime;
	private String language;
	private String couponUrl;
	private String promotionUrl;
	private String imageUrl;
	private String origPrice;
	private String salePrice;
	private String couponPrice;
	private boolean isProduct;
	private long   compareDate;

	public GBCouponsVo(LinkedHashMap<String, Object> item) {
		this.couponName = (String) item.get("coupon_name");
		this.category = (String) item.get("category");
		this.couponCode = (String) item.get("coupon_code");
		this.startTime = (String) item.get("start_time");
		this.compareDate = StringUtil.compareDate(startTime.substring(0, 10));
		if(compareDate < 0) compareDate=compareDate*-1;
		this.endTime = (String) item.get("end_time");
		this.language = (String) item.get("language");
		this.couponUrl = (String) item.get("coupon_url");
		this.promotionUrl = (String) item.get("promotion_url");
		this.imageUrl = (String) item.get("image_url");
		if(promotionUrl.indexOf("pp_") > 0) {
			isProduct = true;
		}
		else {
			isProduct = false;
		}
		
		if (promotionUrl.indexOf("www") > 0 && isProduct) {
			HashMap<String, String> map = HttpConnectionUtil.sendGetRequestHtmlMap(promotionUrl);
			productName = map.get("productName");
			origPrice = map.get("origPrice");
			couponPrice = StringUtil.getGBProductCouponPrice(couponName);
		} else {
			imageUrl = "";
			productName = couponName.toString();
		}
		
	}

	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCouponUrl() {
		return couponUrl;
	}

	public void setCouponUrl(String couponUrl) {
		this.couponUrl = couponUrl;
	}

	public String getPromotionUrl() {
		return promotionUrl;
	}

	public void setPromotionUrl(String promotionUrl) {
		this.promotionUrl = promotionUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getCouponPrice() {
		return couponPrice;
	}
	
	public void setCouponPrice(String couponPrice) {
		this.couponPrice = couponPrice;
	}
	
	public String getOrigPrice() {
		return origPrice;
	}
	
	public void setOrigPrice(String origPrice) {
		this.origPrice = origPrice;
	}
	
	public String getSalePrice() {
		return salePrice;
	}
	
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
	
	public boolean isProduct() {
		return isProduct;
	}
	
	public void setProduct(boolean isProduct) {
		this.isProduct = isProduct;
	}
	
	public long getCompareDate() {
		return compareDate;
	}
	
	public void setCompareDate(long compareDate) {
		this.compareDate = compareDate;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("productName").append(" : ").append(productName).append(System.lineSeparator());
		sb.append("couponName").append(" : ").append(couponName).append(System.lineSeparator());
		sb.append("category").append(" : ").append(category).append(System.lineSeparator());
		sb.append("couponCode").append(" : ").append(couponCode).append(System.lineSeparator());
		sb.append("startTime").append(" : ").append(startTime).append(System.lineSeparator());
		sb.append("endTime").append(" : ").append(endTime).append(System.lineSeparator());
		sb.append("compareDate").append(" : ").append(compareDate).append(System.lineSeparator());
		sb.append("language").append(" : ").append(language).append(System.lineSeparator());
		sb.append("couponUrl").append(" : ").append(couponUrl).append(System.lineSeparator());
		sb.append("promotionUrl").append(" : ").append(promotionUrl).append(System.lineSeparator());
		sb.append("imageUrl").append(" : ").append(imageUrl).append(System.lineSeparator());
		sb.append("origPrice").append(" : ").append(origPrice).append(System.lineSeparator());
		sb.append("salePrice").append(" : ").append(salePrice).append(System.lineSeparator());
		sb.append("couponPrice").append(" : ").append(couponPrice).append(System.lineSeparator());
		sb.append("isProduct").append(" : ").append(isProduct).append(System.lineSeparator());
		sb.append("************************************");
		return sb.toString();
	}
}
