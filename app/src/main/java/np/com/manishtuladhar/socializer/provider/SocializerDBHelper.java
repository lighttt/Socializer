package np.com.manishtuladhar.socializer.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SocializerDBHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String DB_NAME = "socializer.db";

    public SocializerDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE " + SocializerContract.PostEntry.TABLE_NAME + " (" +
                SocializerContract.PostEntry._ID + " INTEGER PRIMARY KEY, " +
                SocializerContract.PostEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                SocializerContract.PostEntry.COLUMN_AUTHOR_KEY + " TEXT NOT NULL, " +
                SocializerContract.PostEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                SocializerContract.PostEntry.COLUMN_MESSAGE + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SocializerContract.PostEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
