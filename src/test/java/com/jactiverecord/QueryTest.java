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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

/**
 * Test for Query class
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
        Query.executeFromFile("src/test/java/com/jactiverecord/mysql.sql");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        ConnectionManager.connection.close();
    }

    /**
     * Test of getConnection method, of class Query.
     */
    public void testGetConnection() {
        Connection result = Query.getConnection();
        assertEquals(result, ConnectionManager.connection);
    }

    /**
     * Test of getIdentifierQuoteString method, of class Query.
     *
     * @throws java.lang.Exception
     */
    public void testGetIdentifierQuoteString() throws Exception {
        String expResult = "`";
        String result = Query.getIdentifierQuoteString();
        assertEquals(expResult, result);
    }

    /**
     * Test of prepareStatement method, of class Query.
     */
    public void testPrepareStatement() {
        try {
            Query.prepareStatement("select * from test where id=? limit ?", 1);
            fail("Exception needed");
        } catch (SQLException ex) {
            assertNotNull(ex);
        }
        try {
            Query.prepareStatement("select * from test where 1", 1);
            fail("Exception needed");
        } catch (SQLException ex) {
            assertNotNull(ex);
        }
        try {
            Query.prepareStatement("select * from test where id=?", 1);
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
        try {
            Query.prepareStatement("select * from test where 1");
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
        try {
            Query.prepareStatement("select * from test where id=? limit ?", 1, 1);
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
        try {
            List<Object> inParams = new ArrayList<Object>();
            Query.prepareStatement("select * from test where id in (?) limit ?", inParams, 1);
            fail("Exception needed");
        } catch (SQLException ex) {
            assertNotNull(ex);
        }
        try {
            List<Object> inParams = new ArrayList<Object>();
            inParams.add(1);
            inParams.add(2);
            inParams.add(3);
            Query.prepareStatement("select * from test where id in (?) limit ?", inParams, 1);

        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of executeQuery method, of class Query.
     */
    public void testExecuteQuery_3args() {
        try {
            Query.executeQuery(Query.QUERY_SELECT, "select * from test limit ?", 1);
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
        try {
            Query.executeQuery(Query.QUERY_INSERT, "select * from test limit ?", 1);
            fail("Fail select query do not throw exception");
        } catch (SQLException ex) {
            assertNotNull(ex);
        }
        try {
            Query.executeQuery(Query.QUERY_SELECT, "insert into test () values ()");
            fail("Fail insert query do not throw exception");
        } catch (SQLException ex) {
            assertNotNull(ex);
        }
    }

    /**
     * Test of executeQuery method, of class Query.
     *
     * @throws java.lang.Exception
     */
    public void testExecuteQuery_String_ObjectArr() throws Exception {
        try {
            Query.executeQuery("select * from test limit ?", 1);
            Query.executeQuery("insert into test () values ()");
        } catch (SQLException ex) {
            fail(ex.getMessage());
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
     * Test of select method, of class Query.
     */
    public void testSelect() {
        Query query = new Query();
        QueryResult qr = new QueryResult();
        try {
            qr = query.select("string").from("test").whereId(1).execute();
            qr.getData().next();
            assertEquals(qr.getData().getString("string"), "Test string");
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
        try {
            qr.getData().getInt("int");
            fail("need exception");
        } catch (SQLException ex) {
            assertNotNull(ex);
        }

    }

    /**
     * Test of update method, of class Query.
     */
    public void testUpdate() {
        Query query = new Query();
        try {
            QueryResult qr = query.update("test").set("string", "Test string").whereId(1).execute();
            assertEquals(qr.getCountAffectedRows(), 1);
            query.clean();
            qr = query.update("test").set("string", "Test string").execute();
            assertTrue(qr.getCountAffectedRows() > 1);
            query.clean();
            qr = query.update("test").set("string", "Test string").where("id=?", -100).execute();
            assertTrue(qr.getCountAffectedRows() == 0);

        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of insert method, of class Query.
     */
    public void testInsert() {
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
     * Test of delete method, of class Query.
     */
    public void testDelete() {
        Query query = new Query();
        try {
            QueryResult qr = query.delete("test").where("id>?", 1).execute();
            assertTrue(qr.getCountAffectedRows() > 0);
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of from method, of class Query.
     */
    public void testFrom() {
        Query query = new Query();
        try {
            query.delete("testes").where("id>?", 2).execute();
            fail("table do not exists");
        } catch (SQLException ex) {
            assertNotNull(ex);
        }
    }

    /**
     * Test of set method, of class Query.
     */
    public void testSet() {
        Query query = new Query();
        try {
            QueryResult qr = query.update("test").set("string", "Test string").set("string", "Test string 2").set("string", "Test string").whereId(1).execute();
            assertEquals(qr.getCountAffectedRows(), 1);
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of where method, of class Query.
     *
     * @throws java.lang.Exception
     */
    public void testWhere() throws Exception {
        Query query = new Query();
        try {
            QueryResult qr = query.update("test").set("string", "Test string").where("id=?", 1).execute();
            assertEquals(qr.getCountAffectedRows(), 1);
            query.clean();
            qr = query.update("test").set("string", "Test string").where("id=?", 1).where("id=?", 2).execute();
            assertEquals(qr.getCountAffectedRows(), 0);
            query.clean();

        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of whereId method, of class Query.
     *
     * @throws java.lang.Exception
     */
    public void testWhereId() throws Exception {
        Query query = new Query();
        try {
            QueryResult qr = query.update("test").set("string", "Test string").whereId(1).execute();
            assertEquals(qr.getCountAffectedRows(), 1);
            query.clean();
            qr = query.update("test").set("string", "Test string").whereId(1).whereId(2).execute();
            assertEquals(qr.getCountAffectedRows(), 0);
            query.clean();

        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of order method, of class Query.
     */
    public void testOrder() {
        try {
            Query query = new Query();
            QueryResult rs = query.from("test").order("string", "desc").order("id", "asc").execute();
            assertTrue(rs.getData().next());
            assertTrue(rs.getData().getInt("id") == 1);
            query.clean();
            rs = query.from("test").order("id", "desc").execute();
            assertTrue(rs.getData().next());
            assertTrue(rs.getData().getInt("id") > 1);

        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of limit method, of class Query.
     */
    public void testLimit() {
        try {
            Query query = new Query();
            QueryResult rs = query.from("test").limit(1).execute();
            assertTrue(rs.size() == 1);
            query.clean();
            rs = query.from("test").limit(2).execute();
            assertTrue(rs.size() == 2);
            query.clean();
            rs = query.from("test").whereId(1).limit(2).execute();
            assertTrue(rs.size() == 1);

        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of offset method, of class Query.
     */
    public void testOffset() {
        try {
            Query query = new Query();
            QueryResult rs = query.from("test").limit(2).offset(0).execute();
            assertTrue(rs.getData().next());
            assertTrue(rs.getData().next());
            Integer testId = rs.getData().getInt("id");
            query.clean();
            rs = query.from("test").limit(1).offset(1).execute();
            assertTrue(rs.getData().next());
            assertEquals(rs.getData().getInt("id"), testId.intValue());
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of execute method, of class Query.
     */
    public void testExecute() {
        try {
            List<Object> inParams = new ArrayList<Object>();
            inParams.add(1);
            inParams.add(2);
            inParams.add(3);
            Query query = new Query();
            QueryResult rs = query.from("test").where("id in (?)", inParams).order("id", "desc").limit(1).execute();
            assertTrue(rs.size() == 1);

        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of clean method, of class Query.
     */
    public void testClean() {
        try {
            QueryResult rs = new Query().where("id=?", -1).clean().select("*").from("test").execute();
            assertEquals(rs.size(), 2);
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of processValue method, of class Query.
     */
    public void testProcessValue() {

    }

    /**
     * Test of executeFromFile method, of class Query.
     */
    public void testExecuteFromFile() {
        String filename = "src/test/java/com/jactiverecord/mysql.sql";
        try {
            Query.executeFromFile(filename);
        } catch (IOException ex) {
            fail(ex.getMessage());
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
}
