package at.nachrichten.newsapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import at.nachrichten.newsapp.article.Article;
import at.nachrichten.newsapp.database.DBHandler;
import at.nachrichten.newsapp.listener.DragListenerContent;
import at.nachrichten.newsapp.listener.TouchListener;
import at.nachrichten.newsapp.utils.Utils;

import static android.view.View.VISIBLE;

/**
 * Created by Harald Eibensteiner
 * Matr: k01300179
 */

public class Bookmarks extends MainActivity {

    private List<TextView> shortArticle;
    private List<TextView> markedArticlesShort;
    private static List<String> shortArticleHeader;
    private DBHandler db;

    public static List<String> getShortArticleHeader() {
        return shortArticleHeader;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticker);

        setSizeNavigationComponent();

        /*Initialize Listeners*/
        dragListener = new DragListenerContent(Bookmarks.this);
        touchListener = new TouchListener(Bookmarks.this);

        /*Set Listeners*/
        findViewById(R.id.navigationComponent).setOnTouchListener(touchListener);
        findViewById(R.id.scrollView).setOnDragListener(dragListener); //drag erweitern. nach unten scroll
        findViewById(R.id.Back).setOnDragListener(dragListener);

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
        Iterator<TextView> iterNewsFeed = shortArticle.iterator();
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
        db = new DBHandler(this);
        List<Article> articles = db.getAllArticles();
        shortArticleHeader = new ArrayList<String>();
        shortArticle = new ArrayList<TextView>();
        for (Article article : articles) {
            if (article.getIsBookMarked().equals("Yes")) {
                shortArticleHeader.add(article.getDate() + "\n" + article.getHeader() + "\n");
                TextView textViewToAdd = createNextTextView();
                textViewToAdd.setText(article.getDate() + "\n" + article.getHeader() + "\n");
                textViewToAdd.setTextSize(sizeTextViewTextHeight());
                shortArticle.add(textViewToAdd);
            }
        }

    }

    private TextView createNextTextView() {
        TextView layout = (TextView) findViewById(R.id.ArticleTextView);
        TextView nextView = new TextView(this);
        nextView.setLayoutParams(layout.getLayoutParams());
        nextView.setBackgroundResource(R.drawable.ticker_newsfeed_border);
        nextView.setTextColor(Color.BLACK);
        nextView.setTextSize(sizeTextViewTextHeight());
        nextView.setVisibility(VISIBLE);
        nextView.setOnDragListener(dragListener);
        if (nextView.getParent() != null) {
            ((ViewGroup) nextView.getParent()).removeView(nextView);
        }
        return nextView;
    }

    private void assignToLoadingArticleView(Iterator<TextView> iterNewsFeed) {
        ((TextView) findViewById(R.id.ArticleTextView)).setText(iterNewsFeed.next().getText());
        ((TextView) findViewById(R.id.ArticleTextView)).setTextSize(sizeTextViewTextHeight());
        ((TextView) findViewById(R.id.ArticleTextView)).setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        ((TextView) findViewById(R.id.ArticleTextView)).setTextColor(Color.BLACK);
        ((TextView) findViewById(R.id.ArticleTextView)).setOnDragListener(dragListener);
    }


    public float sizeTextViewTextHeight() {
        //    Integer height = ((TextView) findViewById(R.id.ArticleTextView)).getHeight();
        //    Float heightF = (float) height;
        return Utils.getScreenHeight(this) / 65;
    }
}
