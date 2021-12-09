package com.vic.transfer.service;

/**
 * @author vic
 * @date 2021/12/5 2:38 下午
 */
public interface TransferService {

    /**
     * 转换
     *
     * @param fromCardNo
     * @param toCardNo
     * @param money      钱
     * @throws Exception 异常
     */
    void transfer(String fromCardNo,String toCardNo,int money) throws Exception;
}
