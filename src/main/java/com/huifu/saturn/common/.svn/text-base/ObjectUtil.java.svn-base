/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.common;


import java.io.Serializable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectUtil {
    public static final Object NULL = new Serializable() {
            private static final long serialVersionUID = 7092611880189329093L;

            private Object readResolve() {
                return NULL;
            }
        };

    public static Object defaultIfNull(Object object, Object defaultValue) {
        return (object != null) ? object
                                : defaultValue;
    }

    public static boolean equals(Object object1, Object object2) {
        return ArrayUtil.equals(object1, object2);
    }

    public static int hashCode(Object object) {
        return ArrayUtil.hashCode(object);
    }

    public static int identityHashCode(Object object) {
        return (object == null) ? 0
                                : System.identityHashCode(object);
    }

    public static String identityToString(Object object) {
        if (object == null) {
            return null;
        }

        return appendIdentityToString(null, object).toString();
    }

    public static String identityToString(Object object, String nullStr) {
        if (object == null) {
            return nullStr;
        }

        return appendIdentityToString(null, object).toString();
    }

    public static StringBuffer appendIdentityToString(StringBuffer buffer, Object object) {
        if (object == null) {
            return null;
        }

        if (buffer == null) {
            buffer = new StringBuffer();
        }

        buffer.append(ClassUtil.getClassNameForObject(object));

        return buffer.append('@').append(Integer.toHexString(identityHashCode(object)));
    }

    public static Object clone(Object array) {
        if (array == null) {
            return null;
        }

        if (array instanceof Object[]) {
            return ArrayUtil.clone((Object[]) array);
        }

        if (array instanceof long[]) {
            return ArrayUtil.clone((long[]) array);
        }

        if (array instanceof int[]) {
            return ArrayUtil.clone((int[]) array);
        }

        if (array instanceof short[]) {
            return ArrayUtil.clone((short[]) array);
        }

        if (array instanceof byte[]) {
            return ArrayUtil.clone((byte[]) array);
        }

        if (array instanceof double[]) {
            return ArrayUtil.clone((double[]) array);
        }

        if (array instanceof float[]) {
            return ArrayUtil.clone((float[]) array);
        }

        if (array instanceof boolean[]) {
            return ArrayUtil.clone((boolean[]) array);
        }

        if (array instanceof char[]) {
            return ArrayUtil.clone((char[]) array);
        }

        // Not cloneable
        if (!(array instanceof Cloneable)) {
            throw new RuntimeException("Object of class " + array.getClass().getName()
                + " is not Cloneable");
        }

        Class clazz = array.getClass();

        try {
            Method cloneMethod = clazz.getMethod("clone", ArrayUtil.EMPTY_CLASS_ARRAY);

            return cloneMethod.invoke(array, ArrayUtil.EMPTY_OBJECT_ARRAY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
    }

    public static boolean isSameType(Object object1, Object object2) {
        if ((object1 == null) || (object2 == null)) {
            return true;
        }

        return object1.getClass().equals(object2.getClass());
    }

    public static String toString(Object object) {
        return (object == null) ? StringUtil.EMPTY_STRING
                                : (object.getClass().isArray() ? ArrayUtil.toString(object)
                                                               : object.toString());
    }

    public static String toString(Object object, String nullStr) {
        return (object == null) ? nullStr
                                : (object.getClass().isArray() ? ArrayUtil.toString(object)
                                                               : object.toString());
    }
}

