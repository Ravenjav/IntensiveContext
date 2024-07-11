package com.aston.context;

import com.aston.context.annotation.IntensiveContext;
import com.aston.context.entity.TestEntity;

public class Main {
    public static void main(String[] args) {
        String basePackage = "com.aston.context";
        IntensiveContext context = new IntensiveContext(basePackage);


        TestEntity testEntity = context.getObject(TestEntity.class);
        testEntity.use();
    }
}