/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jactiverecord;

import com.jactiverecord.ConnectionManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author maxazan
 */
public class QueryBuilderTest extends TestCase {

    Long testLong = new Long("123456789123456789");
    String testString = "Test string";
    String secondString = "Second string";
    String testText = "Test text";
    String testEnum = "normal";
    String testDateString = "2010-11-10";
    String testDateTimeString = "2010-11-10 20:40:22";
    String testDateTimestampString = "2010-11-10 20:40:23";

    public QueryBuilderTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ConnectionManager.connect("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/test", "root", "");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
    //SELECT
    QueryResult result=new Query().select("name").from("table").where("name=?","Jack").order("name","asc").limit(10).execute();
    QueryResult result=Query.executeQuery("select * from table where name=? order by name asc limit 10","Jack");
    result.size();
    for(ResultRow row : result){
        row.get("name");
        row.get("id");
    }
    
    query.add(Query);
    
    query.join("table","t",Query.JOIN_LEFT).where("t.dd=?",3)
    
    //INSERT
    QueryResult result=new Query().insert("table").set("name","Jack").set("cname","Vorobey").set("created_at",new Date()).execute();
    QueryResult result=Query.executeQuery("insert into table (name, cname, created_at) values (?, ?, ?)", "Jack", "Vorobey", new Date());
    Integer insertedId=result.getLastInsertId();
    
    //UPDATE
    QueryResult result=new Query().update("table").set("name","Jack").where("cname=?", "Vorobey").execute();
    QueryResult result=Query.executeQuery("update table set name=? where cname=?", "Jack", "Vorobey");
    Integer countUpdatedRows=result.getCountAffectedRows();

    //DELETE
    QueryResult result=new Query().delete("table").where("cname=?", "Vorobey").execute();
    QueryResult result=Query.executeQuery("delete from table where cname=?", "Vorobey");
    Integer countDeletedRows=result.getCountAffectedRows();
    */
    
    /**
     * Test of from method, of class QueryBuilder.
     */
//    public void testFrom() {
//        //.update("test").set("id",5).where("id",7).write()// return count updated rows
//        //.insert("test").set("id",5).write() // return last_insert_id
//        //.from("test").select("*").read() // return rs
//        try {
//            assertTrue(new Query().from("test").executeSelect().next());
//        } catch (SQLException ex) {
//            fail(ex.getMessage());
//        }
//    }

//    /**
//     * Test of update method, of class QueryBuilder.
//     */
//    public void testUpdate() {
//        System.out.println("update");
//        QueryBuilder instance = new QueryBuilder();
//        QueryBuilder expResult = null;
//        QueryBuilder result = instance.update();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of inset method, of class QueryBuilder.
//     */
//    public void testInset() {
//        System.out.println("inset");
//        QueryBuilder instance = new QueryBuilder();
//        QueryBuilder expResult = null;
//        QueryBuilder result = instance.inset();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
    /**
     * Test of where method, of class QueryBuilder.
     */
//    public void testWhere() {
//        try {
//            Query queryBuilder = new Query();
//            assertTrue(queryBuilder.from("test").where("id=?", 1).executeSelect().next());
//            System.out.println(queryBuilder.getLastQuery());
//            //assertFalse(new QueryBuilder().from("test").where("id=?", -1).executeSelect().next());
//        } catch (SQLException ex) {
//            fail(ex.getMessage());
//        }
//
//    }
//
//    /**
//     * Test of order method, of class QueryBuilder.
//     */
//    public void testOrder() {
//        try {
//            Query queryBuiled = new Query();
//            ResultSet rs = queryBuiled.from("test").order("string", "desc").order("id", "asc").executeSelect();
//            assertTrue(rs.next());
//            assertTrue(rs.getInt("id") == 1);
//            queryBuiled.clean();
//            rs = queryBuiled.from("test").order("string", "asc").order("id", "asc").executeSelect();
//            assertTrue(rs.next());
//            assertTrue(rs.getString("string").equals(this.secondString));
//
//        } catch (SQLException ex) {
//            fail(ex.getMessage());
//        }
//    }
//
//    /**
//     * Test of limit method, of class QueryBuilder.
//     */
//    public void testLimit() {
//        try {
//            Query queryBuiled = new Query();
//            ResultSet rs = queryBuiled.from("test").limit(1).executeSelect();
//            assertTrue(rs.next());
//            assertFalse(rs.next());
//            queryBuiled.clean();
//            rs = queryBuiled.from("test").limit(2).executeSelect();
//            assertTrue(rs.next());
//            assertTrue(rs.next());
//            assertFalse(rs.next());
//            rs = queryBuiled.from("test").limit(null).executeSelect();
//            assertTrue(rs.next());
//            assertTrue(rs.next());
//        } catch (SQLException ex) {
//            fail(ex.getMessage());
//        }
//    }
//
//    /**
//     * Test of offset method, of class QueryBuilder.
//     */
//    public void testOffset() {
//        try {
//            Query queryBuiled = new Query();
//            ResultSet rs = queryBuiled.from("test").limit(2).offset(0).executeSelect();
//            assertTrue(rs.next());
//            assertTrue(rs.next());
//            Integer testId = rs.getInt("id");
//            rs = queryBuiled.from("test").limit(1).offset(1).executeSelect();
//            assertTrue(rs.next());
//            assertEquals(rs.getInt("id"), testId.intValue());
//        } catch (SQLException ex) {
//            fail(ex.getMessage());
//        }
//    }
//
//    /**
//     * Test of in condition in where method, of class QueryBuilder.
//     */
//    public void testInCondition() {
//
//        Query queryBuiled = new Query();
//        List<Object> list = new ArrayList<Object>();
//        list.add(1);
//        list.add(2);
//        List<Object> list2 = new ArrayList<Object>();
//        list2.add(this.testString);
//        list2.add(this.secondString);
//        try {
//            ResultSet rs = queryBuiled.select("string").from("test").where("id in (?) and string in (?)", list, list2).executeSelect();
//            assertTrue(rs.next());
//            assertTrue(rs.next());
//            assertFalse(rs.next());
//        } catch (SQLException ex) {
//            fail(ex.getMessage());
//        }
//
//    }
}
