package at.nachrichten.newsapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import at.nachrichten.newsapp.article.Article;
import at.nachrichten.newsapp.article.ArticleList;
import at.nachrichten.newsapp.database.DBHandler;
import at.nachrichten.newsapp.listener.DragListenerContent;
import at.nachrichten.newsapp.listener.TouchListener;
import at.nachrichten.newsapp.utils.Utils;

import static android.view.View.VISIBLE;

/**
 * Created by Harald on 17.01.2018.
 */

public class News extends MainActivity {
    private List<TextView> categories;
    private static String categoryChoosed;

    public static void setCategoryChoosed(String categoryChoosed) {
        News.categoryChoosed = categoryChoosed;
    }

    public static String getCategoryChoosed() {
        return categoryChoosed;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticker);

        setSizeNavigationComponent();

        //Load hardcoded databse
        DBHandler db = new DBHandler(this);
        db.addArticlesFromList(new ArticleList(this));
        List<Article> articlesFromDb = db.getAllArticles();

        /*Initialize Listeners*/
        dragListener = new DragListenerContent(News.this);
        touchListener = new TouchListener(News.this);

        /*Set Listeners*/
        findViewById(R.id.navigationComponent).setOnTouchListener(touchListener);
        findViewById(R.id.scrollView).setOnDragListener(dragListener); //drag erweitern. nach unten scroll
        findViewById(R.id.Back).setOnDragListener(dragListener);
        setBackTextViewHeight();

        createNewsFeed();
        appendTextViewsToLayout();

    }

    private void setSizeNavigationComponent() {
        ImageView navigationComponent = (ImageView) findViewById(R.id.navigationComponent);
        navigationComponent.getLayoutParams().height = Utils.getScreenHeight(this) / 2;
        navigationComponent.getLayoutParams().width = Utils.getScreenWidth(this) / 2;
    }

    private void appendTextViewsToLayout() {
        LinearLayout contentLayout = (LinearLayout) findViewById(R.id.content);
        Iterator<TextView> iterNewsFeed = categories.iterator();
        boolean overwriteLoadingArticle = false;
        while (iterNewsFeed.hasNext()) {
            if (!overwriteLoadingArticle) {
                assignToLoadingArticleView(iterNewsFeed);
                overwriteLoadingArticle = !overwriteLoadingArticle;
            } else {
                contentLayout.addView(iterNewsFeed.next());
            }
        }
    }

    private void createNewsFeed() {
        categories = new ArrayList<TextView>();
        TextView textViewToAddEconomy = createNextTextView();
        textViewToAddEconomy.setText(getString(R.string.cat_economy));
        TextView textViewToAddSports = createNextTextView();
        textViewToAddSports.setText(getString(R.string.cat_sports));
        TextView textViewToAddLifestyle = createNextTextView();
        textViewToAddLifestyle.setText(getString(R.string.cat_lifestyle));
        TextView textViewToAddPolitics = createNextTextView();
        textViewToAddPolitics.setText(getString(R.string.cat_politics));
        categories.add(textViewToAddEconomy);
        categories.add(textViewToAddSports);
        categories.add(textViewToAddLifestyle);
        categories.add(textViewToAddPolitics);
    }

    private TextView createNextTextView() {
        TextView layout = (TextView) findViewById(R.id.ArticleTextView);
        TextView nextView = new TextView(this);
        nextView.setLayoutParams(layout.getLayoutParams());
        nextView.setBackgroundResource(R.drawable.ticker_newsfeed_border);
        nextView.setMinHeight(sizeTextViewHeight());
        nextView.setHeight(sizeTextViewHeight());
        nextView.setTextColor(Color.BLACK);
        nextView.setTextSize(sizeTextViewTextHeight());
        nextView.setVisibility(VISIBLE);
        nextView.setGravity(Gravity.CENTER);
        nextView.setOnDragListener(dragListener);
        if (nextView.getParent() != null) {
            ((ViewGroup) nextView.getParent()).removeView(nextView);
        }
        return nextView;
    }

    private void assignToLoadingArticleView(Iterator<TextView> iterNewsFeed) {
        ((TextView) findViewById(R.id.ArticleTextView)).setText(iterNewsFeed.next().getText());
        ((TextView) findViewById(R.id.ArticleTextView)).setTextSize(sizeTextViewTextHeight());
        ((TextView) findViewById(R.id.ArticleTextView)).setHeight(sizeTextViewHeight());
        ((TextView) findViewById(R.id.ArticleTextView)).setMinHeight(sizeTextViewHeight());
        ((TextView) findViewById(R.id.ArticleTextView)).setGravity(Gravity.CENTER);
        ((TextView) findViewById(R.id.ArticleTextView)).setTextColor(Color.BLACK);
        ((TextView) findViewById(R.id.ArticleTextView)).setOnDragListener(dragListener);
    }

    public float sizeTextViewTextHeight() {
        return Utils.getScreenHeight(this) / 30;
    }

    public float sizeBackTextViewTextHeight() {
        return Utils.getScreenHeight(this)/65;
    }
    public int sizeTextViewHeight() {
        return Utils.getScreenHeight(this) / 5;
    }
    public int sizeBackTextViewHeight() {
        return Utils.getScreenHeight(this) / 6;
    }
    public void setBackTextViewHeight() {
        ((TextView) findViewById(R.id.Back)).setHeight(sizeBackTextViewHeight());
        ((TextView) findViewById(R.id.Back)).setMinHeight(sizeBackTextViewHeight());
        ((TextView) findViewById(R.id.Back)).setTextSize(sizeBackTextViewTextHeight());
    }
}
