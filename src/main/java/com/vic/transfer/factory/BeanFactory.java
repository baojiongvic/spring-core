package com.vic.transfer.factory;

import cn.hutool.core.util.ClassUtil;
import com.vic.transfer.annotation.Autowired;
import com.vic.transfer.annotation.Component;
import com.vic.transfer.annotation.Transactional;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author vic
 * @date 2021/12/5 3:05 下午
 * <p>
 * 工厂类，生产对象
 */
public class BeanFactory {

    public static final char A = 'A';

    public static final char Z = 'Z';

    public static final String PROXY_FACTORY_BEAN_NAME = "proxyFactory";

    private static final Map<String, Object> BEANS = new HashMap<>();

    private static Set<Class<?>> CLASS = new HashSet<>();

    private static List<String> filedAlreadyProcessed = new ArrayList<>();

    static {
        parseComponentScan();
    }

    /**
     * 解析组件扫描
     */
    private static void parseComponentScan() {
        InputStream is = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
        try {
            Document document = new SAXReader().read(is);
            Element rootElement = document.getRootElement();
            Element scanElement = (Element) rootElement.selectSingleNode("//component-scan");
            String scanPackage = scanElement.attributeValue("base-package");
            CLASS = ClassUtil.scanPackage(scanPackage);
            doInstance();
            doAutowired();
            doTransactional();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doInstance() throws Exception {
        for (Class<?> clazz : CLASS) {
            if (!clazz.isAnnotationPresent(Component.class)) {
                continue;
            }
            // 实例对象放入内存中 key=首字母小写类名
            BEANS.put(lowerFirst(clazz.getSimpleName()), clazz.newInstance());
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces.length != 0) {
                for (Class<?> clazzInterface : interfaces) {
                    BEANS.put(lowerFirst(clazzInterface.getSimpleName()), clazz.newInstance());
                }
            }
        }
        System.out.println(BEANS);
    }

    private static void doAutowired() throws Exception {
        for (Object obj : BEANS.values()) {
            doDependency(obj);
        }
    }

    private static void doDependency(Object obj) throws Exception {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (!field.isAnnotationPresent(Autowired.class)) {
                continue;
            }

            // 变量已注入，跳过
            if (filedAlreadyProcessed.contains(obj.getClass().getName() + "." + field.getName())) {
                continue;
            }

            Object dependency = BEANS.get(field.getType().getName());

            // 如果为null尝试类名首字母小写为key获取
            if (dependency == null) {
                dependency = BEANS.get(lowerFirst(field.getType().getSimpleName()));
            }

            if (dependency == null) {
                throw new RuntimeException(field.getType().getName() + "未实例");
            }

            // 添加标记，表示变量已注入
            filedAlreadyProcessed.add(obj.getClass().getName() + "." + field.getName());

            // 递归
            doDependency(dependency);

            field.setAccessible(true);
            field.set(obj, dependency);
        }
    }

    private static void doTransactional() {
        ProxyFactory proxyFactory = (ProxyFactory) BEANS.get(PROXY_FACTORY_BEAN_NAME);

        for (Map.Entry<String, Object> entry : BEANS.entrySet()) {
            if (!entry.getValue().getClass().isAnnotationPresent(Transactional.class)) {
                continue;
            }
            // 有实现接口
            Class<?>[] interfaces = entry.getValue().getClass().getInterfaces();
            if (interfaces != null && interfaces.length > 0) {
                // 使用jdk动态代理
                BEANS.put(entry.getKey(), proxyFactory.getJdkProxy(entry.getValue()));
            } else {
                // 使用cglib动态代理
                BEANS.put(entry.getKey(), proxyFactory.getCglibProxy(entry.getValue()));
            }
        }
    }

    /**
     * 首字母小写方法
     */
    private static String lowerFirst(String str) {
        char[] chars = str.toCharArray();
        if (A <= chars[0] && chars[0] <= Z) {
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }

    /**
     * 解析beans.xml
     */
    private static void parseBeansXml() {
        InputStream is = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
        try {
            Document document = new SAXReader().read(is);
            Element rootElement = document.getRootElement();
            List<Element> beanList = rootElement.selectNodes("//bean");
            for (Element element : beanList) {
                String id = element.attributeValue("id");
                String className = element.attributeValue("class");
                BEANS.put(id, Class.forName(className).newInstance());
            }

            List<Element> propertyList = rootElement.selectNodes("//property");
            for (Element element : propertyList) {

                // <property name="AccountDao" ref="accountDao"></property>
                String name = element.attributeValue("name");
                String ref = element.attributeValue("ref");

                // 找到需要注入依赖的bean
                Element parent = element.getParent();
                String parentId = parent.attributeValue("id");
                Object parentObject = BEANS.get(parentId);
                Method[] methods = parentObject.getClass().getMethods();
                for (Method method : methods) {
                    if (method.getName().equalsIgnoreCase("set" + name)) {
                        method.invoke(parentObject, BEANS.get(ref));
                    }
                }

                // 重新放入map
                BEANS.put(parentId, parentObject);
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
        return BEANS.get(id);
    }
}
