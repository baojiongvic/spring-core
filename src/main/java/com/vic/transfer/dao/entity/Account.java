package com.vic.transfer.dao.entity;

import java.util.StringJoiner;

/**
 * @author vic
 * @date 2021/12/5 2:40 下午
 */
public class Account {

    private String cardNo;
    private String name;
    private int money;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Account.class.getSimpleName() + "[", "]")
                .add("cardNo='" + cardNo + "'")
                .add("name='" + name + "'")
                .add("money=" + money)
                .toString();
    }
}
