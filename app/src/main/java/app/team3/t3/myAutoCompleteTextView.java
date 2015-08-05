package app.team3.t3;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
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
