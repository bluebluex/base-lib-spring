package com.loo.lesson.spring.util.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @program: base-core
 * @description: 动态系统属性文件配置，为注入Servlet上下文准备
 * @author: Tomax
 * @create: 2018-07-20 18:06
 **/
public class DynamicServerConfig extends PropertyPlaceholderConfigurer {

    private boolean localOverride = false;

    private Properties[] localProperties;
    private static Map ctxPropertiesMap;
    private Resource[] locations;

    private final PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();

    private boolean ignoreResourceNotFound = false;

    /**
     * Set if failure to find the property resource should be ignored. True is appropriate if the properties file
     * id completely optional. Default is "false".
     * @param ignoreResourceNotFound
     */
    @Override
    public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
        this.ignoreResourceNotFound = ignoreResourceNotFound;
    }

    /**
     * Set local properties, e.g. via the "props" tag in XML bean definitions.
     * These can be considered defaults, to be overridden by properties loaded from files.
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        this.localProperties = new Properties[] {properties};
        super.setProperties(properties);
    }

    /**
     * Set local properties, e.g. via the "prop" tag in XML bean definitions, allowing for
     * merging multiple properties sets into one.
     * @param propertiesArray
     */
    @Override
    public void setPropertiesArray(Properties... propertiesArray) {
        this.localProperties = propertiesArray;
    }

    @Override
    public void setLocation(Resource location) {
        this.locations = new Resource[]{location};
        super.setLocation(location);
    }

    /**
     * Set locations of properties files to be loaded.
     * <p>
     * Can point to classic properties files or to XML files that follow JDK 1.5`s properties XML format.
     * @param locations
     */
    @Override
    public void setLocations(Resource... locations) {
        this.locations = locations;
        super.setLocations(locations);
    }

    /**
     * Set whether local properties override properties from files. Default is "false": properties from
     * files override local defaults. Can be switched to "true" to let local properties override defaults from files.
     */
    @Override
    public void setLocalOverride(boolean localOverride) {
        this.localOverride = localOverride;
    }

    @Override
    protected Properties mergeProperties() throws IOException {
        Properties result = new Properties();

        if (this.localOverride) {
            // Load properties from file upfront, to let local properties
            loadProperties(result);
        }

        if (this.localProperties != null) {
            for (int i = 0; i < this.localProperties.length; i++) {
                CollectionUtils.mergePropertiesIntoMap(this.localProperties[i], result);
            }
        }

        if (!this.localOverride) {
            // Load properties from file afterwards, to let those properties.
            loadProperties(result);
        }
        return result;
    }

    @Override
    protected void loadProperties(Properties props) throws IOException {
        if (this.locations != null) {
            for (int i = 0; i < this.locations.length; i++) {
                Resource location = this.locations[i];
                logger.info("Loading properties file from " + location);
                try (InputStream is = location.getInputStream()){ // JDK 1.7
                    String runEnv = System.getProperty("spring.profiles.active", "dev");
                    if (location.getFilename().indexOf("." + runEnv + ".") > 0) {
                        this.propertiesPersister.load(props, is);
                        break;
                    }
                } catch (IOException e) {
                    if (this.ignoreResourceNotFound) {
                        logger.warn("Could not load properties from " + location + ": " + e.getMessage());
                    } else {
                        throw e;
                    }
                }
            }
        }
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        ctxPropertiesMap = new HashMap();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            ctxPropertiesMap.put(keyStr, value);
        }
    }

    public static Object getContextProperty(String name) {
        return ctxPropertiesMap.get(name);
    }
}
