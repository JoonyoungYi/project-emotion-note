package kr.emotyp.note.premium.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.emotyp.note.premium.R;
import kr.emotyp.note.premium.api.MainListApi;
import kr.emotyp.note.premium.model.Note;
import kr.emotyp.note.premium.utils.Argument;


public class MainActivity extends FragmentActivity {
    private final static String TAG = "MainActivity";

    /**
     *
     */
    private ListView mLv;
    private View mErrorView;
    private TextView mErrorTv;
    private ProgressBar mErrorPb;
    private LvAdapter mLvAdapter;

    /**
     *
     */
    private Typeface[] TYPEFACES = new Typeface[Argument.CUSTOM_FONTS.length];
    private MainListApiTask mainListApiTask = null;
    private boolean doubleBackToExitPressedOnce = false;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        View headerView = new View(this);
        View footerView = new View(this);
        /*

         */
        TYPEFACES[0] = Typeface.DEFAULT;
        for (int i = 1; i < Argument.CUSTOM_FONTS.length; i++) {

            try {
                TYPEFACES[i] = Typeface.createFromAsset(this.getAssets(), Argument.CUSTOM_FONTS[i]);

            } catch (Exception e) {
                e.printStackTrace();
                TYPEFACES[i] = Typeface.SANS_SERIF;
            }
        }


        /*

         */
        mLv = (ListView) findViewById(R.id.lv);
        mErrorView = findViewById(R.id.error_view);
        mErrorTv = (TextView) mErrorView.findViewById(R.id.error_tv);
        mErrorPb = (ProgressBar) mErrorView.findViewById(R.id.error_pb);

        /*

         */
        mLv.addHeaderView(headerView);
        mLv.addFooterView(footerView);
        ArrayList<Note> notes = new ArrayList<Note>();
        mLvAdapter = new LvAdapter(this, R.layout.main_activity_lv, notes);
        mLv.setAdapter(mLvAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.add_note_btn) {
            startAddActivity();
            return true;
        } else if ( id == R.id.action_settings ) {
            //Intent intent = new Intent(this, GooglePlayServicesActivity.class);
            //startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "resume");
        if (mainListApiTask == null) {
            mainListApiTask = new MainListApiTask();
            mainListApiTask.execute();
        }

    }


    /**
     *
     */
    public class MainListApiTask extends AsyncTask<Void, Void, ArrayList<Note>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showErrorView(true, "");

        }

        /**
         * @param params
         * @return
         */
        @Override
        protected ArrayList<Note> doInBackground(Void... params) {

            MainListApi mainListApi = new MainListApi(getApplication());

            return mainListApi.getResult();
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            mainListApiTask = null;
            showErrorView(false, "");
            mLvAdapter.clear();
            if (notes != null)
                mLvAdapter.addAll(notes);

            if (notes.size() == 0)
                showErrorView(true, "저장된 노트가 없습니다. 노트를 작성해주세요.");
            mLvAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            showErrorView(true, "오류가 발생했습니다. 잠시후 다시 시도해주세요.");
            mainListApiTask = null;
            super.onCancelled();

        }
    }

    /**
     * ListView Apdater Setting
     */

    private class LvAdapter extends ArrayAdapter<Note> {
        private static final String TAG = "MainFragment LvAdapter";

        private ViewHolder viewHolder = null;
        public ArrayList<Note> notes;
        private int textViewResourceId;

        public LvAdapter(Activity context, int textViewResourceId,
                         ArrayList<Note> notes) {
            super(context, textViewResourceId, notes);

            this.textViewResourceId = textViewResourceId;
            this.notes = notes;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public int getCount() {
            return notes.size();
        }

        @Override
        public Note getItem(int position) {
            return notes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

			/*
             * UI Initiailizing : View Holder
			 */

            if (convertView == null) {
                convertView = getLayoutInflater()
                        .inflate(textViewResourceId, null);

                viewHolder = new ViewHolder();
                viewHolder.mTextTv = (TextView) convertView.findViewById(R.id.text_tv);
                viewHolder.mDescriptionTv = (TextView) convertView.findViewById(R.id.description_tv);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

			/*
             * Data Import and export
			 */
            Note note = notes.get(position);

            viewHolder.mTextTv.setTypeface(TYPEFACES[note.getFont_code()]);
            viewHolder.mTextTv.setText(note.getText());
            viewHolder.mDescriptionTv.setText(note.getTs() + "에 작성하셨습니다");
            viewHolder.mTextTv.setTextColor(note.getColor());

            return convertView;
        }


        private class ViewHolder {
            TextView mTextTv;
            TextView mDescriptionTv;
        }

    }

    /**
     *
     */
    private void startAddActivity() {

        Intent intent = new Intent(MainActivity.this, MainAddActivity.class);
        startActivity(intent);
    }

    /**
     * 뒤로가기 두번
     */
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "한 번 더 누르시면, \"Emotyp\"에서 빠져나갑니다.", Toast.LENGTH_SHORT)
                .show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;

            }
        }, 2000);
    }

    @Override
    public void onDestroy() {
        if (mainListApiTask != null) {
            mainListApiTask.cancel(true);
        }

        super.onDestroy();
    }

    /**
     * @param show
     * @param msg
     */
    private void showErrorView(final boolean show, String msg) {

        if (show) {
            mErrorView.setVisibility(View.VISIBLE);
            mErrorTv.setText(msg);

            if (msg.equals("")) {
                mErrorPb.setVisibility(View.VISIBLE);
            } else {
                mErrorPb.setVisibility(View.GONE);
            }

        } else {
            mErrorPb.setVisibility(View.VISIBLE);
            mErrorView.setVisibility(View.GONE);
        }

    }
}

