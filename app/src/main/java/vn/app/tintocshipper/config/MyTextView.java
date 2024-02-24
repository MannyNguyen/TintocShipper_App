package vn.app.tintocshipper.config;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import vn.app.tintocshipper.R;

/**
 * Created by Admin on 7/25/2017.
 */

public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context,attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomFontTextView);
        int cf = a.getInteger(R.styleable.CustomFontTextView_font_family, 0);
        int fontName = 0;
        switch (cf)
        {
            case 1:
                fontName = R.string.segoe_light;
                break;
            case 2:
                fontName = R.string.segoe_regular;
                break;
            case 3:
                fontName = R.string.segoe_semi_bold;
                break;
            case 4:
                fontName = R.string.segoe_italic;
                break;
            case 5:
                fontName = R.string.segoe_bold;
                break;

            default:
                fontName = R.string.segoe_regular;
                break;
        }

        String customFont = getResources().getString(fontName);

        Typeface tf = Typeface.createFromAsset(context.getAssets(),customFont + ".ttf");
        setTypeface(tf);
        a.recycle();
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), getResources().getString(R.string.segoe_regular) + ".tff");
        setTypeface(customFont);
    }
}
