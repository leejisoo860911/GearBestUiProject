package com.gb.manager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

import com.gb.consts.GBConst;
import com.gb.utils.GBMap;
import com.gb.utils.HttpConnectionUtil;
import com.gb.utils.StringUtil;
import com.gb.vo.GBCouponsVo;

public class GBApiHelper {
	
	private GBApiHelper() {}

	public static List<GBCouponsVo> getCoupons(GBMap params, ProgressBar bar, Label label) {
		/*
 		 * https://affiliate.gearbest.com/api/coupon/list-coupons?api_key=[APIkey]&time=[time]&language=[language]&category=[category]&page=[page]&sign=[Signature]&lkid=[lkid]
		 * language* : total 8 languages supported(en,de,it,jp,es,pt,ru,fr)
		 * page* : The page of data request for (default page 1 if missing)
		 * category : Banner category (Only Primary categories, just write the number of the category) https://affiliate.gearbest.com/home/api/category-index
		 */
		String URL = GBConst.URL_COUPONS + params.toString();
		Map<String, Object> responseMap = HttpConnectionUtil.sendGetRequestJsonToMap(URL);
		Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");
		ArrayList itemList = (ArrayList) dataMap.get("items");
		int total = itemList.size();
		int progress = 0;
		GBCouponsVo vo = null;
		if(total > 0) {
			List<GBCouponsVo> list = new ArrayList<GBCouponsVo>();
			LinkedHashMap<String, GBCouponsVo> listMap = new LinkedHashMap<String, GBCouponsVo>();
			for(int i = 0; i < total ; i++) {
				LinkedHashMap<String, Object> item = (LinkedHashMap<String, Object>) itemList.get(i);
				String url = (String) item.get("promotion_url");
				if(!StringUtil.isBlockWid(url)){
					vo = new GBCouponsVo(item);
					if(listMap.get(vo.getCouponCode())==null) {
						listMap.put(vo.getCouponCode(), vo);
					} else {
						if(vo.getCouponCode() == "en") {
							listMap.put(vo.getCouponCode(), vo);
						}
					}
				}
				progress = (int) (( (double)i / (double)total ) * 100);
				bar.setSelection(progress);
				label.setText(progress + "%" +" (" + i + "/" + total + ")");
			}
			for(String key : listMap.keySet()) {
				list.add(listMap.get(key));
			}
			return list;
		}
		return null;
	}
}
