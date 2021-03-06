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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A class for making SQL query. Examples:
 * <pre><code>
 *
 * //SELECT
 *   QueryResult result=new Query().select("name").from("table").where("name=?","Jack").order("name","asc").limit(10).execute();
 *   QueryResult result=Query.executeQuery("select * from table where name=? order by name asc limit 10","Jack");
 *   result.size();
 *   for(ResultRow row : result){
 *       row.get("name");
 *       row.get("id");
 *   }
 *
 *   //INSERT
 *   QueryResult result=new Query().insert("table").set("name","Jack").set("cname","Vorobey").set("created_at",new Date()).execute();
 *   QueryResult result=Query.executeQuery("insert into table (name, cname, created_at) values (?, ?, ?)", "Jack", "Vorobey", new Date());
 *   Integer insertedId=result.getLastInsertId();
 *
 *   //UPDATE
 *   QueryResult result=new Query().update("table").set("name","Jack").where("cname=?", "Vorobey").execute();
 *   QueryResult result=Query.executeQuery("update table set name=? where cname=?", "Jack", "Vorobey");
 *   Integer countUpdatedRows=result.getCountAffectedRows();
 *
 *   //DELETE
 *   QueryResult result=new Query().delete("table").where("cname=?", "Vorobey").execute();
 *   QueryResult result=Query.executeQuery("delete from table where cname=?", "Vorobey");
 *   Integer countDeletedRows=result.getCountAffectedRows();
 * }
 * </code></pre>
 *
 * @version 1.0
 * @author maxazan
 */
public class Query {

    /**
     * Type for "select" query
     */
    public static final int QUERY_SELECT = 1;

    /**
     * Type for "insert" query
     */
    public static final int QUERY_INSERT = 2;

    /**
     * Type for "update" query
     */
    public static final int QUERY_UPDATE = 3;

    /**
     * Type for "delete" query
     */
    public static final int QUERY_DELETE = 4;

    private String selectString;
    private String tableName;
    private int queryType;

    private String whereCondition;
    private List whereParams;
    private List setParams;
    private Map<String, String> order;
    private Map<String, Object> fields;
    private Integer limit;
    private Integer offset;

    {
        this.clean();
    }

    /**
     * Method reset all variables
     *
     * @return Query object for continue query building
     */
    public Query clean() {
        this.selectString = "*";
        this.tableName = null;
        this.queryType = Query.QUERY_SELECT;

        this.whereCondition = null;
        this.whereParams = new ArrayList();
        this.setParams = new ArrayList();
        this.order = new LinkedHashMap<String, String>();
        this.fields = new HashMap<String, Object>();
        this.limit = null;
        this.offset = null;
        return this;
    }

    /**
     * Method for getting global connection
     *
     * @return Connection
     */
    public static Connection getConnection() {
        //TODO: Check connection
        return ConnectionManager.connection;
    }

    /**
     * Method return String for quoting
     * <pre>
     * String field = "order"; //SQL word
     * String quotedField = Query.getIdentifierQuoteString()+field+Query.getIdentifierQuoteString();
     * Quey.executeQuery("SELECT " + field + " FROM ..."); // throws SQL Exception
     * Quey.executeQuery("SELECT " + quotedField + " FROM ..."); //Will execute without exception
     * </pre>
     *
     * @return String for quoting fields of SQL table
     * @throws SQLException
     */
    public static String getIdentifierQuoteString() throws SQLException {
        return Query.getConnection().getMetaData().getIdentifierQuoteString();
    }

    /**
     * Add Value to the statement based on it`s type
     *
     * @param statement PreparedStatement to add value
     * @param index to add value
     * @param value
     * @throws SQLException if can`t add value
     */
    protected static void processValue(PreparedStatement statement, int index, Object value) throws SQLException {
        if (value instanceof java.util.Date) {
            statement.setTimestamp(index, new Timestamp(((java.util.Date) value).getTime()));
            return;
        }
        if (value instanceof java.sql.Date) {
            statement.setTimestamp(index, new Timestamp(((java.sql.Date) value).getTime()));
            return;
        }
        if (value instanceof java.sql.Timestamp) {
            statement.setTimestamp(index, (java.sql.Timestamp) value);
            return;
        }
        if (value instanceof Long) {
            statement.setLong(index, (Long) value);
            return;
        }
        statement.setObject(index, value);
    }

    /**
     * Prepare statement and add all values in it. If one of value will be List,
     * '?' in query will be replaced on "?,?,?,.." depends on List size. And all
     * value from List will be added as simple value.
     * <pre>
     * Query.executeQuery("select * from test where id in(?, ?, ?)", 1, 2, 3);
     * //the same as
     * List list=new ArrayList();
     * list.add(1);
     * list.add(2);
     * list.add(3);
     * Query.executeQuery("select * from test where id in(?)", list);
     * </pre>
     *
     * @param query string
     * @param args zero or more arguments to adding in query
     * @return statement with values added in it
     * @throws SQLException if something wrong
     */
    protected static PreparedStatement prepareStatement(String query, Object... args) throws SQLException {
        String[] splitedQuery = (query + " ").split("\\?");
        int count = splitedQuery.length - 1;
        String resultQuery = "";
        List<Object> resultArgs = new ArrayList<Object>();
        if (count != args.length) {
            throw new SQLException("Invalid arguments count for query " + query + ". Expected " + count + " Actual " + args.length);
        } else {

            int counterArgiments = -1;
            String q;
            for (String queryPart : splitedQuery) {
                q = "?";
                if (counterArgiments == -1) {
                    resultQuery = queryPart;
                } else {
                    int counterList = 0;
                    if (args[counterArgiments] instanceof Collection) {
                        if (((Collection) args[counterArgiments]).isEmpty()) {
                            throw new SQLException("Invalid argument. Collection is Empty.");
                        }
                        for (Object object : (Collection) args[counterArgiments]) {
                            q = (counterList++ == 0 ? "" : q + ",") + "?";
                            resultArgs.add(object);
                        }
                    } else {
                        resultArgs.add(args[counterArgiments]);
                    }
                    resultQuery = resultQuery + q + queryPart;

                }
                counterArgiments++;
            }

            PreparedStatement statement = Query.getConnection().prepareStatement(resultQuery, Statement.RETURN_GENERATED_KEYS);
            if (!resultArgs.isEmpty()) {
                int index = 0;
                for (Object param : resultArgs) {
                    Query.processValue(statement, ++index, param);
                }
            }
            return statement;
        }
    }

    private static void exequteSelectQuery(QueryResult result) throws SQLException {
        PreparedStatement statement = Query.prepareStatement(result.getQuery(), result.getQueryParams());
        result.setData(statement.executeQuery());
    }

    private static void exequteInsertQuery(QueryResult result) throws SQLException {
        PreparedStatement statement = Query.prepareStatement(result.getQuery(), result.getQueryParams());
        if (statement.executeUpdate() > 0) {
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                result.setLastInsertId(rs.getInt(1));
            }
        }
    }

    private static void exequteUpdateQuery(QueryResult result) throws SQLException {
        PreparedStatement statement = Query.prepareStatement(result.getQuery(), result.getQueryParams());
        result.setCountAffectedRows(statement.executeUpdate());
    }

    private static void exequteDeleteQuery(QueryResult result) throws SQLException {
        Query.exequteUpdateQuery(result);
    }

    /**
     * Executing SQL query with arguments depends on its type.
     *
     * @see #identifyQuery(java.lang.String) List value will be replaced same as
     * in prepareStatement method
     * @see #prepareStatement(java.lang.String, java.lang.Object...) ;
     *
     * @param queryType one of query type
     * @param query string
     * @param args zero or more params
     * @return Result of Query
     * @throws SQLException
     */
    public static QueryResult executeQuery(int queryType, String query, Object... args) throws SQLException {
        QueryResult rs = new QueryResult();
        rs.setQuery(query);
        rs.setQueryParams(args);

        switch (queryType) {
            case Query.QUERY_SELECT:
                Query.exequteSelectQuery(rs);
                break;
            case Query.QUERY_INSERT:
                Query.exequteInsertQuery(rs);
                break;
            case Query.QUERY_UPDATE:
                Query.exequteUpdateQuery(rs);
                break;
            case Query.QUERY_DELETE:
                Query.exequteDeleteQuery(rs);
                break;
        }
        return rs;
    }

    /**
     * Executing SQL query with arguments. Type of query will be identifying
     * automatic.
     *
     * @see #identifyQuery(java.lang.String)
     * <p>
     * List value will be replaced same as in prepareStatement method
     * @see #prepareStatement(java.lang.String, java.lang.Object...) ;
     * </p>
     *
     * @param query string
     * @param args zero or more params
     * @return Result of Query
     * @throws SQLException
     */
    public static QueryResult executeQuery(String query, Object... args) throws SQLException {
        return Query.executeQuery(Query.identifyQuery(query), query, args);
    }

    /**
     * Import SQL from file
     *
     * @param filename
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    public static void executeFromFile(String filename) throws FileNotFoundException, IOException, SQLException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filename)));
        String line;
        String sql = "";

        while ((line = bufferedReader.readLine()) != null) {
            if (line.trim().startsWith("//")) {
                continue;
            }
            if (line.trim().startsWith("--")) {
                continue;
            }

            sql += " " + line.trim();
            sql = sql.trim();

            if (line.indexOf("--") >= 0) {
                sql += "\n";
            }

            if (sql.endsWith(";")) {
                Query.executeQuery(sql.substring(0, sql.length() - 1));
                sql = "";
            }
        }

        if (!sql.equals("")) {
            Query.executeQuery(sql);
        }

    }

    /**
     * Method for identifying query
     *
     * @param query
     * @return type of query
     */
    public static int identifyQuery(String query) {
        query = query.toLowerCase().trim();
        if (query.startsWith("insert")) {
            return Query.QUERY_INSERT;
        }
        if (query.startsWith("update")) {
            return Query.QUERY_UPDATE;
        }
        if (query.startsWith("delete")) {
            return Query.QUERY_DELETE;
        }
        if (query.startsWith("truncate") || query.startsWith("create") || query.startsWith("drop") || query.startsWith("alter")) {
            return Query.QUERY_UPDATE;
        }

        return Query.QUERY_SELECT;
    }

    /**
     * Method part of query creation.
     *
     * <pre>
     * QueryResult result=new Query().select("*").from("table").execute();
     * //the same as
     * QueryResult result=new Query().from("table").execute();
     * </pre>
     *
     * @param selectString default "*"
     * @return Query object for continue query building
     */
    public Query select(String selectString) {
        this.selectString = selectString;
        this.queryType = Query.QUERY_SELECT;
        return this;
    }

    /**
     * Method part of query creation. Synonym for select("*")
     *
     * <pre>
     * QueryResult result=new Query().select().from("table").execute();
     * the same as
     * QueryResult result=new Query().select("*").from("table").execute();
     * </pre>
     *
     * @return Query object for continue query building
     */
    public Query select() {
        return this.select("*");
    }

    /**
     * Method part of query creation.
     *
     * <pre>
     *   QueryResult result=new Query().update("table").set("name","Jack").where("cname=?", "Vorobey").execute();
     *   Integer countUpdatedRows=result.getCountAffectedRows();
     * </pre>
     *
     * @param tableName
     * @return Query object for continue query building
     */
    public Query update(String tableName) {
        this.queryType = Query.QUERY_UPDATE;
        return this.from(tableName);
    }

    /**
     * Method part of query creation.
     *
     * <pre>
     *   QueryResult result=new Query().insert("table").set("name","Jack").set("cname","Vorobey").set("created_at",new Date()).execute();
     *   Integer insertedId=result.getLastInsertId();
     * </pre>
     *
     * @param tableName
     * @return Query object for continue query building
     */
    public Query insert(String tableName) {
        this.queryType = Query.QUERY_INSERT;
        return this.from(tableName);
    }

    /**
     * Method part of query creation.
     *
     * <pre>
     *   QueryResult result=new Query().delete("table").where("cname=?", "Vorobey").execute();
     *   Integer countDeletedRows=result.getCountAffectedRows();
     * </pre>
     *
     * @param tableName
     * @return Query object for continue query building
     */
    public Query delete(String tableName) {
        this.queryType = Query.QUERY_DELETE;
        return this.from(tableName);
    }

    /**
     * Method part of query creation.
     *
     * <pre>
     * QueryResult result=new Query().select("*").from("table").execute();
     * //the same as
     * QueryResult result=new Query().from("table").execute();
     * </pre>
     *
     * @param tableName
     * @return Query object for continue query building
     */
    public Query from(String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * Method part of query creation. Using for insert and update commands.
     *
     * <pre>
     *   QueryResult result=new Query().update("table").set("name","Jack").where("cname=?", "Vorobey").execute();
     *   QueryResult result=new Query().insert("table").set("name","Jack").set("cname","Vorobey").set("created_at",new Date()).execute();
     * </pre>
     *
     * @param field to update/insert
     * @param value to update/insert
     * @return Query object for continue query building
     */
    public Query set(String field, Object value) {
        this.fields.put(field, value);
        return this;
    }

    /**
     * Method part of query creation. Using for select, update and delete.
     *
     * <pre>
     *   QueryResult result=new Query().select("name").from("table").where("name=?","Jack").execute();
     *   QueryResult result=new Query().update("table").set("name","Jack").where("cname=?", "Vorobey").execute();
     *   QueryResult result=new Query().delete("table").where("cname=?", "Vorobey").execute();
     * </pre>
     *
     * @param condition string like "id=?"
     * @param args values using in condition
     * @return Query object for continue query building
     * @throws SQLException if count '?' condition!=arguments.count
     */
    public Query where(String condition, Object... args) throws SQLException {
        int qCount = (" " + condition + " ").split("\\?").length - 1;
        if (qCount != args.length) {
            throw new SQLException("Invalid arguments in where method. Expected " + qCount + " Actual " + args.length);
        }
        this.whereParams.addAll(Arrays.asList(args));
        this.whereCondition = (this.whereCondition == null ? " WHERE " : this.whereCondition + " AND ") + condition;
        return this;
    }

    /**
     * Alias for .where("id=?",id)
     *
     * @param id
     * @return Query object for continue query building
     * @throws SQLException
     */
    public Query whereId(Integer id) throws SQLException {
        return this.where("id=?", id);
    }

    /**
     * Method part of query creation. Can be used more than once if need more
     * than once orders.
     *
     * <pre>
     *   QueryResult result=new Query().select("name").from("table").where("name=?","Jack").order("name","asc").limit(10).execute();
     * </pre>
     *
     * @param OrderField
     * @param direction "asc" or "desc"
     * @return Query object for continue query building
     */
    public Query order(String OrderField, String direction) {
        this.order.put(OrderField, direction);
        return this;
    }

    /**
     * Method part of query creation.
     *
     * <pre>
     *   QueryResult result=new Query().select("name").from("table").limit(10).execute();
     * </pre>
     *
     * @param limit
     * @return Query object for continue query building
     */
    public Query limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Method part of query creation.
     *
     * <pre>
     *   QueryResult result=new Query().select("name").from("table").limit(10).offset(10).execute();
     * </pre>
     *
     * @param offset
     * @return Query object for continue query building
     */
    public Query offset(Integer offset) {
        this.offset = offset;
        return this;
    }

    /**
     * Executing query and return QueryResult - once result for all types of
     * query
     *
     * @see QueryResult
     *
     * <pre>
     *   QueryResult result=new Query().select("name").from("table").limit(10).offset(10).execute();
     * </pre>
     *
     * @return QueryResult - once result for all types of query
     * @throws SQLException
     */
    public QueryResult execute() throws SQLException {
        return Query.executeQuery(this.queryType, this.createQuery(), this.prepareParams().toArray());
    }

    private List prepareParams() {
        List params = new ArrayList();
        //TODO: optimize switch
        switch (queryType) {
            case Query.QUERY_SELECT:
                params.addAll(this.whereParams);
                break;
            case Query.QUERY_INSERT:
                params.addAll(this.setParams);
                break;
            case Query.QUERY_UPDATE:
                params.addAll(setParams);
                params.addAll(whereParams);
                break;
            case Query.QUERY_DELETE:
                params.addAll(this.whereParams);
                break;
        }
        return params;
    }

    private String createSelectQuery() throws SQLException {
        String query = "SELECT " + this.selectString + " FROM " + this.tableName;
        if (this.whereCondition != null) {
            query = query + this.whereCondition;
        }
        if (!this.order.isEmpty()) {
            String orderString = " ORDER BY ";
            int index = 0;
            for (String field : this.order.keySet()) {
                orderString = orderString + (index++ != 0 ? "," : "") + Query.getIdentifierQuoteString() + field + Query.getIdentifierQuoteString() + " " + this.order.get(field);
            }
            query = query + orderString;
        }
        if (this.limit != null) {
            query = query + " LIMIT " + this.limit;
        }
        if (this.offset != null) {
            query = query + " OFFSET " + this.offset;
        }
        return query;
    }

    private String createUpdateQuery() throws SQLException {
        String query = "UPDATE " + this.tableName + " ";
        int index = 0;
        for (String field : this.fields.keySet()) {
            query = query + (index++ != 0 ? "," : "SET ") + Query.getIdentifierQuoteString() + field + Query.getIdentifierQuoteString() + "=?";
            this.setParams.add(this.fields.get(field));
        }
        if (this.whereCondition != null) {
            query = query + this.whereCondition;
        }
        if (this.limit != null) {
            query = query + " LIMIT " + this.limit;
        }
        if (this.offset != null) {
            query = query + " OFFSET " + this.offset;
        }
        return query;
    }

    private String createInsertQuery() throws SQLException {
        String query = "INSERT INTO " + this.tableName;
        String fieldNames = " (";
        int index = 0;
        for (String field : this.fields.keySet()) {
            fieldNames = fieldNames + (index++ != 0 ? "," : "") + Query.getIdentifierQuoteString() + field + Query.getIdentifierQuoteString();
            this.setParams.add(this.fields.get(field));
        }
        fieldNames = fieldNames + ")";
        String values = " (";
        for (int i = 0; i < this.fields.values().size(); i++) {
            values = values + (i != 0 ? "," : "") + "?";
        }
        values = values + ")";
        query = query + fieldNames + " VALUES " + values;
        return query;
    }

    private String createDeleteQuery() throws SQLException {
        String query = "DELETE FROM " + this.tableName + " ";
        if (this.whereCondition != null) {
            query = query + this.whereCondition;
        }
        if (this.limit != null) {
            query = query + " LIMIT " + this.limit;
        }
        if (this.offset != null) {
            query = query + " OFFSET " + this.offset;
        }
        return query;
    }

    private String createQuery() throws SQLException {
        switch (queryType) {
            case Query.QUERY_SELECT:
                return this.createSelectQuery();
            case Query.QUERY_INSERT:
                return this.createInsertQuery();
            case Query.QUERY_UPDATE:
                return this.createUpdateQuery();
            case Query.QUERY_DELETE:
                return this.createDeleteQuery();
        }
        return null;
    }

}
