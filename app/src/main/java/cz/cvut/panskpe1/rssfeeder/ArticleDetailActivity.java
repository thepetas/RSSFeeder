package cz.cvut.panskpe1.rssfeeder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by petr on 3/19/16.
 */
public class ArticleDetailActivity extends AppCompatActivity implements ArticleDetailFragment.ArticleDetailFragmentListener {


    private static String TAG = "ARTICLE_DETAIL_ACTIVITY";
    private int articleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        Intent intent = getIntent();
        articleId = intent.getIntExtra(MainActivity.ARTICLE_ID, 0);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.containerArticleDetailActivity, ArticleDetailFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article_detail_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public int getArticleId() {
        return articleId;
    }

    @Override
    public void shareArticle(Intent intent) {
        startActivity(intent);
    }
}
