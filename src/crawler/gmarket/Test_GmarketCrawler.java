package crawler.gmarket;

import java.util.ArrayList;

import crawler.Crawler;
import textminer.gmarket.GmarketTextMiner;

public class Test_GmarketCrawler {

	public static void main(String[] args) {
		Crawler crawler = new GmarketCrawler();
		ArrayList<String>urlList = crawler.getProductUrl(1);
		
		GmarketTextMiner textMiner = new GmarketTextMiner();
		
		for(int i = 0; i < urlList.size(); i++) {
			textMiner.excute(urlList.get(i));
		}
		
	}

}
