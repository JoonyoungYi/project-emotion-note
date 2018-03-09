package kr.emotyp.note.premium.view;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import kr.emotyp.note.premium.R;
import kr.emotyp.note.premium.api.AddNoteApi;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainAddActivity extends Activity {
    private static String TAG = "Main Add Activity";

    AddNoteApiTask addNoteApiTask = null;
    /**
     *
     */
    private View mPb;
    private EditText mEditText;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_add_activity);

        mEditText = (EditText) findViewById(R.id.et);
        mPb = findViewById(R.id.pb);

        findViewById(R.id.cancel_btn).setOnClickListener(onClickListener);
        findViewById(R.id.confirm_btn).setOnClickListener(onClickListener);
    }

    /**
     * Btn OnClickListener
     */

    View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            clickListenerHandler(v.getId());
        }
    };

    private void clickListenerHandler(int id) {
        switch (id) {
            case R.id.cancel_btn:
                onDestroy();
                break;
            case R.id.confirm_btn: {

                if (mEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "노트를 입력해주세요", Toast.LENGTH_LONG).show();

                } else if (addNoteApiTask == null)
                    (addNoteApiTask = new AddNoteApiTask()).execute();
                break;
            }

            default:
                break;
        }
    }

    ;


    /**
     *
     */
    public class AddNoteApiTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mPb.setVisibility(View.VISIBLE);
            mEditText.setVisibility(View.GONE);


        }

        /**
         * @param params
         * @return
         */
        @Override
        protected Void doInBackground(Void... params) {

            AddNoteApi addNoteApi = new AddNoteApi(getApplication(), mEditText.getText().toString().trim());

            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            addNoteApiTask = null;
            finish();

        }

        @Override
        protected void onCancelled() {
            mPb.setVisibility(View.GONE);
            mEditText.setVisibility(View.VISIBLE);

            super.onCancelled();
            addNoteApiTask = null;
        }

    }

    @Override
    public void onDestroy() {
        if (addNoteApiTask != null) {
            addNoteApiTask.cancel(true);
        }
        super.onDestroy();
    }
}

