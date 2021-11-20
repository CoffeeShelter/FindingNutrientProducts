package crawler.gmarket;

import java.util.ArrayList;
import java.util.Vector;

import crawler.Crawler;
import textminer.gmarket.GmarketTextMiner;

public class Test_GmarketCrawler {

	public static void main(String[] args) {
		Crawler crawler = new GmarketCrawler();
		ArrayList<String>urlList = crawler.getProductUrl(1);
		
		GmarketTextMiner textMiner = new GmarketTextMiner(urlList.get(0));
		Vector<String> images = textMiner.getImage();
		
		System.out.println(images.get(0));
		
	}

}
