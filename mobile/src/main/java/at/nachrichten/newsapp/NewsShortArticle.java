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


public class NewsShortArticle extends MainActivity {
    private List<TextView> shortArticle;
    private static List<String> shortArticleHeader;
    private static String categoriesToLoad;
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
        dragListener = new DragListenerContent(NewsShortArticle.this);
        touchListener = new TouchListener(NewsShortArticle.this);

        /*Set Listeners*/
        findViewById(R.id.navigationComponent).setOnTouchListener(touchListener);
        findViewById(R.id.scrollView).setOnDragListener(dragListener); //drag erweitern. nach unten scroll
        findViewById(R.id.Back).setOnDragListener(dragListener);
        setBackTextViewHeight();

        categoriesToLoad = News.getCategoryChoosed();

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
        int i = 0;
        while (iterNewsFeed.hasNext() && i <= 2) {
            if (!overwriteLoadingArticle) {
                assignToLoadingArticleView(iterNewsFeed);
                overwriteLoadingArticle = !overwriteLoadingArticle;
            } else {
                contentLayout.addView(iterNewsFeed.next());
            }
            i++;
        }
    }

    private void createNewsFeed() {
        db = new DBHandler(this);
        List<Article> articles = db.getAllArticles();
        shortArticleHeader = new ArrayList<String>();
        shortArticle = new ArrayList<TextView>();
        for (Article article : articles) {
            if (article.equals(articles.indexOf(3)))
                return;
            if (article.getCategoryEng().equals(categoriesToLoad) || article.getCategoryGer().equals(categoriesToLoad)) {
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
        return Utils.getScreenHeight(this) / 65;
    }

    public float sizeBackTextViewTextHeight() {
        return Utils.getScreenHeight(this) / 65;
    }

    public int sizeTextViewHeight() {
        return Utils.getScreenHeight(this) / 5;
    }

    public void setBackTextViewHeight() {
        ((TextView) findViewById(R.id.Back)).setHeight(sizeTextViewHeight());
        ((TextView) findViewById(R.id.Back)).setMinHeight(sizeTextViewHeight());
        ((TextView) findViewById(R.id.Back)).setTextSize(sizeBackTextViewTextHeight());
    }
}
