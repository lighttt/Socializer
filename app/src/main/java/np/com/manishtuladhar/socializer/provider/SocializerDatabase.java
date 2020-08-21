package np.com.manishtuladhar.socializer.provider;

import net.simonvt.schematic.annotation.Database;

@Database(version = SocializerDatabase.VERSION )
public class SocializerDatabase {

    public static final int VERSION = 1;

    //table name
    public static final String SOCIALIZER_POSTS = "socializer_posts";

}
