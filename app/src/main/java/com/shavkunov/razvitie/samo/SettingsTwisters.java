package com.shavkunov.razvitie.samo;

import android.app.Dialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;

public class SettingsTwisters extends AAH_FabulousFragment {

    public static SettingsTwisters newInstance() {
        return new SettingsTwisters();
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragment_settings_twisters, null);
        RelativeLayout rlContent = (RelativeLayout) contentView.findViewById(R.id.rl_content);
        LinearLayout llButtons = (LinearLayout) contentView.findViewById(R.id.ll_buttons);

        //params to set
        setAnimationDuration(250); //optional; default 500ms
        setPeekHeight(400); // optional; default 400dp
        setViewgroupStatic(llButtons); // optional; layout to stick at bottom on slide
        setViewMain(rlContent); //necessary; main bottomsheet view
        setMainContentView(contentView); // necessary; call at end before super
        super.setupDialog(dialog, style); //call super at last
    }
}
