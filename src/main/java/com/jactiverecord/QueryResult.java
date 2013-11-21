/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jactiverecord;

import java.sql.ResultSet;
import java.util.Iterator;

/**
 *
 * @author maxazan
 */
public class QueryResult implements Iterable<ResultSet> {

    private ResultSet data = null;
    private Integer lastInsertId = null;
    private int countAffectedRows = 0;

    public ResultSet getData() {
        return data;
    }

    public void setData(ResultSet data) {
        this.data = data;
    }

    public Integer getLastInsertId() {
        return lastInsertId;
    }

    public void setLastInsertId(Integer lastInsertId) {
        this.lastInsertId = lastInsertId;
    }

    public int getCountAffectedRows() {
        return countAffectedRows;
    }

    public void setCountAffectedRows(int countAffectedRows) {
        this.countAffectedRows = countAffectedRows;
    }

    public Iterator iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int size() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
