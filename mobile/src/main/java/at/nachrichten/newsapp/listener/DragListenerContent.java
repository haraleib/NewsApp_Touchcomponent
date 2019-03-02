package at.nachrichten.newsapp.listener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import at.nachrichten.newsapp.Bookmarks;
import at.nachrichten.newsapp.BookmarksFullArticle;
import at.nachrichten.newsapp.Home;
import at.nachrichten.newsapp.News;
import at.nachrichten.newsapp.NewsFullArticle;
import at.nachrichten.newsapp.NewsShortArticle;
import at.nachrichten.newsapp.R;
import at.nachrichten.newsapp.Ticker;
import at.nachrichten.newsapp.TickerFullArticle;
import at.nachrichten.newsapp.async.TickerHandlerFullArticle;
import at.nachrichten.newsapp.messages.Messages;
import at.nachrichten.newsapp.utils.Utils;

/**
 * Created by Harald on 07.12.2017.
 */

public class DragListenerContent extends DragListener {

    public DragListenerContent(Context context) {
        super(context);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        //View v = current View
        int action = event.getAction();
        View rootView = Utils.getRootView(getActivity());
        View navigationComponent = (View) event.getLocalState();

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackgroundColor(Color.LTGRAY);

                //due to we haven't a ID on TextViews of Ticker, because they are autogenerated, we can not search if ID exists in rootView
                if (v != null && v instanceof TextView) {
                    TextView tv = (TextView) v;
                    if (!tv.getText().toString().isEmpty()) {
                        final String readTextView = tv.getText().toString();
                        getSpeak().speak(readTextView);
                    }
                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ticker_newsfeed_border));
                break;
            case DragEvent.ACTION_DROP:
                /*
                * TextView in which the content is stored have the same name in Ticker and TickerFullArticle
                * */
                getSpeak().stopReading();

                if (v instanceof TextView) {

                    String textViewName = "";
                    try {
                        textViewName = rootView.getResources().getResourceEntryName(v.getId());
                    } catch (Exception e) {
                        Log.d(null, "No Id found in View");
                    }

                   if (textViewName.equals(Messages.TICKER_TICKERFULLARTICLE_BACK)) {

                      chooseRightActivityToGo();

                    }else if (Utils.classEqualsOtherClass(getContext(), Ticker.class)) {
                        //TextView in Ticker
                           String contentKey = ((TextView) v).getText().toString();
                           TickerHandlerFullArticle.setUpUrl(contentKey);
                           new TickerHandlerFullArticle().execute();
                           Utils.goToActivity(getContext(), TickerFullArticle.class);
                           //Back TextView in TickerFullArticle

                   } else if (Utils.classEqualsOtherClass(getContext(), News.class)) {
                       //TextView in Ticker
                       String cateGoryChoosed = ((TextView) v).getText().toString();
                       News.setCategoryChoosed(cateGoryChoosed);
                       Utils.goToActivity(getContext(), NewsShortArticle.class);
                   } else if(Utils.classEqualsOtherClass(getContext(), NewsShortArticle.class)){

                       for(String header : NewsShortArticle.getShortArticleHeader()){
                           if(header.equals(((TextView) v).getText().toString())){
                               NewsFullArticle.setHeaderFulArticleToLoad(header);
                           }
                       }
                       Utils.goToActivity(getContext(), NewsFullArticle.class);
                   } else if(Utils.classEqualsOtherClass(getContext(), Bookmarks.class)){

                       for(String header : Bookmarks.getShortArticleHeader()){
                           if(header.equals(((TextView) v).getText().toString())){
                               BookmarksFullArticle.setHeaderFulArticleToLoad(header);
                           }
                       }
                       Utils.goToActivity(getContext(), BookmarksFullArticle.class);
                   }

                    navigationComponent.setVisibility(View.VISIBLE);
                    break;
                }
            case DragEvent.ACTION_DRAG_ENDED:
                v.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ticker_newsfeed_border));
                navigationComponent.setVisibility(View.VISIBLE);
            default:
                break;
        }
        return true;
    }

    public void chooseRightActivityToGo(){
        if(Utils.classEqualsOtherClass(getContext(), Ticker.class)){
            Utils.goToActivity(getContext(), Home.class);
        }else if (Utils.classEqualsOtherClass(getContext(), TickerFullArticle.class)) {
            Utils.goToActivity(getContext(), Ticker.class);
        }else if (Utils.classEqualsOtherClass(getContext(), News.class)) {
            Utils.goToActivity(getContext(), Home.class);
        }else if (Utils.classEqualsOtherClass(getContext(), NewsShortArticle.class)) {
            Utils.goToActivity(getContext(), News.class);
        }else if (Utils.classEqualsOtherClass(getContext(), NewsFullArticle.class)) {
            Utils.goToActivity(getContext(), NewsShortArticle.class);
        }else if (Utils.classEqualsOtherClass(getContext(), Bookmarks.class)) {
            Utils.goToActivity(getContext(), Home.class);
        }else if (Utils.classEqualsOtherClass(getContext(), BookmarksFullArticle.class)) {
            Utils.goToActivity(getContext(), Bookmarks.class);
        }
    }
}
