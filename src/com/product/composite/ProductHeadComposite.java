package com.product.composite;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;

import com.gb.consts.GBConst;
import com.gb.manager.PropertiesManager;
import com.gb.utils.HttpConnectionUtil;
import com.gb.utils.StringUtil;
import com.gb.utils.TemplateManager;

public class ProductHeadComposite extends Composite{
	
	private Button callBtn;
	private Button shourtUrlBtn;
	private Text urlText;
	private Browser browser;
	private ProgressListener progressListener;
	private ProductBodyComposite bodyComposite;
	
	public ProductHeadComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(5, false));
	    GridData gdHead = new GridData(GridData.FILL_HORIZONTAL);
	    gdHead.minimumHeight = 200;
	    setLayoutData(gdHead);
	    
		new Label(this, SWT.BEGINNING).setText("상품주소 : ");
		urlText = new Text(this, SWT.BORDER);
		urlText.setEditable(true);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = GridData.VERTICAL_ALIGN_CENTER;
		gd.grabExcessHorizontalSpace = true;
		urlText.setLayoutData(gd);
		urlText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if((e.keyCode == 13 || e.keyCode == 16777296) && callBtn.isEnabled()) {
					search();
				}
			}
		});
		
		urlText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if(!StringUtil.isEmpty(((Text)e.getSource()).getText())) {
					callBtn.setEnabled(true);
				}
			}
		});
		
		shourtUrlBtn = new Button(this, SWT.CHECK);
		shourtUrlBtn.setText("URL 줄이기");
		shourtUrlBtn.setSelection(true);
		
		callBtn = new Button(this, SWT.PUSH);
		Image searchicon = new Image(getDisplay(), GBConst.ICON_SEARCH);
		callBtn.setImage(searchicon);
		callBtn.setText("찾기");
		callBtn.setEnabled(false);
		callBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
				PropertiesManager.set("URL_TEXT", urlText.getText());
				PropertiesManager.saveFile();
			}
		}); 
		
		String url = PropertiesManager.get("URL_TEXT");
		if(url!=null && !"".equals(url)) {
			urlText.setText(url);
		}
		
		browser = new Browser(this, SWT.NO_SCROLL|SWT.NONE);
		browser.setVisible(false);
		browser.setLayoutData(new GridData(0, 0));
		progressListener = new ProgressListener(){
			@Override
			public void changed(ProgressEvent arg0){}
			@Override
			public void completed(ProgressEvent arg0) {
				searchProduct(browser.getText());
				browser.removeProgressListener(progressListener);
				browser.stop();
			}
		};
	}

	public void setBodyComposite(ProductBodyComposite body) {
		this.bodyComposite = body;
	}
	
	private void search() {
		// URL 호출
		// call Jsnope ?
		final String url = urlText.getText().trim();
		final boolean isShortUrl = shourtUrlBtn.getSelection();
		
		Document doc = null;
		
		if(url.contains("https://couponsfromchina.com")) {
			try {
				doc  = Jsoup.connect(url).get();
			} catch (Exception ex) {
				MessageDialog.openError(getShell(), "에러", "URL연결에 실패하였습니다. 입력주소를 확인하여 주세요.\n" + ex.getMessage());
				bodyComposite.getBtnHtmlCopy().setEnabled(false);
				bodyComposite.getBtnTextCopy().setEnabled(false);
				bodyComposite.getBtnSaveImage().setEnabled(false);
				return;
			}
			
			Element table = doc.getElementsByTag("table").get(0); 
			final Elements rows = table.getElementsByTag("tr");
			final StringBuffer sb = new StringBuffer();
			if(rows != null && rows.size() > 0) {
				sb.append("<table id=\"alphabetically\" style=\"width:auto; border:1px solid #ddd; padding: 1px\">").append("\n");
				sb.append(" <tbody>").append("\n");
				sb.append("  <tr>").append("\n");
				sb.append("   <td width=\"5%\" bgcolor=\"a1d5fb\" style=\"text-align: center;\"><strong>No</strong></td>").append("\n");
				sb.append("   <td width=\"14%\" bgcolor=\"a1d5fb\" style=\"text-align: center;\"><strong>사진</strong></td>").append("\n");
				sb.append("   <td width=\"30%\" bgcolor=\"a1d5fb\" style=\"text-align: center;\"><strong>상품명(링크)</strong></td>").append("\n");
				sb.append("   <td width=\"13%\" bgcolor=\"a1d5fb\" style=\"text-align: center;\"><strong>가격</strong></td>").append("\n");
				sb.append("   <td width=\"13%\" bgcolor=\"a1d5fb\" style=\"text-align: center;\"><strong>쿠폰 적용 가격</strong></td>").append("\n");
				sb.append("   <td width=\"25%\" bgcolor=\"a1d5fb\" style=\"text-align: center;\"><strong>쿠폰</strong></td>").append("\n");
				sb.append("  </tr>").append("\n");
				
				Thread th = new Thread(new Runnable() {
					@Override
					public void run() {
						int n = 1;
						String bgcolor = "";
						Element aTag;
						for(int i = 1; i < rows.size() ; i++) {
							Element tr = rows.get(i);
							if(!tr.getElementsByTag("td").get(2).text().contains("[GW")) {
								if(n%2==0) {
									bgcolor = "edf5fa";
								} else {
									bgcolor = "ffffff";
								}
								
								sb.append("  <tr>").append("\n");
								
								Elements tdList = tr.getElementsByTag("td");
								String orgLink = "";
								String newLink = "";
								for(int j = 0 ; j < tdList.size() ; j++) {
									Element td = tdList.get(j);
									td.attr("bgcolor", bgcolor);
									switch (j) {
									case 0: // 번호
										td.attr("style", "text-align: center;");
										td.attr("width", "5%");
										td.text(n+"");
										sb.append("   ").append(td.toString()).append("\n");
										break;
									case 1: // 사진
										aTag =td.getElementsByTag("a").first();
										aTag.getElementsByTag("img").attr("height", GBConst.IMG_WIDTH);
										aTag.getElementsByTag("img").attr("width", GBConst.IMG_WIDTH);
										aTag.attr("target", "_blank");
										orgLink = aTag.attr("href");
										newLink = StringUtil.getUrlWithLkid(StringUtil.convertShortLinkToOrginLink(orgLink));
										// URL 변환 체크 시 변환처리 
										if(isShortUrl){
											newLink = HttpConnectionUtil.getShortUrl(newLink);
										}
										aTag.attr("href", newLink);
										sb.append("   ").append(td.toString()).append("\n");
										break;
									case 2: // 상품명(링크)
										aTag = td.getElementsByTag("a").first();
										aTag.attr("target", "_blank");
										aTag.attr("href", newLink);
										sb.append("   ").append(td.toString()).append("\n");
										break;
									case 3: // 가격
										td.attr("style", "text-align: center");
										sb.append("   ").append(td.toString()).append("\n");
										break;
									case 4: // 쿠폰가격
										td.attr("style", "text-align: center");
										sb.append("   ").append(td.toString()).append("\n");
										break;
									case 5: // 역대가격
										// nothing...
										break;
									case 6: // 쿠폰번호
										for(Element child : td.getElementsByTag("span")) {
											if("COUPONSFROMCHINA.COM".equals(child.text().replace(" ", "").toUpperCase())){
												child.remove();
											}
											if(child.html().indexOf("<br>Number of rows in DB") > -1) {
												int endIdx = child.html().indexOf("<br>Number of rows in DB");
												child.html(child.html().substring(0, endIdx));
											}
										}
										sb.append("   ").append(td.toString().replace("<br><br>", "<br>")).append("\n");
										break;
									default:
										break;
									}
									
									// shourt url처리 
								}
								sb.append("  </tr>").append("\n");
								
								final String message = "("+(n++)+"/"+(rows.size()-1)+") short url 변환 처리 중...";
								getDisplay().asyncExec(new Runnable() {
									@Override
									public void run() {
										bodyComposite.getMainText().setText(message);
										bodyComposite.getMainText().update();
									}
								});
							}
						}
						sb.append(" </tbody>").append("\n");
						sb.append("</table>").append("\n");
						getDisplay().asyncExec(new Runnable() {
							@Override
							public void run() {
								bodyComposite.getMainText().setText(sb.toString());
								bodyComposite.getBtnHtmlCopy().setEnabled(true);
								bodyComposite.getBtnTextCopy().setEnabled(true);
								bodyComposite.getBtnSaveImage().setEnabled(true);
							}
						});
					}
				});
				th.start();
			}
		}
		
		else {
			browser.addProgressListener(progressListener);
			browser.setUrl(url);
		}
	}
	
	private void searchProduct(String html) {
		String title 		= null;
		String summary 		= null;
		String productImage	= null;
		String origPrice	= null;
		String desc			= null;
		String videoUrl		= null;
		String content		= null;
		String productUrl	  = null;
		String productLkidUrl = null;
		
		Document doc  = Jsoup.parse(html);
		productUrl	  = StringUtil.getUrl(urlText.getText().trim());
		productLkidUrl = StringUtil.getUrlWithLkid(urlText.getText().trim());
		
//		LinkedHashMap<String, String> specMap = new LinkedHashMap<String, String>();
		ArrayList<String> imgList = new ArrayList<String>();
		ArrayList<String> youtubeList = new ArrayList<String>();
		
		try {title = doc.getElementsByClass("goodsIntro_titleWrap").first().getElementsByTag("h1").first().text();} catch (Exception ex){}
		try {summary = doc.getElementsByClass("goodsIntro_summary").first().text();} catch (Exception ex){}
		try {productImage = doc.getElementsByAttributeValue("property", "og:image").first().attr("content");} catch (Exception ex){}
		try {origPrice	= doc.getElementsByClass("goodsIntro_price").first().attr("data-currency");} catch (Exception ex){}
		if(!"$".startsWith(origPrice)) origPrice = "$"+origPrice;
		try {desc = doc.getElementsByClass("product_pz_info mainfeatures").first().html();} catch (Exception ex){}
		try {
			Elements youtubeNode = doc.getElementsByClass("goodsDesc_videoItem");
			if(youtubeNode!=null && youtubeNode.size() > 0){
				for(Element node : youtubeNode) {
					videoUrl = node.attr("data-video-inline").trim();
					videoUrl = "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/"+videoUrl+"\" frameborder=\"0\" allow=\"autoplay; encrypted-media\" allowfullscreen></iframe>";
					youtubeList.add(videoUrl);
				}
			}
		}
		catch (Exception ex){}
		try {
			
			Elements imgNodes = doc.getElementsByAttributeValueContaining("data-lazy", "https://des.gbtcdn.com");
			for(Element img : imgNodes) {
				imgList.add(img.attr("data-lazy").trim());
			}
			
			/*
			Elements selfNodes = doc.getElementsByClass("self-adaption");
			if(selfNodes.size() == 1) {
				selfNodes = selfNodes.get(0).children();
			}
			if(selfNodes != null && selfNodes.size() > 0) {
				for(Element self : selfNodes) {
					imgList.add(self.getElementsByTag("img").first().attr("data-lazy").trim());
				}
			}
			*/
		} catch (Exception ex){}
		
		HashMap<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("title", title);
		inputMap.put("summary", summary == null ? title : summary);
		inputMap.put("imgUrl", productImage);
		inputMap.put("origPrice", origPrice);
		inputMap.put("description", desc);
		if(productUrl.contains("utm_source=mail_api")) {
			productUrl = productUrl.substring(0, productUrl.indexOf("?"));
		}
		inputMap.put("productLink", productUrl);
		if(shourtUrlBtn.getSelection()) {
			productLkidUrl = HttpConnectionUtil.getShortUrl(productLkidUrl);
		}
		inputMap.put("productRealLink", productLkidUrl);
		inputMap.put("youtubeList", youtubeList);
		inputMap.put("imageList", imgList);
		
		content = TemplateManager.getProductContent(inputMap);
		bodyComposite.getMainText().setText(content);
		bodyComposite.getBtnHtmlCopy().setEnabled(true);
		bodyComposite.getBtnTextCopy().setEnabled(true);
		bodyComposite.getBtnSaveImage().setEnabled(true);
	}
}
