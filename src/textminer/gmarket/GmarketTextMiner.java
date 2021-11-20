package textminer.gmarket;

import java.io.IOException;
import java.util.Vector;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import textminer.TextMiner;

public class GmarketTextMiner implements TextMiner {

	private String productURL = null;
	private Connection conn = null;
	private Document html = null;

	public GmarketTextMiner(String url) {
		this.productURL = url;

		this.conn = Jsoup.connect(this.productURL);
		try {
			this.html = this.conn.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 상품 명 가져옴
	public String getTitle() {
		String selector = "#itemcase_basic > div > h1";

		Elements elements = this.html.select(selector);
		String title = elements.text();

		return title;
	}

	// 상품 가격 가져옴
	public String getPrice() {
		String selector = "#itemcase_basic > div.box__item-title > p > span > strong";

		Elements elements = this.html.select(selector);
		String title = elements.text();

		return title;
	}

	// 상품 이미지 가져옴 (url 형식)
	public Vector<String> getImage() {
		String selector = "#container > div.item-topinfowrap > div.thumb-gallery > div.box__viewer-container > ul.viewer > li > a > img";

		Vector<String> imagePath = new Vector<>();

		Elements elements = this.html.select(selector);

		for (Element element : elements) {
			imagePath.add(element.attr("src"));
		}

		if (imagePath.size() > 0 && imagePath.size() != 1) {
			imagePath.remove(0);
		}

		return imagePath;
	}
}
