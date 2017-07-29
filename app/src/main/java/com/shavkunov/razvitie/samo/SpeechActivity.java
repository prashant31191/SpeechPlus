package com.shavkunov.razvitie.samo;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SpeechActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SpeechFragment.newInstance();
    }
}
