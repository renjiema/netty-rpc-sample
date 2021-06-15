package com.mrj.test.service;

/**
 * HelloServiceImpl
 *
 * @author by mrj
 * @description
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "你好，" + name;
    }
}
