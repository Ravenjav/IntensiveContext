package com.aston.context.service;

import com.aston.context.annotation.IntensiveComponent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class IntensiveContextDependencyFactory implements DependencyFactory{
    private Map<Class<?>, Object> singletonObjects = new HashMap<>();

    public <T> T createInstance(Class<T> clazz) {
        try {
            // Проверяем, не создан ли объект ранее
            if (singletonObjects.containsKey(clazz)) {
                return clazz.cast(singletonObjects.get(clazz));
            }

            // Создаем новый объект
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            T instance = constructor.newInstance();

            // Инициализируем поля, аннотированные @IntensiveComponent
            initializeFields(instance);

            // Если объект является синглтоном, сохраняем его
            if (clazz.isAnnotationPresent(IntensiveComponent.class)) {
                singletonObjects.put(clazz, instance);
            }

            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create instance of " + clazz.getName(), e);
        }
    }

    private void initializeFields(Object instance) throws IllegalAccessException {
        Class<?> clazz = instance.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(IntensiveComponent.class)) {
                field.setAccessible(true);
                Object fieldValue = createInstance(field.getType());
                field.set(instance, fieldValue);
            }
        }
    }
}
