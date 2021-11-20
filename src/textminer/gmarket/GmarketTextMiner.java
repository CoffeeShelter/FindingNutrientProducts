package textminer.gmarket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import management.product.ProductDAO;
import management.product.ProductVO;
import textminer.TextMiner;

public class GmarketTextMiner implements TextMiner {

	private String productURL = null;
	private Connection conn = null;
	private Document html = null;

	public GmarketTextMiner() {

	}
	
	public void excute(String url) {
		ProductVO productVO = getProduct(url);
		if(productVO != null) {
			insertDB(productVO);
			System.out.println(url + " >> 추가완료");
		}else {
			System.out.println("[DB] : 상품정보 저장 실패");
		}
	}
	
	public void insertDB(ProductVO productVO) {
		ProductDAO productDAO = new ProductDAO();
		
		Map<String, Object> productMap = new HashMap<>();
		productMap.put("id", productVO.getId());
		productMap.put("name", productVO.getName());
		productMap.put("url", productVO.getUrl());
		productMap.put("price", productVO.getPrice());
		productMap.put("image", productVO.getImage());
		productMap.put("pDate", productVO.getPdate());
		
		int result = productDAO.insertProduct(productMap);
		if(result == -1) {
			System.out.println("이미 존재하는 상품 추가 x");
		}
	}
	
	public ProductVO getProduct(String url) {
		ProductDAO productDAO = new ProductDAO();
		ProductVO product = new ProductVO();

		this.productURL = url;
		this.conn = Jsoup.connect(this.productURL);
		try {
			this.html = this.conn.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int id = productDAO.maxProductId() + 1;
		String name = getName();
		String price = getPrice();
		String image = getImage();
		
		// 상품 DB에 null값이 존재 해서는 안됨
		if(name.length()==0 || price.length()==0 || image.length()==0) {
			return null;
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String utilDate = simpleDateFormat.format(new java.util.Date());
		java.sql.Date sqlDate = java.sql.Date.valueOf(utilDate);
		
		product.setId(id);
		product.setName(name);
		product.setPrice(price);
		product.setImage(image);
		product.setUrl(url);
		product.setPdate(sqlDate);

		return product;
	}

	// 상품 명 가져옴
	public String getName() {
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
	public String getImage() {
		String selector = "#container > div.item-topinfowrap > div.thumb-gallery > div.box__viewer-container > ul.viewer > li > a > img";

		Vector<String> imagePath = new Vector<>();

		Elements elements = this.html.select(selector);

		for (Element element : elements) {
			imagePath.add(element.attr("src"));
		}

		if (imagePath.size() > 0 && imagePath.size() != 1) {
			imagePath.remove(0);
		}

		return imagePath.get(0);
	}
}
