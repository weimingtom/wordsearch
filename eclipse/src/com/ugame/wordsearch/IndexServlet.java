package com.ugame.wordsearch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TestServlet
 */
public class IndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private SQLHelper helper = new SQLHelper();
    private MongoHelper mongohelper = new MongoHelper();
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexServlet() {
	super();
	// TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	// TODO Auto-generated method stub
	
	// start
	Long startTime = Calendar.getInstance().getTimeInMillis(); 
	System.out.println(this.getClass().toString() + ".doGet()");
	
	// parameters
	String word = checkParam(request.getParameter("word"));
	Integer p = checkParamInt(request.getParameter("p"));

	// database query 
	SQLResult result = mysqlQuery(word, p);
	//SQLResult result = mongoQuery(word, p);
	
	// normal attribute
	request.setAttribute("pass", "pass");
	request.setAttribute("word", word);
	request.setAttribute("startTime", startTime);
	
	// database attribute
	request.setAttribute("p", result.p);
	request.setAttribute("wordTotal", result.wordTotal);
	request.setAttribute("pageTotal", result.pageTotal);
	request.setAttribute("currentPage", result.p);
	request.setAttribute("resultList", result.resultList);
	
	//redirect
	request.getRequestDispatcher("/jsp/index.jsp").forward(request,
		response);
    }

    private SQLResult mysqlQuery(String word, Integer p) {
	String[] names = new String[] {"word"};
	String[] values = new String[] {word};
	String condition = paramToSQLCondition(names, values);
	System.out.println("sql condition : " + condition);
	SQLResult result = helper.query(p, 15, condition);
	return result;
    }
    
    private SQLResult mongoQuery(String word, Integer p) {
	SQLResult result = mongohelper.query(word, p, 15);
	return result;
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	// TODO Auto-generated method stub
    }

    
    private String checkParam(String param) {
	try {
	    if (param != null)
		return new String(param.getBytes("ISO-8859-1"), "utf-8");
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private Integer checkParamInt(String param) {
	String ret = checkParam(param);
	if (ret == null) {
	    return new Integer(0);
	} else {
	    return Integer.parseInt(ret);
	}
    }
    
    private String paramToSQLCondition(String[] names, String[] values) {
	if(names == null || values == null || names.length != values.length) {
	    return "";
	}
	String ret = "";
	int num = 0;
	for(int i = 0; i < names.length; i++) {
	    if(num == 0 && values[i] != null && !values[i].equals("")) {
		ret += names[i] + " REGEXP " + "'" + values[i] + "'";
		num++;
	    } else if(values[i] != null && !values[i].equals("")){
		ret += " AND " + names[i] + " REGEXP " + "'" + values[i] + "'";
		num++;
	    }
	}
	if(ret.equals("")) {
	    return "";
	}
	return " WHERE " + ret;
    }
}
