package com.example;

public class MyClass {

    public static void main(String[] args) {
        PosgreUtil posgreUtil = new PosgreUtil();
        User user = new User();
        user.Username = "wkm12";
        user.Password = "wkm12";
        posgreUtil.putUser(user);
        User user1 = posgreUtil.getUser("wkm");
        log(user1);
    }

    public static void log(Object o) {
        System.out.println(o.toString());
    }
}
