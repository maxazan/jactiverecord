/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jactiverecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author maxazan
 */
public class ConnectionManager {

    public static Connection connection;

    public static void connect(String driverName, String url, String user, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        ConnectionManager.connection = DriverManager.getConnection(url, user, password);
    }

}
