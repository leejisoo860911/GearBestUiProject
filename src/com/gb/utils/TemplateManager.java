package com.gb.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import com.gb.consts.GBConst;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class TemplateManager {

	static private Configuration cfg;
	
	static private Template couponTemplate;
	
	static private Template productTemplate;
	
	static private Template excelTemplate;
	
	private TemplateManager() {
		
	}
	
	public static void init() {
		cfg = new Configuration(Configuration.VERSION_2_3_23);
		cfg.setDefaultEncoding("UTF-8");
		try {
			File templateFolder = new File(GBConst.PATH_TEMPLATE_DIR);
			cfg.setDirectoryForTemplateLoading(templateFolder);
			couponTemplate = cfg.getTemplate(GBConst.PATH_COUPON_TEMPLATE_FILE);
			productTemplate = cfg.getTemplate(GBConst.PATH_PRODUCT_TEMPLATE_FILE);
			excelTemplate = cfg.getTemplate(GBConst.PATH_EXCEL_TEMPLATE_FILE);
		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getCouponContent(HashMap<String, Object> input) {
		if(cfg == null) init();
		input.put("today", StringUtil.getDateString());
		StringWriter result = new StringWriter();
        
		try {
			couponTemplate.process(input, result);
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return result.toString();
	}
	
	public static String getProductContent(HashMap<String, Object> input) {
		if(cfg == null) init();
		input.put("today", StringUtil.getDateString());
		StringWriter result = new StringWriter();
        
		try {
			productTemplate.process(input, result);
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return result.toString();
	}
	
	public static String getExcelContent(HashMap<String, Object> input) {
		if(cfg == null) init();
		input.put("today", StringUtil.getDateString());
		StringWriter result = new StringWriter();
        
		try {
			excelTemplate.process(input, result);
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return result.toString();
	}

}
