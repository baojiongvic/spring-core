package com.vic.transfer.factory;

import com.vic.transfer.annotation.Autowired;
import com.vic.transfer.annotation.Component;
import com.vic.transfer.utils.TransactionManager;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Proxy;

/**
 * @author vic
 * @date 2021/12/6 11:11 下午
 */
@Component
public class ProxyFactory {

    @Autowired
    private TransactionManager transactionManager;

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

//    private volatile static ProxyFactory instance;
//
//    public static ProxyFactory getInstance() {
//        if (instance == null) {
//            synchronized (ProxyFactory.class) {
//                if (instance == null) {
//                    instance = new ProxyFactory();
//                }
//            }
//        }
//        return instance;
//    }

    /**
     * Jdk代理
     *
     * @param o o
     * @return {@link Object}
     */
    public Object getJdkProxy(Object o) {
        return Proxy.newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), (proxy, method, args) -> {
            Object result = null;
            try {
                // 开启事务,关闭自动提交
                transactionManager.begin();
                result = method.invoke(o, args);
                transactionManager.commit();
            } catch (Exception e) {
                transactionManager.rollback();
                throw e;
            }
            return result;

        });
    }

    /**
     * cglib代理
     *
     * @param o o
     * @return {@link Object}
     */
    public Object getCglibProxy(Object o) {
        return Enhancer.create(o.getClass(), (InvocationHandler) (o1, method, objects) -> {
            Object result = null;
            try {
                transactionManager.begin();
                result = method.invoke(o1, objects);
                transactionManager.commit();
            } catch (Exception e) {
                e.printStackTrace();
                transactionManager.rollback();
                throw e;
            }
            return result;
        });
    }
}
