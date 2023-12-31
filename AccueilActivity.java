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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.ingenico.pclutilities.PclLog;
import com.ingenico.pclutilities.PclUtilities;

import java.util.ArrayList;
import java.util.Map;


public class AccueilActivity  extends CommonActivity implements OnClickListener{
	private static final boolean optionalBarCode = true;
	private ImageButton buttonPaprika,buttonBadiane, buttonPavotBleu, buttonCannelle, buttonCurcuma, buttonReglisse, buttonNigelle, buttonSumac, buttonSettings;
	private ImageView buttonViewCart, buttonActiveCodebar;
    private TextView textTotalPrice;
    //private SpicesShop appContext = null;
    
    
    @Override
	protected void onCreate(Bundle savedInstanceState) 
    {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.shop);
       buttonBadiane  =  (ImageButton) findViewById(R.id.buttonBadiane);
       buttonPaprika  =  (ImageButton) findViewById(R.id.buttonPaprika);
       buttonPavotBleu = (ImageButton) findViewById(R.id.buttonPavotBleu);
       buttonCannelle  =  (ImageButton) findViewById(R.id.buttonCannelle);
       buttonCurcuma  =  (ImageButton) findViewById(R.id.buttonCurcuma);
       buttonReglisse = (ImageButton) findViewById(R.id.buttonReglisse);
       buttonNigelle =  (ImageButton) findViewById(R.id.buttonNigelle);
       buttonSumac  =  (ImageButton) findViewById(R.id.buttonSumac);
       buttonViewCart = (ImageView) findViewById(R.id.buttonViewCart);
       buttonActiveCodebar = (ImageView) findViewById(R.id.buttonActiveCodebar);
       buttonSettings = (ImageButton) findViewById(R.id.buttonSettings);
       
       PclUtilities mPclUtil = new PclUtilities(this, BuildConfig.APPLICATION_ID, "pairing_addr.txt");
       /*
       if(!mPclUtil.getActivatedCompanion().isEmpty()){
    	   startPclService();
    	   initService();
       }
       */
       
       buttonBadiane.setOnClickListener(this);
       buttonPaprika.setOnClickListener(this);
       buttonPavotBleu.setOnClickListener(this);
       buttonCannelle.setOnClickListener(this);
       buttonCurcuma.setOnClickListener(this);
       buttonReglisse.setOnClickListener(this);
       buttonNigelle.setOnClickListener(this);
       buttonSumac.setOnClickListener(this);
       buttonViewCart.setOnClickListener(this);
       buttonActiveCodebar.setOnClickListener(this);
       buttonSettings.setOnClickListener(this);
       
       textTotalPrice = (TextView) findViewById(R.id.textViewTotalMain);
       
       if(!optionalBarCode)
    	   buttonActiveCodebar.setVisibility(View.INVISIBLE);
       //appContext = ((SpicesShop)getApplicationContext());
    }
	
	@Override
	protected void onStart()
	{
		super.onStart();
		updateTotalPrice();
	}
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        //SpicesShop appContext = ((SpicesShop)getApplicationContext());
		if(checkAndRequestPermissions())
			initService();
		updateTotalPrice();
	}
	
	private void updateTotalPrice()
	{
		textTotalPrice.setText(String.format("Total:\r\n%.2f \u20ac", appContext.getTotalPrice()));
	}
	
	@Override
	public void onBarCodeReceived(String barCodeValue, int symbology)
	{
		SpicesTypes type = getSpiceCodeList().get(Integer.valueOf(barCodeValue));
		
		if(type != null)
			showSpiceDetails(type);
	}
	
	private void showSpiceDetails(SpicesTypes type)
	{
		if((type.Value() > 0) && (type.Value() < SpicesTypes.MAX_SpicesTypes.Value()))
		{
			byte spiceID = (byte) (type.Value() - 1);

			Intent spiceIntent = new Intent(AccueilActivity.this, ShopActivity.class);
			spiceIntent.putExtra("com.ingenico.spicesshop.ARTICLE", spiceID);
			startActivity(spiceIntent);
		}
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.buttonBadiane:
			showSpiceDetails(SpicesTypes.SpicesTypes_Badian);
			break;
			
		case R.id.buttonPaprika:
			showSpiceDetails(SpicesTypes.SpicesTypes_Paprika);
			break;
			
		case R.id.buttonPavotBleu:
			showSpiceDetails(SpicesTypes.SpicesTypes_PavoBleu);
		break;
		
		case R.id.buttonCannelle:
			showSpiceDetails(SpicesTypes.SpicesTypes_Cannelle);
			break;
			
		case R.id.buttonCurcuma:
			showSpiceDetails(SpicesTypes.SpicesTypes_Curcuma);
			break;
			
		case R.id.buttonReglisse:
			showSpiceDetails(SpicesTypes.SpicesTypes_Reglisse);
		break;
		
		case R.id.buttonNigelle:
			showSpiceDetails(SpicesTypes.SpicesTypes_Nigelle);
			break;
			
		case R.id.buttonSumac:
			showSpiceDetails(SpicesTypes.SpicesTypes_Sumac);
		break;
		
		case R.id.buttonViewCart:
			//Button ViewCart
    	    Intent intent = new Intent(AccueilActivity.this,CartActivity.class);
    	    startActivity(intent);
			break;
			
		case R.id.buttonActiveCodebar:
			reopenBarCode();
			break;
			
		case R.id.buttonSettings:
    	    Intent intent2 = new Intent(AccueilActivity.this,ConnectionActivity.class);
    	    intent2.putExtra(INTENT_EXTRA_STARTING,false);
    	    startActivity(intent2);
			break;
			
		default:
			break;
		
		}
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		releaseService();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		releaseService();
	}

	@Override
	public void onServiceConnected(ComponentName arg0, IBinder arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
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
