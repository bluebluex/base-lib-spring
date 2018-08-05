package com.loo.lesson.spring.utils.bean;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

import static org.apache.commons.beanutils.BeanUtils.copyProperty;

/**
 * @program: base-core
 * @description:
 * @author: Tomax
 * @create: 2018-07-20 18:06
 **/
public class BeanUtils {

    private static Log log = LogFactory.getLog(BeanUtils.class);

    public static void copuProperties(Object target, Object source) throws InvocationTargetException, IllegalAccessException {
        ConvertUtils.register(new DateConverter(), java.util.Date.class);
        BeanUtilsBean beanUtil = new BeanUtilsBean();

        // Validate existence of the specified beans
        if (target == null) {
            throw new IllegalArgumentException("No target bean specified");
        }
        if (source == null) {
            throw new IllegalArgumentException("No source bean specified");
        }

        // Copy the properties, converting as necessary
        if (source instanceof DynaBean) {
            DynaProperty[] sourceDescriptors = ((DynaBean) source).getDynaClass().getDynaProperties();
            for (int i = 0; i < sourceDescriptors.length; i++) {
                String name = sourceDescriptors[i].getName();
                if (beanUtil.getPropertyUtils().isWriteable(source, name)) {
                    Object value = ((DynaBean) source).get(name);
                    copyProperty(target, name, value);
                }
            }
        } else if (source instanceof Map) {
            Iterator names = ((Map) source).keySet().iterator();
            while (names.hasNext()) {
                String name = (String) names.next();
                if (beanUtil.getPropertyUtils().isWriteable(source, name)) {
                    Object value = ((Map) source).get(name);
                    copyProperty(target, name, value);
                }
            }
        } else {
            PropertyDescriptor[] sourceDescriptors = beanUtil.getPropertyUtils().getPropertyDescriptors(source);
            for (int i = 0; i < sourceDescriptors.length; i++) {
                String name = sourceDescriptors[i].getName();
                if ("class".equals(name)) {
                    continue;// No point in trying to set an object`s clas
                }
                if (beanUtil.getPropertyUtils().isWriteable(source, name)
                        && beanUtil.getPropertyUtils().isReadable(source, name)) {
                    try {
                        Object value = beanUtil.getPropertyUtils().getSimpleProperty(source, name);
                        if (value == null) {
                            continue;
                        }
                        copyProperty(target, name, value);
                    } catch (NoSuchMethodException e) {
                        log.error("异常： ", e);
                    }

                }
            }
        }
    }
}
