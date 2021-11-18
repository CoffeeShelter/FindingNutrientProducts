package crawler.gmarket;

import java.util.ArrayList;

import crawler.Crawler;

public class Test_GmarketCrawler {

	public static void main(String[] args) {
		Crawler crawler = new GmarketCrawler();
		ArrayList<String >urlList = crawler.getProductUrl(2);
		
		int count = 0;
		for(String list : urlList)
		{
			System.out.println(list);
			count ++;
			if (count > 10)
				break;
		}
	}

}
