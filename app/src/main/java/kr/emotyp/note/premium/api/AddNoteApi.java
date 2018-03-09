package kr.emotyp.note.premium.api;

import android.app.Application;
import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import kr.emotyp.note.premium.utils.Argument;
import kr.emotyp.note.premium.utils.StorageManager;


/**
 * Created by yearnning on 2014. 3. 23..
 */
public class AddNoteApi {
    private static final String TAG = "AddNoteApi";

    public AddNoteApi(Application application, String text) {

        try {
            String db = StorageManager.getStringFromFile(application, "db");

            JSONObject jsonObject = new JSONObject(db);
            JSONArray jsonArray = jsonObject.getJSONArray("notes");

            //
            JSONObject noteObject = new JSONObject();
            noteObject.put("text", text);
            Random rnd = new Random();
            int r, g, b;
            do {
                r = rnd.nextInt(256);
                g = rnd.nextInt(256);
                b = rnd.nextInt(256);

            } while ((r + g + b) < 384);

            int color = Color.argb(255, r, g, b);
            noteObject.put("color", color);
            noteObject.put("font_code", (int) (Math.random() * Argument.CUSTOM_FONTS.length));
            noteObject.put("ts", System.currentTimeMillis());
            //
            jsonArray.put(noteObject);
            jsonObject.put("notes", jsonArray);

            StorageManager.saveStringToFile(application, jsonObject.toString(), "db");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
