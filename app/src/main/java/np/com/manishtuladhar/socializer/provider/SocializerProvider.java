package np.com.manishtuladhar.socializer.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class SocializerProvider extends ContentProvider {

    // ========================== URI ==================================

    //uri : directory for a single item and notes
    public static final  int POSTS = 100;
    public static final  int POSTS_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /**
     * Helps to find the associated Uri and checks if it exists or not
     *
     */
    public static UriMatcher buildUriMatcher()
    {
        //initialize
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //adding two uri
        uriMatcher.addURI(SocializerContract.AUTHORITY,SocializerContract.PATH_POSTS,POSTS);
        uriMatcher.addURI(SocializerContract.AUTHORITY,SocializerContract.PATH_POSTS + "/#",POSTS_WITH_ID);
        return  uriMatcher;
    }


    // ========================== DB : CONTENT PROVIDER ==================================

    // database helper
    private SocializerDBHelper socializerDBHelper;


    @Override
    public boolean onCreate() {
        socializerDBHelper = new SocializerDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = socializerDBHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match)
        {
            case POSTS:
                retCursor = db.query(SocializerContract.PostEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI "+uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return  retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        //database access
        final SQLiteDatabase db = socializerDBHelper.getWritableDatabase();
        //match ansuar hamile curosr ra data pathauchua
        int match = sUriMatcher.match(uri);
        //uri return garne
        Uri returnUri;
        switch (match)
        {
            case POSTS:
                long id = db.insert(SocializerContract.PostEntry.TABLE_NAME,null,contentValues);
                if(id>0){
                    //content://np.com.manishtuladhar.notetaker/notes/4
                    returnUri = ContentUris.withAppendedId(SocializerContract.PostEntry.CONTENT_URI,id);
                }
                else{
                    throw new SQLException("Failed to insert new row into "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        //notifying content resolver about new data insertion
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

}
