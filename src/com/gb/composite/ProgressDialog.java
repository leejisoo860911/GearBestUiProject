package com.gb.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.gb.consts.GBConst;
import com.gb.manager.ConfigManager;
import com.gb.manager.ConfigVo;
import com.gb.manager.GBApiHelper;
import com.gb.manager.PropertiesManager;
import com.gb.utils.GBMap;
import com.gb.utils.StringUtil;
import com.gb.utils.TemplateManager;
import com.gb.vo.GBCouponsVo;
import com.gb.vo.TabVo;

public class ProgressDialog extends Dialog {

	private ProgressBar bar;

	private Label label;
	
	private List<TabVo> tblList;
	
	
	protected ProgressDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		bar = new ProgressBar (parent, SWT.SMOOTH );
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_CENTER);
		gd.verticalIndent = 15;
		bar.setLayoutData(gd);
		
		label = new Label(parent, SWT.NONE);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		gd.verticalIndent = 5;
		gd.widthHint = 100;
		label.setLayoutData(gd);
		label.setAlignment(SWT.CENTER);
		
	    return super.createDialogArea(parent);
	}
	
	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		Button btn = super.createButton(parent, id, label, defaultButton);
		btn.setVisible(false);
		btn.setSize(0, 0);
		return btn;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Call Gearbest API...");
		Image searchicon = new Image(newShell.getDisplay(), GBConst.ICON_SEARCH);
		newShell.setImage(searchicon);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(250, 110);
	}

	public void callApi() {
		if (bar.isDisposed()) return;
		// TODO Auto-generated method stub
		GBMap params = new GBMap();
		params.put(GBConst.PARAM_PAGE, "1");
		params.put(GBConst.PARAM_LKID, GBConst.API_LKID);
		// 정렬
		List<GBCouponsVo> list = GBApiHelper.getCoupons(params, bar, label);
		
		Collections.sort(list, new Comparator<GBCouponsVo>() {
			@Override
			public int compare(GBCouponsVo o1, GBCouponsVo o2) {
				if(o1.getCategory().equals(o2.getCategory())) {
					return o2.getStartTime().compareTo(o1.getStartTime());
				} else {
					return o1.getCategory().compareTo(o2.getCategory());
				}
			}
		});

		List<ConfigVo> configList = ConfigManager.getSettingVoList();
		List<GBCouponsVo> filteredList = null;
		HashMap<String, Object> fmMap = new HashMap<String, Object>();
		TabVo vo = null;
		tblList = new ArrayList<TabVo>();
		for(ConfigVo config : configList) {
			if(!StringUtil.isEmpty(config.getTitle())) {
				fmMap.clear();
				filteredList = getFillteredList(list, config);

				fmMap.put("newdate", 		PropertiesManager.get("newdate"));
				fmMap.put("appendCategory", config.isHasCategory());
				fmMap.put("couponList", filteredList);
				
				vo = new TabVo();
				vo.setTitle(config.getTitle());
				vo.setContent(TemplateManager.getCouponContent(fmMap));
				tblList.add(vo);
			}
		}
		
		okPressed();
	}
	
	public List<TabVo> getTblList() {
		return tblList;
	}
	
	private List<GBCouponsVo> getFillteredList(List<GBCouponsVo> list, ConfigVo config) {
		ArrayList<GBCouponsVo> resultList = new ArrayList<GBCouponsVo>();
		List<String> keywordList = null;
		for(GBCouponsVo vo : list) {
			if(vo.isProduct() && config.getDay() >= vo.getCompareDate()) {
				if(StringUtil.isEmpty(config.getListString())) {
					resultList.add(vo);
				}
				else {
					keywordList = config.getList();
					for(String keyword : keywordList) {
						keyword = keyword.toUpperCase();
						if(vo.getCategory().toUpperCase().contains(keyword)
						|| vo.getCouponName().toUpperCase().contains(keyword)
						|| vo.getProductName().toUpperCase().contains(keyword)
						|| vo.getPromotionUrl().toUpperCase().contains(keyword)
						) {
							resultList.add(vo);
							break;
						}
					}
				}
			}
		}
		return resultList;
	}
}
