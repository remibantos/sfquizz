package org.rembx.android.sfquizz.repository;

import android.content.Context;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * @author rembx
 * Items cache management.
 * Key=Item class
 * Value=Item instance
 */
@ContextSingleton
public class RepositoryItemsCache {

    Context context;

    RepositoryItemSerializer repositoryItemSerializer;

    /**
     * Stores deserialized Item objects
     */
    private Map<Class, Object> itemsCache;

    @Inject
    public RepositoryItemsCache(Context context, RepositoryItemSerializer repositoryItemSerializer) {
        this.context = context;
        this.repositoryItemSerializer = repositoryItemSerializer;
        itemsCache = new HashMap<Class, Object>();
    }

    public <T> T getItem(Class<T> itemClass) throws ItemException {
        T item = null;
        try {
            if (itemsCache.get(itemClass) == null) {
                item = itemClass.cast(repositoryItemSerializer.deSerialize(itemClass));
                itemsCache.put(itemClass, item);
            }
            if (itemsCache.get(itemClass) != null)
                item = itemClass.cast(itemsCache.get(itemClass));

        } catch (IOException e) {
            throw new ItemException(e);
        } catch (ClassNotFoundException e) {
            throw new ItemException(e);
        }
        return item;
    }

    /**
     * Save items cache
     */
    public void updateItem(Object item) throws ItemException {
        itemsCache.put(item.getClass(), item);

    }

    public <T extends Serializable> void persistItem(T item) throws ItemException {
        try {
            repositoryItemSerializer.serialize(item);
        } catch (IOException e) {
            throw new ItemException(e);
        }
    }

    public <T extends Serializable> void persistItems(T... items) throws ItemException {
        for (T item : items) {
            if (item != null)
                persistItem(item);
        }
    }


    public void removeItem(Object item) throws ItemException {
        itemsCache.remove(item.getClass());
        try {
            repositoryItemSerializer.delete(item);
        } catch (Exception e) {
            throw new ItemException(e);
        }

    }

    public Map<Class, Object> getItemsCache() {
        return itemsCache;
    }
}
