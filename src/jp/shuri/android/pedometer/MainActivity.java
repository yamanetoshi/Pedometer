package jp.shuri.android.pedometer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
	private boolean visible;
	private TextView count;
	private static MainActivity instance = null;
	
    private class CountReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
        	if (visible) {
        		count.setText(intent.getStringExtra("count"));
        	}
        }
    }
    
    private CountReceiver receiver = new CountReceiver();
    private IMainService binder;
    
    private ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("ServiceConnection", "onServiceConnected");
            binder = IMainService.Stub.asInterface(service);
            int c = 0;
            try {
            	c = binder.getCount();
            } catch (Exception e) {
            	e.printStackTrace();
            }
            count.setText(new Integer(c).toString());
        }
		
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "onServiceDisconnected");
            binder = null;
        }
    };

    public static MainActivity getInstance() {
        return instance;
      }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        instance = this;
        
        count = (TextView)findViewById(R.id.count);
        
        IntentFilter filter = new IntentFilter(MainService.ACTION);
        registerReceiver(receiver, filter);

    }

	@Override
	protected void onPause() {
		super.onPause();
		visible = false;
		unbindService(conn);
	}

	@Override
	protected void onResume() {
		super.onResume();
		visible = true;
		bindService(new Intent(this, MainService.class), conn, 0);
	}
    
}