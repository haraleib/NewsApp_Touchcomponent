package at.nachrichten.newsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import at.nachrichten.newsapp.async.TickerHandlerFullArticle;
import at.nachrichten.newsapp.listener.DragListenerContent;
import at.nachrichten.newsapp.listener.TouchListener;
import at.nachrichten.newsapp.utils.Utils;

/**
 * Created by Harald Eibensteiner
 * Matr: k01300179
 */


public class TickerFullArticle extends MainActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticker);

        setSizeNavigationComponent();

        dragListener = new DragListenerContent(TickerFullArticle.this);
        touchListener = new TouchListener(TickerFullArticle.this);

        TickerHandlerFullArticle.setTextViewToSetContent(findViewById(R.id.ArticleTextView));

        findViewById(R.id.navigationComponent).setOnTouchListener(touchListener);

        findViewById(R.id.ArticleTextView).setOnDragListener(dragListener);
        findViewById(R.id.scrollView).setOnDragListener(dragListener); //drag erweitern. nach unten scroll
        findViewById(R.id.Back).setOnDragListener(dragListener);
        setBackTextViewHeight();

        sizeTextViewTextHeight();
    }

    private void setSizeNavigationComponent() {
        ImageView navigationComponent = (ImageView) findViewById(R.id.navigationComponent);
        navigationComponent.getLayoutParams().height = Utils.getScreenHeight(this) / 2;
        navigationComponent.getLayoutParams().width = Utils.getScreenWidth(this) / 2;
    }

    public void sizeTextViewTextHeight() {
        ((TextView) findViewById(R.id.ArticleTextView)).setTextSize(Utils.getScreenHeight(this) / 90);
    }

    public int sizeTextViewHeight() {
        return Utils.getScreenHeight(this) / 6;
    }
    public float sizeBackTextViewTextHeight() {
        return Utils.getScreenHeight(this) / 65;
    }

    public void setBackTextViewHeight() {
        ((TextView) findViewById(R.id.Back)).setHeight(sizeTextViewHeight());
        ((TextView) findViewById(R.id.Back)).setMinHeight(sizeTextViewHeight());
        ((TextView) findViewById(R.id.Back)).setTextSize(sizeBackTextViewTextHeight());
    }
}
