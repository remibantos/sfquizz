package org.rembx.android.sfquizz.persistence;

import android.content.Context;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Persistence Manager
 * use a cache to optimize ItemSerializer usage.
 */
@ContextSingleton
public class PersistenceManager {

    Context context;

    ItemSerializer repositoryItemSerializer;

    /**
     * Stores deserialized Item objects
     */
    private Map<Class<? extends Serializable>, Object> itemsCache;

    @Inject
    public PersistenceManager(Context context, ItemSerializer repositoryItemSerializer) {
        this.context = context;
        this.repositoryItemSerializer = repositoryItemSerializer;
        itemsCache = new HashMap<>();
    }

    public <T extends Serializable> T getItem(Class<T> itemClass) {
        T item = null;
        try {
            if (itemsCache.get(itemClass) == null) {
                item = itemClass.cast(repositoryItemSerializer.deSerialize(itemClass));
                itemsCache.put(itemClass, item);
            }
            if (itemsCache.get(itemClass) != null)
                item = itemClass.cast(itemsCache.get(itemClass));

        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("error while getting item of type: " + itemClass, e);
        }
        return item;
    }

    /**
     * Save items cache
     */
    public void updateItem(Serializable item) {
        itemsCache.put(item.getClass(), item);
        try{
            repositoryItemSerializer.serialize(item);
        }catch (IOException e) {
            throw new IllegalStateException("error during persistence of item "+item+" to cache", e);
        }

    }


    public void removeItem(Class<? extends  Serializable> itemType) {
        try {
            repositoryItemSerializer.delete(itemsCache.get(itemType));
            itemsCache.remove(itemType);
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("error during cached item removal", e);
        }


    }

    public Map<Class<? extends Serializable>, Object> getCache() {
        if (itemsCache == null)
            return new HashMap<>();
        return itemsCache;
    }
}
