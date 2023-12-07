package com.ykj.graduation_design.common;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentProperty {

    @Autowired
    public  Environment environment;
    private static EnvironmentProperty EnvironmentProperty;

    @PostConstruct
    public void init(){
        EnvironmentProperty = this;
        EnvironmentProperty.environment = this.environment;
    }

    public static String getProperty(String keyValue){
        return EnvironmentProperty.environment.getProperty(keyValue);
    }
}
