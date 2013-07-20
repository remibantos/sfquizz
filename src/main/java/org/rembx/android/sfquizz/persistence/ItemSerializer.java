package org.rembx.android.sfquizz.persistence;

import android.content.Context;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;

import java.io.*;
import java.util.Arrays;

/**
 * Serialize objects to handset memory.
 *
 * @author remibantos
 */
@ContextSingleton
public class ItemSerializer {

    private final static String FILE_EXT = ".ser";

    private Context context;

    @Inject
    public ItemSerializer(Context context) {
        this.context = context;
    }

    /**
     * Serialize provided repository to file with configured  fileMode
     *
     * @param item the item to serialize
     * @throws IOException
     */
    public void serialize(Serializable item) throws IOException {
        FileOutputStream fout = null;
        try {
            fout = context.openFileOutput(computeItemSerializationFileName(item), Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(item);

        } finally {
            if (fout != null)
                fout.close();
        }

    }

    public Object deSerialize(Class itemType) throws IOException, ClassNotFoundException {
        FileInputStream fin = null;
        Object deserialized = null;
        if (!Arrays.asList(context.fileList()).contains(computeItemSerializationFileNameFromClass(itemType)))
            return null;
        try {
            fin = context.openFileInput(computeItemSerializationFileNameFromClass(itemType));
            ObjectInputStream oos = new ObjectInputStream(fin);
            deserialized = oos.readObject();
        } finally {
            if (fin != null)
                fin.close();
        }
        return deserialized;
    }

    public void delete(Object item) throws IOException, ClassNotFoundException {
        if (!Arrays.asList(context.fileList()).contains(computeItemSerializationFileName(item)))
            return;
        context.deleteFile(computeItemSerializationFileName(item));
    }

    private String computeItemSerializationFileName(Object item) {
        return item.getClass().getSimpleName() + FILE_EXT;
    }

    private String computeItemSerializationFileNameFromClass(Class item) {
        return item.getSimpleName() + FILE_EXT;
    }

}
