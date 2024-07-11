package com.aston.context.service;

public interface DependencyFactory {
    public <T> T createInstance(Class<T> Tclass);
}
