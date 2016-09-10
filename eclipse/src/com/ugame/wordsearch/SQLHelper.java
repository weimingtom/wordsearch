package com.ugame.wordsearch;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class SQLHelper {
    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private static final String JDBC_URL = 
	"jdbc:mysql://localhost:3306/wordsearch?" + 
	//"autoReconnect=true";
	"autoReconnect=true&" +
	"useUnicode=true&" + 
	"characterEncoding=utf-8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    private static final String QUERY_NUM_STATE = "SELECT COUNT(id) FROM words %s;";
    private static final String QUERY_STATE = "SELECT * FROM words %s LIMIT %d,%d";
    
    private static ComboPooledDataSource pool = null;
    private Connection conn = null;
    private Statement stmt = null;

    public SQLHelper() {
	if (pool == null) {
	    try {
		pool = new ComboPooledDataSource();
		pool.setDriverClass(DRIVER_CLASS);
		pool.setJdbcUrl(JDBC_URL);
		pool.setUser(USERNAME);
		pool.setPassword(PASSWORD);
		pool.setAcquireIncrement(3);
		pool.setMaxPoolSize(30);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
    }

    public SQLResult query(int page, int numPerPage, String condition) {
	SQLResult result = new SQLResult();
	try {
	    conn = pool.getConnection();
	    stmt = conn.createStatement();
	    result.wordTotal = queryNum(condition);
	    if(numPerPage <= 0) {
		result.numPerPage = 100;
	    } else {
		result.numPerPage = numPerPage;
	    }
	    result.pageTotal = (int)Math.ceil((double)result.wordTotal / numPerPage);
	    if(page <= 0 || page > result.pageTotal) {
		result.p = 1;
	    } else {
		result.p = page;
	    }
	    queryData(result.resultList, result.p, result.numPerPage, condition);
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    closeDBConn();
	}
	return result;
    }
    
    public int queryNum(String condition) throws SQLException {
	String sql = null;
	sql = String.format(QUERY_NUM_STATE, condition);
	System.out.println(sql);
	ResultSet rset = stmt.executeQuery(sql);
	if (rset != null && rset.next()) {
	    return rset.getInt(1);
	}
	return 0;
    }
    
    public void queryData(List list, int page, int numPerPage, String condition) throws SQLException {
	String sql = String.format(QUERY_STATE,
		condition,
		(page - 1) * numPerPage,
		numPerPage);
	System.out.println(sql);
	ResultSet rset = stmt.executeQuery(sql);
	while (rset != null && rset.next()) {
	    String[] item = new String[6];
	    item[0] = readDBField(rset, "id");
	    item[1] = readDBField(rset, "word");
	    list.add(item);
	}
    }

    private String readDBField(ResultSet rset, String fieldName)
	    throws SQLException {
	try {
	    byte[] value = rset.getBytes(fieldName);
	    if (value == null) {
		return "";
	    } else {
		String ret = new String(value, "UTF-8");
		// System.out.println(ret);
		return ret;
	    }
	} catch (UnsupportedEncodingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return "";
    }

    private void closeDBConn() {
	if (stmt != null) {
	    try {
		stmt.close();
	    } catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	if (conn != null) {
	    try {
		conn.close();
	    } catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }
}
