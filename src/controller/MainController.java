package controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.MemberService;
import service.ProdService;
import util.ScanUtil;
import util.View;
import view.Print;
 
public class MainController extends Print {
	
	static public Map<String, Object> sessionStorage = new HashMap<>();
	
	MemberService memberService = MemberService.getInstance();
	ProdService prodService = ProdService.getInstance();
	
	boolean debug = true;

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
			case LOGIN:
				view = login();
				break;
			case SIGN:
				view = sign();
				break;
			case ADMIN:
				view = admin();
				break;
			case ADMIN_PROD_LIST:
				view = adminProdList();
				break;
			case ADMIN_PROD_ALL_LIST:
				view = adminProdAllList();
				break;
			case ADMIN_PROD_MANAGE:
				view = adminProdManage();
				break;
			case MEMBER:
				view = member();
				break;
			case MYINFO:
				view = myInfo();
				break;
			case PROD_LIST:
				view = prodList();
				break;
			case PROD_DETAIL:
				view = prodDetail();
				break;
			case PROD_INSERT:
				view = prodInsert();
				break;
			case PROD_UPDATE:
				view = prodUpdate();
				break;
			case PROD_DELETE:
				view = prodDelete();
				break;
			default:
				break;
			}
		}
	}
	
	
	private View prodDelete() {
		adminProdList();
		
		List<Object> param = new ArrayList<Object>();
		
		int prodNo = ScanUtil.nextInt("번호 선택 : ");
		param.add(prodNo);
		
		int result = prodService.prodDelete(param);
		
		if(result == 0) {
			System.out.println("존재하지 않는 상품 번호입니다.");
		} else if(result != 0) {
			System.out.println("상품 삭제를 성공했습니다.");
		}
		
		return View.ADMIN_PROD_ALL_LIST;
	}

	
	private View prodUpdate() {
		adminProdList();
		
		int prodNo = ScanUtil.nextInt("상품 번호 : ");
		String name = ScanUtil.nextLine("상품명 : ");
		String type = ScanUtil.nextLine("타입 : ");
		int price = ScanUtil.nextInt("가격 : ");
		
		List<Object> param = new ArrayList<Object>();
		
		param.add(name);
		param.add(type);
		param.add(price);
		param.add(prodNo);
		
		prodService.prodUpdate(param);
		
		return View.ADMIN_PROD_ALL_LIST;
	}
	

	private View prodInsert() {
		adminProdList();
		
		List<Object> param = new ArrayList<Object>();
		
		String name = ScanUtil.nextLine("상품명 : ");
		String type = ScanUtil.nextLine("상품 타입 : ");
		String price = ScanUtil.nextLine("가격 : ");
		
		param.add(name);
		param.add(type);
		param.add(price);
		
		prodService.prodInsert(param);
		
		return View.ADMIN_PROD_ALL_LIST;
	}
	

	private View prodDetail() {
		if (debug) System.out.println("상품 상세 보기");
		
		System.out.println("1. 상품 삭제");
		System.out.println("2. 상품 정보 변경");
		System.out.println("3. 상품 리스트");
		
		int sel = ScanUtil.menu();
		
		if (sel==1) return View.PROD_DELETE;
		else if (sel==2) return View.PROD_UPDATE;
		else if (sel==3) return View.ADMIN_PROD_LIST;
		return View.ADMIN;
	}

	
	private View adminProdManage() {
		if (debug) System.out.println("상품 관리 게시판");
		
		System.out.println("1. 상품 전체 리스트");
		System.out.println("2. 상품 추가");
		
		int sel = ScanUtil.menu();
		
		if (sel==1) return View.ADMIN_PROD_ALL_LIST;
		else if (sel==2) return View.PROD_INSERT;
		return View.ADMIN;
	}
	

	private View adminProdAllList() {
		if (debug) System.out.println("상품 전체 리스트");
		
		System.out.println("1. 상품 상세 보기");
		System.out.println("2. 상품 관리 게시판");
		
		int sel = ScanUtil.menu();
		
		if (sel==1) return View.PROD_DETAIL;
		else if (sel==2) return View.ADMIN_PROD_MANAGE;
		return View.ADMIN;
	}

	
	private View adminProdList() {
		if (debug) System.out.println("===상품 상세보기");
		
		List<Map<String, Object>> param = prodService.adminProdList();
				
		for(Map<String, Object> map : param) {
		BigDecimal prodNo = (BigDecimal) map.get("NO");
		String name = (String) map.get("NAME");
		String type = (String) map.get("TYPE");
		BigDecimal price = (BigDecimal) map.get("PRICE");
		String delyn = (String) map.get("DELYN");
		System.out.println(prodNo+"\t"+name+"\t"+type+"\t"+price+"\t"+delyn);
		}
		
		int role = (int) sessionStorage.get("role");
		
		if(role == 1) return View.MEMBER;
		else if(role == 2) return View.ADMIN;
		return View.HOME;
	}
	
	
	private View prodList() {
		if (debug) System.out.println("상품 상세보기");
		
		List<Map<String, Object>> param = prodService.prodList();
				
		for(Map<String, Object> map : param) {
		BigDecimal prodNo = (BigDecimal) map.get("NO");
		String name = (String) map.get("NAME");
		String type = (String) map.get("TYPE");
		BigDecimal price = (BigDecimal) map.get("PRICE");
		System.out.println(prodNo+"\t"+name+"\t"+type+"\t"+price);
		}
		
		int role = (int) sessionStorage.get("role");
		
		if(role == 1) return View.MEMBER;
		else if(role == 2) return View.ADMIN;
		return View.HOME;
	}

	
	private View myInfo() {
		if (debug) System.out.println("내 정보 보기");
		
		Map<String, Object> myInfo = (Map<String, Object>) sessionStorage.get("member");
		
		System.out.println("이름 : "+myInfo.get("ID")+"\t 비밀번호 : "+myInfo.get("PASS")+"\t 이름 : "+myInfo.get("NAME"));
		
		return View.MEMBER;
	}

	
	private View sign() {
		if (debug) System.out.println("회원가입");
		
		List<Object> param = new ArrayList<Object>();
		String id = ScanUtil.nextLine("ID : ");
		String pw = ScanUtil.nextLine("PW : ");
		String name = ScanUtil.nextLine("이름 : ");
		param.add(id);
		param.add(pw);
		param.add(name);
		
		memberService.sign(param);
		
		System.out.println("회원가입에 성공하였습니다.");
		
		return View.HOME;
	}

	
	private View login() {
		
		String id = ScanUtil.nextLine("ID >> ");
		String pw = ScanUtil.nextLine("PASS >> ");
		
		int role = (int) sessionStorage.get("role");
		
		List<Object> param = new ArrayList<Object>();
		param.add(id);
		param.add(pw);
		param.add(role);
		
		boolean loginChk = memberService.login(param, role);
		
		if(!loginChk) {
			System.out.println("로그인 실패");
			return View.LOGIN;
		} else {
			System.out.println("로그인 성공");
		}
		if(role == 1) return View.MEMBER;
		else if(role == 2) return View.ADMIN;
		return View.HOME;
	}
	
	
	private View admin() {
		
		if(!sessionStorage.containsKey("admin")) {
			sessionStorage.put("role", 2);
			return View.LOGIN;
		}
		
		
		if (debug) System.out.println("=========관리자=========");
		
		System.out.println("1. 상품 전체 리스트 출력");
		System.out.println("2. 상품 추가");
		System.out.println("3. 로그아웃");
		
		int sel = ScanUtil.menu();
		
		if (sel==1) return View.ADMIN_PROD_ALL_LIST;
		else if (sel==2) return View.PROD_INSERT;
		else if (sel==3) {
			sessionStorage.remove("admin");
			return View.HOME;
		}
		else return View.ADMIN;
	}
	
	
	private View member() {
		
		if(!sessionStorage.containsKey("member")) {
			sessionStorage.put("role", 1);
			return View.LOGIN;
		}
		
		
		if (debug) System.out.println("=========일반회원=========");
		
		System.out.println("1. 내 정보 보기");
		System.out.println("2. 상품 리스트");
		System.out.println("3. 로그아웃");

		
		int sel = ScanUtil.menu();
		
		if (sel==1) return View.MYINFO;
		else if (sel==2) return View.PROD_LIST;
		else if (sel==3) {
			sessionStorage.remove("member");
			return View.HOME;
		}
		else return View.MEMBER;
	}
	
	
	private View home() {
		System.out.println("==========홈==========");
		
		System.out.println("1. 관리자");
		System.out.println("2. 일반 회원");
		System.out.println("3. 회원가입");
		
		int sel = ScanUtil.menu();
		
		if (sel==1) return View.ADMIN;
		else if (sel==2) return View.MEMBER;
		else if (sel==3) return View.SIGN;
		return View.HOME;
	}
	
	
}