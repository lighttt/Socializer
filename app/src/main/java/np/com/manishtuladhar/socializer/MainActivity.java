package np.com.manishtuladhar.socializer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import np.com.manishtuladhar.socializer.following.FollowingPreferenceActivity;
import np.com.manishtuladhar.socializer.provider.SocializerContract;
import np.com.manishtuladhar.socializer.provider.SocializerProvider;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";

    //loader id
    private static final int LOADER_ID_POSTS = 0;

    //views
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;
    SocializerAdapter mAdapter;

    //DATABASE vars
    static final String[] POSTS_PROJECTION = {
            SocializerContract.PostEntry.COLUMN_AUTHOR,
            SocializerContract.PostEntry.COLUMN_MESSAGE,
            SocializerContract.PostEntry.COLUMN_DATE,
            SocializerContract.PostEntry.COLUMN_AUTHOR_KEY,
    };
    static final int COL_NUM_AUTHOR = 0;
    static final int COL_NUM_MESSAGE = 1;
    static final int COL_NUM_DATE = 2;
    static final int COL_NUM_AUTHOR_KEY = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //rv
        mRecyclerView = findViewById(R.id.socializer_rv);
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        //divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation()
        );
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        //adpater
        mAdapter = new SocializerAdapter();
        mRecyclerView.setAdapter(mAdapter);

        //init loader
        getSupportLoaderManager().initLoader(LOADER_ID_POSTS,null,this);

        //create notification channel
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(
                   "socializer_channel",
                    "socializer_channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        //get the notification data
        Bundle extras = getIntent().getExtras();

        if(extras!=null && extras.containsKey("test"))
        {
            Log.e(TAG, "onCreate: "+extras.getString("test") );
        }

        //firebase token
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                String msg = getString(R.string.message_token_format,token);
                Log.e(TAG, "onSuccess: "+msg );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: Sorry failed to get id");
            }
        });

    }

    // ==================== MENU =========================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_following_prefs)
        {
            Intent intent = new Intent(this, FollowingPreferenceActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ================ LOADERS =========================

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
       String selection = SocializerContract.PostEntry.createdSelectionForCurrentFollowers(
               PreferenceManager.getDefaultSharedPreferences(this));
       Log.e(TAG, "onCreateLoader: Selection is"+selection );
       return new CursorLoader(
                this,
                SocializerContract.PostEntry.CONTENT_URI,
                POSTS_PROJECTION,selection,
                null,
               SocializerContract.PostEntry.COLUMN_DATE + " DESC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}