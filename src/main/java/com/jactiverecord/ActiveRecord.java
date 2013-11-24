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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maxazan
 * @param <T>
 */
abstract public class ActiveRecord<T extends ActiveRecord> {

    private Map<String, Object> properties = new HashMap<String, Object>();
    private Query query = null;

    public String getTableName() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    public T set(String field, Object value) {
        this.set(field, value, true);
        return (T) this;
    }

    public void flush() {
        this.query = null;
    }

    public Query getQuery() {
        if (this.query == null) {
            this.query = this.prepareQuery();
        }
        return this.query;
    }

    public Query prepareQuery() {
        return new Query().from(this.getTableName());
    }

    /**
     *
     * @param field
     * @param value
     * @param update mark field to be updated in save. If false field will be
     * not use for update
     */
    public void set(String field, Object value, boolean update) {
        if (update) {
            this.getQuery().set(field, value);
        }
        this.properties.put(field, value);
    }

    public Object get(String field) {
        return this.properties.get(field);
    }

    private T createModel() {
        try {
            return (T) this.getClass().newInstance();
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ActiveRecord.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ActiveRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private T createModelFromResultSet(ResultSet rs) throws SQLException {
        T model = this.createModel();

        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            if (rs.getMetaData().getColumnType(i) == Types.DATE) {
                model.set(rs.getMetaData().getColumnName(i), new Date(rs.getDate(i).getTime()), false);
            } else if (rs.getMetaData().getColumnType(i) == Types.TIMESTAMP) {
                model.set(rs.getMetaData().getColumnName(i), new Date(rs.getTimestamp(i).getTime()), false);
            } else {
                model.set(rs.getMetaData().getColumnName(i), rs.getObject(i), false);
            }
        }
        return model;
    }

    protected Connection getConnection() {
        //TODO: Check connection
        return ConnectionManager.connection;
    }

    public T find(Integer id) throws SQLException {
        ResultSet rs = this.getQuery().select("*").whereId(id).limit(1).execute().getData();
        if (!rs.next()) {
            return null;
        }
        return (T) this.createModelFromResultSet(rs);
    }

    public boolean save() throws SQLException {
        if (this.beforeSave()) {
            if (this.get("id") == null) {
                return this.insert();
            } else {
                return this.update();
            }
        } else {
            return false;
        }
    }

    public boolean delete() throws SQLException {
        if (this.get("id") == null) {
            return false;
        } else {
            return this.getQuery().delete(this.getTableName()).whereId(this.getId()).execute().getCountAffectedRows() > 0;
        }
    }

    private boolean insert() throws SQLException {
        Integer lastInsertId = this.getQuery().insert(this.getTableName()).execute().getLastInsertId();
        if (lastInsertId == null) {
            return false;
        } else {
            this.set("id", lastInsertId, false);
            return true;
        }
    }

    private boolean update() throws SQLException {
        if (this.getQuery().update(this.getTableName()).whereId(this.getId()).execute().getCountAffectedRows() > 0) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean beforeSave() {
        return true;
    }

    protected void afterSave() {
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "{" + "table=" + this.getTableName() + ", properties=" + properties + '}';
    }

    public T order(String order, String direction) {
        this.getQuery().order(order, direction);
        return (T) this;
    }

    public T where(String condition, Object... args) throws SQLException {
        this.getQuery().where(condition, args);
        return (T) this;
    }

    public T limit(Integer limit) {
        this.getQuery().limit(limit);
        return (T) this;
    }

    public T offset(Integer offset) {
        this.getQuery().offset(offset);
        return (T) this;
    }

    public Integer count() throws SQLException {
        ResultSet rs = this.getQuery().select("count(*)").execute().getData();
        while (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public Integer getId() {
        return (Integer) this.get("id");
    }

    public List<T> findAll() throws SQLException {
        List result = new ArrayList();
        ResultSet rs = this.getQuery().select("*").execute().getData();
        while (rs.next()) {
            T model = this.createModelFromResultSet(rs);
            result.add(model);
        }
        return result;
    }

    public T find() throws SQLException {
        this.getQuery().limit(1);
        List result = this.findAll();
        if (result.isEmpty()) {
            return null;
        } else {
            return this.findAll().get(0);
        }

    }

}
