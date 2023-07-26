package org.caesar.finalWork.dao.task;

import lombok.Data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * @ClassName ObjectInfo
 * @Description 用于储存构造对象所需信息
 */
@Data
public class ObjectInfo {
    private Class[] classes;
    private Method[] methods;
    private String[] names;

    private int size;

    private Constructor<?> constructor;

    public ObjectInfo(Class clazz) throws NoSuchMethodException {
        constructor = clazz.getConstructor();
        Field[] fields = clazz.getDeclaredFields();
        size = fields.length;
        classes = new Class[size];
        methods = new Method[size];
        names = new String[size];

        for (int i = 0; i < size; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            classes[i] = field.getType();
            names[i] = field.getName();
            methods[i] = clazz.getMethod("set" +
                    Character.toUpperCase(names[i].charAt(0)) + names[i].substring(1), classes[i]);
        }

    }
}
