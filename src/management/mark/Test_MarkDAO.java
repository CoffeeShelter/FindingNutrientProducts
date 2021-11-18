package management.mark;

import java.util.ArrayList;

public class Test_MarkDAO {

	public static void main(String[] args) {
		MarkDAO markDAO = new MarkDAO();
		ArrayList<MarkVO> markList = new ArrayList<>();
		
		markList = (ArrayList<MarkVO>) markDAO.selectAllMemberList();
		
		for(MarkVO mark : markList) {
			System.out.println("[인증 국가] : " + mark.getCountry());
			System.out.println("[인증마크 종류] : " + mark.getKind());
			System.out.println("[인증마크 설명] : " + mark.getInfo());
			System.out.println("==================");
		}

	}

}
