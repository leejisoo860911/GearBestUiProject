package com.gb.composite;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.gb.vo.TabVo;

public class BodyComposite extends Composite{

	private TabItem[] tabItem;
	private Text[] tabText;
	private List<TabVo> tabList;
	
	public BodyComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout());
	    GridData gdBody = new GridData(GridData.FILL_BOTH);
	    gdBody.minimumHeight = 600;
	    setLayoutData(gdBody);
		createContent();
	}
	
	private void createContent() {
		final TabFolder tabFolder = new TabFolder(this, SWT.BORDER);
		tabFolder.setLayout(new GridLayout());
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		tabItem = new TabItem[10];
		tabText = new Text[10];
		
	    for (int i = 0 ; i < 10 ; i++) {
	    	tabItem[i] = new TabItem(tabFolder, SWT.NONE);
	    	tabItem[i].setText("Tab " + (i+1));
	    	
	    	tabText[i] = new Text(tabFolder, SWT.BORDER|SWT.READ_ONLY|SWT.V_SCROLL);
	    	tabText[i].setText("Press Call API Button");
	    	tabText[i].setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
	    	tabText[i].addKeyListener(new KeyAdapter() {
	    		@Override
	    		public void keyPressed(KeyEvent e) {
	    			if(e.keyCode == 97 && e.stateMask == SWT.CTRL) {
	    				((Text)e.getSource()).selectAll();
	    			}
	    		}
			});
	    	tabItem[i].setControl(tabText[i]);
	    }
	    tabFolder.setSize(400, 200);
	}
	
	public void setTabList(List<TabVo> tabList) {
		this.tabList = tabList;
		
		TabVo tabVo = null;
		for(int i = 0 ; i < tabItem.length ; i++) {
			try {
				tabVo = tabList.get(i);
			} catch (IndexOutOfBoundsException e) {
				tabVo = new TabVo();
				tabVo.setTitle("X");
				tabVo.setContent("Plase More Setting First");
			}
			tabItem[i].setText(tabVo.getTitle());
			tabText[i].setText(tabVo.getContent());
		}
	}
}
