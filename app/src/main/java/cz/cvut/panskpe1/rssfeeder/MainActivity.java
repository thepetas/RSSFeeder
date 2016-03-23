package cz.cvut.panskpe1.rssfeeder;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by petr on 3/19/16.
 */
public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentListener {


    private static String TAG = "MAIN_ACTIVITY";
    public static final String ARTICLE_ID = "article_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.containerArticleDetailActivity, MainFragment.newInstance())
                    .commit();

        }
    }


    @Override
    public void showArticle(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARTICLE_ID, id);

        Intent intent = new Intent(this, ArticleDetailActivity.class);
        intent.putExtra(ARTICLE_ID, id);
        startActivity(intent);
    }
}
