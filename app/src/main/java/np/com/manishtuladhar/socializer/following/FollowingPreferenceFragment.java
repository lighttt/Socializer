package np.com.manishtuladhar.socializer.following;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import np.com.manishtuladhar.socializer.R;

public class FollowingPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "FollowingPreferenceFrag";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.following_preferences, rootKey);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if(preference!=null && preference instanceof SwitchPreferenceCompat)
        {
            boolean isOn = sharedPreferences.getBoolean(key,false);
            if(isOn)
            {
                FirebaseMessaging.getInstance().subscribeToTopic(key);
                Log.e(TAG, "Subscribing to "+key);
            }
            else{
                FirebaseMessaging.getInstance().unsubscribeFromTopic(key);
                Log.e(TAG, "un-subscribing to "+key);
            }
        }
    }
}