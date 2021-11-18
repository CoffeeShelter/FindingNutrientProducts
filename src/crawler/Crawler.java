package crawler;

import java.util.ArrayList;

public interface Crawler {
	
	// 상품 주소 가져오기
	public ArrayList<String> getProductUrl(int page);
}
