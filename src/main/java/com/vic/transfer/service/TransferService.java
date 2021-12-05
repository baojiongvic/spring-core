package com.vic.transfer.service;

/**
 * @author vic
 * @date 2021/12/5 2:38 下午
 */
public interface TransferService {

    void transfer(String fromCardNo,String toCardNo,int money) throws Exception;
}
