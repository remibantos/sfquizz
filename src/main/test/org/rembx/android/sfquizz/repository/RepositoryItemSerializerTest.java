/*
package org.rembx.android.sfquizz.repository;

import android.content.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.api.easymock.PowerMock;
import org.rembx.android.sfquizz.model.GameState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;

*/
/**
 * @author rembx
 *//*

@RunWith(JUnit4.class)
public class RepositoryItemSerializerTest {

    Context context;
    RepositoryItemSerializer repositoryItemSerializer;
    private RepositoryItemsCache repositoryItemsCache;

    @Before
    public void setup(){
        context = PowerMock.createMock(Context.class);
        repositoryItemSerializer=PowerMock.createMock(RepositoryItemSerializer.class);
        repositoryItemsCache = new RepositoryItemsCache(context,repositoryItemSerializer);

    }

    @After
    public void tearDown(){
        verify(context,repositoryItemSerializer);
    }

    private void replayAll(){
        replay(context,repositoryItemSerializer);
    }

    @Test
    public void getItem() throws Exception {
        GameState gameState = new GameState(new ArrayList<Integer>());
        expect(repositoryItemSerializer.deSerialize(GameState.class)).andReturn(gameState);

        replayAll();

        assertNotNull(repositoryItemsCache.getItem(GameState.class));
    }

    @Test
    public void updateItem() throws Exception {
        GameState gameState = new GameState(new ArrayList<Integer>());
        repositoryItemSerializer.serialize(gameState);

        replayAll();

        repositoryItemsCache.updateItem(gameState);
        assertEquals(gameState,repositoryItemsCache.getItemsCache().get(GameState.class));
    }

    @Test
    public void removeItem() throws Exception{
        GameState gameState1 = new GameState(new ArrayList<Integer>());
        Set<Object> items = new HashSet<Object>();
        repositoryItemsCache.getItemsCache().put(GameState.class,items);
        repositoryItemSerializer.delete(gameState1);
        replayAll();
        repositoryItemsCache.removeItem(gameState1);
        assertNull(repositoryItemsCache.getItemsCache().get(GameState.class));
    }



}
*/
