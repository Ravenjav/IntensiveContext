package com.aston.context.annotation;

import com.aston.context.service.DependencyFactory;
import com.aston.context.service.IntensiveComponentSearchService;
import com.aston.context.service.IntensiveContextDependencyFactory;
import com.aston.context.service.SearchService;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class IntensiveContext{
    private String basePackage;
    private SearchService searchService;
    private DependencyFactory dependencyFactory;
    private Map<Class<?>, Object> singletonObjects = new ConcurrentHashMap<>();

    public IntensiveContext(String basePackage) {
        this.basePackage = basePackage;
        this.searchService = new IntensiveComponentSearchService();
        this.dependencyFactory = new IntensiveContextDependencyFactory();
    }
    public <T> T getObject(Class<T> type) {
        // Находим все классы с аннотацией @IntensiveComponent
        Set<Class<?>> classes = searchService.findClassesWithAnnotation(basePackage, IntensiveComponent.class);

        // Выбираем реализацию интерфейса
        Class<?> implementation = findImplementation(classes, type);

        // Создаем экземпляр объекта
        return (T) dependencyFactory.createInstance(implementation);
    }

    private Class<?> findImplementation(Set<Class<?>> classes, Class<?> type) {
        for (Class<?> clazz : classes) {
            if (type.isAssignableFrom(clazz)) {
                return clazz;
            }
        }
        throw new RuntimeException("No implementation found for " + type.getName());
    }
}
