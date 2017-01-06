package com.example.android.sunshine.app.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Sync adapter framework to access the authenticator
 */
public class SunshineAuthenticatorService extends Service {
    private SunshineAuthenticator auth;

    @Override
    public void onCreate() {
        auth = new SunshineAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return auth.getIBinder();
    }
}
