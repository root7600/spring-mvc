package com.yan;

/**
 * @author hairui
 * @date 2021/11/19
 * @des
 */
public class TestException extends RuntimeException {

    private String name;

    public TestException(String message, String name) {
        super(message);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
