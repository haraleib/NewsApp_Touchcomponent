package at.nachrichten.newsapp.article;

import android.content.Context;
import android.content.res.Resources;
import android.preference.PreferenceActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.nachrichten.newsapp.R;

/**
 * Created by Harald on 17.01.2018.
 */

public class ArticleList {
    public List<Article> articleList;
    private int id = 0;
    private Context context;

    public ArticleList(Context context){
        articleList = new ArrayList<Article>();
        this.context = context;
        setUpArticles();
    }

    public void setUpArticles(){
        Article a1 = new Article(id++,getDate(), context.getString(R.string.cat_sports_header1), context.getString(R.string.cat_sports_data1), "Sports", "Sport", "No");
        articleList.add(a1);
        Article a2 = new Article(id++, getDate(), context.getString(R.string.cat_sports_header2), context.getString(R.string.cat_sports_data2), "Sports", "Sport", "No");
        articleList.add(a2);
        Article a3 = new Article(id++, getDate(), context.getString(R.string.cat_economy_header1), context.getString(R.string.cat_economy_data1),"Economy", "Wirtschaft", "No");
        articleList.add(a3);
        Article a4 = new Article(id++, getDate(), context.getString(R.string.cat_economy_header2), context.getString(R.string.cat_economy_data2) ,"Economy", "Wirtschaft","No");
        articleList.add(a4);
        Article a5 = new Article(id++, getDate(), context.getString(R.string.cat_politics_header1), context.getString(R.string.cat_politics_data1) ,"Politics", "Politik","No");
        articleList.add(a5);
        Article a6 = new Article(id++, getDate(), context.getString(R.string.cat_policits_header2), context.getString(R.string.cat_policits_data2) ,"Politics", "Politik","No");
        articleList.add(a6);
        Article a8 = new Article(id++,getDate(),context.getString(R.string.cat_lifestyle_header1), context.getString(R.string.cat_lifestyle_data1),"Lifestyle", "Lifestyle", "No");
        articleList.add(a8);
        Article a9 = new Article(id++, getDate(), context.getString(R.string.cat_lifestyle_header2), context.getString(R.string.cat_lifestyle_data2) ,"Lifestyle", "Lifestyle", "No");
        articleList.add(a9);
    }

    public String getDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy ");
        String format = context.getString(R.string.date) + ": " + formatter.format(date);
        return format;
    }
}
