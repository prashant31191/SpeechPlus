package com.shavkunov.razvitie.samo;

import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;
import android.view.View;

public class SettingsHideAndShowFab {

    private float touchDown;
    private float touchUp;
    private FloatingActionButton fabTwisters;

    public SettingsHideAndShowFab(FloatingActionButton fabTwisters) {
        this.fabTwisters = fabTwisters;
    }

    public void getTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                touchUp = event.getY();
                break;
        }

        if (touchDown > touchUp && fabTwisters.getVisibility() == View.VISIBLE) {
            fabTwisters.hide();
        } else if (touchDown < touchUp && fabTwisters.getVisibility() != View.VISIBLE) {
            fabTwisters.show();
        }
    }
}
