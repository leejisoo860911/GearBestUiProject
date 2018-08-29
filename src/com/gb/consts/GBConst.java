package com.gb.consts;

public class GBConst {
	public static String PATH_HOME 			= System.getProperty("HOME") == null ? "" : System.getProperty("HOME");
	public static String PATH_TEMPLATE_DIR  = PATH_HOME + "data/template";
	public static String PATH_COUPON_TEMPLATE_FILE = "blogPost.ftl";
	public static String PATH_PRODUCT_TEMPLATE_FILE = "productInfo.ftl";
	public static String PATH_EXCEL_TEMPLATE_FILE = "excelInfo.ftl";
	
	public static String PATH_PROPERTIES 	= PATH_HOME + "data/config/setting.properties";
	public static String PATH_LOG_HOME		= PATH_HOME + "log/";  
	
	public static String ICON_SETTING 		= PATH_HOME + "data/icon/icon_setting.png";
	public static String ICON_SEARCH 		= PATH_HOME + "data/icon/icon_search.png";
	
	/* 최정호 */
	/*
	public static String API_KEY		= "1355014";
	public static String API_SECRETE	= "mOGgVvqrIGe";
	public static String API_LKID		= "12231305";
	public static String IMG_WIDTH		= "150px";
	public static String SHOURT_URL_LOGIN_KEY = "o_5dss30jebk";
	public static String SHOURT_URL_API_KEY = "R_3f4ec5a3eaa84ceca1c8ab7d63e36f5b";
	*/
	
	// 이지수
	public static String API_KEY		= "1431122";
	public static String API_SECRETE	= "OBayMpehkNE";
	public static String API_LKID		= "12088421";
	public static String IMG_WIDTH		= "200px";
	public static String SHOURT_URL_LOGIN_KEY = "o_361o0ek9rn";
	public static String SHOURT_URL_API_KEY = "R_653a167e53554031be200993e5ae41a3";
	
	public static String[] WID_BOLCK 	= new String[]{"wid=81", "wid=89", "wid=91", "wid=94", "wid=95", "wid=135", "wid=137"};

	/**
	 * API URL
	 */
	public static String URL_PROMOTION_PRODUCT = "https://affiliate.gearbest.com/api/products/list-promotion-products"; 
	public static String URL_EVENT_CREATIVE	 = "https://affiliate.gearbest.com/api/banner/list-event-creative";
	public static String URL_PRODUCT_CREATIVE	 = "https://affiliate.gearbest.com/api/promotions/list-product-creative";
	public static String URL_COUPONS			 = "https://affiliate.gearbest.com/api/coupon/list-coupons";
	
	/**
	 * Common GET Parameters
	 */
	public static String PARAM_APIKEY	= "api_key";
	public static String PARAM_TIME		= "time";
	public static String PARAM_LKID		= "lkid";
	public static String PARAM_SIGN		= "sign";
	
	/**
	 * Each GET Parameters
	 */
	public static String PARAM_TYPE 		= "type";
	public static String PARAM_CATEGORY	= "category";
	public static String PARAM_PAGE		= "page";
	public static String PARAM_CURRENCY	= "currency";
	public static String PARAM_LANGUAGE	= "language";
	public static String PARAM_SIZE		= "size";
	
	public static String META_OG_TITLE	= "<meta property=\"og:title\" content=\"";
	public static String META_OG_DESC	= "<meta property=\"og:description\" content=\"";
	public static String META_OG_IMG	= "<meta property=\"og:image\" content=\"";
	
	public static double USD_CURRENCY		= 1.24;
}
