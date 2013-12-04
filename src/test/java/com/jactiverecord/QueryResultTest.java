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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import junit.framework.TestCase;

/**
 *
 * @author maxazan
 */
public class QueryResultTest extends TestCase {

    public QueryResultTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ConnectionManager.connect("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/test", "root", "");
        Query.executeFromFile("src/test/java/com/jactiverecord/mysql.sql");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        ConnectionManager.connection.close();
    }

    /**
     * Test of getQueryParams method, of class QueryResult.
     */
    public void testGetQueryParams() {
        Object[] objs = new Object[2];
        objs[0] = "hello";
        objs[1] = "world";
        QueryResult qr = new QueryResult();
        qr.setQueryParams(objs);
        assertEquals(qr.getQueryParams(), objs);
    }

    /**
     * Test of setQueryParams method, of class QueryResult.
     */
    public void testSetQueryParams() {
        Object[] objs = new Object[2];
        objs[0] = "hello";
        objs[1] = "world";
        QueryResult qr = new QueryResult();
        qr.setQueryParams(objs);
        assertEquals(qr.getQueryParams(), objs);
    }

    /**
     * Test of getQuery method, of class QueryResult.
     */
    public void testGetQuery() {
        String query = "SELECT * FROM test WHERE id=?";
        QueryResult qr = new QueryResult();
        qr.setQuery(query);
        assertEquals(qr.getQuery(), query);
    }

    /**
     * Test of setQuery method, of class QueryResult.
     */
    public void testSetQuery() {
        String query = "SELECT * FROM test WHERE id=?";
        QueryResult qr = new QueryResult();
        qr.setQuery(query);
        assertEquals(qr.getQuery(), query);
    }

    /**
     * Test of getData method, of class QueryResult.
     */
    public void testGetData() throws SQLException {
        QueryResult qr = new Query().from("test").whereId(1).execute();
        assertTrue(qr.getData().next());
    }

    /**
     * Test of setData method, of class QueryResult.
     */
    public void testSetData() throws SQLException {
        QueryResult result = new QueryResult();
        String query = "SELECT * FROM test WHERE id=?";
        result.setQuery(query);
        Object[] objs = new Object[1];
        objs[0] = 1;
        result.setQueryParams(objs);
        PreparedStatement statement = Query.prepareStatement(result.getQuery(), result.getQueryParams());
        ResultSet rs = statement.executeQuery();
        result.setData(rs);
        assertEquals(result.getData(), rs);
    }

    /**
     * Test of getLastInsertId method, of class QueryResult.
     */
    public void testGetLastInsertId() throws SQLException {
        Query query = new Query();
        try {
            QueryResult qr = query.insert("test").set("string", "Test string").execute();
            int lastId = qr.getLastInsertId();
            assertTrue(lastId > 1);
            query.clean();
            qr = query.insert("test").set("string", "Test string").execute();
            assertTrue(qr.getLastInsertId() > lastId);
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }

    }

    /**
     * Test of setLastInsertId method, of class QueryResult.
     */
    public void testSetLastInsertId() {
        QueryResult qr = new QueryResult();
        qr.setLastInsertId(3);
        assertEquals(qr.getLastInsertId(), new Integer(3));
    }

    /**
     * Test of getCountAffectedRows method, of class QueryResult.
     */
    public void testGetCountAffectedRows() {
        Query query = new Query();
        try {
            QueryResult qr = query.update("test").set("string", "Hello world").execute();
            assertTrue(qr.getCountAffectedRows() == 2);
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }

    }

    /**
     * Test of setCountAffectedRows method, of class QueryResult.
     */
    public void testSetCountAffectedRows() {
        QueryResult qr = new QueryResult();
        qr.setCountAffectedRows(2);
        assertEquals(qr.getCountAffectedRows(), 2);
    }

    /**
     * Test of iterator method, of class QueryResult.
     */
    public void testIterator() throws SQLException {
        QueryResult instance = new Query().select().from("test").execute();
        int count = 0;
        for (ResultSet rs : instance) {
            count++;
            assertFalse(rs.getString("string").isEmpty());
        }
        assertEquals(count, 2);

    }

    /**
     * Test of size method, of class QueryResult.
     */
    public void testSize() throws SQLException {
        QueryResult instance = new QueryResult();
        assertEquals(instance.size(), 0);
        instance.clear();
        instance = new Query().select().from("test").execute();
        assertEquals(instance.size(), 2);
        instance.clear();
        instance = new Query().select().from("test").whereId(-1).execute();
        assertEquals(instance.size(), 0);
    }

    /**
     * Test of isEmpty method, of class QueryResult.
     */
    public void testIsEmpty() throws SQLException {
        QueryResult instance = new QueryResult();
        assertTrue(instance.isEmpty());
        instance.clear();
        instance = new Query().select().from("test").execute();
        assertFalse(instance.isEmpty());
        instance.clear();
        instance = new Query().select().from("test").whereId(-1).execute();
        assertTrue(instance.isEmpty());

    }

    /**
     * Test of clear method, of class QueryResult.
     */
    public void testClear() {
        QueryResult instance = new QueryResult();
        instance.clear();
    }

}
