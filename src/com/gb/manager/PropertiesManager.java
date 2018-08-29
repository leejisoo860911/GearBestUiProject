package com.gb.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.gb.consts.GBConst;

public class PropertiesManager {
	private static Properties properties;
	
	private PropertiesManager(){}

	public static void loadFile() {
		if(properties == null) properties = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(GBConst.PATH_PROPERTIES));
			properties.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
	
	public static void saveFile() {
		FileOutputStream fis = null;
		try {
			fis = new FileOutputStream(new File(GBConst.PATH_PROPERTIES));
			properties.store(fis, "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String get(String key) {
		if(properties==null) loadFile();
		Object ret = "";
		try {
			ret = properties.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(ret == null) ret = "";
		return (String) ret;
	}
	
	
	public static void set(String key, String value) {
		properties.setProperty(key, value);
	}
}
