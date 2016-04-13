package cz.cvut.panskpe1.rssfeeder.activity.main;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by petr on 4/13/16.
 */
public class TaskFragment extends Fragment {

    private TaskCallbacks mCallbacks;
    private UpdateAsyncTask mTask;

    public interface TaskCallbacks {
        void onPreExecute();

        void onPostExecute();

        void updateProgress(int progress);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void executeTask() {
        mTask = new UpdateAsyncTask();
        mTask.execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private class UpdateAsyncTask extends AsyncTask<Void, Integer, Void> {


        @Override
        protected void onPreExecute() {
            if (mCallbacks != null) {
                mCallbacks.onPreExecute();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; !isCancelled() && i < 100; i++) {
                SystemClock.sleep(50);
//                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mCallbacks.updateProgress(values[0]);
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            if (mCallbacks != null) {
                mCallbacks.onPostExecute();
            }
        }
    }
}
