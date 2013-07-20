package org.rembx.android.sfquizz.persistence;

import android.content.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


public class PersistenceManagerTest {

    Context context;

    private PersistenceManager instance;

    private ItemSerializer repositoryItemSerializerMock;

    @Before
    public void setup(){

        repositoryItemSerializerMock = mock(ItemSerializer.class);
        instance = new PersistenceManager(context, repositoryItemSerializerMock);

    }

    @After
    public void clean(){
        if (instance.getCache()!=null)
            instance.getCache().clear();
    }

    @Test
    public void getItem_NotCachedItem_shouldDeSerializeAndCache() throws Exception {

        Serializable serializable = new Serializable() {
        };
        when(repositoryItemSerializerMock.deSerialize(serializable.getClass())).thenReturn(serializable);

        instance.getItem(serializable.getClass());

        verify(repositoryItemSerializerMock, times(1)).deSerialize(serializable.getClass());
        assertEquals(1, instance.getCache().size());
        assertNotNull(instance.getCache().get(serializable.getClass()));
    }

    @Test
    public void updateItem_shouldCacheAndSerialize() throws Exception {
        Serializable serializable = new Serializable() {
        };

        instance.updateItem(serializable);

        assertEquals(1, instance.getCache().size());
        assertEquals(serializable,instance.getCache().get(serializable.getClass()));

        verify(repositoryItemSerializerMock,times(1)).serialize(serializable);

    }


    @Test
    public void removeItem_shouldRemoveFromCacheAndDelete() throws Exception {
        Map<Class<? extends  Serializable>, Object> cache = instance.getCache();
        assertNotNull(cache);
        Serializable serializable = new Serializable() {
        };
        cache.put(serializable.getClass(), serializable);
        instance.removeItem(serializable.getClass());
        verify(repositoryItemSerializerMock).delete(serializable);
        assertEquals(0, cache.size());
    }

    @Test
    public void getCache_shouldReturnEmptyMap() throws Exception {
        assertNotNull(instance.getCache());
        assertEquals(0,instance.getCache().size());
    }
}
