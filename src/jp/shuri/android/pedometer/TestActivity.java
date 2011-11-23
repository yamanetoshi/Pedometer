package jp.shuri.android.pedometer;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends Activity {
	
	private final String TAG = "TextActivity";
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		
		tv = (TextView)findViewById(R.id.query_text);
		queryAll(tv);
		
		Button btn = (Button)findViewById(R.id.test_button);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ContentValues initialValues = new ContentValues();
		        initialValues.put(DBColumns.KEY_DATE, "2011-02-25");
		        initialValues.put(DBColumns.KEY_COUNT, 15);

		        Uri tmp = getApplicationContext().getContentResolver().insert(DBColumns.CONTENT_URI, initialValues);
		        
		        queryAll(tv);
			}
			
		});
		
		btn = (Button)findViewById(R.id.test_button_delete);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				getApplicationContext().getContentResolver().delete(DBColumns.CONTENT_URI, null, null);
				
				queryAll(tv);
			}
			
		});
	}

	private void queryAll(TextView tv2) {
		StringBuffer tmp = new StringBuffer();
		Cursor c = getApplicationContext().getContentResolver().query(DBColumns.CONTENT_URI, null, null, null, "_id DESC");
		if (c == null) {
			tv2.setText("no data");
			return;
		}
		int count = c.getCount();
		c.moveToFirst();
		for (int i = 0; i < count; i++) {
			int id = c.getInt(0);
			String date = c.getString(1);
			int counter = c.getInt(2);

//			tmp += "record no. " + i + " id : " + id + "Date : " + date + " count : " + counter + "\n";
			tmp.append("record no. " + i + " id : " + id + "Date : " + date + " count : " + counter + "\n");
		}
		tv2.setText(tmp);
	}

}
