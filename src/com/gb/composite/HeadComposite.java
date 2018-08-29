package com.gb.composite;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.gb.consts.GBConst;
import com.gb.vo.TabVo;

public class HeadComposite extends Composite{
	private BodyComposite bodyComposite;
	
	public HeadComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(4, true));
	    GridData gdHead = new GridData(GridData.FILL_HORIZONTAL);
	    gdHead.minimumHeight = 200;
	    setLayoutData(gdHead);
	    createContent();
	}

	private void createContent() {
		Composite firstComposite = new Composite(this, SWT.NONE);
		firstComposite.setLayout(new GridLayout(6, false));
		GridData firstGd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		firstGd.horizontalSpan = 3;
		firstComposite.setData(firstGd);
		
		Label labelApiKey = new Label(firstComposite, SWT.BEGINNING);
		labelApiKey.setText("API key :");
		
		Text txtApiKey = new Text(firstComposite, SWT.BORDER);
		txtApiKey.setText(GBConst.API_KEY);
		txtApiKey.setEditable(false);
		
		Label labelApiSecrete = new Label(firstComposite, SWT.BEGINNING);
		labelApiSecrete.setText("API Secret :");
		
		Text txtApiSecrete = new Text(firstComposite, SWT.BORDER);
		txtApiSecrete.setText(GBConst.API_SECRETE);
		txtApiSecrete.setEditable(false);
		
		Label labelLkid = new Label(firstComposite, SWT.BEGINNING);
		labelLkid.setText("LKID :");
		
		Text txtLkid = new Text(firstComposite, SWT.BORDER);
		txtLkid.setText(GBConst.API_LKID);
		txtLkid.setEditable(false);
		
		Composite btnComposite = new Composite(this, SWT.NONE);
		btnComposite.setLayout(new GridLayout(2, true));
		GridData btnGd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btnGd.horizontalSpan = 1;
		btnComposite.setData(btnGd);
		
		Button callbtn = new Button(btnComposite, SWT.PUSH);
		Image searchicon = new Image(getDisplay(), GBConst.ICON_SEARCH);
		callbtn.setImage(searchicon);
		callbtn.setText(" Call API ");
		callbtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProgressDialog dia = new ProgressDialog(getShell());
				dia.setBlockOnOpen(false);
				int result = dia.open();
				dia.callApi();
				if(result == Dialog.OK) {
					List<TabVo> list = dia.getTblList();
					bodyComposite.setTabList(list);
				}
			}
		}); 
		
		Button setBtn = new Button(btnComposite, SWT.PUSH);
		Image settingIcon = new Image(getDisplay(), GBConst.ICON_SETTING);
		setBtn.setImage(settingIcon);
		setBtn.setText(" Setting ");
		setBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SettingDialog dia = new SettingDialog(getShell());
				dia.setBlockOnOpen(true);
				dia.open();
			}
		});
	}

	public void setBodyComposite(BodyComposite body) {
		this.bodyComposite = body;
	}
}
