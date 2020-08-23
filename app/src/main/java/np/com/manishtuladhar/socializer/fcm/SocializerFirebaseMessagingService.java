package np.com.manishtuladhar.socializer.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import np.com.manishtuladhar.socializer.MainActivity;
import np.com.manishtuladhar.socializer.R;
import np.com.manishtuladhar.socializer.following.FollowingPreferenceActivity;
import np.com.manishtuladhar.socializer.provider.SocializerContract;
import np.com.manishtuladhar.socializer.provider.SocializerProvider;

public class SocializerFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "SocializerFCM";

    //json keys
    private static final String JSON_KEY_AUTHOR = SocializerContract.COLUMN_AUTHOR;
    private static final String JSON_KEY_AUTHOR_KEY = SocializerContract.COLUMN_AUTHOR_KEY;
    private static final String JSON_KEY_MESSAGE = SocializerContract.COLUMN_MESSAGE;
    private static final String JSON_KEY_DATE = SocializerContract.COLUMN_DATE;

    @Override
    public void onNewToken(@NonNull String s) {
        Log.e(TAG, "onNewToken: the token is" + s);
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //print the messages got
        Log.e(TAG, "onMessageReceived: " + remoteMessage.toString());
        Log.e(TAG, "onMessageReceived: " + remoteMessage.getFrom());

        //parse the data got
        Map<String, String> data = remoteMessage.getData();
        if (data.size() > 0) {
            Log.e(TAG, "onMessageReceived: " + data);
            sendNotification(data);
            //insertToSocializer(data);
        }
    }

    /**
     * Insert the coming data to the database;
     */
    private void insertToSocializer(final Map<String, String> data) {
        //database operation doing on new thread
        AsyncTask<Void, Void, Void> insertToSocializerTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ContentValues newMessage = new ContentValues();
                newMessage.put(SocializerContract.COLUMN_AUTHOR, data.get(JSON_KEY_AUTHOR));
                newMessage.put(SocializerContract.COLUMN_MESSAGE, data.get(JSON_KEY_MESSAGE).trim());
                newMessage.put(SocializerContract.COLUMN_DATE, data.get(JSON_KEY_DATE));
                newMessage.put(SocializerContract.COLUMN_AUTHOR_KEY, data.get(JSON_KEY_AUTHOR_KEY));
                getContentResolver().insert(SocializerProvider.SocializerPosts.CONTENT_URI, newMessage);
                return null;
            }
        };
        insertToSocializerTask.execute();
    }

    private void sendNotification(Map<String, String> data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //create pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        String author = data.get(JSON_KEY_AUTHOR);
        String message = data.get(JSON_KEY_MESSAGE);

        if (message.length() > 30)
        {
            message=message.substring(0,30) + "\u2026";
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"socializer_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(String.format(getString(R.string.notification_message),author))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());

    }
}
