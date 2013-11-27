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

import java.sql.ResultSet;
import java.util.Iterator;
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
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getQueryParams method, of class QueryResult.
     */
    public void testGetQueryParams() {
        System.out.println("getQueryParams");
        QueryResult instance = new QueryResult();
        Object[] expResult = null;
        Object[] result = instance.getQueryParams();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setQueryParams method, of class QueryResult.
     */
    public void testSetQueryParams() {
        System.out.println("setQueryParams");
        Object[] queryParams = null;
        QueryResult instance = new QueryResult();
        instance.setQueryParams(queryParams);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getQuery method, of class QueryResult.
     */
    public void testGetQuery() {
        System.out.println("getQuery");
        QueryResult instance = new QueryResult();
        String expResult = "";
        String result = instance.getQuery();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setQuery method, of class QueryResult.
     */
    public void testSetQuery() {
        System.out.println("setQuery");
        String query = "";
        QueryResult instance = new QueryResult();
        instance.setQuery(query);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getData method, of class QueryResult.
     */
    public void testGetData() {
        System.out.println("getData");
        QueryResult instance = new QueryResult();
        ResultSet expResult = null;
        ResultSet result = instance.getData();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setData method, of class QueryResult.
     */
    public void testSetData() {
        System.out.println("setData");
        ResultSet data = null;
        QueryResult instance = new QueryResult();
        instance.setData(data);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLastInsertId method, of class QueryResult.
     */
    public void testGetLastInsertId() {
        System.out.println("getLastInsertId");
        QueryResult instance = new QueryResult();
        Integer expResult = null;
        Integer result = instance.getLastInsertId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLastInsertId method, of class QueryResult.
     */
    public void testSetLastInsertId() {
        System.out.println("setLastInsertId");
        Integer lastInsertId = null;
        QueryResult instance = new QueryResult();
        instance.setLastInsertId(lastInsertId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCountAffectedRows method, of class QueryResult.
     */
    public void testGetCountAffectedRows() {
        System.out.println("getCountAffectedRows");
        QueryResult instance = new QueryResult();
        int expResult = 0;
        int result = instance.getCountAffectedRows();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCountAffectedRows method, of class QueryResult.
     */
    public void testSetCountAffectedRows() {
        System.out.println("setCountAffectedRows");
        int countAffectedRows = 0;
        QueryResult instance = new QueryResult();
        instance.setCountAffectedRows(countAffectedRows);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of iterator method, of class QueryResult.
     */
    public void testIterator() {
        System.out.println("iterator");
        QueryResult instance = new QueryResult();
        Iterator expResult = null;
        Iterator result = instance.iterator();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of size method, of class QueryResult.
     */
    public void testSize() {
        System.out.println("size");
        QueryResult instance = new QueryResult();
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isEmpty method, of class QueryResult.
     */
    public void testIsEmpty() {
        System.out.println("isEmpty");
        QueryResult instance = new QueryResult();
        boolean expResult = false;
        boolean result = instance.isEmpty();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clear method, of class QueryResult.
     */
    public void testClear() {
        System.out.println("clear");
        QueryResult instance = new QueryResult();
        instance.clear();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
