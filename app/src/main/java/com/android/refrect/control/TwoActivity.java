package com.android.refrect.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.refrect.model.Bike;

/**
 * Created by yulong.liu on 2016/12/14.
 */

public class TwoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        Bike bike = i.getParcelableExtra("aaa");

    }
}
