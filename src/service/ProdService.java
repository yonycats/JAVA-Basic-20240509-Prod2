package service;

import java.util.List;
import java.util.Map;

import dao.ProdDao;

public class ProdService {
	private static ProdService instance;

	private ProdService() {

	}

	public static ProdService getInstance() {
		if (instance == null) {
			instance = new ProdService();
		}
		return instance;
	}
	
	
	ProdDao prodDao = ProdDao.getInstance();

	
	public List<Map<String, Object>> prodList() {
		return prodDao.prodList();
	}
	
	
	public List<Map<String, Object>> adminProdList() {
		return prodDao.adminProdList();
	}

	
	public void prodInsert(List<Object> param) {
		prodDao.prodInsert(param);
	}

	
	public int prodDelete(List<Object> param) {
		return prodDao.prodDelete(param);
	}

	
	public void prodUpdate(List<Object> param) {
		prodDao.prodUpdate(param);
	}

	
	

	


	

	
	
	
	
}
