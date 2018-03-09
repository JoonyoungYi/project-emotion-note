package kr.emotyp.note.premium.view;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import kr.emotyp.note.premium.R;
import kr.emotyp.note.premium.utils.StorageManager;


public class SplashActivity extends Activity {

    SplashApiTask mSplashApiTask = null;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        mSplashApiTask = new SplashApiTask();
        mSplashApiTask.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     */
    public class SplashApiTask extends AsyncTask<Void, Void, Void> {

        /**
         * @param params
         * @return
         */
        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            final File[] dirs = ContextCompat.getExternalCacheDirs(getApplicationContext());

            File dir = new File(dirs[0].getAbsolutePath());
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }

            File file = new File(dirs[0].getAbsolutePath() + File.separator.toString() + "db");
            if (!file.isFile()) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    jsonObject.put("notes", jsonArray);
                    StorageManager.saveStringToFile(getApplication(), jsonObject.toString(), "db");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void param) {

            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);

            finish();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mSplashApiTask = null;
        }
    }

    @Override
    public void onDestroy() {
        if (mSplashApiTask != null) {
            mSplashApiTask.cancel(true);
        }

        super.onDestroy();
    }

}
