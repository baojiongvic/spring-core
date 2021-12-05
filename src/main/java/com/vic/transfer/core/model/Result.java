package com.vic.transfer.core.model;

import java.util.StringJoiner;

/**
 * @author vic
 * @date 2021/12/5 2:40 下午
 */
public class Result {

    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Result.class.getSimpleName() + "[", "]")
                .add("status='" + status + "'")
                .add("message='" + message + "'")
                .toString();
    }
}
