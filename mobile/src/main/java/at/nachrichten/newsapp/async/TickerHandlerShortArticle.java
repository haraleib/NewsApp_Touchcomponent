package at.nachrichten.newsapp.async;

import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import at.nachrichten.newsapp.messages.Messages;

/**
 * Created by Harald on 07.12.2017.
 */

public class TickerHandlerShortArticle extends ContentHandler {
    private final String URL = "http://www.nachrichten.at/nachrichten/ticker/";
    //key is content, necessary to map conent to link to find correct article. String [0] content, [1] time, [2] link
    private static HashMap<String, String[]> contentMap;
    private static boolean fetched = false;
    private List<String> headlineList;
    private List<String> timeList;
    private List<String> linksList;

    public static HashMap<String, String[]> getContentMap() {
        return contentMap;
    }

    public static boolean isExecuted() {
        return fetched;
    }

    private List<String> getHeadlineList() {
        return headlineList;
    }

    private List<String> getTimeList() {
        return timeList;
    }

    private List<String> getLinksList() {
        return linksList;
    }

    @Override
    protected String doInBackground(String... strings) {
        fetched = false;
        try {
            Document jdoc = Jsoup.connect(URL).get();
            Elements headlineTag = jdoc.select("h2").attr("class", "textsize uticker_headline");
            Elements timeTag = jdoc.select("span.uticker_time");
            Elements linksTag = headlineTag.select("a");
            headlineList = headlineTag.eachText();
            timeList = timeTag.eachText();
            linksList = linksTag.eachAttr("href");
        } catch (IOException e1) {
            fetched = false;
            e1.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        buildcontentMap();
        fetched = true;

    }

    private void buildcontentMap() {
        contentMap = new HashMap<String, String[]>();
        Iterator<String> iterHeadline = getHeadlineList().iterator();
        Iterator<String> iterTime = getTimeList().iterator();
        Iterator<String> iterLink = getLinksList().iterator();
        while (iterHeadline.hasNext() && iterTime.hasNext() && iterLink.hasNext()) {
            String[] content = new String[2];
            content[0] = Messages.TICKER_TIME + iterTime.next() + '\n' + iterHeadline.next();
            // content[0] =  iterHeadline.next();
            // content[1] = iterTime.next();
            content[1] = iterLink.next();
            contentMap.put(content[0], content);
        }

    }


}
