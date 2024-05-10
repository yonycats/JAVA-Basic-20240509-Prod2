package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class ProdDao {
	private static ProdDao instance;

	private ProdDao() {

	}

	public static ProdDao getInstance() {
		if (instance == null) {
			instance = new ProdDao();
		}
		return instance;
	}
	
	JDBCUtil jdbc = JDBCUtil.getInstance();


	public List<Map<String, Object>> prodList() {
		String sql = " SELECT NO, NAME, TYPE, PRICE\r\n" + 
				"FROM JAVA_PROD\r\n" + 
				"ORDER BY NO";
		
		return jdbc.selectList(sql);
	}
	
	
	public List<Map<String, Object>> adminProdList() {
		String sql = " SELECT NO, NAME, TYPE, PRICE, DELYN\r\n" + 
					 "FROM JAVA_PROD\r\n" + 
					 "ORDER BY NO";
		
		return jdbc.selectList(sql);
	}

	
	public void prodInsert(List<Object> param) {
		String sql = " INSERT INTO JAVA_PROD\r\n" + 
					 "VALUES ( (SELECT NVL(MAX(NO),0)+1 FROM JAVA_PROD), ?, ?, ?, 'N' )";
		
		jdbc.update(sql, param);
	}

	
	public int prodDelete(List<Object> param) {
		String sql = "UPDATE JAVA_PROD\r\n" + 
				 	 "SET DELYN = 'Y'\r\n" + 
				 	 "WHERE NO = ?";
		
		return jdbc.update(sql, param);
	}

	public void prodUpdate(List<Object> param) {
		String sql = "UPDATE JAVA_PROD\r\n" + 
					 "SET NAME = ?,\r\n" + 
					 "    TYPE = ?,\r\n" + 
					 "    PRICE = ?\r\n" + 
					 "    WHERE NO = ?";
		
		jdbc.update(sql, param);
	}

	
	
}
