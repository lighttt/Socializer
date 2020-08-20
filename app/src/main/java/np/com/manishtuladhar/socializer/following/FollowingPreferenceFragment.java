package np.com.manishtuladhar.socializer.following;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import np.com.manishtuladhar.socializer.R;

public class FollowingPreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.following_preferences, rootKey);
    }
}