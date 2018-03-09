package kr.emotyp.note.premium.api;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.emotyp.note.premium.model.Note;
import kr.emotyp.note.premium.utils.StorageManager;
import kr.emotyp.note.premium.utils.TimeStampManager;


/**
 * Created by yearnning on 2014. 3. 23..
 */
public class MainListApi {
    ArrayList<Note> notes = new ArrayList<Note>();

    public MainListApi(Application application) {
        try {
            String db = StorageManager.getStringFromFile(application, "db");

            JSONObject jsonObject = new JSONObject(db);
            JSONArray jsonArray = jsonObject.getJSONArray("notes");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject noteObject = jsonArray.getJSONObject(i);

                Note note = new Note();

                note.setText(noteObject.getString("text"));
                note.setColor(noteObject.getInt("color"));
                note.setFont_code(noteObject.getInt("font_code"));
                note.setTs(TimeStampManager.convertTimeFormat(noteObject.getLong("ts")));

                notes.add(0, note);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Note> getResult() {
        return notes;
    }


}
