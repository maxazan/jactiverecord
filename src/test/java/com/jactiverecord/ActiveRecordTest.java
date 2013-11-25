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

import com.jactiverecord.models.FinalTest;
import com.jactiverecord.models.Test;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
//
///**
// *
// * @author maxazan
// */

public class ActiveRecordTest extends TestCase {
//
//    public ModelTest(String testName) {
//        super(testName);
//    }

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
     * Test of getTableName method, of class Model.
     */
    public void testGetTableName() {
        ActiveRecord instance = new Test();
        String expResult = "test";
        String result = instance.getTableName();
        assertEquals(expResult, result);
    }

    /**
     * Test of set method, of class Model.
     */
    public void testSetGet() {
        Test model = new Test();
        String field = "field";
        String value = "value";
        model.set(field, value);
        assertEquals("String", model.get(field), value);
        value = null;
        model.set(field, value);
        assertEquals("null", model.get(field), value);
    }

    /**
     * Test of getConnection method, of class Model.
     */
    public void testGetConnection() {
        Test model = new Test();
        assertEquals(model.getConnection(), ConnectionManager.connection);
    }

    /**
     * Test of find method, of class Model.
     */
    public void testFindById() {
        Integer existsId = 1;
        Integer notexistsId = -1;
        Test model;
        try {
            model = new Test().find(existsId);
        } catch (SQLException ex) {
            ex.printStackTrace();
            fail("die");
        }
        /*assertNotNull(model);
         model = new Test().find(notexistsId);
         assertNull(model);*/
    }

    /**
     * Test of find method, of class Model.
     */
    public void testInternalClass() {
        //TODO: internal class newInstance() error in createModel()
    }

    /**
     * Test of save method, of class Model.
     */
    public void testGetParamTypes() throws SQLException {
        Long testLong = new Long("123456789123456789");
        String testString = "Test string";
        String testText = "Test text";
        String testEnum = "normal";
        String testDate = "2013-11-10";
        String testDateTimeString = "2013-11-10 20:40:22";
        String testDateTimestampString = "2013-11-10 20:40:23";
        Test model = new Test().find(1);
        assertEquals(model.get("long"), testLong);
        assertEquals(model.get("string"), testString);
        assertEquals(model.get("text"), testText);
        assertEquals(model.get("enum"), testEnum);
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals(testDate, formater.format((Date) model.get("date")));

        formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date datetime = formater.parse(testDateTimeString);
            assertEquals(datetime, (Date) model.get("datetime"));
        } catch (ParseException ex) {
            fail("No need exception");
        }
        try {
            Date datetime = formater.parse(testDateTimestampString);
            assertEquals(datetime, (Date) model.get("timestamp"));
        } catch (ParseException ex) {
            fail("No need exception");
        }

    }

    /**
     * Test of save method, of class Model.
     */
    public void testSetParamTypes() throws SQLException {
        Long testLong = new Long("123456789123456789");
        String testString = "Test string";
        String testText = "Test text";
        String testEnum = "normal";
        String testDateString = "2010-11-10";
        String testDateTimeString = "2010-11-10 20:40:22";
        String testDateTimestampString = "2010-11-10 20:40:23";

        ActiveRecord model = new Test();
        model.set("long", testLong);
        model.set("string", testString);
        model.set("text", testText);
        model.set("enum", testEnum);
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        try {
            model.set("date", formater.parse(testDateString));
        } catch (ParseException ex) {
            fail("No need exception");
        }
        formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            model.set("datetime", formater.parse(testDateTimeString));
            model.set("timestamp", formater.parse(testDateTimestampString));
        } catch (ParseException ex) {
            fail("No need exception");
        }
        assertTrue(model.save());
        //load
        model = new Test().find(model.getId());
        assertEquals(model.get("long"), testLong);
        assertEquals(model.get("string"), testString);
        assertEquals(model.get("text"), testText);
        assertEquals(model.get("enum"), testEnum);
        formater = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals(testDateString, formater.format((Date) model.get("date")));

        formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date datetime = formater.parse(testDateTimeString);
            assertEquals(datetime, (Date) model.get("datetime"));
        } catch (ParseException ex) {
            fail("No need exception");
        }
        try {
            Date datetime = formater.parse(testDateTimestampString);
            assertEquals(datetime, (Date) model.get("timestamp"));
        } catch (ParseException ex) {
            fail("No need exception");
        }
    }

    /**
     * Test of where method, of class Model.
     */
    public void testWhereErrors() {
        ActiveRecord model = new Test();
        try {
            model.where("id=", 1).findAll();
            fail("Need exception");
        } catch (SQLException ex) {

        }
        try {
            model.where("id=", 1).findAll();
            fail("Need exception");
        } catch (SQLException ex) {

        }

    }

    /**
     * Test of where method, of class Model.
     */
    public void testWhere() throws SQLException {
        Long testLong = new Long("123456789123456789");
        String testString = "Test string";
        String testText = "Test text";
        String testEnum = "normal";
        String testDateString = "2010-11-11";
        String testDateTimeString = "2010-11-10 20:40:22";
        String testDateTimestampString = "2010-11-10 20:40:23";

        assertNotNull(new Test().where("id=?", 1).find());
        assertNull(new Test().where("id=?", -1).find());
        assertNotNull(new Test().where("`long`=?", testLong).find());
        assertNull(new Test().where("`long`=?", testLong - 1).find());
        assertNotNull(new Test().where("string=?", testString).find());
        assertNotNull(new Test().where("string like?", "%s%").find());
        assertNull(new Test().where("string=?", "Unknown string").find());
        assertNotNull(new Test().where("text=?", testText).find());
        assertNotNull(new Test().where("text like?", "%s%").find());
        assertNull(new Test().where("text=?", "Unknown text").find());
        assertNotNull(new Test().where("enum=?", testEnum).find());
        assertNull(new Test().where("enum=?", "Unknown enum").find());
        assertNotNull(new Test().where("date=?", testDateString).find());
        assertNull(new Test().where("date+interval 1 day=?", testDateString).find());
        assertNotNull(new Test().where("datetime=?", testDateTimeString).find());
        assertNotNull(new Test().where("timestamp=?", testDateTimestampString).find());
        assertNotNull(new Test().where("DATE(datetime)=DATE(?)", testDateTimestampString).find());
        assertNull(new Test().where("datetime=?", testDateTimestampString).find());

    }

    /**
     * Test of findAll method, of class Model.
     */
    public void testFindAll() throws SQLException {
        assertTrue(new Test().findAll().size() > 0);
        assertTrue(new Test().where("id<0").findAll().isEmpty());
    }

    /**
     * Test of limit method, of class Model.
     */
    public void testLimit() throws SQLException {
        int count = new Test().findAll().size() - 1;
        assertEquals(new Test().limit(count).findAll().size(), count);
    }

    /**
     * Test of offset method, of class Model.
     */
    public void testOffset() throws SQLException {
        int count = new Test().findAll().size() - 1;
        assertEquals(new Test().limit(10).offset(count).findAll().size(), 1);
    }

    /**
     * Test of count method, of class Model.
     */
    public void testCount() throws SQLException {
        Integer count = new Test().findAll().size();
        assertEquals(count, new Test().count());
        count = 0;
        assertEquals(new Test().where("id<?", 0).count(), count);
    }

    /**
     * Test of order method, of class Model.
     */
    public void testOrder() throws SQLException {
        assertEquals(new Test().order("id", "asc").find().getId(), new Integer(1));
        assertTrue(new Test().order("id", "desc").find().getId() > 1);
        assertEquals(new Test().order("id", "desc").order("id", "asc").find().getId(), new Integer(1));
    }

    /**
     * Test of order method, of class Model.
     */
    public void testFinalTest() throws SQLException {
        FinalTest ft = new FinalTest();
        FinalTest ft2 = ft.onlyNew().where("id in (1,2)").find();
    }

    /**
     * Test of order method, of class Model.
     */
    public void testDelete() throws SQLException {
        List<Test> list = new Test().where("id>?", 2).findAll();
        for (Test model : list) {
            assertTrue(model.delete());
        }
    }
}
