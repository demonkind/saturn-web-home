/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.common;


import java.util.AbstractList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;


/**
 * @author Michael Zhou
 * @version $Id: ArrayHashMap.java 1291 2005-03-04 03:23:30Z baobao $
 *
 * @see DefaultHashMap
 * @see ListMap
 */
public class ArrayHashMap extends DefaultHashMap
        implements ListMap {
    private static final long serialVersionUID = 3258411729271927857L;

    /* ============================================================================ */
    /* ============================================================================ */

    protected transient Entry[] order;

    private transient List keyList;

    private transient List valueList;

    private transient List entryList;

    /* ============================================================================ */
    /* ============================================================================ */

    /**
     */
    public ArrayHashMap() {
        super();
    }

    /**
     *
     */
    public ArrayHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     *
     */
    public ArrayHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     *
     */
    public ArrayHashMap(Map map) {
        super(map);
    }

    /* ============================================================================ */
    /* ============================================================================ */

    /**
     */
    public boolean containsValue(Object value) {
        for (int i = 0; i < size; i++) {
            Entry entry = order[i];

            if (eq(value, entry.getValue())) {
                return true;
            }
        }

        return false;
    }

    /**
     */
    public void clear() {
        super.clear();
        Arrays.fill(order, null);
    }

    /**
     */
    public Object get(int index) {
        checkRange(index);
        return order[index].getValue();
    }

    /**
     */
    public Object getKey(int index) {
        checkRange(index);
        return order[index].getKey();
    }

    /**
     */
    public Map.Entry remove(int index) {
        checkRange(index);
        return removeEntryForKey(order[index].getKey());
    }

    /**
     */
    public List keyList() {
        return ((keyList != null) ? keyList
                                  : (keyList = new KeyList()));
    }

    /**
     */
    public List valueList() {
        return ((valueList != null) ? valueList
                                    : (valueList = new ValueList()));
    }

    /**
     */
    public List entryList() {
        return ((entryList != null) ? entryList
                                    : (entryList = new EntryList()));
    }

    /* ============================================================================ */
    /* ============================================================================ */

    /**
     */
    protected class Entry extends DefaultHashMap.Entry {
        protected int index;

        /**
         *
         */
        protected Entry(int h, Object k, Object v, DefaultHashMap.Entry n) {
            super(h, k, v, n);
        }

        /**
         */
        protected void onRemove() {
            int numMoved = size - index;

            if (numMoved > 0) {
                System.arraycopy(order, index + 1, order, index, numMoved);
            }

            order[size] = null;

            for (int i = index; i < size; i++) {
                order[i].index--;
            }
        }
    }

    /**
     */
    private abstract class ArrayHashIterator
            implements ListIterator {
        private Entry lastReturned;

        private int cursor;

        private int expectedModCount;

        /**
         *
         */
        protected ArrayHashIterator(int index) {
            if ((index < 0) || (index > size())) {
                throw new IndexOutOfBoundsException("Index: " + index);
            }

            cursor           = index;
            expectedModCount = modCount;
        }

        /**
         *
         */
        public void add(Object o) {
            throw new UnsupportedOperationException();
        }

        /**
         *
         */
        public void set(Object o) {
            throw new UnsupportedOperationException();
        }

        /**
         *
         */
        public boolean hasNext() {
            return cursor < size;
        }

        /**
         *
         */
        public boolean hasPrevious() {
            return cursor > 0;
        }

        /**
         *
         */
        public int nextIndex() {
            return cursor;
        }

        /**
         *
         */
        public int previousIndex() {
            return cursor - 1;
        }

        /**
         */
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }

            checkForComodification();

            removeEntryForKey(lastReturned.getKey());

            if (lastReturned.index < cursor) {
                cursor--;
            }

            lastReturned     = null;
            expectedModCount = modCount;
        }

        /**
         *
         */
        protected Entry nextEntry() {
            checkForComodification();

            if (cursor >= size) {
                throw new NoSuchElementException();
            }

            lastReturned = order[cursor++];

            return lastReturned;
        }

        /**
         *
         */
        protected Entry previousEntry() {
            checkForComodification();

            if (cursor <= 0) {
                throw new NoSuchElementException();
            }

            lastReturned = order[--cursor];

            return lastReturned;
        }

        /**
         *
         */
        protected void setValue(Object o) {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }

            checkForComodification();

            lastReturned.setValue(o);
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
    private class KeyIterator extends ArrayHashIterator {
        /**
         *
         */
        protected KeyIterator(int index) {
            super(index);
        }

        /**
         *
         */
        public Object next() {
            return nextEntry().getKey();
        }

        /**
         *
         */
        public Object previous() {
            return previousEntry().getKey();
        }
    }

    /**
     */
    private class ValueIterator extends ArrayHashIterator {
        /**
         *
         */
        protected ValueIterator(int index) {
            super(index);
        }

        /**
         *
         */
        public void set(Object o) {
            setValue(o);
        }

        /**
         *
         */
        public Object next() {
            return nextEntry().getValue();
        }

        /**
         *
         */
        public Object previous() {
            return previousEntry().getValue();
        }
    }

    /**
     */
    private class EntryIterator extends ArrayHashIterator {
        /**
         *
         */
        protected EntryIterator(int index) {
            super(index);
        }

        /**
         *
         */
        public Object next() {
            return nextEntry();
        }

        /**
         *
         */
        public Object previous() {
            return previousEntry();
        }
    }

    /**
     */
    private abstract class ArrayHashList extends AbstractList {
        /**
         *
         */
        public int size() {
            return size;
        }

        /**
         *
         */
        public boolean isEmpty() {
            return size == 0;
        }

        /**
         */
        public void clear() {
            ArrayHashMap.this.clear();
        }

        /**
         *
         *
         */
        public Object remove(int index) {
            checkRange(index);
            return removeEntryForKey(order[index].getKey());
        }

        /**
         *
         *
         */
        public int lastIndexOf(Object o) {
            return indexOf(o);
        }
    }

    /**
     */
    private class EntryList extends ArrayHashList {
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }

            Map.Entry entry     = (Map.Entry) o;
            Entry     candidate = (ArrayHashMap.Entry) getEntry(entry.getKey());

            return eq(candidate, entry);
        }

        /**
         */
        public Iterator iterator() {
            return newEntryIterator();
        }

        /**
         */
        public boolean remove(Object o) {
            return removeEntry(o) != null;
        }

        /**
         */
        public Object get(int index) {
            checkRange(index);
            return order[index];
        }

        /**
         */
        public int indexOf(Object o) {
            if ((o != null) && o instanceof Map.Entry) {
                Entry entry = (Entry) getEntry(((Map.Entry) o).getKey());

                if ((entry != null) && entry.equals(o)) {
                    return entry.index;
                }
            }

            return -1;
        }

        /**
         *
         * @return list iterator
         */
        public ListIterator listIterator(int index) {
            return new EntryIterator(index);
        }
    }

    /**
     */
    private class KeyList extends ArrayHashList {
        /**
         */
        public boolean contains(Object o) {
            return ArrayHashMap.this.containsKey(o);
        }

        /**
         */
        public Iterator iterator() {
            return newKeyIterator();
        }

        /**
         */
        public boolean remove(Object o) {
            Entry entry = (Entry) getEntry(o);

            if (entry == null) {
                return false;
            } else {
                removeEntry(entry);
                return true;
            }
        }

        /**
         */
        public Object get(int index) {
            checkRange(index);
            return order[index].getKey();
        }

        /**
         */
        public int indexOf(Object o) {
            Entry entry = (Entry) getEntry(o);

            if (entry != null) {
                return entry.index;
            }

            return -1;
        }

        /**
         *
         * @return list iterator
         */
        public ListIterator listIterator(int index) {
            return new KeyIterator(index);
        }
    }

    /**
     */
    private class ValueList extends ArrayHashList {
        /**
         */
        public boolean contains(Object o) {
            return ArrayHashMap.this.containsValue(o);
        }

        /**
         */
        public Iterator iterator() {
            return newValueIterator();
        }

        /**
         */
        public boolean remove(Object o) {
            int index = indexOf(o);

            if (index != -1) {
                ArrayHashMap.this.remove(index);
                return true;
            }

            return false;
        }

        /**
         */
        public Object get(int index) {
            checkRange(index);
            return order[index].getValue();
        }

        /**
         */
        public int indexOf(Object o) {
            for (int i = 0; i < size; i++) {
                if (eq(o, order[i].getValue())) {
                    return i;
                }
            }

            return -1;
        }

        /**
         *
         * @return list iterator
         */
        public ListIterator listIterator(int index) {
            return new ValueIterator(index);
        }
    }

    /* ============================================================================ */
    /* ============================================================================ */

    /**
     */
    protected void onInit() {
        order = new Entry[threshold];
    }

    /**
     *
     */
    protected void addEntry(Object key, Object value) {
        int   hash  = hash(key);
        int   i     = indexFor(hash, table.length);
        Entry entry = new Entry(hash, key, value, table[i]);

        table[i]      = entry;
        entry.index   = size;
        order[size++] = entry;
    }

    /**
     *
     */
    protected Iterator newKeyIterator() {
        return new KeyIterator(0);
    }

    /**
     *
     */
    protected Iterator newValueIterator() {
        return new ValueIterator(0);
    }

    /**
     *
     */
    protected Iterator newEntryIterator() {
        return new EntryIterator(0);
    }

    /**
     *
     */
    protected void resize(int newCapacity) {
        super.resize(newCapacity);

        if (threshold > order.length) {
            Entry[] newOrder = new Entry[threshold];

            System.arraycopy(order, 0, newOrder, 0, order.length);

            order = newOrder;
        }
    }

    /**
     *
     */
    protected void transfer(DefaultHashMap.Entry[] newTable) {
        int newCapacity = newTable.length;

        for (int i = 0; i < size; i++) {
            Entry entry = order[i];
            int   index = indexFor(entry.hash, newCapacity);

            entry.next      = newTable[index];
            newTable[index] = entry;
        }
    }

    /**
     *
     */
    private void checkRange(int index) {
        if ((index >= size) || (index < 0)) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}

