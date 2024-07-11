package com.aston.context.service;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface SearchService {
    Set<Class<?>> findClassesWithAnnotation(String basePackage, Class<? extends Annotation> annotation);
}
