package at.nachrichten.newsapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
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

public class BookmarksFullArticle extends MainActivity {

    private List<TextView> fullArticle;
    public static String headerFulArticleToLoad;
    private DBHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticker);

        setSizeNavigationComponent();

        /*Initialize Listeners*/
        dragListener = new DragListenerContent(BookmarksFullArticle.this);
        touchListener = new TouchListener(BookmarksFullArticle.this);

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
        //     LinearLayout contentLayout = (LinearLayout) findViewById(R.id.content);
        //    Iterator<TextView> iterNewsFeed = fullArticle.iterator();
        //     while (iterNewsFeed.hasNext()) {
        //         contentLayout.addView(iterNewsFeed.next());
        //     }
        if (fullArticle.iterator().hasNext()) {
            TextView textView = (TextView) findViewById(R.id.ArticleTextView);
            textView.setText(fullArticle.iterator().next().getText().toString());
        }
    }

    private void createNewsFeed() {
        db = new DBHandler(this);
        List<Article> articles = db.getAllArticles();
        fullArticle = new ArrayList<TextView>();
        for (Article article : articles) {
            String headerStart = article.getDate() + "\n" + article.getHeader() + "\n" + article.getData() + "\n";
            headerStart = headerStart.substring(0, 15);
            headerFulArticleToLoad = headerFulArticleToLoad.substring(0, 15);
            if (headerFulArticleToLoad.equals(headerStart)) {
                TextView textViewToAdd = createNextTextView();
                textViewToAdd.setText(article.getDate() + "\n" + article.getHeader() + "\n" + article.getData() + "\n");
                textViewToAdd.setTextSize(sizeTextViewTextHeight());
                fullArticle.add(textViewToAdd);
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

    public float sizeTextViewTextHeight() {
        return Utils.getScreenHeight(this) / 65;
    }

    public static void setHeaderFulArticleToLoad(String headerFullArticleToLoad) {
        headerFulArticleToLoad = headerFullArticleToLoad;
    }
}
