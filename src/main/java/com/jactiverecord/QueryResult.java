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
import java.sql.SQLException;
import java.util.Iterator;

/**
 *
 * @author maxazan
 */
public class QueryResult implements Iterable<ResultSet> {

    private ResultSet data = null;
    private Integer lastInsertId = null;
    private int countAffectedRows = 0;
    private String query;
    private Object[] queryParams;

    public Object[] getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Object[] queryParams) {
        this.queryParams = queryParams;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

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
        QueryResultIterator iterator=new QueryResultIterator(this.getData());
        return iterator;
    }

    public int size() {
        if (this.getData() == null) {
            return 0;
        }
        int rowsCount = 0;
        try {
            if (this.getData().last()) {
                rowsCount = this.getData().getRow();
                this.getData().beforeFirst();
            }
        } catch (SQLException ex) {

        }
        return rowsCount;

    }

    public boolean isEmpty() {
        if (this.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public void clear() {
        this.data = null;
        this.lastInsertId = null;
        this.countAffectedRows = 0;
        this.query = null;
        this.queryParams = null;
    }

}
