package com.example.gabrielsilveira.bizu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by gabrielsilveira on 14/04/16.
 */
public class ToggleableRadioButton extends RadioButton {

    public ToggleableRadioButton(Context context){
        super(context);
    }

    public ToggleableRadioButton(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public ToggleableRadioButton(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    @Override
        public void toggle() {
            if(isChecked()) {
                if(getParent() instanceof RadioGroup) {
                    ((RadioGroup)getParent()).clearCheck();
                } else {
                    setChecked(false);
                }
            } else {
                setChecked(true);
            }
        }
}

