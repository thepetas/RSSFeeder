package cz.cvut.panskpe1.rssfeeder;

import android.app.Activity;
import android.content.res.Configuration;
import android.widget.Toast;

/**
 * Created by petr on 3/20/16.
 */
public class TestSize {

    public static String getSize(Activity gActivity){
        int screenSize = gActivity.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        String toastMsg;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                toastMsg = "Large screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                toastMsg = "Normal screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                toastMsg = "Small screen";
                break;
            default:
                toastMsg = "Screen size is neither large, normal or small";
        }
        return toastMsg;
    }
}
