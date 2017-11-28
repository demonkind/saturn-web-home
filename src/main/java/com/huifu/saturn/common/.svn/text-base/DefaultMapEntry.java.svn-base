/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.common;


import java.util.Map;

public class DefaultMapEntry implements Map.Entry {
    private final Object key;
    private Object       value;
    public DefaultMapEntry(Object key, Object value) {
        this.key   = key;
        this.value = value;
    }

    public Object getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public Object setValue(Object value) {
        Object oldValue = this.value;

        this.value = value;

        return oldValue;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof Map.Entry)) {
            return false;
        }

        Map.Entry e  = (Map.Entry) o;
        Object    k1 = getKey();
        Object    k2 = e.getKey();

        if ((k1 == k2) || ((k1 != null) && k1.equals(k2))) {
            Object v1 = getValue();
            Object v2 = e.getValue();

            if ((v1 == v2) || ((v1 != null) && v1.equals(v2))) {
                return true;
            }
        }

        return false;
    }

    public int hashCode() {
        return ((key == null) ? 0
                              : key.hashCode())
               ^ ((value == null) ? 0
                                  : value.hashCode());
    }

    public String toString() {
        return getKey() + "=" + getValue();
    }
}
