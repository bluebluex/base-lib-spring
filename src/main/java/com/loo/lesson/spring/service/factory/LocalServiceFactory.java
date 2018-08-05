package com.loo.lesson.spring.service.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @program: base-core
 * @description: 本地服务工厂
 * @author: Tomax
 * @create: 2018-07-20 18:08
 **/
public class LocalServiceFactory implements Serializable, ServiceFactory, BeanFactoryAware {

    private static final long serialVersionUID = -6674433306272419490L;

    private BeanFactory beanFactory;

    @Override
    public <T> T getService(Class<T> clazz){
        String beanId = this.getServiceName(clazz, null);
        return beanFactory.getBean(beanId, clazz);
    }

    @Override
    public <T> T getService(Class<T> clazz, String beanId){
        beanId = this.getServiceName(clazz, beanId);
        return beanFactory.getBean(beanId, clazz);
    }

    private <T> String getServiceName(Class<T> clazz, String servicename) {
        String beanName = servicename;
        if (!StringUtils.hasLength(servicename)) {
            // 得到HelloService
            beanName = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
            // 首字母小写，得到helloService
            beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);

        }
        return beanName;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
