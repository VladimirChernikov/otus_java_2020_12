package ru.otus.hw11.cachehw;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы
 
    private final WeakHashMap<K, V> cache;
    private final List<WeakReference< HwListener<K, V> >> listeners;
    private final ReferenceQueue<HwListener<K, V>> finalizingListenersQueue;

    public MyCache() {
        this.cache = new WeakHashMap<>();
        this.listeners = new ArrayList<>();
        this.finalizingListenersQueue = new ReferenceQueue<>();
    }

    @Override
    public void put(K key, V value) {
        this.notifyAllListeners( key, value, "PUT" );
        this.cache.put( key, value );
    }

    @Override
    public void remove(K key) {
        this.notifyAllListeners( key, this.cache.get(key), "REMOVE" );
        this.cache.remove(key);
    }

    @Override
    public V get(K key) {
        V value = this.cache.get( key );
        this.notifyAllListeners( key, value, "GET" );
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        this.listeners.add( new WeakReference<>(listener, this.finalizingListenersQueue) );
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        this.listeners.removeIf( l -> l.get().equals(listener) );
    }

    private void notifyAllListeners( K key, V value, String action ) {
        // clean up finalizing references
        while ( this.listeners.remove( this.finalizingListenersQueue.poll() ) ){};

        // notify all listeners
        for ( var listener : this.listeners )  {
            listener.get().notify( key, value, action );
        }
    }

}
