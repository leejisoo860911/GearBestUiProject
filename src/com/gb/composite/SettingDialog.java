package com.gb.composite;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.gb.consts.GBConst;
import com.gb.manager.ConfigManager;
import com.gb.manager.ConfigVo;
import com.gb.manager.PropertiesManager;
import com.gb.utils.StringUtil;

public class SettingDialog extends Dialog {

	Text newText;
	List<Text> 		titleList;
	List<Button> 	categoryBtnList;
	List<Text> 		dayTextList;
	List<Text> 		keyWordList;
	
	protected SettingDialog(Shell parentShell) {
		super(parentShell);
		titleList 		= new ArrayList<Text>(10);
		categoryBtnList = new ArrayList<Button>(10);
		dayTextList 	= new ArrayList<Text>(10);
		keyWordList 	= new ArrayList<Text>(10);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		List<ConfigVo> configList = ConfigManager.getSettingVoList();

		Composite composite = new Composite(parent, SWT.BORDER);
		composite.setLayout(new GridLayout(4, false));
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		GridData gd = new GridData();
		gd.horizontalSpan=2;
		
		Label lable = new Label(composite, SWT.CENTER);
		lable.setText("[NEW] 표시 기준일 일자 : ");
		lable.setLayoutData(gd);
		
		newText = new Text(composite, SWT.BORDER);
		newText.setText(StringUtil.nvl(PropertiesManager.get("newdate"), "3"));
		gd = new GridData();
		gd.widthHint=50;
		newText.setLayoutData(gd);
		
		new Label(composite, SWT.CENTER);
		
		GridData gd100 = new GridData();
		gd100.widthHint=100;
		
		GridData gd60 = new GridData();
		gd60.widthHint=60;
		
		GridData gdBtn60 = new GridData();
		gdBtn60.horizontalIndent = 20;
		gdBtn60.widthHint=60;
		
		GridData gd200 = new GridData();
		gd200.widthHint=200;
		gd200.grabExcessHorizontalSpace=true;
		
		Label l1 = new Label(composite, SWT.CENTER);
		l1.setText("제목");
		l1.setLayoutData(gd100);
		
		Label l2 = new Label(composite, SWT.CENTER);
		l2.setText("카테고리");
		l2.setLayoutData(gd60);
		
		Label l3 = new Label(composite, SWT.CENTER);
		l3.setText("n일 이내");
		l3.setLayoutData(gd60);
		
		Label l4 = new Label(composite, SWT.CENTER);
		l4.setText("검색어");
		l4.setLayoutData(gd200);
		
		ConfigVo vo = null;
		for(int i = 0; i < 10 ; i++) {
			if(i < configList.size()) {
				vo = configList.get(i);
			}
			Text c1 = new Text(composite, SWT.BORDER);
			c1.setText(vo.getTitle()==null?"" : vo.getTitle() );
			c1.setLayoutData(gd100);
			titleList.add(c1);
			
			Button c2 = new Button(composite, SWT.CHECK|SWT.CENTER);
			c2.setSelection(vo.isHasCategory());
			c2.setLayoutData(gdBtn60);
			categoryBtnList.add(c2);
			
			Text c3 = new Text(composite, SWT.BORDER);
			c3.setText(vo.getDay()==null?"365":vo.getDay()+"");
			c3.setLayoutData(gd60);
			dayTextList.add(c3);
			
			Text c4 = new Text(composite, SWT.BORDER);
			c4.setText(vo.getListString());
			c4.setLayoutData(gd200);
			keyWordList.add(c4);
		}
		
	    return super.createDialogArea(composite);
	}
	
	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		if(id==OK) {
			label = "Save";
		}
		return super.createButton(parent, id, label, defaultButton);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Setting");
		Image icon = new Image(newShell.getDisplay(), GBConst.ICON_SETTING);
		newShell.setImage(icon);
	}
	
	@Override
	protected void okPressed() {
		PropertiesManager.set("newdate", StringUtil.nvl(newText.getText(), "3"));
		ConfigVo vo = null;
		for(int i = 0 ; i < 10; i++) {
			vo = new ConfigVo();
			vo.setTitle(StringUtil.nvl(titleList.get(i).getText(), "").trim());
			vo.setHasCategory(categoryBtnList.get(i).getSelection());
			vo.setDay(Integer.parseInt(StringUtil.nvl(dayTextList.get(i).getText(), "365").trim()));
			vo.setListString(StringUtil.nvl(keyWordList.get(i).getText(), "").trim());
			PropertiesManager.set("item"+(i+1), vo.toString());
		}
		
		PropertiesManager.saveFile();
		
		super.okPressed();
	}
	
	
}
