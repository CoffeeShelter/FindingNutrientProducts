package management.product;

import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class ProductDAO {
	private static SqlSessionFactory sqlMapper = null;

	private static SqlSessionFactory getInstance() {
		if(sqlMapper == null){
			try {
				String resource = "mybatis/SqlMapConfig.xml";
				Reader reader = Resources.getResourceAsReader(resource);
				sqlMapper = new SqlSessionFactoryBuilder().build(reader);
				reader.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sqlMapper;
	}
	
	// 전체 상품 정보 조회
	public List<ProductVO> selectAllProductList(){
		if(countProduct() == 0) {
			return null;
		}
		
		sqlMapper = getInstance();
		SqlSession session = sqlMapper.openSession();
		List<ProductVO> productList = null;
		productList = session.selectList("mapper.product.selectAllProduct");
		return productList;
	}
	
	// 상품 전체 개수 조회
	public int countProduct(){
		sqlMapper = getInstance();
		SqlSession session = sqlMapper.openSession();
		int count = 0;
		count = session.selectOne("mapper.product.countProduct");
		return count;
	}
	
	// 상품 최대 ID 값 조회
	public int maxProductId(){
		if(countProduct() == 0) {
			return 0;
		}
		
		sqlMapper = getInstance();
		SqlSession session = sqlMapper.openSession();
		int maxId = 0;
		maxId = session.selectOne("mapper.product.maxProductId");
		return maxId;
	}
	
	// 중복되는 상품 정보 확인
	// 이미지 주소 존재 여부 검사
	public int inProductByImage(String image){
		if(countProduct() == 0) {
			return 0;
		}
		
		sqlMapper = getInstance();
		SqlSession session = sqlMapper.openSession();
		int result = 0;
		result = session.selectOne("mapper.product.inProductByImage", image);
		return result;
	}
	
	// 상품정보 추가
	public int insertProduct(Map<String, Object> productMap){		
		sqlMapper = getInstance();
		SqlSession session = sqlMapper.openSession();
		int result = 0;
		
		// 이미 존재하는 상품인 경우 추가하지 않음
		if(inProductByImage((String)productMap.get("image")) == 1) {
			return -1;
		}
		
		result = session.insert("mapper.product.insertProduct", productMap);
		session.commit();
		return result;
	}
}
