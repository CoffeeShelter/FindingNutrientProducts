package crawler.gmarket;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import crawler.Crawler;

public class GmarketCrawler implements Crawler{
	
	private static final String mainURL = "https://browse.gmarket.co.kr/list?category=200001192&k=24&p=";
	private static final String CRAWLING_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36";
	private static final int CRAWLING_TIMEOUT = 200 * 1000;
	
	private ArrayList<String> productsURL = new ArrayList<>();
	
	public ArrayList<String> getProductUrl(int page) {
		execute(productsURL, page);
		
		return productsURL;
	}

	// 크롤러 실행
	public void execute(ArrayList<String> merchanDocURLList, int page) {
		int totalPageCnt = getTotalMerchandiseCnt(mainURL);
		System.out.println("총 페이지 수 : " + totalPageCnt);

		// 상품 상세문서 수집
		for (int i = 1; i <= page; i++) {
			// 상품 상세문서 수집
			String page_url = mainURL + i;
			setMerchandiseDocumentList(merchanDocURLList, page_url);
		}
		System.out.println("merchanDocURLList_size = " + merchanDocURLList.size());
	}

	// 총 상품 수 반환
	public int getTotalMerchandiseCnt(String merchanDocListURL) {
		int totalPageCnt = 0;
		Document doc = getDocument(merchanDocListURL, 0);
		String totalCount = doc.select(
				"#IamMasterFrameYesIam_ctl02_Pagination_Bottom_UserControl_lastPageLink")
				.text().replace(",", "");
		System.out.println("상품 총 개수 : " + totalCount);

		return totalPageCnt;
	}

	// 전체 상품 문서 설정
	private void setMerchandiseDocumentList(ArrayList<String> merchanURLList, String merchanDocListURL) {
		System.out.println(merchanDocListURL + " / 페이지 문서 수집 중...");
		try {
			// 상품 리스트 문서 획득
			Document merchanListDoc = getDocument(merchanDocListURL, 0);
			// 상품 리스트 세팅
			Elements merchanList = merchanListDoc
					.select("div#section__inner-content-body-container > div[module-design-id=15] > div");
			for (Element merchandise : merchanList) {
				String productURL = merchandise.select("div.box__item-container > div.box__image > a").attr("href");
				merchanURLList.add(productURL);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("null point 에러");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(merchanDocListURL + ": Nutrione 크롤링 오류");
		}
	}

	// 문서 반환
	private Document getDocument(String url, int cnt) {
		Document doc = null;

		// 시도회수 초과
		if (cnt > 10)
			return null;

		try {
			Connection conn = Jsoup.connect(url);
			conn.userAgent(CRAWLING_USER_AGENT);
			conn.timeout(CRAWLING_TIMEOUT);
			conn.header("Accept-Encoding", "gzip, deflate, br");
			conn.header("Accept-Language", "ko-KR,ko;q=0.9");
			doc = conn.execute().parse();
		} catch (HttpStatusException e) {
			if (e.getStatusCode() >= 500) {
				System.out.println("http " + e.getStatusCode() + " 에러 발생 재시도 url: " + url);
				sleep(500, 1000);
				return getDocument(url, cnt++);
			} else {
				System.out.println("Gmarket 크롤링 오류: " + e.getStatusCode());
				e.printStackTrace();
			}
		} catch (ConnectException e) {
			System.out.println("연결 타임 아웃 재시도 url: " + url);
			sleep(500, 1000);
			return getDocument(url, cnt++);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}
	
	protected synchronized Response execute(Connection conn) throws IOException {
		sleep(500, 1000);
		return conn.execute();
	}

	private long sleep(long min, long max) {
		try {
			long millis = ThreadLocalRandom.current().nextLong(min, max + 1l);
			Thread.sleep(millis);
			return millis;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
