package com.chl.stickynav;

import android.content.Context;

/**
 * Created by caihanlin on 17/3/1.
 */

public class Util {

    public static  float dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return scale * dp + 0.5f;
    }
}
