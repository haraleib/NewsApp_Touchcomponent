package at.nachrichten.newsapp.async;

import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

/**
 * Created by Harald Eibensteiner
 * Matr: k01300179
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
            Document jSoup = Jsoup.connect(url).get();
            Elements mainContentTag = jSoup.select("p");
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
            if(content.length() > 751){
                content = ((String) content).substring(0, 750);
                content = ((String) content).substring(0,((String) content).lastIndexOf(".") + 1);
            }
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
