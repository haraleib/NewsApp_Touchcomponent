package at.nachrichten.newsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.nachrichten.newsapp.article.Article;
import at.nachrichten.newsapp.article.ArticleList;

import static android.provider.Contacts.SettingsColumns.KEY;

/**
 * Created by Harald on 17.01.2018.
 */

public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 6;
    // Database Name
    private static final String DATABASE_NAME = "articleInfo";
    // Contacts table name
    private static final String TABLE_ARTICLE = "article";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_HEADER = "header";
    private static final String KEY_DATA = "data";
    private static final String KEY_CATEGORY_ENGL = "category_engl";
    private static final String KEY_CATEGORY_GER = "category_ger";
    private static final String KEY_IS_BOOKMARKED = "bookmarked";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_ARTICLE + "("
        + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT,"
        + KEY_HEADER + " TEXT, " + KEY_DATA + " TEXT,  "  + KEY_CATEGORY_ENGL + " TEXT , " + KEY_CATEGORY_GER + " TEXT , "  + KEY_IS_BOOKMARKED + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);
    // Creating tables again
        onCreate(db);
    }

    public void dropTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + DATABASE_NAME + "." + TABLE_ARTICLE);
    }

    public void addArticle(Article article){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, article.getDate().toString());
        values.put(KEY_HEADER, article.getHeader());
        values.put(KEY_DATA, article.getData());
        values.put(KEY_CATEGORY_ENGL, article.getCategoryEng());
        values.put(KEY_CATEGORY_GER, article.getCategoryGer());
        values.put(KEY_IS_BOOKMARKED, article.getIsBookMarked());
        // Inserting Row
        db.insert(TABLE_ARTICLE, null, values);
        db.close(); // Closing database connection
    }

    public void addArticlesFromList(ArticleList articleList){
        List<Article> articlesToStore = articleList.articleList;
        for(Article article : articlesToStore){
            addArticle(article);
        }
    }

    // Getting one shop
    public Article getSArticle(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ARTICLE, new String[] { KEY_ID,
                        KEY_DATA, KEY_HEADER, KEY_DATA}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Article article = new Article(Integer.parseInt(cursor.getString(0)),
                (cursor.getString(1)), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        return article;
    }

    public List<Article> getAllArticles() {
        List<Article> articleList = new ArrayList<Article>();
        String selectQuery = "SELECT * FROM " + TABLE_ARTICLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Article article = new Article(0, null, null, null, null, null, null);
                article.setId(Integer.parseInt(cursor.getString(0)));
                article.setDate((cursor.getString(1)));
                article.setHeader(cursor.getString(2));
                article.setData(cursor.getString(3));
                article.setCategoryEng(cursor.getString(4));
                article.setCategoryGer(cursor.getString(5));
                article.setIsBookMarked(cursor.getString(6));
                articleList.add(article);
            } while (cursor.moveToNext());
        }
        return articleList;
    }

    // Updating a article
    public int updateArticle(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, article.getId());
        values.put(KEY_DATE, article.getDate().toString());
        values.put(KEY_HEADER, article.getHeader());
        values.put(KEY_DATA, article.getData());
        values.put(KEY_CATEGORY_ENGL, article.getCategoryEng());
        values.put(KEY_CATEGORY_GER, article.getCategoryGer());
        values.put(KEY_IS_BOOKMARKED, article.getIsBookMarked());
        // updating row
        return db.update(TABLE_ARTICLE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(article.getId())});
    }

    // Deleting a article
    public void deleteShop(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ARTICLE, KEY_ID + " = ?",
                new String[] { String.valueOf(article.getId()) });
        db.close();
    }
}
