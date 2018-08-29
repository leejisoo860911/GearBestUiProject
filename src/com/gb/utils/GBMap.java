package com.gb.utils;

import java.util.Date;
import java.util.TreeMap;

import com.gb.consts.GBConst;

public class GBMap extends TreeMap<String, String> {
	
	@Override
	public String toString() {
		// 공통파라메터 세팅
		put(GBConst.PARAM_APIKEY, GBConst.API_KEY);
		put(GBConst.PARAM_TIME,	  String.valueOf(new Date().getTime()));
		
		// Sign만들기
        StringBuilder sb = new StringBuilder();
        sb.append(GBConst.API_SECRETE);
        for (String key : keySet()) {
            sb.append(key).append(get(key));
        }
        sb.append(GBConst.API_SECRETE);
        put(GBConst.PARAM_SIGN,	  StringUtil.getMD5(sb.toString()).toUpperCase());
        
        sb = new StringBuilder();
        for (String key : keySet()) {
        	sb.append("&").append(key).append("=").append(get(key));
        }
        
		return sb.toString().replaceFirst("&", "?");
	}

}
