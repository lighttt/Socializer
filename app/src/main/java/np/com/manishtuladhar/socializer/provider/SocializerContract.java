package np.com.manishtuladhar.socializer.provider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

public class SocializerContract {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement
    public static final String COLUMN_ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_AUTHOR = "author";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_AUTHOR_KEY = "authorKey";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_MESSAGE = "message";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String COLUMN_DATE = "date";


    public static final String MANISH_KEY = "key_manish";
    public static final String SAMIP_KEY = "key_samip";
    public static final String RAM_KEY = "key_ram";
    public static final String SHYAM_KEY = "key_shyam";
    public static final String HARI_KEY = "key_hari";

    public static final String[] AUTHOR_KEYS = {
            MANISH_KEY, SAMIP_KEY, RAM_KEY, SHYAM_KEY, HARI_KEY
    };
}
