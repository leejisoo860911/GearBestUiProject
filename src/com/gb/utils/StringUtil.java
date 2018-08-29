package com.gb.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gb.consts.GBConst;

public class StringUtil {
	
	public static String[] fileNameArr = {"\\","/",":","*","?","\"","<",">","|"};
	
	public static String getMD5(String str){
		String MD5 = ""; 

		try{
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(str.getBytes()); 
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			MD5 = sb.toString();
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace(); 
			MD5 = ""; 
		}

		return MD5;
	}
	

	/**
	 * @deprecated
	 */
	public static String getGBImageUrl(String html) {
		int strIdx = html.indexOf("<span class=\"jqzoom\" id=\"js_jqzoom\">");
		if( strIdx == -1 ) {
			return "";
		}
		else {
			int endidx = html.indexOf("</span>", strIdx);
			html = html.substring(strIdx, endidx);
			Pattern nonValidPattern = Pattern.compile("<img[^>]*data-original=[\"']?([^>\"']+)[\"']?[^>]*>");
			Matcher matcher = nonValidPattern.matcher(html);
			if(matcher.find()) {
				html = matcher.group(1);
			}
		}
		return html;
	}
	
	public static String getGBProductName(String title) {
		// Men Trendy RFID Blocking PU Bifold Wallet -$6.99 Online Shopping| GearBest.com
		title = title.substring(0, title.lastIndexOf("-"));
		return title.trim();
	}

	public static String getGBProductOrigPrice(String title) {
		// Men Trendy RFID Blocking PU Bifold Wallet -$6.99 Online Shopping| GearBest.com
		title = title.substring(title.lastIndexOf("-")+1, title.indexOf(" ", title.lastIndexOf("-")));
		return title.trim();
	}

	/**
	 * @deprecated
	 */
	public static String getGBProductSalePrice(String html) {
		int strIdx = html.indexOf("ajax-price");
		if( strIdx == -1 ) {
			return "";
		}
		else {
			int endidx = html.indexOf("</", strIdx);
			html = html.substring(strIdx, endidx);
			Pattern nonValidPattern = Pattern.compile("data-orgp=[\"']?([^>\"']+)[\"']?[^>]*");
			Matcher matcher = nonValidPattern.matcher(html);
			if(matcher.find()) {
				html = matcher.group(1);
			}
		}
		return html.trim();
	}

	public static String getGBProductCouponPrice(String inStr) {
		String price = "";
		String currency = "";
		try {
			if(inStr.contains("$")) {
				currency = "$";
			} else if (inStr.contains("€")) {
				currency = "€";
//				System.out.println("[유로]" + inStr);
			}
			
			if(!StringUtil.isEmpty(currency)) {
				inStr = inStr.replace(" ", "");
				Pattern p = Pattern.compile("\\"+currency+"[0-9.]+");
				Matcher m = p.matcher(inStr);
				
				String maxPrice = "";
				String curPrice = "";
				while(m.find()) {
					if(isEmpty(maxPrice)) {
						maxPrice = m.group(0);
					} else {
						curPrice = m.group(0);
						if ( Double.valueOf(maxPrice.replace(currency, "").trim()) < Double.valueOf(curPrice.replace(currency, "").trim())) {
							maxPrice = curPrice;
						}
					}
				}
				price = maxPrice;
			}
			
			if("€".equals(currency)) {
				price = price.substring(1); 
				double dPrice = Double.parseDouble(price) * GBConst.USD_CURRENCY;
				price = "$"+String.format("%.2f", dPrice);
			}
		} catch (Exception e) {
		}
		
		if(StringUtil.isEmpty(price)) {
//			System.out.println("[값없음]" + inStr);
		}
		return price;
	}

	public static long compareDate(String start, String end) {
		long diffDays = 0;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date beginDate;
				beginDate = formatter.parse(start);
	        Date endDate = formatter.parse(end);
	         
	        // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
	        long diff = endDate.getTime() - beginDate.getTime();
	        diffDays = diff / (24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return diffDays;
	}
	
	/**
	 * 현재보다 미래가 입력되면 양수
	 * @param end
	 * @return
	 */
	public static long compareDate(String end) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        return StringUtil.compareDate(sdf.format(c1.getTime()), end);
	}
	
	/**
	 * 현재보다 미래가 입력되면 양수
	 * @param end
	 * @return
	 */
	public static String getDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        return sdf.format(c1.getTime());
	}
	
	public static boolean isEmpty(String str) {
		return str==null || "".equals(str.trim());
	}
	
	public static String nvl(String str, String alt) {
		if(isEmpty(str))
			return alt;
		else
			return str;
	}
	
	public static boolean isBlockWid(String url) {
		for(String wid : GBConst.WID_BOLCK){
			if(url.contains(wid)) {
				return true;
			}
		}
		return false;
	}

	public static String getUrl(String url) {
		if(url.contains("?")) {
			String tempUrl = url.substring(url.indexOf("?")+1);
			url = url.substring(0,url.indexOf("?"));
			String[] params = tempUrl.split("&");  
		    Map<String, String> map = new LinkedHashMap<String, String>();  
		    for(String param : params) {  
		        String name = param.split("=")[0];  
		        String value = param.split("=")[1];
		        if(!"lkid".equals(name)&&!"source".equals(name)) {
		        	map.put(name, value);  
		        }
		    }
		    
		    if(map.size() > 0){
		    	url = url + "?";
			    for(String key : map.keySet()) {
			    	url = url + key + "=" + map.get(key)+"&";
			    }
		    }
		    
		    if(url.endsWith("&")) {
		    	url = url.substring(0, url.length()-1);
		    }
		}
		return url;
	}

	public static String getUrlWithLkid(String url) {
		url = getUrl(url);
		if(url.indexOf("?") > 0) {
			url = url + "&lkid=" + GBConst.API_LKID;  
		} else {
			url = url + "?lkid=" + GBConst.API_LKID;
		}
		System.out.println(url);
		return url;
	}

	public static String convertShortLinkToOrginLink(String url) {
		try {
			URL obj = new URL(url);
			HttpURLConnection con;
			con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			int responseCode = con.getResponseCode();
			if(responseCode == 301) {
				url = con.getHeaderField("Location");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return url;
	}
	
	public static String getPosibleFileName(String name) {
		for(String token : fileNameArr) {
			name = name.replace(token, "");
		}
		return name;
	}
	
	public static String getFileNameNoExt(String path) {
		int str = path.lastIndexOf("/");
		int end = path.lastIndexOf(".");
		return path.substring(str+1, end);
	}
	
	public static String getFileNameWithExt(String path) {
		int str = path.lastIndexOf("/");
		return path.substring(str+1);
	}
}
