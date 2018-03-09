package kr.emotyp.note.premium.utils;

import android.app.Application;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by yearnning on 2014. 3. 23..
 */
public class StorageManager {

    private static final String TAG = "Storage Manager";

    /**
     * @param application
     * @param file_name
     * @return
     */
    public static String getStringFromFile(Application application, String file_name) {
        final File[] dirs = ContextCompat.getExternalCacheDirs(application);
        File file = new File(dirs[0].getAbsolutePath() + File.separator.toString() + file_name);

        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }

    /**
     * 파일로 저장합니다.
     */
    public static void saveStringToFile(Application application, String str, String file_name) {

        /*
         *
         */

        byte[] note_bytes = str.getBytes();

        /*
         * 저장할 파일을 생성합니다.
         */
        final File[] dirs = ContextCompat.getExternalCacheDirs(application);
        Log.d(TAG, dirs[0].getAbsolutePath());
        File f = new File(dirs[0].getAbsolutePath() + File.separator.toString() + file_name);

        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * 파일에 저장합니다.
         */
        FileOutputStream fo;
        try {
            fo = new FileOutputStream(f);
            fo.write(note_bytes);
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
