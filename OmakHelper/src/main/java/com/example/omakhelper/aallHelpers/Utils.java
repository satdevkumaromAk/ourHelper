package com.example.omakhelper.aallHelpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.omakhelper.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * Utils class used in MDC-111 application.
 */
public abstract class Utils {

    /**
     * Set pointer to end of text in edittext when user clicks Next on KeyBoard.
     */
    public static View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (b) {
                ((TextInputEditText) view).setSelection(((TextInputEditText) view).getText().length());
            }
        }
    };

    public static <T extends View> List<T> findViewsWithType(View root, Class<T> type) {
        List<T> views = new ArrayList<>();
        findViewsWithType(root, type, views);
        return views;
    }

    private static <T extends View> void findViewsWithType(View view, Class<T> type, List<T> views) {
        if (type.isInstance(view)) {
            views.add(type.cast(view));
        }

        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                findViewsWithType(viewGroup.getChildAt(i), type, views);
            }
        }
    }

    //  Request Focus Method :-
    public static <T extends View> void setOnFocustListener(View root, Class<T> type) {
        List<T> views = new ArrayList<>();
        findViewsWithType(root, type, views);

        for (T textInputEditText : views) {
            textInputEditText.setOnFocusChangeListener(onFocusChangeListener);
        }
    }

    //Cheking Edit Text field Empty or not :-
    public static <T extends View> boolean is_valid_til(View rootView, Class<T> type) {
        final List<TextInputLayout> textInputLayouts = findViewsWithType(rootView, TextInputLayout.class);

        boolean noErrors = true;
        for (TextInputLayout textInputLayout : textInputLayouts) {
            String editTextString = textInputLayout.getEditText().getText().toString();
            if (editTextString.isEmpty()) {
                textInputLayout.setError("Field must not be empty.");
                noErrors = false;
            } else {
                textInputLayout.setError(null);
            }
        }

        return noErrors;
    }

    //Cheking Edit Text View field Empty or not :-
    public static <T extends View> boolean is_valid_edit_textView(View rootView, Class<T> type) {
        final List<EditText> textInputLayouts = findViewsWithType(rootView, EditText.class);

        boolean noErrors = true;
        for (EditText textInputLayout : textInputLayouts) {
            String editTextString = textInputLayout.getText().toString();
            if (editTextString.isEmpty()) {
                textInputLayout.setError("Field must not be empty.");
                noErrors = false;
            } else {
                textInputLayout.setError(null);
            }
        }

        return noErrors;
    }

    public static String getTitleFromLabel(String string) {
        string = string.replace("-", " ");
        //string = capitalize(string);
        return string;
    }

    public static String formatTime(String datetime) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("d MMM, yyyy HH:mm");
        try {
            Date dateObject = inputFormat.parse(datetime);
            datetime = outputFormat.format(dateObject);
        } catch (ParseException e) {
            // e.printStackTrace();
        }

        return datetime;
    }

    public static String formatTimeForQuery(String datetime) {
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateObject = inputFormat.parse(datetime);
            datetime = outputFormat.format(dateObject);
        } catch (ParseException e) {
            // e.printStackTrace();
        }

        return datetime;
    }



    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

