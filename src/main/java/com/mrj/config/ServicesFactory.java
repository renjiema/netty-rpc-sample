package com.mrj.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServicesFactory
 *
 * @author by mrj
 * @description
 */
@Slf4j
public class ServicesFactory {
    private final static Properties PROPERTIES = new Properties();
    private final static Map<String, Object> SERVICE_MAP = new ConcurrentHashMap<>();

    static {
        try (InputStream resource = ServicesFactory.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(resource);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Object getServiceImpl(String interfaceClass) {
        return SERVICE_MAP.computeIfAbsent(interfaceClass, k -> {
            final String implName = PROPERTIES.getProperty(interfaceClass);
            final Class<?> instanceClass;
            try {
                instanceClass = Class.forName(implName);
                return instanceClass.newInstance();
            } catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
                log.error("Failed to find com.mrj.test.service implement, com.mrj.test.service name:{}", interfaceClass);
                throw new RuntimeException(e);
            }
        });
    }
}
