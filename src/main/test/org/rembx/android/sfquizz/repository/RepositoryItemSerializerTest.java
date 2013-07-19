package org.rembx.android.sfquizz.repository;

import android.content.Context;
import org.junit.Before;
import org.junit.Test;
import org.rembx.android.sfquizz.model.GameState;

import java.io.*;
import java.util.ArrayList;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class RepositoryItemSerializerTest {

    private Context context;
    private RepositoryItemSerializer repositoryItemSerializer;

    @Before
    public void setup(){
        context = mock(Context.class);

        repositoryItemSerializer = new RepositoryItemSerializer(context);

    }

    @Test
    public void testSerialize() throws Exception {
        Serializable entity = new GameState(new ArrayList<Integer>());

        FileOutputStream fout = new FileOutputStream("./GameState.ser");

        when(context.openFileOutput("GameState.ser", Context.MODE_PRIVATE)).thenReturn(fout);

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
        when(context.fileList()).thenReturn(toto);

        FileInputStream fin = new FileInputStream("GameState.ser");
        when(context.openFileInput("GameState.ser")).thenReturn(fin);

        Object actual = repositoryItemSerializer.deSerialize(GameState.class);
        assertNotNull(actual);
        assertEquals(expected,actual);
    }


}
