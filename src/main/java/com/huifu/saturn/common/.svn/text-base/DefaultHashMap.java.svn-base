/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.common;


import java.io.IOException;
import java.io.Serializable;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class DefaultHashMap extends AbstractMap implements Map, Cloneable, Serializable {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    protected transient Entry[] table;

    protected transient int size;

    protected int threshold;

    protected final float loadFactor;

    protected transient volatile int modCount;

    private transient Set keySet = null;
    private transient Set entrySet = null;

    private transient Collection values = null;

    public DefaultHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public DefaultHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public DefaultHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }

        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        if ((loadFactor <= 0) || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        int capacity = 1;

        while (capacity < initialCapacity) {
            capacity <<= 1;
        }

        this.loadFactor = loadFactor;
        this.threshold  = (int) (capacity * loadFactor);
        this.table      = new Entry[capacity];

        onInit();
    }

    public DefaultHashMap(Map map) {
        this(Math.max((int) (map.size() / DEFAULT_LOAD_FACTOR) + 1, DEFAULT_INITIAL_CAPACITY),
             DEFAULT_LOAD_FACTOR);
        putAllForCreate(map);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Object get(Object key) {
        Entry entry = getEntry(key);

        return (entry == null) ? null
                               : entry.getValue();
    }

    public boolean containsKey(Object key) {
        Entry entry = getEntry(key);

        return entry != null;
    }

    public Object put(Object key, Object value) {
        Entry entry = getEntry(key);

        if (entry != null) {
            Object oldValue = entry.getValue();

            entry.setValue(value);
            entry.onAccess();

            return oldValue;
        } else {
            modCount++;

            if (size >= threshold) {
                resize(table.length * 2);
            }

            addEntry(key, value);

            return null;
        }
    }

    public void putAll(Map map) {
        int n = map.size();

        if (n == 0) {
            return;
        }

        if (n >= threshold) {
            n = (int) (n / loadFactor + 1);

            if (n > MAXIMUM_CAPACITY) {
                n = MAXIMUM_CAPACITY;
            }

            int capacity = table.length;

            while (capacity < n) {
                capacity <<= 1;
            }

            resize(capacity);
        }

        for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();

            put(entry.getKey(), entry.getValue());
        }
    }

    public Object remove(Object key) {
        Entry entry = removeEntryForKey(key);

        return ((entry == null) ? null
                                : entry.getValue());
    }

    public void clear() {
        modCount++;
        Arrays.fill(table, null);
        size = 0;
    }

    public boolean containsValue(Object value) {
        Entry[] tab = table;

        for (int i = 0; i < tab.length; i++) {
            for (Entry entry = tab[i]; entry != null; entry = entry.next) {
                if (eq(value, entry.getValue())) {
                    return true;
                }
            }
        }

        return false;
    }

    public Set keySet() {
        Set ks = keySet;

        return ((ks != null) ? ks
                             : (keySet = new KeySet()));
    }

    public Collection values() {
        Collection vs = values;

        return ((vs != null) ? vs
                             : (values = new Values()));
    }

    public Set entrySet() {
        Set es = entrySet;

        return ((es != null) ? es
                             : (entrySet = new EntrySet()));
    }

    protected static class Entry extends DefaultMapEntry {
        protected final int hash;

        protected Entry next;

        protected Entry(int h, Object k, Object v, Entry n) {
            super(k, v);
            next = n;
            hash = h;
        }

        protected void onAccess() {
        }

        protected void onRemove() {
        }
    }

    private abstract class HashIterator implements Iterator {
        private Entry current;

        private Entry next;

        private int expectedModCount;
        private int index;

        /**
         */
        protected HashIterator() {
            expectedModCount = modCount;

            Entry[] t = table;
            int     i = t.length;
            Entry   n = null;

            if (size != 0) { // advance to first entry

                while ((i > 0) && ((n = t[--i]) == null)) {
                    ;
                }
            }

            next  = n;
            index = i;
        }

        public boolean hasNext() {
            return next != null;
        }

        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }

            checkForComodification();

            Object k = current.getKey();

            current = null;
            DefaultHashMap.this.removeEntryForKey(k);
            expectedModCount = modCount;
        }

        protected Entry nextEntry() {
            checkForComodification();

            Entry entry = next;

            if (entry == null) {
                throw new NoSuchElementException();
            }

            Entry   n = entry.next;
            Entry[] t = table;
            int     i = index;

            while ((n == null) && (i > 0)) {
                n = t[--i];
            }

            index = i;
            next  = n;

            return current = entry;
        }

        /**
         */
        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /**
     */
    private class KeyIterator extends HashIterator {
        /**
         *
         */
        public Object next() {
            return nextEntry().getKey();
        }
    }

    /**
     */
    private class ValueIterator extends HashIterator {
        /**
         *
         */
        public Object next() {
            return nextEntry().getValue();
        }
    }

    /**
     */
    private class EntryIterator extends HashIterator {
        /**
         *
         */
        public Object next() {
            return nextEntry();
        }
    }

    /**
     */
    private class KeySet extends AbstractSet {
        /**
         *
         */
        public Iterator iterator() {
            return newKeyIterator();
        }

        /**
         *
         */
        public int size() {
            return size;
        }

        /**
         */
        public boolean contains(Object o) {
            return containsKey(o);
        }

        /**
         */
        public boolean remove(Object o) {
            return DefaultHashMap.this.removeEntryForKey(o) != null;
        }

        /**
         */
        public void clear() {
            DefaultHashMap.this.clear();
        }
    }

    /**
     */
    private class Values extends AbstractCollection {
        /**
         */
        public Iterator iterator() {
            return newValueIterator();
        }

        /**
         */
        public int size() {
            return size;
        }

        /**
         */
        public boolean contains(Object o) {
            return containsValue(o);
        }

        /**
         */
        public void clear() {
            DefaultHashMap.this.clear();
        }
    }

    /**
     */
    private class EntrySet extends AbstractSet {
        /**
         */
        public Iterator iterator() {
            return newEntryIterator();
        }

        /**
         */
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }

            Map.Entry entry     = (Map.Entry) o;
            Entry     candidate = getEntry(entry.getKey());

            return eq(candidate, entry);
        }

        /**
         */
        public boolean remove(Object o) {
            return removeEntry(o) != null;
        }

        /**
         */
        public int size() {
            return size;
        }

        /**
         */
        public void clear() {
            DefaultHashMap.this.clear();
        }
    }

    private static final long serialVersionUID = 362498820763181265L;

    /**
     */
    private void readObject(java.io.ObjectInputStream is) throws IOException,
                                                                 ClassNotFoundException {
        is.defaultReadObject();

        int numBuckets = is.readInt();

        table = new Entry[numBuckets];


        onInit();

        int size = is.readInt();

        for (int i = 0; i < size; i++) {
            Object key   = is.readObject();
            Object value = is.readObject();

            putForCreate(key, value);
        }
    }

    /**
     */
    private void writeObject(java.io.ObjectOutputStream os) throws IOException {
        os.defaultWriteObject();


        os.writeInt(table.length);


        os.writeInt(size);

        for (Iterator i = entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();

            os.writeObject(entry.getKey());
            os.writeObject(entry.getValue());
        }
    }

    public Object clone() {
        DefaultHashMap result = null;

        try {
            result = (DefaultHashMap) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }

        result.table    = new Entry[table.length];
        result.entrySet = null;
        result.modCount = 0;
        result.size     = 0;
        result.onInit();
        result.putAllForCreate(this);

        return result;
    }

    protected void onInit() {
    }

    protected Entry getEntry(Object key) {
        int hash = hash(key);
        int i = indexFor(hash, table.length);

        for (Entry entry = table[i]; entry != null; entry = entry.next) {
            if ((entry.hash == hash) && eq(key, entry.getKey())) {
                return entry;
            }
        }

        return null;
    }

    protected void addEntry(Object key, Object value) {
        int hash = hash(key);
        int i = indexFor(hash, table.length);

        table[i] = new Entry(hash, key, value, table[i]);
        size++;
    }

    private void putForCreate(Object key, Object value) {
        Entry entry = getEntry(key);

        if (entry != null) {
            entry.setValue(value);
        } else {
            addEntry(key, value);
        }
    }

    private void putAllForCreate(Map map) {
        for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();

            putForCreate(entry.getKey(), entry.getValue());
        }
    }

    protected Entry removeEntryForKey(Object key) {
        int   hash  = hash(key);
        int   i     = indexFor(hash, table.length);
        Entry prev  = table[i];
        Entry entry = prev;

        while (entry != null) {
            Entry next = entry.next;

            if ((entry.hash == hash) && eq(key, entry.getKey())) {
                modCount++;
                size--;

                if (prev == entry) {
                    table[i] = next;
                } else {
                    prev.next = next;
                }

                entry.onRemove();

                return entry;
            }

            prev  = entry;
            entry = next;
        }

        return entry;
    }

    protected Entry removeEntry(Object o) {
        if (!(o instanceof Map.Entry)) {
            return null;
        }

        Map.Entry entry = (Map.Entry) o;
        Object    key  = entry.getKey();
        int       hash = hash(key);
        int       i    = indexFor(hash, table.length);
        Entry     prev = table[i];
        Entry     e    = prev;

        while (e != null) {
            Entry next = e.next;

            if ((e.hash == hash) && e.equals(entry)) {
                modCount++;
                size--;

                if (prev == e) {
                    table[i] = next;
                } else {
                    prev.next = next;
                }

                e.onRemove();

                return e;
            }

            prev = e;
            e    = next;
        }

        return e;
    }

    protected Iterator newKeyIterator() {
        return new KeyIterator();
    }

    protected Iterator newValueIterator() {
        return new ValueIterator();
    }

    protected Iterator newEntryIterator() {
        return new EntryIterator();
    }

    protected static int hash(Object obj) {
        int h = (obj == null) ? 0
                              : obj.hashCode();

        return h - (h << 7); // Ҳ����, -127 * h
    }

    protected static boolean eq(Object x, Object y) {
        return (x == null) ? (y == null)
                           : ((x == y) || x.equals(y));
    }

    protected static int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    protected void resize(int newCapacity) {
        Entry[] oldTable    = table;
        int     oldCapacity = oldTable.length;

        if ((size < threshold) || (oldCapacity > newCapacity)) {
            return;
        }

        Entry[] newTable = new Entry[newCapacity];

        transfer(newTable);
        table     = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    protected void transfer(Entry[] newTable) {
        Entry[] src         = table;
        int     newCapacity = newTable.length;

        for (int j = 0; j < src.length; j++) {
            Entry entry = src[j];

            if (entry != null) {
                src[j] = null;

                do {
                    Entry next = entry.next;
                    int   i = indexFor(entry.hash, newCapacity);

                    entry.next  = newTable[i];
                    newTable[i] = entry;
                    entry       = next;
                } while (entry != null);
            }
        }
    }

    /**
     *
     */
    protected int getCapacity() {
        return table.length;
    }

    /**
     */
    protected float getLoadFactor() {
        return loadFactor;
    }

    protected int getThreshold() {
        return threshold;
    }
}
