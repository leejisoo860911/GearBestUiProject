package com.product.composite;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gb.manager.PropertiesManager;
import com.gb.utils.StringUtil;

public class ProductBodyComposite extends Composite{

	private Text mainText;
	private ProductHeadComposite headComposite;
	private Button btnHtmlCopy;
	private Button btnTextCopy;
	private Button btnSaveImage;
	
	public ProductBodyComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;

		setLayout(layout);
	    GridData gdBody = new GridData(GridData.FILL_BOTH);
	    setLayoutData(gdBody);
	    
	    Composite textComp = new Composite(this, SWT.NONE);
	    textComp.setLayout(new GridLayout(1, true));
	    textComp.setLayoutData(new GridData(GridData.FILL_BOTH));
	    crateTextArea(textComp);
	    
	    Composite buttonComp = new Composite(this, SWT.NONE);
	    buttonComp.setLayout(new GridLayout(3, true));
	    buttonComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    crateButtonArea(buttonComp);
	}
	
	private void crateTextArea(Composite parent) {
	    mainText = new Text(parent, SWT.BORDER|SWT.READ_ONLY|SWT.V_SCROLL);
    	mainText.setText("상품 URL을 입력 후 찾기를 수행하세요.");
    	mainText.setLayoutData(new GridData(GridData.FILL_BOTH));
    	mainText.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
    	mainText.addKeyListener(new KeyAdapter() {
    		@Override
    		public void keyPressed(KeyEvent e) {
    			if(e.keyCode == 97 && e.stateMask == SWT.CTRL) {
    				((Text)e.getSource()).selectAll();
    			}
    		}
		});
	}
	
	private void crateButtonArea(Composite parent) {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		gd.minimumHeight = 500;
		
		btnHtmlCopy = new Button(parent, SWT.PUSH);
		btnHtmlCopy.setText("HTML 복사");
		btnHtmlCopy.setLayoutData(gd);
		btnHtmlCopy.setEnabled(false);
		btnHtmlCopy.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				String content = mainText.getText();
				Clipboard cb = new Clipboard(getDisplay());
				cb.setContents(new Object[]{content}, new Transfer[]{TextTransfer.getInstance()});
				MessageDialog.openInformation(getShell(), "안내", "클립보드로 HTML 내용을 하였습니다.");
				cb.dispose();
			}
		});
		
		btnTextCopy = new Button(parent, SWT.PUSH);
		btnTextCopy.setText("텍스트 복사");
		btnTextCopy.setLayoutData(gd);
		btnTextCopy.setEnabled(false);
		btnTextCopy.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				String content = mainText.getText();
				content = content.replace("<br>", "$NEWLINE$");
				Document doc = Jsoup.parse(content);
				content = doc.text();
				content = content.replace("$NEWLINE$", "\n");
				Clipboard cb = new Clipboard(getDisplay());
				cb.setContents(new Object[]{content}, new Transfer[]{TextTransfer.getInstance()});
				MessageDialog.openInformation(getShell(), "안내", "클립보드로 텍스트를 추출하여 복사하였습니다.");
				cb.dispose();
			}
		});
			
		btnSaveImage = new Button(parent, SWT.PUSH);
		btnSaveImage.setText("이미지 저장");
		btnSaveImage.setLayoutData(gd);
		btnSaveImage.setEnabled(false);
		btnSaveImage.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				String content = mainText.getText();
				Document doc = Jsoup.parse(content);
				Elements imgNods = doc.getElementsByTag("img");
				if(imgNods!=null && imgNods.size() > 0) {
					DirectoryDialog dlg = new DirectoryDialog(getShell());
					dlg.setText("다운로드 폴더선택");
					dlg.setMessage("이미지 파일을 일괄 다운로드 할 폴더를 지정하여 주세요.");
					String folderPath = PropertiesManager.get("LAST_PATH");
					if(!StringUtil.isEmpty(folderPath) && (new File(folderPath).exists())){
						dlg.setFilterPath(folderPath);
					}
					
					folderPath = dlg.open();
					if(!StringUtil.isEmpty(folderPath)) {
						String imgPath = "";
						String fileName = "";
						InputStream in = null;
						OutputStream out = null;
						URL url = null;
						for(int i = 0; i < imgNods.size(); i++) {
							imgPath = imgNods.get(i).attr("src");
							if(!StringUtil.isEmpty(imgPath)) {
								fileName = String.format("%02d", i+1)+"_"+imgPath.substring(imgPath.lastIndexOf("/")+1);
							}
							try {
								url = new URL(imgPath);
								in = new BufferedInputStream(url.openStream());
								out = new BufferedOutputStream(new FileOutputStream(folderPath+"/"+fileName));
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
						PropertiesManager.set("LAST_PATH", folderPath);
						PropertiesManager.saveFile();
						
						MessageDialog dialog = new MessageDialog(getShell(), "안내", null,
							    "이미지 저장이 완료되었습니다. 폴더를 오픈할까요?", MessageDialog.QUESTION, new String[] { "열기",
							    "다음에"}, 0);
						int ret = dialog.open();
						if(ret == 0) {
							Program.launch(folderPath);
						}
					}
					
				} else {
					MessageDialog.openError(getShell(), "에러", "HTML내용에 <img>태그가 없어 이미지를 추출할 수 없습니다.");	
				}
			}
		});
	}

	public Button getBtnHtmlCopy() {
		return btnHtmlCopy;
	}
	
	public Button getBtnTextCopy() {
		return btnTextCopy;
	}
	
	public Button getBtnSaveImage() {
		return btnSaveImage;
	}

	public Text getMainText() {
		return mainText;
	}
	
	public void setHeadComposite(ProductHeadComposite head) {
		this.headComposite = head;
	}
}
