package np.com.manishtuladhar.socializer.provider;

import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.BaseColumns;

public class SocializerContract {

    public static final String AUTHORITY = "np.com.manishtuladhar.socializer";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" +AUTHORITY );
    public static final String PATH_POSTS = "posts";

    //inner class
    public static final class PostEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_POSTS).build();

        //table name
        public static final String TABLE_NAME = "posts";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_AUTHOR_KEY = "authorKey";
        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_DATE = "date";

        public static final String MANISH_KEY = "key_manish";
        public static final String SAMIP_KEY = "key_samip";
        public static final String RAM_KEY = "key_ram";
        public static final String SHYAM_KEY = "key_shyam";
        public static final String HARI_KEY = "key_hari";

        public static final String TEST_ACCOUNT_KEY = "key_test";

        public static final String[] AUTHOR_KEYS = {
                MANISH_KEY, SAMIP_KEY, RAM_KEY, SHYAM_KEY, HARI_KEY
        };

        /**
         * Creates sql selection based on the selected followers
         */
        public static String createdSelectionForCurrentFollowers(SharedPreferences preferences)
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(COLUMN_AUTHOR_KEY).append(" IN ('").append(TEST_ACCOUNT_KEY).append("'");

            for(String key: AUTHOR_KEYS)
            {
                if(preferences.getBoolean(key,false))
                {
                    stringBuilder.append(",");
                    stringBuilder.append("'").append(key).append("'");
                }

            }
            stringBuilder.append(")");
            return  stringBuilder.toString();
        }
    }
}
