package com.loo.lesson.spring.service.factory;

/**
 * @program: base-core
 * @description: 服务工厂接口
 * @author: Tomax
 * @create: 2018-07-20 18:08
 **/
public interface ServiceFactory {
    /**
     * 获取服务，通过class
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getService(Class<T> clazz);

    /**
     * 获取服务，通过beanId
     * @param clazz
     * @param beanId
     * @param <T>
     * @return
     */
    <T> T getService(Class<T> clazz, String beanId);
}
