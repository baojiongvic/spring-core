package com.vic.transfer.dao;

import com.vic.transfer.dao.entity.Account;

/**
 * @author vic
 * @date 2021/12/5 2:39 下午
 */
public interface AccountDao {

    /**
     * 查询账户
     *
     * @param cardNo 卡号
     * @return {@link Account}
     * @throws Exception 异常
     */
    Account queryAccountByCardNo(String cardNo) throws Exception;

    /**
     * 更新账户
     *
     * @param account 账户
     * @return int
     * @throws Exception 异常
     */
    int updateAccountByCardNo(Account account) throws Exception;
}
