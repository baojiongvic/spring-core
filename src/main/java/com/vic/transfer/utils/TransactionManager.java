package com.vic.transfer.utils;

import java.sql.SQLException;

/**
 * @author vic
 * @date 2021/12/6 10:27 下午
 * <p>
 * 负责手动事务开启、提交、回滚
 */
public class TransactionManager {

    private volatile static TransactionManager instance;

    public static TransactionManager getInstance() {
        if (instance == null) {
            synchronized (TransactionManager.class) {
                if (instance == null) {
                    instance = new TransactionManager();
                }
            }
        }
        return instance;
    }

    private TransactionManager() {
    }


    /**
     * 开启
     */
    public void begin() throws SQLException {
        ConnectionUtils.getInstance().getCurrentThreadConnection().setAutoCommit(false);
    }

    /**
     * 提交
     */
    public void commit() throws SQLException {
        ConnectionUtils.getInstance().getCurrentThreadConnection().commit();
    }

    /**
     * 回滚
     */
    public void rollback() throws SQLException {
        ConnectionUtils.getInstance().getCurrentThreadConnection().rollback();
    }
}
