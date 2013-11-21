/*
 * The MIT License
 *
 * Copyright 2013 maxazan.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.jactiverecord;

import com.jactiverecord.ConnectionManager;
import com.jactiverecord.Query;
import com.jactiverecord.QueryResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * 
 * 
 * @author maxazan
 */
public class QueryTest extends TestCase {

    public QueryTest(String testName) {
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

//    /**
//     * Test of clean method, of class Query.
//     */
//    public void testClean() {
//        System.out.println("clean");
//        Query instance = new Query();
//        instance.clean();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of getConnection method, of class Query.
     */
    public void testGetConnection() {
        Connection result = Query.getConnection();
        assertEquals(result, ConnectionManager.connection);
    }

    /**
     * Test of getIdentifierQuoteString method, of class Query.
     */
    public void testGetIdentifierQuoteString() throws Exception {
        String expResult = "`";
        String result = Query.getIdentifierQuoteString();
        assertEquals(expResult, result);
    }

//    /**
//     * Test of processValue method, of class Query.
//     */
//    public void testProcessValue() throws Exception {
//        PreparedStatement statement = Query.prepareStatement("select * from test where id=? limit ?", 1);
//        Query.processValue(statement, index, value);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of prepareStatement method, of class Query.
     */
    public void testPrepareStatement() {
        String query = "select * from test where id=? limit ?";
        PreparedStatement result;
        try {
            result = Query.prepareStatement("select * from test where id=? limit ?", 1);
            fail("Exception needed");
        } catch (SQLException ex) {

        }
        try {
            result = Query.prepareStatement("select * from test where 1", 1);
            fail("Exception needed");
        } catch (SQLException ex) {

        }
        try {
            result = Query.prepareStatement("select * from test where id=?", 1);
        } catch (SQLException ex) {
            fail("Exception ");
        }
        try {
            result = Query.prepareStatement("select * from test where 1");
        } catch (SQLException ex) {
            fail("Exception ");
        }
        try {
            result = Query.prepareStatement("select * from test where id=? limit ?", 1, 1);
        } catch (SQLException ex) {
            fail("Exception ");
        }
        try {
            List<Object> inParams = new ArrayList<Object>();
            result = Query.prepareStatement("select * from test where id in (?) limit ?", inParams, 1);
            fail("Exception needed");
        } catch (SQLException ex) {

        }
        try {
            List<Object> inParams = new ArrayList<Object>();
            inParams.add(1);
            inParams.add(2);
            inParams.add(3);
            result = Query.prepareStatement("select * from test where id in (?) limit ?", inParams, 1);
            System.out.println(result.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            fail("Exception ");
        }
    }

    /**
     * Test of executeQuery method, of class Query.
     */
    public void testExecuteQuery_3args() {
        QueryResult result;
        try {
            result = Query.executeQuery(Query.QUERY_SELECT, "select * from test limit ?", 1);
        } catch (SQLException ex) {
            fail("Normal select query throws exception");
        }
        try {
            result = Query.executeQuery(Query.QUERY_INSERT, "select * from test limit ?", 1);
            fail("Fail select query do not throw exception");
        } catch (SQLException ex) {
            
        }
        try {
            result = Query.executeQuery(Query.QUERY_SELECT, "insert into test () values ()");
            fail("Fail insert query do not throw exception");
        } catch (SQLException ex) {
            
        }
    }

    /**
     * Test of executeQuery method, of class Query.
     */
    public void testExecuteQuery_String_ObjectArr() throws Exception {
        QueryResult result;
        try {
            result = Query.executeQuery("select * from test limit ?", 1);
            result = Query.executeQuery("insert into test () values ()");
            //result = Query.executeQuery("select * from test limit ?", 1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            fail("Normal query throws exception");
            
        }
    }

    /**
     * Test of identifyQuery method, of class Query.
     */
    public void testIdentifyQuery() {
        assertEquals(Query.identifyQuery("select * from test where id=5"), Query.QUERY_SELECT);
        assertEquals(Query.identifyQuery("( ( (select * from test where id=5"), Query.QUERY_SELECT);
        assertEquals(Query.identifyQuery("insert int test (id, `string`) values (1, \"Jack\")"), Query.QUERY_INSERT);
        assertEquals(Query.identifyQuery("update test set id=2 where id=5"), Query.QUERY_UPDATE);
        assertEquals(Query.identifyQuery(" update test set id=2 where id=5"), Query.QUERY_UPDATE);
        assertEquals(Query.identifyQuery("delete from test where id=5"), Query.QUERY_DELETE);
    }

    /**
     * Test of getLastQuery method, of class Query.
     */
    public void testGetLastQuery() {

    }
//
//    /**
//     * Test of select method, of class Query.
//     */
//    public void testSelect() {
//        System.out.println("select");
//        String selectString = "";
//        Query instance = new Query();
//        Query expResult = null;
//        Query result = instance.select(selectString);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of update method, of class Query.
//     */
//    public void testUpdate() {
//        System.out.println("update");
//        String tableName = "";
//        Query instance = new Query();
//        Query expResult = null;
//        Query result = instance.update(tableName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of insert method, of class Query.
//     */
//    public void testInsert() {
//        System.out.println("insert");
//        String tableName = "";
//        Query instance = new Query();
//        Query expResult = null;
//        Query result = instance.insert(tableName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of delete method, of class Query.
//     */
//    public void testDelete() {
//        System.out.println("delete");
//        String tableName = "";
//        Query instance = new Query();
//        Query expResult = null;
//        Query result = instance.delete(tableName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of from method, of class Query.
//     */
//    public void testFrom() {
//        System.out.println("from");
//        String tableName = "";
//        Query instance = new Query();
//        Query expResult = null;
//        Query result = instance.from(tableName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of set method, of class Query.
//     */
//    public void testSet() {
//        System.out.println("set");
//        String field = "";
//        Object value = null;
//        Query instance = new Query();
//        Query expResult = null;
//        Query result = instance.set(field, value);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of where method, of class Query.
//     */
//    public void testWhere() throws Exception {
//        System.out.println("where");
//        String condition = "";
//        Object[] args = null;
//        Query instance = new Query();
//        Query expResult = null;
//        Query result = instance.where(condition, args);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of whereId method, of class Query.
//     */
//    public void testWhereId() throws Exception {
//        System.out.println("whereId");
//        Integer id = null;
//        Query instance = new Query();
//        Query expResult = null;
//        Query result = instance.whereId(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of order method, of class Query.
//     */
//    public void testOrder() {
//        System.out.println("order");
//        String OrderField = "";
//        String direction = "";
//        Query instance = new Query();
//        Query expResult = null;
//        Query result = instance.order(OrderField, direction);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of limit method, of class Query.
//     */
//    public void testLimit() {
//        System.out.println("limit");
//        Integer limit = null;
//        Query instance = new Query();
//        Query expResult = null;
//        Query result = instance.limit(limit);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of offset method, of class Query.
//     */
//    public void testOffset() {
//        System.out.println("offset");
//        Integer offset = null;
//        Query instance = new Query();
//        Query expResult = null;
//        Query result = instance.offset(offset);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of execute method, of class Query.
//     */
//    public void testExecute() throws Exception {
//        System.out.println("execute");
//        Query instance = new Query();
//        QueryResult expResult = null;
//        QueryResult result = instance.execute();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
