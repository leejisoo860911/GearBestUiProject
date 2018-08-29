package com.gb.manager;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
	
	private static List<ConfigVo> configVoList;
	
	private ConfigManager(){
	}
	public static void main(String[] args) {
		boolean aaa = false;
		System.out.println(aaa);
	}
	
	public static List<ConfigVo> getSettingVoList() {
		PropertiesManager.loadFile();
		
		if(configVoList == null) {
			configVoList = new ArrayList<ConfigVo>(10);
		} else {
			configVoList.clear();
		}
		
		String item = "";
		String[] arr = null;
		ConfigVo vo = null;
		for(int i = 1; i <= 10 ; i++) {
			item = PropertiesManager.get("item"+i);
			arr = item.split("@");
			
			vo = new ConfigVo();
			vo.setTitle(arr[0].trim());
			vo.setHasCategory("true".equals(arr[1].trim()));
			try{
				vo.setDay(Integer.parseInt(arr[2].trim()));
			} catch (NumberFormatException e) {
				vo.setDay(365);
			}
			if(arr.length > 3) {
				vo.setListString(arr[3].trim());
			}
			configVoList.add(vo);
		}
		
		return configVoList;
	}
}
