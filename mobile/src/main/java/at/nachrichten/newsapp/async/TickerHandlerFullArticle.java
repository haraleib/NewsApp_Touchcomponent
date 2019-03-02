package at.nachrichten.newsapp.async;

import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import at.nachrichten.newsapp.utils.Utils;

/**
 * Created by Harald on 07.12.2017.
 */

public class TickerHandlerFullArticle extends TickerHandlerShortArticle {

    private static boolean fetched;
    private static String url = "http://www.nachrichten.at";
    private static TextView articleLoaded;

    public static String getContent() {
        return content;
    }

    private static String content;

    public TickerHandlerFullArticle() {

    }

    @Override
    protected String doInBackground(String... strings) {
        fetched = false;
        try {
            Document jdoc = Jsoup.connect(url).get();
            Elements contentTag = jdoc.select("div").attr("class", "artikeldetail");
            Elements artikelHead = contentTag.select("h2").attr("class", "artikeldetailhead_title");
            Elements leadText = contentTag.select("h3").attr("class", "leadtext textsize");
            Elements mainContentTag = jdoc.select("p");
            List<String> mainContentText = mainContentTag.eachText();
            content = mainContentText.get(1) + " " + mainContentTag.get(2);
            fetched = !fetched;
        } catch (IOException e1) {
            fetched = false;
            e1.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (articleLoaded != null) {
            CharSequence content = getContent();
            articleLoaded.setText(content);
        }
        fetched = true;
    }

    public static void setTextViewToSetContent(View textViewToSetContent) {
        articleLoaded = (TextView) textViewToSetContent;
        if (fetched) {
            CharSequence content = getContent();
            articleLoaded.setText(content);
        }
    }

    public static void setUpUrl(String content) {
        url += TickerHandlerShortArticle.getContentMap().get(content)[1];
    }
}
