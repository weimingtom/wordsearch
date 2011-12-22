package com.ugame.wordsearch;

import java.net.UnknownHostException;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoHelper {
    
    public MongoHelper() {
	
    }
    
    public SQLResult query(String word, int page, int numPerPage) {
	SQLResult result = new SQLResult();
	try {
	    Mongo m = new Mongo("127.0.0.1" , 27017);
	    DB db = m.getDB("test");
	    DBCollection coll = db.getCollection("wordsearch");
	    BasicDBObject query = null;
	    if (word != null && word.length() != 0) {
		query = new BasicDBObject("word", Pattern.compile(word));
	    }
	    BasicDBObject orderBy = new BasicDBObject("_id", 1);
	    result.wordTotal = (int) coll.getCount(query);
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
	    //System.out.println("count:" + coll.getCount(query));
	    DBCursor cur = coll.find(query);
	    cur.sort(orderBy).skip((result.p - 1) * result.numPerPage).limit(result.numPerPage);
	    while(cur.hasNext()) {
		String[] item = new String[6];
		DBObject obj = cur.next();
		item[0] = obj.get("_id").toString();
		item[1] = obj.get("word").toString();
		result.resultList.add(item);
		//System.out.println(cur.next().get("word"));
	    }
	} catch (UnknownHostException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (MongoException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return result;
    }
}
