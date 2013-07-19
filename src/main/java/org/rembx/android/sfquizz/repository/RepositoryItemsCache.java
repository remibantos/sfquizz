package org.rembx.android.sfquizz.repository;

import android.content.Context;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author rembx
 *         Items cache management.
 *         Key=Item class
 *         Value=Item instance
 */
@ContextSingleton
public class RepositoryItemsCache {

    Context context;

    RepositoryItemSerializer repositoryItemSerializer;

    /**
     * Stores deserialized Item objects
     */
    private Map<Class<? extends Serializable>, Object> itemsCache;

    @Inject
    public RepositoryItemsCache(Context context, RepositoryItemSerializer repositoryItemSerializer) {
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

    }


    public <T extends Serializable> void persistItems(List<T> items) {
        for (T item : items) {
            try {
                itemsCache.put(item.getClass(), item);
                repositoryItemSerializer.serialize(item);
            } catch (IOException e) {
                throw new IllegalStateException("error during persistence of items to cache", e);
            }
        }
    }


    public void removeItem(Serializable item) {
        try {
            itemsCache.remove(item.getClass());
            repositoryItemSerializer.delete(item);
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
