package com.alexlee1987.smartlrecyclerview.bean;

/**
 * 测试Demo的bean类
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class TestBean {
    private String name;
    private String city;

    public TestBean(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String age) {
        this.city = age;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
