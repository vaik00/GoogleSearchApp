package app.dev.googlesearchapp.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by vaik00 on 24.05.2017.
 */

public class FileHelper {
    private static final String FILE_EXTENSION = ".jpeg";
    private static final String IMAGE_PATH = "/googlesearchapp/images/";


    public static void saveToFile(Bitmap bitmap, String fileName) {
        String root = Environment.getExternalStorageDirectory().toString();
        File dir = new File(root + IMAGE_PATH);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
        }
        File fTo = new File(root + IMAGE_PATH + fileName + FILE_EXTENSION);
        try {
            FileOutputStream outputStream = new FileOutputStream(fTo);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getFile(String fileName) {
        String root = Environment.getExternalStorageDirectory().toString();
        return new File(root + IMAGE_PATH + fileName + FILE_EXTENSION);
    }

    public static void removeFile(String fileName) {
        String root = Environment.getExternalStorageDirectory().toString();
        File file = new File(root + IMAGE_PATH + fileName + FILE_EXTENSION);
        boolean deleted = file.delete();
    }
}
