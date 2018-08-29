package com.gb.launch;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.product.composite.ProductBodyComposite;
import com.product.composite.ProductHeadComposite;

public class ProductMain {

	public static void main(String[] args) {
		Display display = new Display();
	    final Shell shell = new Shell(display);
	    shell.setText("기어베스트 상품정보 추출기");
	    shell.setSize(800, 800);
	    shell.setLayout(new GridLayout(1, true));
	    ProductHeadComposite head = new ProductHeadComposite(shell, SWT.FILL);
	    ProductBodyComposite body = new ProductBodyComposite(shell, SWT.FILL);
	    head.setBodyComposite(body);
	    body.setHeadComposite(head);
	    
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();
	}
}
