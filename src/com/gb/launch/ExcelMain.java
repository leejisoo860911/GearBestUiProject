package com.gb.launch;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.excel.composite.ExcelBodyComposite;
import com.excel.composite.ExcelHeadComposite;

public class ExcelMain {

	public static void main(String[] args) {
		Display display = new Display();
	    final Shell shell = new Shell(display);
	    shell.setText("기어베스트 엑셀 양식 정리");
	    shell.setSize(800, 800);
	    shell.setLayout(new FillLayout());
	    
	    SashForm sashForm = new SashForm(shell, SWT.VERTICAL|SWT.BORDER);
	    sashForm.setLayout(new FillLayout());
	    sashForm.SASH_WIDTH = 1;
	    
	    ExcelHeadComposite head = new ExcelHeadComposite(sashForm, SWT.FILL);
	    ExcelBodyComposite body = new ExcelBodyComposite(sashForm, SWT.FILL);
	    head.setBody(body);
	    
	    sashForm.setWeights(new int[]{3, 5});
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();
	}
}
