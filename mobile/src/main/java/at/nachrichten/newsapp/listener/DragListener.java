package at.nachrichten.newsapp.listener;

/**
 * Created by Harald on 07.12.2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.DragEvent;
import android.view.View;

import at.nachrichten.newsapp.speak.Speak;

/**
 * Created by hei on 20.10.2017.
 */

public class DragListener implements View.OnDragListener {

    private Activity activity;
    private Context context;
    private Speak speak;
    private final int NO_TEXT_VIEW_ID = 0;

    public DragListener() {

    }

    public DragListener(Context context) {
        this.activity = (Activity) context;
        this.context = context;
        initSpeak();
    }

    public Activity getActivity() {
        return activity;
    }

    public Context getContext() {
        return context;
    }

    public Speak getSpeak() {
        return speak;
    }

    public boolean isTextViewID(int id) {
        return id  == NO_TEXT_VIEW_ID ? false : true;
    }

    public boolean onDrag(View v, DragEvent event) {
        return true;
    }

    public void initSpeak(){
        this.speak = new Speak(context);
    }
}

