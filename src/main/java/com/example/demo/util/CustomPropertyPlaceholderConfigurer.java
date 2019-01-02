package com.example.demo.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class CustomPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
  private static Map<String, String> propertiesMap;
  private int springSystemPropertiesMode = 1;

  public void setSystemPropertiesMode(int systemPropertiesMode) {
    super.setSystemPropertiesMode(systemPropertiesMode);
    this.springSystemPropertiesMode = systemPropertiesMode;
  }

  protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
      throws BeansException {
    super.processProperties(beanFactory, props);
    propertiesMap = new HashMap();
    Iterator arg3 = props.keySet().iterator();

    while (arg3.hasNext()) {
      Object key = arg3.next();
      String keyStr = key.toString();
      String valueStr = this.resolvePlaceholder(keyStr, props, this.springSystemPropertiesMode);
      propertiesMap.put(keyStr, valueStr);
    }

  }

  public String getProperty(String name) {
    return (String) propertiesMap.get(name);
  }
}
