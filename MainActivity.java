package com.ingenico.spicesshop;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.ingenico.pclutilities.PclLog;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends CommonActivity implements OnTouchListener{
    ImageView img;
    protected void onCreate(Bundle savedInstanceState) 
    {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.main);

               img = (ImageView) findViewById(R.id.imageView1);
               img.setOnTouchListener(this);
               if(checkAndRequestPermissions())
                   initService();
    }
    
       public boolean onTouch(View v, MotionEvent event) 
   {
       switch (event.getAction())
       {
           case MotionEvent.ACTION_DOWN:
           {       
                 // Here u can write code which is executed after the user touch on the screen 
                    break; 
           }
           case MotionEvent.ACTION_UP:
           {          
        	    Intent intent = new Intent(MainActivity.this,AccueilActivity.class);
        	    startActivity(intent);
                break;
           }
           case MotionEvent.ACTION_MOVE:
           {  
              // Here u can write code which is executed when user move the finger on the screen   
               break;
           }
       }
       return true;
   }

	@Override
	public void onBarCodeReceived(String barCodeValue, int symbology) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void onPclServiceConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBarCodeClosed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStateChanged(String state) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		releaseService();
	}

    void showDeniedPermissionAlert(ArrayList<String> permissions){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("ERROR: Can't start PCL service\nMissing permissions :\n" + permissions.toString());
        builder.setCancelable(true);
        builder.create().show();
    }

    protected static final String[] mPclServicePermissions =  {Manifest.permission.BLUETOOTH_CONNECT,Manifest.permission.BLUETOOTH_SCAN};
    boolean checkAndRequestPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PclLog.v(TAG,"checkAndRequestPermissions");
            final ArrayList<String> permissionsToGrant = new ArrayList<>();
            for(String permission : mPclServicePermissions){
                if(checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED)
                    permissionsToGrant.add(permission);
            }
            if(permissionsToGrant.size() > 0) {
                ActivityResultLauncher<String[]> requestMultiplePermissions = registerForActivityResult(
                        new ActivityResultContracts.RequestMultiplePermissions(),
                        new ActivityResultCallback<Map<String, Boolean>>() {
                            @Override
                            public void onActivityResult(java.util.Map<String, Boolean> result) {
                                ArrayList<String> permissionsDenied = new ArrayList<>();
                                for(Map.Entry<String, Boolean> entry : result.entrySet()){
                                    if(!entry.getValue())
                                        permissionsDenied.add(entry.getKey());
                                }
                                if(permissionsDenied.size() > 0){
                                    showDeniedPermissionAlert(permissionsDenied);
                                }else{
                                    initService();
                                }
                            }
                        });
                requestMultiplePermissions.launch(mPclServicePermissions);
                return false;
            }
        }
        return true;
    }
}