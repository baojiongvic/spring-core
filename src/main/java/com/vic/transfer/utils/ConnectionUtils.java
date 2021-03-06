package com.vic.transfer.utils;

import com.vic.transfer.annotation.Component;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author vic
 * @date 2021/12/6 9:50 下午
 */
@Component
public class ConnectionUtils {

    private ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

//    private volatile static ConnectionUtils instance;
//
//    public static ConnectionUtils getInstance() {
//        if (instance == null) {
//            synchronized (ConnectionUtils.class) {
//                if (instance == null) {
//                    instance = new ConnectionUtils();
//                }
//            }
//        }
//        return instance;
//    }

    public Connection getCurrentThreadConnection() throws SQLException {
        Connection connection = threadLocal.get();
        if (connection == null) {
            connection = DruidUtils.getInstance().getConnection();
            threadLocal.set(connection);
        }
        return connection;
    }

}
