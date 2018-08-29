package com.excel.composite;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.gb.consts.GBConst;
import com.gb.manager.PropertiesManager;
import com.gb.utils.HttpConnectionUtil;
import com.gb.utils.StringUtil;
import com.gb.utils.TemplateManager;
import com.gb.vo.ExcelVo;

public class ExcelHeadComposite extends Composite{
	
	TableViewer tableViewer;
	ArrayList<ExcelVo> inputList;
	Button shourtUrlChk; 
	Button imgDownChk;
	Button imgTagChk;
	Button processBtn;
	Text lkidText;
	Text imgDownPathText;
	ExcelBodyComposite body;
	
	public ExcelHeadComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		createTopComposite();
		createBottomComposite();
	}
	
	private void createTopComposite() {
		/****************
		 * 첫번째 Composite
		 ****************/
		Composite firstComp = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(9, false);
		layout.marginWidth = 0;
		layout.marginBottom = 0;
		firstComp.setLayout(layout);
		firstComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Button clearTableBtn = new Button(firstComp, SWT.PUSH);
		clearTableBtn.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		clearTableBtn.setText("지우기");
		clearTableBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clear(true);
			}
		});
		
		Button pasteTableBtn = new Button(firstComp, SWT.PUSH);
		pasteTableBtn.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		pasteTableBtn.setText("붙여넣기");
		pasteTableBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				paste();
			}
		});
		
		shourtUrlChk = new Button(firstComp, SWT.CHECK);
		shourtUrlChk.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		shourtUrlChk.setText("URL줄이기");
		if(PropertiesManager.get("EXCEL_URL").equals("true")) shourtUrlChk.setSelection(true);
		
		imgDownChk = new Button(firstComp, SWT.CHECK);
		imgDownChk.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		imgDownChk.setText("이미지 다운");
		if(PropertiesManager.get("EXCEL_IMG_DOWN").equals("true")) imgDownChk.setSelection(true);
		
		imgTagChk = new Button(firstComp, SWT.CHECK);
		imgTagChk.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		imgTagChk.setText("이미지 태그 포함");
		if(PropertiesManager.get("EXCEL_IMG_TAG").equals("true")) imgTagChk.setSelection(true);

		new Label(firstComp, SWT.NONE).setText(" LKID :");
		lkidText = new Text(firstComp, SWT.BORDER);
		String lkid = PropertiesManager.get("EXCEL_LKID");
		lkidText.setText(!StringUtil.isEmpty(lkid) ? lkid : GBConst.API_LKID);
		while(lkidText.getText().length() < 8) {
			lkidText.setText(lkidText.getText() + " ");
		}
		GBConst.API_LKID = lkidText.getText().trim();
		lkidText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				GBConst.API_LKID = lkidText.getText().trim();
			}
		});
		
		Label label = new Label(firstComp, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		label.setLayoutData(gd);
		
		processBtn = new Button(firstComp, SWT.PUSH);
		processBtn.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		processBtn.setText("테이블 처리시작");
		processBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				process();
			}
		});
		
		/****************
		 * 두번째 Composite
		 ****************/
		Composite secComp = new Composite(this, SWT.NONE);
		layout = new GridLayout(4, false);
		layout.marginWidth = 0;
		layout.marginBottom = 0;
		layout.marginHeight = 0;
		layout.verticalSpacing = 0;
		secComp.setLayout(layout);
		secComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label folderLabel = new Label(secComp, SWT.NONE);
		folderLabel.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
		folderLabel.setText("이미지 경로 :");
		
		imgDownPathText = new Text(secComp, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		imgDownPathText.setLayoutData(gd);
		imgDownPathText.setEditable(false);
		String savePath = PropertiesManager.get("EXCEL_IMG_PATH");
		if(StringUtil.isEmpty(savePath)) {
			savePath = System.getProperty("user.home");
		}
		imgDownPathText.setText(savePath);
		
		Button changePathBtn = new Button(secComp, SWT.PUSH);
		changePathBtn.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		changePathBtn.setText("폴더변경");
		changePathBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dlg = new DirectoryDialog(getShell());
				dlg.setText("다운로드 폴더선택");
				dlg.setMessage("이미지 파일을 일괄 다운로드 할 폴더를 지정하여 주세요.");
				String folderPath = imgDownPathText.getText().trim();
				if(!StringUtil.isEmpty(folderPath) && (new File(folderPath).exists())){
					dlg.setFilterPath(folderPath);
				}
				folderPath = dlg.open();
				if(!StringUtil.isEmpty(folderPath)) {
					imgDownPathText.setText(folderPath);
				}
			}
		});
		
		Button openPathBtn = new Button(secComp, SWT.PUSH);
		openPathBtn.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
		openPathBtn.setText("폴더열기");
		openPathBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Program.launch(imgDownPathText.getText());
			}
		});
	}
	
	private void createBottomComposite() {
		/*
		 * TableLayout 생성
		 */
		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(6, true));
		layout.addColumnData(new ColumnWeightData(6, true));
		layout.addColumnData(new ColumnWeightData(3, true));
		layout.addColumnData(new ColumnWeightData(3, true));
		layout.addColumnData(new ColumnWeightData(2, true));
		layout.addColumnData(new ColumnWeightData(3, true));
		layout.addColumnData(new ColumnWeightData(3, true));

		/*
		 * TableViewer 생성 및 각종 기본 설정
		 */
		tableViewer = new TableViewer(this, SWT.BORDER|SWT.FULL_SELECTION);
		tableViewer.getTable().setLayout(layout);
		tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.setUseHashlookup(true);
		
		String[] headers = new String[]{"상품명", "링크", "가격", "쿠폰", " 수량", " 시작일자", " 종료일자"};
		for(String header : headers ) {
			new TableColumn(tableViewer.getTable(), SWT.COLOR_DARK_GRAY).setText(header);
		}
		
		tableViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object arg1, Object arg2) {}
			@Override
			public void dispose() {}
			/**
			 * Object배열을 리턴한다.
			 *  - 이 때, 리턴되는 Object배열은 전체 테이블 데이터를 포함하는 배열
			 */
			@Override
			public Object[] getElements(Object input) {
				return ((ArrayList<ExcelVo>)input).toArray();
			}
		});
		
		tableViewer.setLabelProvider(new ITableLabelProvider() {
			public void addListener(ILabelProviderListener arg0) {}
			public void removeListener(ILabelProviderListener arg0) {}
			public boolean isLabelProperty(Object arg0, String arg1) {return false;}
			public Image getColumnImage(Object arg0, int arg1) {return null;}
			public void dispose() {}
			public String getColumnText(Object element, int idx) {
				if(element instanceof ExcelVo) {
					ExcelVo vo = (ExcelVo) element;
					switch (idx) {
					case 0:
						return vo.getTitle();
					case 1:
						return vo.getUrl();
					case 2:
						return vo.getPrice();
					case 3:
						return vo.getCoupon();
					case 4:
						return vo.getCount();
					case 5:
						return vo.getStrDtm();
					case 6:
						return vo.getEndDtm();
					}
					return "";
				}
				else return "";
			}
		});
		
		tableViewer.getTable().addKeyListener(new KeyAdapter() {
			@Override
    		public void keyPressed(KeyEvent e) {
    			if(e.keyCode == 118 && e.stateMask == SWT.CTRL) {
    				paste();
    			}
    		}
		});
		
		inputList = new ArrayList<ExcelVo>();
		inputList.add(new ExcelVo("이곳을 클릭 후 데이터를 붙여넣으세요.\t[테이블 Insert버튼] 혹은 [Ctrl+v]\t \t \t "));
		tableViewer.setInput(inputList);
	}
	
	private void paste() {
		clear(false);
		Clipboard cb = new Clipboard(getDisplay());
		String content = (String) cb.getContents(TextTransfer.getInstance());
		Scanner sc = new Scanner(content);
		String line;
		ExcelVo vo;
		while(sc.hasNext()) {
			line = sc.nextLine();
			if(!StringUtil.isEmpty(line)) {
				if(line.split("\t").length < 1) {
					MessageDialog.openError(getShell(), "에러", "입력 내용에 문제가 있습니다. 엑셀파일 양식을 확인하여 주세요.\n"+line);
					clear(true);
					return;
				}
			
				vo = new ExcelVo(line);
				if(StringUtil.isEmpty(vo.getTitle())&&!StringUtil.isEmpty(vo.getUrl())) {
					HashMap<String, String> retMap = HttpConnectionUtil.sendGetRequestHtmlMap(vo.getUrl());
					vo.setTitle(retMap.get("productName"));
				}
				inputList.add(vo);
			}
		}
		cb.dispose();
		tableViewer.setInput(inputList);
	}
	
	private void clear(boolean toClear) {
		inputList.clear();
		if(toClear) {
			inputList.add(new ExcelVo("이곳을 클릭 후 데이터를 붙여넣으세요.\t[테이블 Insert버튼] 혹은 [Ctrl+v]\t \t \t "));
			tableViewer.setInput(inputList);
		}
	}
	
	public void setBody(ExcelBodyComposite body) {
		this.body = body;
	}
	
	private void process() {
		// 버튼 상태 Save
		PropertiesManager.set("EXCEL_URL", shourtUrlChk.getSelection()?"true":"false");
		PropertiesManager.set("EXCEL_IMG_DOWN", imgDownChk.getSelection()?"true":"false");
		PropertiesManager.set("EXCEL_IMG_TAG", imgTagChk.getSelection()?"true":"false");
		PropertiesManager.set("EXCEL_IMG_PATH", imgDownPathText.getText());
		PropertiesManager.set("EXCEL_LKID", lkidText.getText().trim());
		PropertiesManager.saveFile();
		
		if(imgDownChk.getSelection()) {
			String path = imgDownPathText.getText();
			if(StringUtil.isEmpty(path)) {
				MessageDialog.openError(getShell(), "에러", "이미지 다운로드 경로를 먼저 세팅하세요.");
				return;
			}
			if(!new File(path).exists()) {
				MessageDialog.openError(getShell(), "에러", "선택한 이미지 다운로드 폴더는 없는 경로입니다. 저장위치를 확인하여 주세요. ("+path+")");
				return;
			}
		}
		
		if(inputList.size() == 0) {
			MessageDialog.openError(getShell(), "에러", "테이블에 데이터를 붙여넣고 수행하세요.");
			return;
		}
		
		if(inputList.size() == 1) {
			ExcelVo vo = inputList.get(0);
			if("이곳을 클릭 후 데이터를 붙여넣으세요.".equals(vo.getUrl())) {
				MessageDialog.openError(getShell(), "에러", "테이블에 데이터를 붙여넣고 수행하세요.");
				return;
			}
		}
		final boolean isShortUrl = shourtUrlChk.getSelection();
		final boolean isImgTag = imgTagChk.getSelection();
		final boolean isImgDown = imgDownChk.getSelection();
		final String folderPath = imgDownPathText.getText();
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				ExcelVo vo;
				String orgUrl;
				HashMap<String, String> resMap;
				String fommat = inputList.size() > 99 ? "%03d" : "%02d";
				String imgPath = "";
				String filePath = "";
				InputStream in = null;
				OutputStream out = null;
				URL url = null;
				for(int i = 0; i < inputList.size() ; i++) {
					vo = inputList.get(i);
					orgUrl = vo.getUrl();
					vo.setUrl(StringUtil.getUrlWithLkid(vo.getUrl()));
					
					if(isShortUrl) {
						vo.setUrl(HttpConnectionUtil.getShortUrl(vo.getUrl()));
					}
					
					if(isImgTag || isImgDown) {
						resMap = HttpConnectionUtil.sendGetRequestHtmlMap(orgUrl);
						vo.setImgUrl(resMap.get("productImage"));
					}
					
					if(isImgDown) {
						imgPath = vo.getImgUrl();
						try {
							filePath = imgPath.replace(StringUtil.getFileNameNoExt(imgPath), StringUtil.getPosibleFileName(vo.getTitle()));
							filePath = folderPath + "/" + String.format(fommat, i+1)+"_"+StringUtil.getFileNameWithExt(filePath);
							url = new URL(imgPath);
							in = new BufferedInputStream(url.openStream());
							out = new BufferedOutputStream(new FileOutputStream(filePath));
							for(int j; (j = in.read()) != -1;) {
								out.write(j);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
							if(in != null) try {in.close();} catch (IOException e1) {e1.printStackTrace();}
							if(out != null) try {out.close();} catch (IOException e1) {e1.printStackTrace();}
						}
					}
					
					final String message = "("+(i+1)+"/"+(inputList.size())+") 데이터 처리 중...";
					getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							body.getMainText().setText(message);
						}
					});
				}
				
				HashMap<String, Object> input = new HashMap<String, Object>();
				input.put("isImgTag", isImgTag);
				input.put("excelList", inputList);
				final String content = TemplateManager.getExcelContent(input);
				getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						body.getMainText().setText(content);
					}
				});
			}
		});
		th.start();
	}
}
