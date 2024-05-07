package controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.MemberService;
import service.BookService;
import util.ScanUtil;
import util.View;
import view.Print;
 
public class MainController extends Print {
	
	static public Map<String, Object> sessionStorage = new HashMap<>();
	// 개발 완료됐을 때, 사용하지 않는 개발용 메시지들을 한꺼번에 비활성화하기 위한 용도
	boolean debug = true;
	
	MemberService memberService = MemberService.getInstance();
	BookService bookService = BookService.getInstance();

	public static void main(String[] args) {
		new MainController().start();
	}

	private void start() {
		View view = View.HOME;
		while (true) {
			switch (view) {
			case HOME:
				view = home();
				break;
			case ADMIN:
				view = admin();
				break;
			case LOGIN:
				view = login();
				break;
			case BOOK_HOME:
				view = bookHome();
				break;
			case BOOK_LIST:
				view = boolList();
				break;
			case BOOK_UPDATE:
				view = bookUpdate();
				break;
			case HOLD_HOME:
				view = holdHome();
				break;
			case HOLD_LIST:
				view = holdList();
				break;
			default:
				break;
			}
		}
	}
	
	
	private View holdList() {
		// 데이터 페이지 나누기
		int page = 1;
		
		if(sessionStorage.containsKey("page")) page = (int) sessionStorage.remove("page");
		
		int startNo = 1 + (page-1)*5;
		int endNo = page*5;
		
		List<Object> param = new ArrayList<Object>();
		param.add(startNo);
		param.add(endNo);
		
		List<Map<String, Object>> list = bookService.holdList(param);
		
		for (Map<String, Object> map : list) {
			System.out.println(map);
		}
		
		System.out.println("<이전페이지\t\t다음페이지>");
		System.out.println("1. 홈");
		
		String sel = ScanUtil.nextLine("메뉴 : ");
		
		if(sel.equals("<")) {
			if(page!=1) page--;
			sessionStorage.put("page", page);
			return View.HOLD_LIST;
		}
		else if(sel.equals(">")) {
			page++;
			sessionStorage.put("page", page);
			return View.HOLD_LIST;
		}
		else if(sel.equals("1")) {
			return View.HOLD_HOME;
		}
		else return View.HOLD_LIST;
		
	}
	
	
	private View holdHome() {
		System.out.println("1. 도서 구매");
		System.out.println("2. 도서 폐기");
		System.out.println("3. 보유도서 출력");
		System.out.println("4. 관리자 홈");
		
		int sel = ScanUtil.menu();
		
		if(sel==1) return View.HOLD_INSERT;
		else if(sel==2) return View.HOLD_DELETE;
		else if(sel==3) return View.HOLD_LIST;
		else if(sel==4) return View.ADMIN;
		else return View.BOOK_HOME;
	}
	
	
	private View bookUpdate() {
		List<Map<String, Object>> bookList = bookService.bookList();
		
//		for(Map<String, Object> map : bookList) {
//			BigDecimal bookNo = (BigDecimal) map.get("BOOK_NO");
//			String title = (String) map.get("TITLE");
//			String content = (String) map.get("CONTENT");
//			String pubdate = (String) map.get("PUBDATE");
//			System.out.println(bookNo+"\t"+title+"\t"+content+"\t"+pubdate);
//		}
		// 위의 긴 코드를 Print 클래스에서 메서드로 받아옴
		bookListPrint(bookList);
		
		// 1번은 제목만, 2번은 내용만, 3번은 제목과 내용 모두 바꿈
		int bookNo = ScanUtil.nextInt("책 번호 선택");
		System.out.println("1. 제목");
		System.out.println("2. 내용");
		System.out.println("3. 전체");
		
		int sel = ScanUtil.menu();
		
		List<Object> param = new ArrayList<Object>();
		
		if (sel==1 || sel==3) {
			String title = ScanUtil.nextLine("제목 : ");
			param.add(title);
		}
		if (sel==2 || sel==3) {
			String content = ScanUtil.nextLine("내용 : ");
			param.add(content);
		}
		param.add(bookNo);
		
		bookService.bookUpdate(param, sel);
		
		return View.BOOK_LIST;
	}
	
	
	private View boolList() {
		List<Map<String, Object>> bookList = bookService.bookList();
		
//		for(Map<String, Object> map : bookList) {
//			BigDecimal bookNo = (BigDecimal) map.get("BOOK_NO");
//			String title = (String) map.get("TITLE");
//			String content = (String) map.get("CONTENT");
//			String pubdate = (String) map.get("PUBDATE");
//			System.out.println(bookNo+"\t"+title+"\t"+content+"\t"+pubdate);
//		}
		// 위의 긴 코드를 Print 클래스에서 메서드로 받아옴
		bookListPrint(bookList);
		
		return View.BOOK_HOME;
	}
	
	
	private View bookHome() {
		System.out.println("1. 도서 정보 전체 출력");
		System.out.println("2. 도서 정보 추가");
		System.out.println("3. 도서 정보 변경");
		System.out.println("4. 도서 정보 삭제");
		System.out.println("5. 관리자 홈");

		int sel = ScanUtil.menu();
		
		if (sel==1) return View.BOOK_LIST;
		else if (sel==2) return View.BOOK_INSERT;
		else if (sel==3) return View.BOOK_UPDATE;
		else if (sel==4) return View.BOOK_DELETE;
		else if (sel==5) return View.ADMIN;
		else return View.BOOK_HOME;
	}
	
	
	private View admin() {
		
		if(!sessionStorage.containsKey("admin")) {
			sessionStorage.put("role", 2);
			return View.LOGIN;
		}
		
//		System.out.println("1. 도서 정보");
//		System.out.println("2. 도서 보유");
		printAdmin();
		
		int sel = ScanUtil.menu();
		
		if (sel==1) return View.BOOK_HOME;
		else if (sel==2) return View.HOLD_HOME;
		else return View.ADMIN;
	}
	
	
	private View login() {
		
//		String id = ScanUtil.nextLine("ID >>");
//		String pw = ScanUtil.nextLine("PASS >>");

		String id = "admin";
		String pw = "1234";
		
		int role = (int) sessionStorage.get("role");
		
		List<Object> param = new ArrayList<Object>();
		param.add(id);
		param.add(pw);
		param.add(role);
		
		boolean loginChk = memberService.login(param, role);
		
		if(!loginChk) {
			System.out.println("로그인 실패");
			return View.LOGIN;
		}
		if(role == 1) return View.MEMBER;
		else return View.ADMIN;
	}
	
	
	private View home() {
//		if (debug) System.out.println("==========홈==========");
//		
//		System.out.println("1. 관리자");
//		System.out.println("2. 일반 회원");
		printHome();
		
		int sel = ScanUtil.menu();
		
		if (sel==1) return View.ADMIN;
//		else if (sel==2) return View.PROD_NORMALMEM;
		return View.HOME;
	}
	
	
}