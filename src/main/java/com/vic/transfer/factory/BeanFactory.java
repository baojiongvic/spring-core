package com.vic.transfer.factory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vic
 * @date 2021/12/5 3:05 下午
 * <p>
 * 工厂类，生产对象
 */
public class BeanFactory {

    private static Map<String, Object> beans = new HashMap<>();

    static {
        InputStream is = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
        try {
            Document document = new SAXReader().read(is);
            Element rootElement = document.getRootElement();
            List<Element> beanList = rootElement.selectNodes("//bean");
            for (Element element : beanList) {
                String id = element.attributeValue("id");
                String className = element.attributeValue("class");
                beans.put(id, Class.forName(className).newInstance());
            }

            List<Element> propertyList = rootElement.selectNodes("//property");
            for (Element element : propertyList) {

                // <property name="AccountDao" ref="accountDao"></property>
                String name = element.attributeValue("name");
                String ref = element.attributeValue("ref");

                // 找到需要注入依赖的bean
                Element parent = element.getParent();
                String parentId = parent.attributeValue("id");
                Object parentObject = beans.get(parentId);
                Method[] methods = parentObject.getClass().getMethods();
                for (Method method : methods) {
                    if (method.getName().equalsIgnoreCase("set" + name)) {
                        method.invoke(parentObject, beans.get(ref));
                    }
                }

                // 重新放入map
                beans.put(parentId, parentObject);
            }

        } catch (DocumentException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象
     *
     * @param id id
     * @return {@link Object}
     */
    public static Object getBean(String id) {
        return beans.get(id);
    }
}
