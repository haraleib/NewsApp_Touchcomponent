package at.nachrichten.newsapp.listener;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import at.nachrichten.newsapp.Bookmarks;
import at.nachrichten.newsapp.BookmarksFullArticle;
import at.nachrichten.newsapp.Home;
import at.nachrichten.newsapp.News;
import at.nachrichten.newsapp.NewsFullArticle;
import at.nachrichten.newsapp.NewsShortArticle;
import at.nachrichten.newsapp.R;
import at.nachrichten.newsapp.Ticker;
import at.nachrichten.newsapp.TickerFullArticle;
import at.nachrichten.newsapp.article.Article;
import at.nachrichten.newsapp.database.DBHandler;
import at.nachrichten.newsapp.messages.Messages;
import at.nachrichten.newsapp.speak.Speak;
import at.nachrichten.newsapp.utils.Utils;

/**
 * Created by Harald Eibensteiner
 * Matr: k01300179
 * <p>
 * This class gives Motions to an GestureDetector with onTouch() method
 * onTouch method gives Motions to the GestureDetector which handles Gestures
 * on initialization of the TouchListener, a GestureDetector is initialized with the current context and
 * with a extended GestureListener which holds the current TouchListener
 * This is necessary, because the GestureListener needs to access the view -> navigationComponent,
 * and Speak needs the current context.
 * and getDetector().onTouchEvent(motionEvent); does not offers a parameter to pass the view
 */

public class TouchListener implements View.OnTouchListener {

    private Context context;
    private GestureDetector gDetector;
    private View view;

    public TouchListener() {
        super();
    }

    public TouchListener(Context context) {
        this(context, null);
    }

    public TouchListener(Context context, GestureDetector gDetector) {
        this.context = context;
        Activity activity = (Activity) context;
        this.view = activity.findViewById(R.id.navigationComponent);
        this.gDetector = new GestureDetector(this.context, new GestureListener(getTouchListener()));
    }

    public View getView() {
        return view;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setgDetector(GestureDetector gDetector) {
        this.gDetector = gDetector;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Context getContext() {
        return context;
    }

    public Activity getActivity() {
        return (Activity) context;
    }

    public TouchListener getTouchListener() {
        return this;
    }

    public GestureDetector getDetector() {
        return this.gDetector;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.i(Messages.LOG_TAG_TouchListener, "onTouch: " + motionEvent.getAction());
        return getDetector().onTouchEvent(motionEvent);
    }
}

class GestureListener extends GestureDetector.SimpleOnGestureListener {

    private TouchListener currTouchListener;
    private Speak speak;
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private final int NO_TEXT_VIEW_ID = 0;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private DBHandler db;


    private GestureListener() {
        super();
    }

    GestureListener(TouchListener touchListener) {
        super();
        this.currTouchListener = touchListener;
        this.speak = new Speak(currTouchListener.getContext());
        this.db = new DBHandler(currTouchListener.getContext());
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.i(Messages.LOG_TAG_GestureListener, "onDown");
        return true;
    }

    public TouchListener getCurrTouchListener() {
        return currTouchListener;
    }

    public Speak getSpeak() {
        return speak;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2,
                           float velocityX, float velocityY) {
        Log.i(Messages.LOG_TAG_GestureListener, "onFling: " + e1.toString() + e2.toString());
        boolean result = false;
        try {

            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                    result = true;
                }
            } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    onSwipeBottom();
                } else {
                    onSwipeTop();
                }
                result = true;
            }
        } catch (Exception exception) {
            currTouchListener.getView().setVisibility(View.VISIBLE);
            Log.i(Messages.LOG_TAG_GestureListener, "exception during onFling: " + e1.toString() + e2.toString());
            exception.printStackTrace();
        }
        return result;
    }

    public void onSwipeTop() {
        getSpeak().onDestroy();
        getCurrTouchListener().getActivity().finishAffinity();
        Log.i(Messages.LOG_TAG_GestureListener, "onSwipeTop: ");
    }

    public void onSwipeRight() {
        Activity currActivity = currTouchListener.getActivity();
        Context currContext = (Context) currActivity;
        if (currActivity instanceof Home) {
            speak.speak(currTouchListener.getContext().getString(R.string.introduction));
        } else if (currActivity instanceof Ticker) {
            stopSpeakIfSpeaking();
            Utils.goToActivity(currContext, Home.class);
        } else if (currActivity instanceof TickerFullArticle) {
            stopSpeakIfSpeaking();
            Utils.goToActivity(currContext, Ticker.class);
        } else if (currActivity instanceof Bookmarks) {
            stopSpeakIfSpeaking();
            Utils.goToActivity(currContext, Home.class);
        } else if (currActivity instanceof BookmarksFullArticle) {
            if (unsetMarkArticleAsBookmarked()) {
                getSpeak().speak(currContext.getString(R.string.REMOVE_BOOKMARK));
            } else {
                getSpeak().speak(currContext.getString(R.string.REMOVE_BOOKMARK));
            }
            Utils.goToActivity(currContext, Bookmarks.class);
        } else if (currActivity instanceof News) {
            stopSpeakIfSpeaking();
            Utils.goToActivity(currContext, Home.class);
        } else if (currActivity instanceof NewsShortArticle) {
            stopSpeakIfSpeaking();
            Utils.goToActivity(currContext, News.class);
        } else if (currActivity instanceof NewsFullArticle) {
            if (markArticleAsBookmarked()) {
                getSpeak().speak(currContext.getString((R.string.SET_BOOKMARK)));
            } else {
                getSpeak().speak(currContext.getString((R.string.FAIL_SET_BOOKMARK)));
            }
            Utils.goToActivity(currContext, NewsShortArticle.class);
        }
        Log.i(Messages.LOG_TAG_GestureListener, "onSwipeRight: ");
    }

    public boolean markArticleAsBookmarked() {
        db = new DBHandler(currTouchListener.getContext());
        List<Article> articles = db.getAllArticles();
        for (Article article : articles) {
            String headerStart = article.getDate() + "\n" + article.getHeader() + "\n" + article.getData() + "\n";
            headerStart = headerStart.substring(0, 15);
            NewsFullArticle.headerFulArticleToLoad = NewsFullArticle.headerFulArticleToLoad.substring(0, 15);
            if (headerStart.equals(NewsFullArticle.headerFulArticleToLoad)) {
                article.setIsBookMarked("Yes");
                db.updateArticle(article);
                return true;
            }
        }
        return false;
    }

    public boolean unsetMarkArticleAsBookmarked() {
        db = new DBHandler(currTouchListener.getContext());
        List<Article> articles = db.getAllArticles();
        for (Article article : articles) {
            String headerStart = article.getDate() + "\n" + article.getHeader() + "\n" + article.getData() + "\n";
            headerStart = headerStart.substring(0, 15);
            BookmarksFullArticle.headerFulArticleToLoad = BookmarksFullArticle.headerFulArticleToLoad.substring(0, 15);
            if (headerStart.equals(BookmarksFullArticle.headerFulArticleToLoad)) {
                article.setIsBookMarked("No");
                db.updateArticle(article);
                return true;
            }
        }
        return false;
    }

    public void onSwipeLeft() {
        getSpeak().onDestroy();
        getCurrTouchListener().getActivity().finish();
        Log.i(Messages.LOG_TAG_GestureListener, "onSwipeLeft: ");
    }

    public void onSwipeBottom() {
        Log.i(Messages.LOG_TAG_GestureListener, "onSwipeBottom: ");
        Activity currActivity = currTouchListener.getActivity();

        if (currActivity instanceof Home) {
            speak.speak(currTouchListener.getContext().getString(R.string.intro_long_or_short));
        } else if (currActivity instanceof Ticker) {
            speak.speak(currTouchListener.getContext().getString(R.string.TICKER_FIRST_USE_SWIPE_DOWN));
        } else if (currActivity instanceof TickerFullArticle) {
            speak.speak(currTouchListener.getContext().getString(R.string.TICKER_FULL_ARTICLE_FIRST_USE_SWIPE_DOWN));
        } else if (currActivity instanceof Bookmarks) {
            speak.speak(currTouchListener.getContext().getString(R.string.BOOKMARKS_FIRST_USE_SWIPE_DOWN));
        } else if (currActivity instanceof BookmarksFullArticle) {
            speak.speak(currTouchListener.getContext().getString(R.string.BOOKMARKS_FULL_ARTICLE_FIRST_USE_SWIPE_DOWN));
        } else if (currActivity instanceof News) {
            speak.speak(currTouchListener.getContext().getString(R.string.NEWS_FIRST_USE_SWIPE_DOWN));
        } else if (currActivity instanceof NewsShortArticle) {
            speak.speak(currTouchListener.getContext().getString(R.string.NEWS_SHORT_FIRST_USE_SWIPE_DOWN));
        } else if (currActivity instanceof NewsFullArticle) {
            speak.speak(currTouchListener.getContext().getString(R.string.NEWS_FULL_ARTICLE_FIRST_USE_SWIPE_DOWN));
        }
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.i(Messages.LOG_TAG_GestureListener, "onLongPress: " + e.toString());
        if (vibrate()) {
            drag();
        }
        super.onLongPress(e);
    }

    public boolean vibrate() {
        Vibrator vb = (Vibrator) currTouchListener.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vb.vibrate(100);
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(Messages.LOG_TAG_GestureListener, "doubleTap: " + e.toString());
        if (getSpeak().isSpeaking()) {
            getSpeak().stopReading();
        }
        Utils.goToActivity(currTouchListener.getContext(), Home.class);
        return true;
    }


    public void goToHomeScreen() {
        Log.d(Messages.LOG_TAG_GestureListener, "goToHomeScreen: ");
        speak.speak(currTouchListener.getContext().getString(R.string.HOME));
        speak.onDestroy();
        Intent intent = new Intent(currTouchListener.getContext(), getHomeScreenClass());
        currTouchListener.getActivity().startActivity(intent);
    }

    private Class getHomeScreenClass() {
        return Home.class;
    }

    private void drag() {
        Log.i(Messages.LOG_TAG_GestureListener, "drag: ");
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                currTouchListener.getView());
        currTouchListener.getView().startDrag(data, shadowBuilder, currTouchListener.getView(), 0);
        currTouchListener.getView().setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.i(Messages.LOG_TAG_GestureListener, "onSingleTapConfirmed");
        Activity currActivity = currTouchListener.getActivity();
        Context currContext = (Context) currActivity;

        if (currActivity instanceof Home) {
            handleSpeaking(" ");
        } else if (currActivity instanceof Ticker || currActivity instanceof Bookmarks
                || currActivity instanceof News || currActivity instanceof NewsShortArticle) {

            handleSpeaking(" ");
            /*
             *
             * NOTHING YET
             *
             * */
        } else if (currActivity instanceof TickerFullArticle || currActivity instanceof NewsFullArticle || currActivity instanceof BookmarksFullArticle) {
            View rootView = Utils.getRootView(getCurrTouchListener().getActivity());
            if (rootView.findViewById(R.id.ArticleTextView) != null) {
                String textViewName = rootView.getResources().getResourceEntryName(R.id.ArticleTextView);
                int id = Utils.getIdFromTextView(getCurrTouchListener().getContext(), textViewName);
                if (isTextViewID(id) && Utils.isTextView(rootView, id)) {
                    TextView tv = (TextView) rootView.findViewById(id);
                    final String readTextView = tv.getText().toString();
                    handleSpeaking(readTextView);
                }
            }
        }

        return super.onSingleTapConfirmed(e);
    }

    public boolean isTextViewID(int id) {
        return id == NO_TEXT_VIEW_ID ? false : true;
    }

    public void handleSpeaking(String speakText) {
        if (!speakText.isEmpty()) {
            if (getSpeak().isSpeaking()) {
                getSpeak().stopReading();
            } else {
                getSpeak().speak(speakText);
            }
        }
    }

    public void stopSpeakIfSpeaking() {
        if (getSpeak().isSpeaking()) {
            getSpeak().stopReading();
        }
    }
}



