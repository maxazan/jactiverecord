/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    private Query query = new Query();

    public String getTableName() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    public void set(String field, Object value) {
        this.set(field, value, true);
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
            this.query.set(field, value);
        }
        this.properties.put(field, value);
    }

    public Object get(String field) {
        return this.properties.get(field);
    }

    private T createModel() {
        try {
            return (T) this.getClass().newInstance();
        } catch (Exception ex) {
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

    public T find(Object id) throws SQLException {
        return (T) this.createModelFromResultSet(this.query.select("*").from(this.getTableName()).where("id = ?", id).limit(1).execute().getData());
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

    private boolean insert() throws SQLException {
        Integer lastInsertId = this.query.insert(this.getTableName()).execute().getLastInsertId();
        if (lastInsertId == null) {
            return false;
        } else {
            this.set("id", lastInsertId, false);
            return true;
        }
    }

    private boolean update() throws SQLException {
        if (this.query.update(this.getTableName()).whereId(this.getId()).execute().getCountAffectedRows() > 0) {
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
        this.query.order(order, direction);
        return (T) this;
    }

    public T where(String condition, Object... args) throws SQLException {
        this.query.where(condition, args);
        return (T) this;
    }

    public T limit(Integer limit) {
        this.query.limit(limit);
        return (T) this;
    }

    public T offset(Integer offset) {
        this.query.offset(offset);
        return (T) this;
    }

    public Integer count() throws SQLException {
        ResultSet rs = this.query.select("count(*)").execute().getData();
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
        ResultSet rs = this.query.select("*").execute().getData();
        while (rs.next()) {
            T model = this.createModelFromResultSet(rs);
            result.add(model);
        }
        return result;
    }

    public T find() throws SQLException {
        this.query.limit(1);
        List result = this.findAll();
        if (result.isEmpty()) {
            return null;
        } else {
            return this.findAll().get(0);
        }

    }

}
