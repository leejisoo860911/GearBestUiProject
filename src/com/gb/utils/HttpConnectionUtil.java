package com.gb.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gb.consts.GBConst;
import com.rosaloves.bitlyj.Bitly;
 
public class HttpConnectionUtil {
	
	private final static String USER_AGENT = "Mozilla/5.0";
	
	private static long lastCallMs;

	private HttpConnectionUtil() {
	}
	
	public static Map<String, Object> sendGetRequestJsonToMap(String url) {
		HashMap<String, Object> responseMap = null;
		
		try {

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", USER_AGENT);
			int responseCode = con.getResponseCode();
			
			// TODO reponseCode에 따른 에러처리
			switch (responseCode) {
			case 200:
				
				break;
			case 400:
				
				break;

			default:
				break;
			}
	
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			ObjectMapper objMapper = new ObjectMapper();
			responseMap = objMapper.readValue(in, new  TypeReference<HashMap<String, Object>>(){});
			
			/*
			 * ErrorCode
			 * 999	unknown error
			 * 1000	need permission
			 * 1001	Missing Required Arguments
			 * 1002	Invalid Arguments
			 * 1003	exceeded API rate limit
			 * 1004	Missing Signature
			 * 1005	Invalid Signature
			 * 1006	Missing App Key
			 * 1007	Invalid App Key api_key
			 * 1008	Missing Time
			 */		
			in.close();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return responseMap;
	}
	
	public static HashMap<String, String> sendGetRequestHtmlMap(String url) {
		HashMap<String, String> retMap = new HashMap<String, String>();

		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", USER_AGENT);
			int responseCode = con.getResponseCode();
			
			// TODO reponseCode에 따른 에러처리
			switch (responseCode) {
			case 200:
				
				break;
			case 400:
				
				break;

			default:
				break;
			}
	
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			int strIdx = -1;
			int endIdx = -1;
			String inputLine;
			while ((inputLine = in.readLine()) != null && (retMap.get("productName") == null || retMap.get("productImage") == null)) {
				if(inputLine.contains(GBConst.META_OG_TITLE)) {
					strIdx = inputLine.indexOf(GBConst.META_OG_TITLE) + GBConst.META_OG_TITLE.length();
					endIdx = inputLine.lastIndexOf("\"");
					String title = inputLine.substring(strIdx, endIdx); 
					retMap.put("productName", StringUtil.getGBProductName(title));
					retMap.put("origPrice", StringUtil.getGBProductOrigPrice(title));
				}
				
				else if(inputLine.contains(GBConst.META_OG_IMG)) {
					strIdx = inputLine.indexOf(GBConst.META_OG_IMG) + GBConst.META_OG_IMG.length();
					endIdx = inputLine.lastIndexOf("\"");
					String img = inputLine.substring(strIdx, endIdx);
					retMap.put("productImage", img.trim());
				}
			}
			in.close();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return retMap;
	}
	
	public static String getShortUrl(String url) {
		try {
			url = Bitly.as(GBConst.SHOURT_URL_LOGIN_KEY, GBConst.SHOURT_URL_API_KEY).call(Bitly.shorten(url)).getShortUrl();
		} catch (Exception e) {
			url = "Error : " + e.getMessage();
		}
		
		return url;
	}
}
