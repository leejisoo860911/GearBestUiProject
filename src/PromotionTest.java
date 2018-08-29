import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gb.utils.HttpConnectionUtil;


public class PromotionTest {

	public static void main(String[] args) {
		try {
			printData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void printData() throws IOException {
		Document doc  = Jsoup.connect("https://www.gearbest.com/promotion-priority-line-special-826.html?11").get();
		Elements list = doc.getElementsByClass("pr showZheKou js_subjectGoodItem subjectGoodItem subject_newLi ");
		for(Element ele : list) {
			String title = ele.getElementsByClass("goods-title").first().text();
			String link = ele.getElementsByClass("buyLink").first().attr("href");
			String price = ele.getElementsByClass("goods-price").first().text().replace("After Coupon: ", "");
			String coupon = ele.getElementsByClass("couponInfo google_newCouponInfo").first().text();
			System.out.println(title + "\t" + link + "\t" + price + "\t" + coupon );
		}
		
	}
	

}
