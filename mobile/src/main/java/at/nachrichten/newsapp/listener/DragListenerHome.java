package at.nachrichten.newsapp.listener;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import at.nachrichten.newsapp.Home;
import at.nachrichten.newsapp.R;
import at.nachrichten.newsapp.utils.Utils;

/**
 * Created by Harald Eibensteiner
 * Matr: k01300179
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
                v.setBackgroundColor(Color.DKGRAY);

                if (rootView.findViewById(v.getId()) != null) {
                    String textViewName = rootView.getResources().getResourceEntryName(v.getId());
                    int id = Utils.getIdFromTextView(getContext(), textViewName);
                    if (isTextViewID(id) && Utils.isTextView(rootView, id)) {
                        TextView tv = (TextView) rootView.findViewById(id);
                        tv.setTextColor(Color.WHITE);
                        final String readTextView = tv.getText().toString();
                        getSpeak().speak(readTextView);
                    }
                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.home_screen_border));
                String textViewName = rootView.getResources().getResourceEntryName(v.getId());
                int id = Utils.getIdFromTextView(getContext(), textViewName);
                if (isTextViewID(id) && Utils.isTextView(rootView, id)) {
                    TextView tv = (TextView) v;
                    tv.setTextColor(Color.BLACK);
                }
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
                        if (clazzName.equals("Login")) {
                            Utils.goToActivity(getContext(), Home.class);
                        } else {
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
                if (v != null && v instanceof TextView) {
                    TextView tv = (TextView) v;
                    tv.setTextColor(Color.BLACK);
                }
            default:
                break;
        }
        return true;
    }
}
