package com.shapira.collectorscanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.shapira.collectorscanner.login.LoginActivity;

public class StartMyActivityAtBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            Intent myStarterIntent = new Intent(context, LoginActivity.class);
            myStarterIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myStarterIntent);

        }
    }
}
