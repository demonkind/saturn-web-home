/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.common;


import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class ClassUtil {

    public static final char RESOURCE_SEPARATOR_CHAR = '/';

    public static final char PACKAGE_SEPARATOR_CHAR = '.';

    public static final String PACKAGE_SEPARATOR = String.valueOf(PACKAGE_SEPARATOR_CHAR);

    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';

    public static final String INNER_CLASS_SEPARATOR = String.valueOf(INNER_CLASS_SEPARATOR_CHAR);

    private static Map TYPE_MAP = Collections.synchronizedMap(new WeakHashMap());

    public static String getClassNameForObject(Object object) {
        if (object == null) {
            return null;
        }

        return getClassName(object.getClass().getName(), true);
    }

    public static String getClassName(Class clazz) {
        if (clazz == null) {
            return null;
        }

        return getClassName(clazz.getName(), true);
    }

    public static String getClassName(String className) {
        return getClassName(className, true);
    }

    private static String getClassName(String className, boolean processInnerClass) {
        if (StringUtil.isEmpty(className)) {
            return className;
        }

        if (processInnerClass) {
            className = className.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
        }

        int length    = className.length();
        int dimension = 0;

        for (int i = 0; i < length; i++, dimension++) {
            if (className.charAt(i) != '[') {
                break;
            }
        }

        if (dimension == 0) {
            return className;
        }

        if (length <= dimension) {
            return className; 
        }

        StringBuffer componentTypeName = new StringBuffer();

        switch (className.charAt(dimension)) {
            case 'Z':
                componentTypeName.append("boolean");
                break;

            case 'B':
                componentTypeName.append("byte");
                break;

            case 'C':
                componentTypeName.append("char");
                break;

            case 'D':
                componentTypeName.append("double");
                break;

            case 'F':
                componentTypeName.append("float");
                break;

            case 'I':
                componentTypeName.append("int");
                break;

            case 'J':
                componentTypeName.append("long");
                break;

            case 'S':
                componentTypeName.append("short");
                break;

            case 'L':

                if ((className.charAt(length - 1) != ';') || (length <= (dimension + 2))) {
                    return className; 
                }

                componentTypeName.append(className.substring(dimension + 1, length - 1));
                break;

            default:
                return className; 
        }

        for (int i = 0; i < dimension; i++) {
            componentTypeName.append("[]");
        }

        return componentTypeName.toString();
    }

    public static String getShortClassNameForObject(Object object) {
        if (object == null) {
            return null;
        }

        return getShortClassName(object.getClass().getName());
    }

    public static String getShortClassName(Class clazz) {
        if (clazz == null) {
            return null;
        }

        return getShortClassName(clazz.getName());
    }

    public static String getShortClassName(String className) {
        if (StringUtil.isEmpty(className)) {
            return className;
        }

        className = getClassName(className, false);

        char[] chars   = className.toCharArray();
        int    lastDot = 0;

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == PACKAGE_SEPARATOR_CHAR) {
                lastDot = i + 1;
            } else if (chars[i] == INNER_CLASS_SEPARATOR_CHAR) {
                chars[i] = PACKAGE_SEPARATOR_CHAR;
            }
        }

        return new String(chars, lastDot, chars.length - lastDot);
    }

    public static String getPackageNameForObject(Object object) {
        if (object == null) {
            return null;
        }

        return getPackageName(object.getClass().getName());
    }

    public static String getPackageName(Class clazz) {
        if (clazz == null) {
            return null;
        }

        return getPackageName(clazz.getName());
    }

    public static String getPackageName(String className) {
        if (StringUtil.isEmpty(className)) {
            return null;
        }

        className = getClassName(className, false);

        int i = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);

        if (i == -1) {
            return "";
        }

        return className.substring(0, i);
    }

    public static String getClassNameForObjectAsResource(Object object) {
        if (object == null) {
            return null;
        }

        return object.getClass().getName().replace(PACKAGE_SEPARATOR_CHAR, RESOURCE_SEPARATOR_CHAR)
        + ".class";
    }

    public static String getClassNameAsResource(Class clazz) {
        if (clazz == null) {
            return null;
        }

        return clazz.getName().replace(PACKAGE_SEPARATOR_CHAR, RESOURCE_SEPARATOR_CHAR) + ".class";
    }

    public static String getClassNameAsResource(String className) {
        if (className == null) {
            return null;
        }

        return className.replace(PACKAGE_SEPARATOR_CHAR, RESOURCE_SEPARATOR_CHAR) + ".class";
    }

    public static String getPackageNameForObjectAsResource(Object object) {
        if (object == null) {
            return null;
        }

        return getPackageNameForObject(object).replace(PACKAGE_SEPARATOR_CHAR,
            RESOURCE_SEPARATOR_CHAR);
    }

    public static String getPackageNameAsResource(Class clazz) {
        if (clazz == null) {
            return null;
        }

        return getPackageName(clazz).replace(PACKAGE_SEPARATOR_CHAR, RESOURCE_SEPARATOR_CHAR);
    }

    public static String getPackageNameAsResource(String className) {
        if (className == null) {
            return null;
        }

        return getPackageName(className).replace(PACKAGE_SEPARATOR_CHAR, RESOURCE_SEPARATOR_CHAR);
    }

    public static Class getArrayClass(Class componentType, int dimension) {
        if (dimension <= 0) {
            return componentType;
        }

        if (componentType == null) {
            return null;
        }

        return Array.newInstance(componentType, new int[dimension]).getClass();
    }

    public static Class getArrayComponentType(Class type) {
        if (type == null) {
            return null;
        }

        return getTypeInfo(type).getArrayComponentType();
    }

    public static int getArrayDimension(Class clazz) {
        if (clazz == null) {
            return -1;
        }

        return getTypeInfo(clazz).getArrayDimension();
    }

    public static List getSuperclasses(Class clazz) {
        if (clazz == null) {
            return null;
        }

        return getTypeInfo(clazz).getSuperclasses();
    }

    public static List getInterfaces(Class clazz) {
        if (clazz == null) {
            return null;
        }

        return getTypeInfo(clazz).getInterfaces();
    }

    public static boolean isInnerClass(Class clazz) {
        if (clazz == null) {
            return false;
        }

        return StringUtil.contains(clazz.getName(), INNER_CLASS_SEPARATOR_CHAR);
    }

    public static boolean isAssignable(Class[] classes, Class[] fromClasses) {
        if (!ArrayUtil.isSameLength(fromClasses, classes)) {
            return false;
        }

        if (fromClasses == null) {
            fromClasses = ArrayUtil.EMPTY_CLASS_ARRAY;
        }

        if (classes == null) {
            classes = ArrayUtil.EMPTY_CLASS_ARRAY;
        }

        for (int i = 0; i < fromClasses.length; i++) {
            if (isAssignable(classes[i], fromClasses[i]) == false) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAssignable(Class clazz, Class fromClass) {
        if (clazz == null) {
            return false;
        }

        if (fromClass == null) {
            return !clazz.isPrimitive();
        }

        if (clazz.isAssignableFrom(fromClass)) {
            return true;
        }

        if (clazz.isPrimitive()) {
            if (Boolean.TYPE.equals(clazz)) {
                return Boolean.class.equals(fromClass);
            }

            if (Byte.TYPE.equals(clazz)) {
                return Byte.class.equals(fromClass);
            }

            if (Character.TYPE.equals(clazz)) {
                return Character.class.equals(fromClass);
            }

            if (Short.TYPE.equals(clazz)) {
                return Short.class.equals(fromClass) || Byte.TYPE.equals(fromClass)
                || Byte.class.equals(fromClass);
            }

            if (Integer.TYPE.equals(clazz)) {
                return Integer.class.equals(fromClass) || Byte.TYPE.equals(fromClass)
                || Byte.class.equals(fromClass) || Short.TYPE.equals(fromClass)
                || Short.class.equals(fromClass) || Character.TYPE.equals(fromClass)
                || Character.class.equals((fromClass));
            }

            if (Long.TYPE.equals(clazz)) {
                return Long.class.equals(fromClass) || Integer.TYPE.equals(fromClass)
                || Integer.class.equals(fromClass) || Byte.TYPE.equals(fromClass)
                || Byte.class.equals(fromClass) || Short.TYPE.equals(fromClass)
                || Short.class.equals(fromClass) || Character.TYPE.equals(fromClass)
                || Character.class.equals((fromClass));
            }

            if (Float.TYPE.equals(clazz)) {
                return Float.class.equals(fromClass) || Long.TYPE.equals(fromClass)
                || Long.class.equals(fromClass) || Integer.TYPE.equals(fromClass)
                || Integer.class.equals(fromClass) || Byte.TYPE.equals(fromClass)
                || Byte.class.equals(fromClass) || Short.TYPE.equals(fromClass)
                || Short.class.equals(fromClass) || Character.TYPE.equals(fromClass)
                || Character.class.equals((fromClass));
            }

            if (Double.TYPE.equals(clazz)) {
                return Double.class.equals(fromClass) || Float.TYPE.equals(fromClass)
                || Float.class.equals(fromClass) || Long.TYPE.equals(fromClass)
                || Long.class.equals(fromClass) || Integer.TYPE.equals(fromClass)
                || Integer.class.equals(fromClass) || Byte.TYPE.equals(fromClass)
                || Byte.class.equals(fromClass) || Short.TYPE.equals(fromClass)
                || Short.class.equals(fromClass) || Character.TYPE.equals(fromClass)
                || Character.class.equals((fromClass));
            }
        }

        return false;
    }

    protected static TypeInfo getTypeInfo(Class type) {
        if (type == null) {
            throw new IllegalArgumentException("Parameter clazz should not be null");
        }

        TypeInfo classInfo;

        synchronized (TYPE_MAP) {
            classInfo = (TypeInfo) TYPE_MAP.get(type);

            if (classInfo == null) {
                classInfo = new TypeInfo(type);
                TYPE_MAP.put(type, classInfo);
            }
        }

        return classInfo;
    }

    protected static class TypeInfo {
        private Class type;
        private Class componentType;
        private int   dimension;
        private List  superclasses = new ArrayList(2);
        private List  interfaces   = new ArrayList(2);

        private TypeInfo(Class type) {
            this.type = type;

            Class componentType = null;

            if (type.isArray()) {
                componentType = type;

                do {
                    componentType = componentType.getComponentType();
                    dimension++;
                } while (componentType.isArray());
            }

            this.componentType = componentType;

            if (dimension > 0) {
                componentType = getNonPrimitiveType(componentType);

                Class superComponentType = componentType.getSuperclass();

                if ((superComponentType == null) && !Object.class.equals(componentType)) {
                    superComponentType = Object.class;
                }

                if (superComponentType != null) {
                    Class superclass = getArrayClass(superComponentType, dimension);

                    superclasses.add(superclass);
                    superclasses.addAll(getTypeInfo(superclass).superclasses);
                } else {
                    for (int i = dimension - 1; i >= 0; i--) {
                        superclasses.add(getArrayClass(Object.class, i));
                    }
                }
            } else {
                type = getNonPrimitiveType(type);

                Class superclass = type.getSuperclass();

                if (superclass != null) {
                    superclasses.add(superclass);
                    superclasses.addAll(getTypeInfo(superclass).superclasses);
                }
            }

            if (dimension == 0) {
                Class[] typeInterfaces = type.getInterfaces();
                List    set = new ArrayList();

                for (int i = 0; i < typeInterfaces.length; i++) {
                    Class typeInterface = typeInterfaces[i];

                    set.add(typeInterface);
                    set.addAll(getTypeInfo(typeInterface).interfaces);
                }

                for (Iterator i = superclasses.iterator(); i.hasNext();) {
                    Class typeInterface = (Class) i.next();

                    set.addAll(getTypeInfo(typeInterface).interfaces);
                }

                for (Iterator i = set.iterator(); i.hasNext();) {
                    Class interfaceClass = (Class) i.next();

                    if (!interfaces.contains(interfaceClass)) {
                        interfaces.add(interfaceClass);
                    }
                }
            } else {
                for (Iterator i = getTypeInfo(componentType).interfaces.iterator(); i.hasNext();) {
                    Class componentInterface = (Class) i.next();

                    interfaces.add(getArrayClass(componentInterface, dimension));
                }
            }
        }

        private Class getNonPrimitiveType(Class type) {
            if (type.isPrimitive()) {
                if (Integer.TYPE.equals(type)) {
                    type = Integer.class;
                } else if (Long.TYPE.equals(type)) {
                    type = Long.class;
                } else if (Short.TYPE.equals(type)) {
                    type = Short.class;
                } else if (Byte.TYPE.equals(type)) {
                    type = Byte.class;
                } else if (Float.TYPE.equals(type)) {
                    type = Float.class;
                } else if (Double.TYPE.equals(type)) {
                    type = Double.class;
                } else if (Boolean.TYPE.equals(type)) {
                    type = Boolean.class;
                } else if (Character.TYPE.equals(type)) {
                    type = Character.class;
                }
            }

            return type;
        }

        public Class getType() {
            return type;
        }

        public Class getArrayComponentType() {
            return componentType;
        }

        public int getArrayDimension() {
            return dimension;
        }

        public List getSuperclasses() {
            return Collections.unmodifiableList(superclasses);
        }

        public List getInterfaces() {
            return Collections.unmodifiableList(interfaces);
        }
    }

    public static Class getPrimitiveType(Class clazz) {
        if (clazz == null) {
            return null;
        }

        if (clazz.isPrimitive()) {
            return clazz;
        }

        if (clazz.equals(Long.class)) {
            return long.class;
        }

        if (clazz.equals(Integer.class)) {
            return int.class;
        }

        if (clazz.equals(Short.class)) {
            return short.class;
        }

        if (clazz.equals(Byte.class)) {
            return byte.class;
        }

        if (clazz.equals(Double.class)) {
            return double.class;
        }

        if (clazz.equals(Float.class)) {
            return float.class;
        }

        if (clazz.equals(Boolean.class)) {
            return boolean.class;
        }

        if (clazz.equals(Character.class)) {
            return char.class;
        }

        return null;
    }

    public static Class getNonPrimitiveType(Class clazz) {
        if (clazz == null) {
            return null;
        }

        if (!clazz.isPrimitive()) {
            return clazz;
        }

        if (clazz.equals(long.class)) {
            return Long.class;
        }

        if (clazz.equals(int.class)) {
            return Integer.class;
        }

        if (clazz.equals(short.class)) {
            return Short.class;
        }

        if (clazz.equals(byte.class)) {
            return Byte.class;
        }

        if (clazz.equals(double.class)) {
            return Double.class;
        }

        if (clazz.equals(float.class)) {
            return Float.class;
        }

        if (clazz.equals(boolean.class)) {
            return Boolean.class;
        }

        if (clazz.equals(char.class)) {
            return Character.class;
        }

        return null;
    }
}

