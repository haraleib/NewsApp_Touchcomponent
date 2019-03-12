package at.nachrichten.newsapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import at.nachrichten.newsapp.async.TickerHandlerShortArticle;
import at.nachrichten.newsapp.listener.DragListenerHome;
import at.nachrichten.newsapp.listener.TouchListener;
import at.nachrichten.newsapp.utils.Utils;

/**
 * Created by Harald Eibensteiner
 * Matr: k01300179
 */


public class Home extends MainActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setSizeNavigationComponent();

        /*Load content for ticker in own thread*/
        new TickerHandlerShortArticle().execute();

        /*Initialize Listeners*/
        dragListener = new DragListenerHome(Home.this);
        touchListener = new TouchListener(Home.this);

        /*Set Listeners*/
        findViewById(R.id.navigationComponent).setOnTouchListener(touchListener);
        findViewById(R.id.Login).setOnDragListener(dragListener);
        findViewById(R.id.News).setOnDragListener(dragListener);
        findViewById(R.id.Bookmarks).setOnDragListener(dragListener);
        findViewById(R.id.Ticker).setOnDragListener(dragListener);

        sizeTextViewTextHeight();
    }

    private void setSizeNavigationComponent() {
        ImageView navigationComponent = (ImageView) findViewById(R.id.navigationComponent);
        navigationComponent.getLayoutParams().height = Utils.getScreenHeight(this) / 2;
        navigationComponent.getLayoutParams().width = Utils.getScreenWidth(this) / 2;
    }

    public void sizeTextViewTextHeight() {
        ((TextView) findViewById(R.id.Login)).setTextSize(Utils.getScreenHeight(this) / 50);
        ((TextView) findViewById(R.id.News)).setTextSize(Utils.getScreenHeight(this) / 50);
        ((TextView) findViewById(R.id.Bookmarks)).setTextSize(Utils.getScreenHeight(this) / 50);
        ((TextView) findViewById(R.id.Ticker)).setTextSize(Utils.getScreenHeight(this) / 50);
        //    Float heightF = (float) height;
    }
}

