package jp.shuri.android.pedometer;

import android.net.Uri;
import android.provider.BaseColumns;

public interface DBColumns extends BaseColumns {
    public static final String TABLE = "pedometer";

    public static final Uri CONTENT_URI =
            //Uri.parse("content://" + PedometerProvider.AUTHORITY + TABLE);
        	Uri.parse("content://" + PedometerProvider.AUTHORITY);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.note";
    public static final String CONTENT_ITEMTYPE = "vnd.android.cursor.item/vnd.google.note";

    public static final String KEY_DATE = "date";
    public static final String KEY_COUNT = "count";

}
