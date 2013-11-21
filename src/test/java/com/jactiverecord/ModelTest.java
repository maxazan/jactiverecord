/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jactiverecord;

import com.jactiverecord.models.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
//
///**
// *
// * @author maxazan
// */
public class ModelTest extends TestCase {
//
//    public ModelTest(String testName) {
//        super(testName);
//    }
//
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        ConnectionManager.connect("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/test", "root", "");
//    }
//
//    @Override
//    protected void tearDown() throws Exception {
//        super.tearDown();
//    }
//
//    /**
//     * Test of getTableName method, of class Model.
//     */
//    public void testGetTableName() {
//        ActiveRecord instance = new Test();
//        String expResult = "test";
//        String result = instance.getTableName();
//        assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of set method, of class Model.
//     */
//    public void testSetGet() {
//        Test model = new Test();
//        String field = "field";
//        String value = "value";
//        model.set(field, value);
//        assertEquals("String", model.get(field), value);
//        value = null;
//        model.set(field, value);
//        assertEquals("null", model.get(field), value);
//    }
//
//    /**
//     * Test of set method, of class Model.
//     */
//    public void testSetFieldToUpdate() {
//        Test model = new Test();
//        String field = "field";
//        String field2 = "field2";
//        String value = "value";
//        String value2 = "value2";
//        model.set(field, value, false);
//        assertTrue(model.getFieldsToUpdate().isEmpty());
//        value = null;
//        model.set(field, value, true);
//        assertTrue(model.getFieldsToUpdate().size() == 1);
//        model.set(field2, value2);
//        System.out.println(model.getFieldsToUpdate().size());
//        assertTrue(model.getFieldsToUpdate().size() == 2);
//
//    }
//
//    /**
//     * Test of getConnection method, of class Model.
//     */
//    public void testGetConnection() {
//        Test model = new Test();
//        assertEquals(model.getConnection(), ConnectionManager.connection);
//    }
//
//    /**
//     * Test of find method, of class Model.
//     */
//    public void testFindById() {
//        Integer existsId = 1;
//        Integer notexistsId = -1;
//        ActiveRecord model = new Test().find(existsId);
//        assertNotNull(model);
//        model = new Test().find(notexistsId);
//        assertNull(model);
//    }
//
//    /**
//     * Test of find method, of class Model.
//     */
//    public void testInternalClass() {
//        //TODO: internal class newInstance() error in createModel()
//    }
//
//    /**
//     * Test of save method, of class Model.
//     */
//    public void testGetParamTypes() {
//        Long testLong = new Long("123456789123456789");
//        String testString = "Test string";
//        String testText = "Test text";
//        String testEnum = "normal";
//        String testDate = "2013-11-10";
//        String testDateTimeString = "2013-11-10 20:40:22";
//        String testDateTimestampString = "2013-11-10 20:40:23";
//        ActiveRecord model = new Test().find(1);
//        assertEquals(model.get("long"), testLong);
//        assertEquals(model.get("string"), testString);
//        assertEquals(model.get("text"), testText);
//        assertEquals(model.get("enum"), testEnum);
//        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
//        assertEquals(testDate, formater.format((Date) model.get("date")));
//
//        formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date datetime = formater.parse(testDateTimeString);
//            assertEquals(datetime, (Date) model.get("datetime"));
//        } catch (ParseException ex) {
//            fail("No need exception");
//        }
//        try {
//            Date datetime = formater.parse(testDateTimestampString);
//            assertEquals(datetime, (Date) model.get("timestamp"));
//        } catch (ParseException ex) {
//            fail("No need exception");
//        }
//
//        System.out.println(model);
//    }
//
//    /**
//     * Test of save method, of class Model.
//     */
//    public void testSetParamTypes() {
//        Long testLong = new Long("123456789123456789");
//        String testString = "Test string";
//        String testText = "Test text";
//        String testEnum = "normal";
//        String testDateString = "2010-11-10";
//        String testDateTimeString = "2010-11-10 20:40:22";
//        String testDateTimestampString = "2010-11-10 20:40:23";
//
//        ActiveRecord model = new Test();
//        model.set("long", testLong);
//        model.set("string", testString);
//        model.set("text", testText);
//        model.set("enum", testEnum);
//        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            model.set("date", formater.parse(testDateString));
//        } catch (ParseException ex) {
//            fail("No need exception");
//        }
//        formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            model.set("datetime", formater.parse(testDateTimeString));
//            model.set("timestamp", formater.parse(testDateTimestampString));
//        } catch (ParseException ex) {
//            fail("No need exception");
//        }
//        System.out.println(model);
//        assertTrue(model.save());
//        //load
//        model = new Test().find(model.get("id"));
//        assertEquals(model.get("long"), testLong);
//        assertEquals(model.get("string"), testString);
//        assertEquals(model.get("text"), testText);
//        assertEquals(model.get("enum"), testEnum);
//        formater = new SimpleDateFormat("yyyy-MM-dd");
//        assertEquals(testDateString, formater.format((Date) model.get("date")));
//
//        formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date datetime = formater.parse(testDateTimeString);
//            assertEquals(datetime, (Date) model.get("datetime"));
//        } catch (ParseException ex) {
//            fail("No need exception");
//        }
//        try {
//            Date datetime = formater.parse(testDateTimestampString);
//            assertEquals(datetime, (Date) model.get("timestamp"));
//        } catch (ParseException ex) {
//            fail("No need exception");
//        }
//        System.out.println(model);
//    }
//
//    /**
//     * Test of where method, of class Model.
//     */
//    public void testWhereErrors() {
//        ActiveRecord model = new Test();
//        assertNull(model.where("id=", 1));
//        assertEquals(model.getLastError(), "Incorrect where arguments. Expected 0, Actual 1");
//        assertNull(model.where("id=?,?,?", 1));
//        assertEquals(model.getLastError(), "Incorrect where arguments. Expected 3, Actual 1");
//    }
//
//    /**
//     * Test of where method, of class Model.
//     */
//    public void testWhere() {
//        Long testLong = new Long("123456789123456789");
//        String testString = "Test string";
//        String testText = "Test text";
//        String testEnum = "normal";
//        String testDateString = "2010-11-10";
//        String testDateTimeString = "2010-11-10 20:40:22";
//        String testDateTimestampString = "2010-11-10 20:40:23";
//
//        assertNotNull(new Test().where("id=?", 1).find());
//        assertNull(new Test().where("id=?", -1).find());
//        assertNotNull(new Test().where("`long`=?", testLong).find());
//        assertNull(new Test().where("`long`=?", testLong-1).find());
//        assertNotNull(new Test().where("string=?", testString).find());
//        assertNotNull(new Test().where("string like?", "%s%").find());
//        assertNull(new Test().where("string=?", "Unknown string").find());
//        assertNotNull(new Test().where("text=?", testText).find());
//        assertNotNull(new Test().where("text like?", "%s%").find());
//        assertNull(new Test().where("text=?", "Unknown text").find());
//        assertNotNull(new Test().where("enum=?", testEnum).find());
//        assertNull(new Test().where("enum=?", "Unknown enum").find());
//        assertNotNull(new Test().where("date=?", testDateString).find());
//        assertNull(new Test().where("date+interval 1 day=?", testDateString).find());
//        assertNotNull(new Test().where("datetime=?", testDateTimeString).find());
//        assertNotNull(new Test().where("timestamp=?", testDateTimestampString).find());
//        assertNotNull(new Test().where("DATE(datetime)=DATE(?)", testDateTimestampString).find());
//        assertNull(new Test().where("datetime=?", testDateTimestampString).find());
//
//    }
//
//    /**
//     * Test of findAll method, of class Model.
//     */
//    public void testFindAll() {
//        assertTrue(new Test().findAll().size() > 0);
//        assertTrue(new Test().where("id<0").findAll().isEmpty());
//    }
//
//    /**
//     * Test of limit method, of class Model.
//     */
//    public void testLimit() {
//        int count = new Test().findAll().size() - 1;
//        assertEquals(new Test().limit(count).findAll().size(), count);
//    }
//
//    /**
//     * Test of offset method, of class Model.
//     */
//    public void testOffset() {
//        int count = new Test().findAll().size() - 1;
//        assertEquals(new Test().offset(count).findAll().size(), 1);
//    }
//
//    /**
//     * Test of count method, of class Model.
//     */
//    public void testCount() {
//        Integer count = new Test().findAll().size();
//        assertEquals(count, new Test().count());
//        count = 0;
//        assertEquals(new Test().where("id<?", 0).count(), count);
//    }
//
//    /**
//     * Test of order method, of class Model.
//     */
//    public void testOrder() {
//        assertEquals(new Test().order("id", "asc").find().getId(), new Integer(1));
//        assertTrue(new Test().order("id", "desc").find().getId() > 1);
//        assertEquals(new Test().order("id", "desc").order("id", "asc").find().getId(), new Integer(1));
//    }
}
