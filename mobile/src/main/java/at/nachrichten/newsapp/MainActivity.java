package at.nachrichten.newsapp;

import android.app.Activity;
import android.os.Bundle;

import at.nachrichten.newsapp.listener.DragListener;
import at.nachrichten.newsapp.listener.TouchListener;

/**
 * Created by Harald Eibensteiner
 * Matr: k01300179
 */


public abstract class MainActivity extends Activity {

    public DragListener dragListener;
    public TouchListener touchListener;

    public void MainActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
