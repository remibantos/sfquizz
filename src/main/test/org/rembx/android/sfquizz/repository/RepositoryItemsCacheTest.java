package org.rembx.android.sfquizz.repository;

import android.content.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


public class RepositoryItemsCacheTest {

    Context context;

    private RepositoryItemsCache instance;

    private  RepositoryItemSerializer repositoryItemSerializerMock;

    @Before
    public void setup(){

        repositoryItemSerializerMock = mock(RepositoryItemSerializer.class);
        instance = new RepositoryItemsCache(context, repositoryItemSerializerMock);

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
    public void testUpdateItem() throws Exception {

    }

    @Test
    public void persistItems_oneItem_shouldCacheAndSerializeOnce() throws Exception {
        Serializable serializable = new Serializable() {
        };
        List<Serializable> items = Arrays.asList(serializable);
        instance.persistItems(items);

        assert(instance.getCache().size()==1);

        verify(repositoryItemSerializerMock,times(1)).serialize(serializable);

    }

    @Test
    public void persistItems_twoItemsSameClass_shouldTriggerSerializeTwice_CacheOnce() throws Exception {
        Serializable serializable = new Serializable() {
        };
        List<Serializable> items = Arrays.asList(serializable,serializable);
        instance.persistItems(items);

        assert(instance.getCache().size()==1);

        verify(repositoryItemSerializerMock,times(2)).serialize(serializable);
    }

    @Test
    public void persistItems_twoDifferentItemsSameClass_shouldCacheAndSerializeTwice() throws Exception {
        Serializable serializable1 = new Serializable() {
        };
        Serializable serializable2 = new Serializable() {
        };
        List<Serializable> items = Arrays.asList(serializable1,serializable2);
        instance.persistItems(items);

        assert(instance.getCache().size()==2);

        verify(repositoryItemSerializerMock,times(1)).serialize(serializable1);
        verify(repositoryItemSerializerMock,times(1)).serialize(serializable2);
    }

    @Test
    public void removeItem_shouldTriggerDeleteAndRemoveFromCache() throws Exception {
        Map<Class<? extends  Serializable>, Object> cache = instance.getCache();
        assertNotNull(cache);
        Serializable serializable = new Serializable() {
        };
        cache.put(serializable.getClass(), serializable);
        instance.removeItem(serializable);
        verify(repositoryItemSerializerMock).delete(serializable);
        assertEquals(0, cache.size());
    }

    @Test
    public void getCache_shouldReturnEmptyMap() throws Exception {
        assertNotNull(instance.getCache());
        assertEquals(0,instance.getCache().size());
    }
}
