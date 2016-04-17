package cz.cvut.panskpe1.rssfeeder.activity.main;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;

import cz.cvut.panskpe1.rssfeeder.data.UpdateManager;

/**
 * Created by petr on 4/13/16.
 */
public class TaskFragment extends Fragment {

    private TaskCallbacks mCallbacks;
    private UpdateAsyncTask mTask;
    private boolean isRunning = false;

    public boolean isRunning() {
        return isRunning;
    }

    public interface TaskCallbacks {
        void onPreExecute();

        void onPostExecute(int cnt);

        void updateProgress();
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

    public class UpdateAsyncTask extends AsyncTask<Void, Void, Integer> {


        @Override
        protected void onPreExecute() {
            if (mCallbacks != null) {
                mCallbacks.onPreExecute();
                isRunning = true;
            }
        }

        @Override
        protected Integer doInBackground(Void... params) {
//            SystemClock.sleep(10000);
            try {
                return UpdateManager.updateArticles(getActivity());
            } catch (Exception e) {
                return -1;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            mCallbacks.updateProgress();
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(Integer values) {
            if (mCallbacks != null) {
                mCallbacks.onPostExecute(values);
                isRunning = false;
            }
        }

    }
}
