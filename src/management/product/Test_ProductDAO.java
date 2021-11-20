package management.product;

public class Test_ProductDAO {

	public static void main(String[] args) {
		ProductDAO productDAO = new ProductDAO();

		System.out.println(productDAO.inProductByImage("http://gdimg.gmarket.co.kr/2127469420/still/600?ver=1626415943"));
		System.out.println(productDAO.countProduct());

	}

}
