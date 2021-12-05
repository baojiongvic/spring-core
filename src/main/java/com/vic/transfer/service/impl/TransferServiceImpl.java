package com.vic.transfer.service.impl;

import com.vic.transfer.dao.AccountDao;
import com.vic.transfer.dao.entity.Account;
import com.vic.transfer.dao.impl.JdbcAccountDaoImpl;
import com.vic.transfer.factory.BeanFactory;
import com.vic.transfer.service.TransferService;

/**
 * @author vic
 * @date 2021/12/5 2:38 下午
 */
public class TransferServiceImpl implements TransferService {

//    private AccountDao accountDao = new JdbcAccountDaoImpl();

//    private AccountDao accountDao = (AccountDao) BeanFactory.getBean("accountDao");

    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {
        Account from = accountDao.queryAccountByCardNo(fromCardNo);
        Account to = accountDao.queryAccountByCardNo(toCardNo);

        from.setMoney(from.getMoney() - money);
        to.setMoney(to.getMoney() + money);

        accountDao.updateAccountByCardNo(to);
//        int c = 1 / 0;
        accountDao.updateAccountByCardNo(from);
    }
}
