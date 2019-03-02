package at.nachrichten.newsapp.listener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.Util;

import at.nachrichten.newsapp.Home;
import at.nachrichten.newsapp.R;
import at.nachrichten.newsapp.messages.Messages;
import at.nachrichten.newsapp.utils.Utils;

/**
 * Created by hei on 20.10.2017.
 */

public class DragListenerHome extends DragListener {

    public DragListenerHome(Context context) {
        super(context);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        //View v = current View
        int action = event.getAction();
        View rootView = Utils.getRootView(getActivity());
        View navigatonComponent = (View) event.getLocalState();

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackgroundColor(Color.GRAY);

                if (rootView.findViewById(v.getId()) != null) {
                    String textViewName = rootView.getResources().getResourceEntryName(v.getId());
                    int id = Utils.getIdFromTextView(getContext(), textViewName);
                    if (isTextViewID(id) && Utils.isTextView(rootView, id)) {
                        TextView tv = (TextView) rootView.findViewById(id);
                        final String readTextView = tv.getText().toString();
                        getSpeak().speak(readTextView);
                    }
                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.home_screen_border));
                break;
            case DragEvent.ACTION_DROP:
                String clazzName = " ";
                String packageName = " ";
                String fullName = " ";
                Class clazz = null;

                getSpeak().stopReading();

                if (v instanceof TextView) {
                    clazzName = rootView.getResources().getResourceEntryName(v.getId());
                    packageName = getActivity().getBaseContext().getApplicationContext().getPackageName();
                    fullName = packageName + "." + clazzName;
                    try {
                        clazz = Class.forName(fullName);
                        if(clazzName.equals("Login")){
                            Utils.goToActivity(getContext(), Home.class);
                        }else {
                            Utils.goToActivity(getContext(), clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        Log.v("Class not found", e.getMessage());
                        Toast toast = Toast.makeText(getContext(), "Activity Not Found -> Errorin: D ragListener", Toast.LENGTH_SHORT);
                        toast.show();
                        Utils.goToActivity(getContext(), Home.class);
                    }
                    navigatonComponent.setVisibility(View.VISIBLE);
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                v.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.home_screen_border));
                navigatonComponent.setVisibility(View.VISIBLE);
            default:
                break;
        }
        return true;
    }
}
