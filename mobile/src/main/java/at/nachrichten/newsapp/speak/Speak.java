package at.nachrichten.newsapp.speak;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.nachrichten.newsapp.messages.Messages;
import at.nachrichten.newsapp.utils.Utils;

/**
 * Created by Harald Eibensteiner
 * Matr: k01300179
 *
 * This class converts text to speech.
 * Every class that uses Speak, needs to initialize an Object from this class at first.
 * Usage: Initialise a Speak object. call speak method which calls a new thread who is responsible for
 * get the text to audio output
 */

public class Speak {

    private static TextToSpeech tts;
    private Context context;
    private String text;
    private static boolean introductionDone = false;
    private final int MAXLENGTH = 300;

    public Speak(final Context context) {
        this.context = context;
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    Locale language = new Locale(Resources.getSystem().getConfiguration().locale.getLanguage());
                    int result = 0;
                    if (tts.isLanguageAvailable(language) == 0) {
                        result = tts.setLanguage(language);
                    } else {
                        result = tts.setLanguage(tts.getDefaultLanguage());
                        tts.setLanguage(Locale.GERMANY);
                    }
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e(Messages.LOG_TAG_SPEAK, "This Language is not supported");
                    } else {
                        Log.i(Messages.LOG_TAG_SPEAK, "Initialized success");
                        Utils.doSpeakEntryText(context, getSpeak());
                    }

                } else {
                    Log.e(Messages.LOG_TAG_SPEAK, "Initialization Failed!");
                }
            }
        });
    }

    Speak getSpeak() {
        return this;
    }

    public void speak(String text) {
        if (text.length() > 3999) {
            doSpeakLongText(text);
        } else {
            doSpeakShortText(text);
        }
    }


    public void doSpeakShortText(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void doSpeakLong(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
    }

    public void doSpeakLongText(String text) {
        ///
        Pattern re = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)", Pattern.MULTILINE | Pattern.COMMENTS);
        Matcher reMatcher = re.matcher(text);
        /////
        int position = 0;
        int sizeOfChar = text.length();
        String testString = text.substring(position, sizeOfChar);
        while (reMatcher.find()) {
            String temp = "";

            try {
                temp = testString.substring(text.lastIndexOf(reMatcher.group()), text.indexOf(reMatcher.group()) + reMatcher.group().length());
                doSpeakLong(temp);

            } catch (Exception e) {
                temp = testString.substring(0, testString.length());
                doSpeakLong(temp);
                break;

            }
        }
    }

    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    public void stopReading() {
        if (tts != null && tts.isSpeaking()) {
            tts.stop();
        }
    }


    public boolean isSpeaking() {
        return tts.isSpeaking();
    }

}
