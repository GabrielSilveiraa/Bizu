package com.example.gabrielsilveira.bizu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.NavUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by gabrielsilveira on 13/04/16.
 */
public final class Utilities {

    public static void goUpActivity(Activity activity){
        NavUtils.navigateUpFromSameTask(activity);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void setFontNexa(Context context, View view){
        String fontPath = "fonts/Nexa Light.ttf";
        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);

        String fontPathBold = "fonts/Nexa Bold.ttf";
        Typeface tfBold = Typeface.createFromAsset(context.getAssets(), fontPathBold);

        if(view instanceof TextView) {
            if (((TextView) view).getTypeface() != null && ((TextView) view).getTypeface().isBold()) {
                ((TextView) view).setTypeface(tfBold);
            }
            else {
                ((TextView) view).setTypeface(tf);
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setFontNexa(context, innerView);
            }
        }
    }

//    public static void setFontNexa(Context context, TextView textView){
//
//        // Font path
//        String fontPath = "fonts/Nexa Bold.ttf";
//
//        // Loading Font Face
//        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);
//
//        // Applying font
//        textView.setTypeface(tf);
//    }

//    public static void setFontNexa(Context context, EditText editText){
//
//        // Font path
//        String fontPath = "fonts/Nexa Light.ttf";
//
//        // Loading Font Face
//        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);
//
//        // Applying font
//        editText.setTypeface(tf);
//    }


}
