package com.jaron.fsconnect.bean;

public class User<T> {
    private T user;
    // 本地缓存多余信息
    private String cookie;

    public User() {
    }

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }


}
