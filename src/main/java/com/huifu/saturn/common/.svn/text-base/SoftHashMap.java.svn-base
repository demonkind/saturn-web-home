/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.common;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class SoftHashMap extends AbstractMap {
    private static class ValueCell extends SoftReference {
        private static Object INVALID_KEY = new Object();
        private static int    dropped = 0;
        private Object        key;

        private ValueCell(Object key, Object value, ReferenceQueue queue) {
            super(value, queue);
            this.key = key;
        }

        private static ValueCell create(Object key, Object value, ReferenceQueue queue) {
            if (value == null) {
                return null;
            }

            return new ValueCell(key, value, queue);
        }

        private static Object strip(Object val, boolean drop) {
            if (val == null) {
                return null;
            }

            ValueCell vc = (ValueCell) val;
            Object    o = vc.get();

            if (drop) {
                vc.drop();
            }

            return o;
        }

        private boolean isValid() {
            return (key != INVALID_KEY);
        }

        private void drop() {
            super.clear();
            key = INVALID_KEY;
            dropped++;
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj == this) {
                return true;
            }

            return valEquals(this.get(), ((ValueCell) obj).get());
        }

        public int hashCode() {
            Object o = this.get();

            return (o == null) ? 0
                               : o.hashCode();
        }
    }

    /* Hash table mapping keys to ValueCells */
    private Map hash;

    /* Reference queue for cleared ValueCells */
    private ReferenceQueue queue = new ReferenceQueue();

    /* Process any ValueCells that have been cleared and enqueued by the
       garbage collector.  This method should be invoked once by each public
       mutator in this class.  We don't invoke this method in public accessors
       because that can lead to surprising ConcurrentModificationExceptions.
     */
    private void processQueue() {
        ValueCell vc;

        while ((vc = (ValueCell) queue.poll()) != null) {
            if (vc.isValid()) {
                hash.remove(vc.key);
            } else {
                ValueCell.dropped--;
            }
        }
    }

    /* -- Constructors -- */

    /**
     * Construct a new, empty <code>SoftCache</code> with the given initial capacity and the given
     * load factor.
     *
     * @param initialCapacity  The initial capacity of the cache
     * @param loadFactor       A number between 0.0 and 1.0
     *
     * @throws IllegalArgumentException  If the initial capacity is less than or equal to zero, or
     *         if the load factor is less than zero
     */
    public SoftHashMap(int initialCapacity, float loadFactor) {
        hash = new HashMap(initialCapacity, loadFactor);
    }

    /**
     * Construct a new, empty <code>SoftCache</code> with the given initial capacity and the
     * default load factor.
     *
     * @param initialCapacity  The initial capacity of the cache
     *
     * @throws IllegalArgumentException  If the initial capacity is less than or equal to zero
     */
    public SoftHashMap(int initialCapacity) {
        hash = new HashMap(initialCapacity);
    }

    /**
     * Construct a new, empty <code>SoftCache</code> with the default capacity and the default load
     * factor.
     */
    public SoftHashMap() {
        hash = new HashMap();
    }

    /* -- Simple queries -- */

    /**
     * Return the number of key-value mappings in this cache.  The time required by this operation
     * is linear in the size of the map.
     */
    public int size() {
        return entrySet().size();
    }

    /**
     * Return <code>true</code> if this cache contains no key-value mappings.
     */
    public boolean isEmpty() {
        return entrySet().isEmpty();
    }

    /**
     * Return <code>true</code> if this cache contains a mapping for the specified key.  If there
     * is no mapping for the key, this method will not attempt to construct one by invoking the
     * <code>fill</code> method.
     *
     * @param key   The key whose presence in the cache is to be tested
     */
    public boolean containsKey(Object key) {
        Object value = hash.get(key);

        if (value == null) {
            return hash.containsKey(key);
        } else {
            return ValueCell.strip(value, false) != null;
        }
    }

    /* -- Lookup and modification operations -- */

    /**
     * Create a value object for the given <code>key</code>.  This method is invoked by the
     * <code>get</code> method when there is no entry for <code>key</code>.  If this method
     * returns a non-<code>null</code> value, then the cache will be updated to map
     * <code>key</code> to that value, and that value will be returned by the <code>get</code>
     * method.
     *
     * <p>
     * The default implementation of this method simply returns <code>null</code> for every
     * <code>key</code> value.  A subclass may override this method to provide more useful
     * behavior.
     * </p>
     *
     * @param key  The key for which a value is to be computed
     *
     * @return A value for <code>key</code>, or <code>null</code> if one could not be computed
     */
    protected Object fill(Object key) {
        return null;
    }

    /**
     * Return the value to which this cache maps the specified <code>key</code>.  If the cache does
     * not presently contain a value for this key, then invoke the <code>fill</code> method in an
     * attempt to compute such a value.  If that method returns a non-<code>null</code> value,
     * then update the cache and return the new value.  Otherwise, return <code>null</code>.
     *
     * <p>
     * Note that because this method may update the cache, it is considered a mutator and may cause
     * <code>ConcurrentModificationException</code>s to be thrown if invoked while an iterator is
     * in use.
     * </p>
     *
     * @param key  The key whose associated value, if any, is to be returned
     */
    public Object get(Object key) {
        processQueue();

        Object v = hash.get(key);

        if (v == null) {
            v = fill(key);

            if (v != null) {
                hash.put(key, ValueCell.create(key, v, queue));
                return v;
            }
        }

        return ValueCell.strip(v, false);
    }

    /**
     * Update this cache so that the given <code>key</code> maps to the given <code>value</code>.
     * If the cache previously contained a mapping for <code>key</code> then that mapping is
     * replaced and the old value is returned.
     *
     * @param key    The key that is to be mapped to the given <code>value</code>
     * @param value  The value to which the given <code>key</code> is to be mapped
     *
     * @return The previous value to which this key was mapped, or <code>null</code> if if there
     *         was no mapping for the key
     */
    public Object put(Object key, Object value) {
        processQueue();

        ValueCell vc = ValueCell.create(key, value, queue);

        return ValueCell.strip(hash.put(key, vc), true);
    }

    /**
     * Remove the mapping for the given <code>key</code> from this cache, if present.
     *
     * @param key  The key whose mapping is to be removed
     *
     * @return The value to which this key was mapped, or <code>null</code> if there was no mapping
     *         for the key
     */
    public Object remove(Object key) {
        processQueue();
        return ValueCell.strip(hash.remove(key), true);
    }

    /**
     * Remove all mappings from this cache.
     */
    public void clear() {
        processQueue();
        hash.clear();
    }

    /* -- Views -- */
    private static boolean valEquals(Object o1, Object o2) {
        return (o1 == null) ? (o2 == null)
                            : o1.equals(o2);
    }

    /* Internal class for entries.
       Because it uses SoftCache.this.queue, this class cannot be static.
     */
    private class Entry implements Map.Entry {
        private Map.Entry ent;
        private Object    value;/* Strong reference to value, to prevent the GC
           from flushing the value while this Entry
           exists */

        Entry(Map.Entry ent, Object value) {
            this.ent   = ent;
            this.value = value;
        }

        public Object getKey() {
            return ent.getKey();
        }

        public Object getValue() {
            return value;
        }

        public Object setValue(Object value) {
            return ent.setValue(ValueCell.create(ent.getKey(), value, queue));
        }

        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }

            Map.Entry e = (Map.Entry) o;

            return (valEquals(ent.getKey(), e.getKey()) && valEquals(value, e.getValue()));
        }

        public int hashCode() {
            Object k;

            return ((((k = getKey()) == null) ? 0
                                              : k.hashCode())
                   ^ ((value == null) ? 0
                                      : value.hashCode()));
        }
    }

    /* Internal class for entry sets */
    private class EntrySet extends AbstractSet {
        Set hashEntries = hash.entrySet();

        public Iterator iterator() {
            return new Iterator() {
                Iterator hashIterator = hashEntries.iterator();
                Entry    next = null;

                public boolean hasNext() {
                    while (hashIterator.hasNext()) {
                        Map.Entry ent = (Map.Entry) hashIterator.next();
                        ValueCell vc = (ValueCell) ent.getValue();
                        Object    v  = null;

                        if ((vc != null) && ((v = vc.get()) == null)) {
                            /* Value has been flushed by GC */
                            continue;
                        }

                        next = new Entry(ent, v);
                        return true;
                    }

                    return false;
                }

                public Object next() {
                    if ((next == null) && !hasNext()) {
                        throw new NoSuchElementException();
                    }

                    Entry e = next;

                    next = null;
                    return e;
                }

                public void remove() {
                    hashIterator.remove();
                }
            };
        }

        public boolean isEmpty() {
            return !(iterator().hasNext());
        }

        public int size() {
            int j = 0;

            for (Iterator i = iterator(); i.hasNext(); i.next()) {
                j++;
            }

            return j;
        }

        public boolean remove(Object o) {
            processQueue();

            if (o instanceof Entry) {
                return hashEntries.remove(((Entry) o).ent);
            } else if (o instanceof Map.Entry) {
                Map.Entry e = (Map.Entry) o;

                return hashEntries.remove(
                               new DefaultMapEntry(e.getKey(),
                                                   ValueCell.create(e.getKey(), e.getValue(), queue)));
            } else {
                return false;
            }
        }
    }

    private Set entrySet = null;

    /**
     * Return a <code>Set</code> view of the mappings in this cache.
     */
    public Set entrySet() {
        if (entrySet == null) {
            entrySet = new EntrySet();
        }

        return entrySet;
    }
}
