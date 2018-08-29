package com.gb.launch;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.gb.composite.BodyComposite;
import com.gb.composite.HeadComposite;
import com.gb.consts.GBConst;
import com.gb.utils.HttpConnectionUtil;

public class Main {

	public static void main(String[] args) {
		try {
			Map<String, Object> currencyMap = HttpConnectionUtil.sendGetRequestJsonToMap("http://api.fixer.io/latest");
			GBConst.USD_CURRENCY = (double)((Map)currencyMap.get("rates")).get("USD");
		} catch (Exception e) {
		}
		
		Display display = new Display();
	    final Shell shell = new Shell(display);
	    shell.setText("Gearbest Api UI Program");
	    shell.setSize(800, 800);
	    shell.setLayout(new GridLayout(1, true));
	    HeadComposite head = new HeadComposite(shell, SWT.FILL);
	    BodyComposite body = new BodyComposite(shell, SWT.FILL);
	    head.setBodyComposite(body);
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();
	}
}
