package com.excel.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class ExcelBodyComposite extends Composite{

	private Text mainText;
	private ExcelHeadComposite headComposite;
	
	public ExcelBodyComposite(Composite parent, int style) {
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
	}
	
	private void crateTextArea(Composite parent) {
	    mainText = new Text(parent, SWT.BORDER|SWT.V_SCROLL);
    	mainText.setText("엑셀 양식을 붙여넣기 후 테이블 처리시작 버튼 클릭.");
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

	public Text getMainText() {
		return mainText;
	}
	
	public void setHeadComposite(ExcelHeadComposite head) {
		this.headComposite = head;
	}
}
