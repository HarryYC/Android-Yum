package app.team3.t3;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

/**
 * Created by sssbug on 7/29/15.
 */
public class myAutoCompleteTextView extends AutoCompleteTextView {
    public myAutoCompleteTextView(Context context) {
        super(context);
    }

    public myAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public myAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public myAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP) {
            ImageButton ib = (ImageButton) ((Activity)getContext()).findViewById(R.id.shake_ImageButton);
            ib.requestFocus();
            return false;
        }
        return super.onKeyPreIme(keyCode, event);
    }
}
