package org.rembx.android.sfquizz.repository;

import android.content.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.api.easymock.PowerMock;
import org.rembx.android.sfquizz.model.GameState;

import java.io.*;
import java.util.ArrayList;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;

/**
 * @author rembx
 */
@RunWith(JUnit4.class)
public class RepositoryItemCacheTest {

    Context context;

    private RepositoryItemSerializer repositoryItemSerializer;

    @Before
    public void setup(){
        context = PowerMock.createMock(Context.class);
        repositoryItemSerializer = new RepositoryItemSerializer(context);

    }

    @After
    public void tearDown(){
        verify(context);
    }

    @Test
    public void testSerialize() throws Exception {
        Serializable entity = new GameState(new ArrayList<Integer>());

        FileOutputStream fout = new FileOutputStream("./GameState.ser");

        expect(context.openFileOutput("GameState.ser", Context.MODE_PRIVATE)).andReturn(fout);

        replay(context);

        repositoryItemSerializer.serialize(entity);
        assertTrue(new File("GameState.ser").exists());
    }

    @Test
    public void testDeSerialize() throws Exception {

        Serializable expected = new GameState(new ArrayList<Integer>());

        FileOutputStream fout = new FileOutputStream("./GameState.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(expected);
        oos.close();

        String [] toto = new String[1];
        toto[0]="GameState.ser";
        expect(context.fileList()).andReturn(toto);

        FileInputStream fin = new FileInputStream("GameState.ser");
        expect(context.openFileInput("GameState.ser")).andReturn(fin);

        replay(context);

        Object actual = repositoryItemSerializer.deSerialize(GameState.class);
        assertNotNull(actual);
        assertEquals(expected,actual);
    }

}
