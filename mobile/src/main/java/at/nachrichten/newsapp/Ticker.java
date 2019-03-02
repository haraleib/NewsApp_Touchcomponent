package at.nachrichten.newsapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import at.nachrichten.newsapp.async.TickerHandlerShortArticle;
import at.nachrichten.newsapp.listener.DragListenerContent;
import at.nachrichten.newsapp.listener.TouchListener;
import at.nachrichten.newsapp.utils.Utils;

import static android.view.View.VISIBLE;

/**
 * Created by Harald on 06.12.2017.
 */

public class Ticker extends MainActivity {

    private List<TextView> newsFeed;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticker);

        setSizeNavigationComponent();

        /*Initialize Listeners*/
        dragListener = new DragListenerContent(Ticker.this);
        touchListener = new TouchListener(Ticker.this);

        /*Set Listeners*/
        findViewById(R.id.navigationComponent).setOnTouchListener(touchListener);

        findViewById(R.id.scrollView).setOnDragListener(dragListener); //drag erweitern. nach unten scroll
        findViewById(R.id.Back).setOnDragListener(dragListener);
        setBackTextViewHeight();
        if (TickerHandlerShortArticle.isExecuted()) {
            createNewsFeed();
            findViewById(R.id.ArticleTextView).setVisibility(View.INVISIBLE);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    appendTextViewsToLayout();
                }
            });
        }
    }

    private void setSizeNavigationComponent() {
        ImageView navigationComponent = (ImageView) findViewById(R.id.navigationComponent);
        navigationComponent.getLayoutParams().height = Utils.getScreenHeight(this)/2;
        navigationComponent.getLayoutParams().width = Utils.getScreenWidth(this)/2;
    }

    private void appendTextViewsToLayout() {
        LinearLayout contentLayout = (LinearLayout) findViewById(R.id.content);
        Iterator<TextView> iterNewsFeed = newsFeed.iterator();
        while (iterNewsFeed.hasNext()) {
            contentLayout.addView(iterNewsFeed.next());
        }
    }

    private void createNewsFeed() {
        HashMap<String, String[]> contentMap = TickerHandlerShortArticle.getContentMap();
        Map.Entry<String, String[]> entry;
        Iterator<Map.Entry<String, String[]>> iterContent = contentMap.entrySet().iterator();
        newsFeed = new ArrayList<TextView>();

        int i = 0;
        while (iterContent.hasNext() && i <=2) {
            TextView textViewToAdd = createNextTextView();
            entry = iterContent.next();
            textViewToAdd.setText(entry.getValue()[0]);
            newsFeed.add(textViewToAdd);
            i++;
        }
    }

    private TextView createNextTextView() {
        TextView layout = (TextView) findViewById(R.id.ArticleTextView);
        TextView nextView = new TextView(this);
        nextView.setLayoutParams(layout.getLayoutParams());
        nextView.setBackgroundResource(R.drawable.ticker_newsfeed_border);
        nextView.setTextColor(Color.BLACK);
        nextView.setHeight(sizeTextViewHeight());
        nextView.setMinHeight(sizeTextViewHeight());
        nextView.setTextSize(sizeTextViewTextHeight());
        nextView.setVisibility(VISIBLE);
        nextView.setOnDragListener(dragListener);
        if (nextView.getParent() != null) {
            ((ViewGroup) nextView.getParent()).removeView(nextView);
        }
        return nextView;
    }

    public float sizeTextViewTextHeight(){
        //    Integer height = ((TextView) findViewById(R.id.ArticleTextView)).getHeight();
        //    Float heightF = (float) height;
        return Utils.getScreenHeight(this)/65;
    }

    public int sizeTextViewHeight(){
        return Utils.getScreenHeight(this)/5;
    }

    public float sizeBackTextViewTextHeight() {
        return Utils.getScreenHeight(this)/75;
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