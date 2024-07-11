package com.aston.context.service;

import com.aston.context.annotation.IntensiveContext;

public interface InjectionService {
    void injectDependency(Object instance, IntensiveContext context);
}
