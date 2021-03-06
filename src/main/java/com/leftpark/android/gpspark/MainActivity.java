package com.leftpark.android.gpspark;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

/**
 * Project > Properties > Android
 * make sure the Project Build Target you have selected is one of the Google APIs ones.
 */
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import com.leftpark.android.gpspark.util.CurrentLocationOverlay;

public class MainActivity extends MapActivity implements OnClickListener, OnLongClickListener{
	
	private static final String TAG = "MainActivity";
	private static final boolean D = true;
	
	// Context
	private static Context mContext;
	
	// Handler
	private Handler mHandler;
	
	// HandlerThread
	private HandlerThread mHandlerThread;
	
	// HANDLER_EVENT
	private static final int HANDLER_EVENT_LOCATION_CHANGED = 1;
	
	// Managers
	private LocationManager mLocationManager;
	
	// Nmea Syntax
	private static final String NMEA_SYNTAX_GNS = "GNS";
	private static final String NMEA_SYNTAX_GGA = "GGA";
	private static final String NMEA_SYNTAX_GSA = "GSA";
	private static final String NMEA_SYNTAX_GSV = "GSV";
	private static final String NMEA_SYNTAX_RMC = "RMC";
	private static final String NMEA_SYNTAX_GST = "GST";
	
	// Views
	private TextView tvGpsStatus;	// GpsStatus
	private TextView tvAddress;		// Address
	private TextView tvTimestamp;	// Timestamp
	private TextView tvLatitude;	// Latitude
	private TextView tvLongitude;	// Longitude
	private TextView tvAltitude;	// Altitude
	private TextView tvNmeaGNS;		// Nmea	--GNS
	private TextView tvNmeaGGA;		// Nmea	GPGGA
	private TextView tvNmeaGSA;		// Nmea	--GSA
	private TextView tvNmeaGSV;		// Nmea	--GSV
	private TextView tvNmeaRMC;		// Nmea	GPRMC
	private TextView tvNmeaGST;		// Nmea	GPGST
	
	//private WebView wvMaps;			// MAPS
	private MapView mapView;	//com.google.android.maps
	
	private Button btnSetToMaps;		// Set to Maps
	
	// Values
	private String strGpsStatus;	// GpsStatu
	private String strAddress;		// Address
	private String strTimestamp;	// Timestamp
	private String strLatitude;		// Latitude
	private String strLongitude;	// Longitude
	private String strAltitude;		// Altitude
	private String strNmeaGNS;		// Nmea --GNS
	private String strNmeaGGA;		// Nmea GPGGA
	private String strNmeaGSA;		// Nmea --GSA
	private String strNmeaGSV;		// Nmea --GSV
	private String strNmeaRMC;		// Nmea GPRMC
	private String strNmeaGST;		// Nmea GPGST
	
	private static final String strGoogleMaps = "https://www.google.co.kr/maps";
	private String strCoordinates;	// Coordinates

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (D) Log.d(TAG,"onCreate() : E");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContext = this;
		
		initValue();
		
		initView();
		
		initHandler();
		
		// initialize GPS
		initGPS();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private void initValue() {
		if (D) Log.d(TAG,"initValue()");
		
		// GpsStatus
		strGpsStatus = getString(R.string.gpsstatus, "");
		
		// Address
		strAddress = getString(R.string.address, "");
		
		// Timestamp
		strTimestamp = getString(R.string.timestamp, 0, 0, 0);
		
		// Latitude
		strLatitude = getString(R.string.latitude, 0.0);
		
		// Longitude
		strLongitude = getString(R.string.longitude, 0.0);
		
		// Altitude
		strAltitude = getString(R.string.altitude, 0.0);
		
		// Nmea --GNS
		strNmeaGNS = getString(R.string.nmea_gns, "");
		
		// Nmea GPGGA
		strNmeaGGA = getString(R.string.nmea_gga, "");
		
		// Nmea --GSA
		strNmeaGSA = getString(R.string.nmea_gsa, "");
		
		// Nmea --GSV
		strNmeaGSV = getString(R.string.nmea_gsv, "");
		
		// Nmea GPRMC
		strNmeaRMC = getString(R.string.nmea_rmc, "");
		
		// Nmea GPGST
		strNmeaGST = getString(R.string.nmea_gst, "");
	}
	
	private void deinitValue() {
		if (D) Log.d(TAG,"deinitValue()");
		
		// GpsStatus
		strGpsStatus = getString(R.string.gpsstatus, "");
		
		// Address
		strAddress = getString(R.string.address, "");
		
		// Latitude
		strLatitude = getString(R.string.latitude, 0.0);
		
		// Longitude
		strLongitude = getString(R.string.longitude, 0.0);
		
		// Altitude
		strAltitude = getString(R.string.altitude, 0.0);
		
		// Nmea --GNS
		strNmeaGNS = getString(R.string.nmea_gns, "");
		
		// Nmea GPGGA
		strNmeaGGA = getString(R.string.nmea_gga, "");
		
		// Nmea --GSA
		strNmeaGSA = getString(R.string.nmea_gsa, "");
		
		// Nmea --GSV
		strNmeaGSV = getString(R.string.nmea_gsv, "");
		
		// Nmea GPRMC
		strNmeaRMC = getString(R.string.nmea_rmc, "");
		
		// Nmea GPGST
		strNmeaGST = getString(R.string.nmea_gst, "");
	}
	
	private void initView() {
		if (D) Log.d(TAG,"initView() : E");
		
		// GpsStatus
		tvGpsStatus = (TextView)findViewById(R.id.tv_gps_status);
		tvGpsStatus.setText(strGpsStatus);
		
		// Address
		tvAddress = (TextView)findViewById(R.id.tv_address);
		tvAddress.setText(strAddress);
		tvAddress.setOnClickListener(this);
		tvAddress.setOnLongClickListener(this);
		
		// Timestamp
		tvTimestamp = (TextView)findViewById(R.id.tv_timestamp);
		tvTimestamp.setText(strTimestamp);
		
		// Latitude
		tvLatitude = (TextView)findViewById(R.id.tv_latitude);
		tvLatitude.setText(strLatitude);
		
		// Longitude
		tvLongitude = (TextView)findViewById(R.id.tv_longitude);
		tvLongitude.setText(strLongitude);
		
		// Altitude
		tvAltitude = (TextView)findViewById(R.id.tv_altitude);
		tvAltitude.setText(strAltitude);
		
		// Nmea --GNS
		tvNmeaGNS = (TextView)findViewById(R.id.tv_nmea_gns);
		tvNmeaGNS.setText(strNmeaGNS);
		
		// Nmea GPGGA
		tvNmeaGGA = (TextView)findViewById(R.id.tv_nmea_gga);
		tvNmeaGGA.setText(strNmeaGGA);
		
		// Nmea --GSA
		tvNmeaGSA = (TextView)findViewById(R.id.tv_nmea_gsa);
		tvNmeaGSA.setText(strNmeaGSA);
		
		// Nmea --GSV
		tvNmeaGSV = (TextView)findViewById(R.id.tv_nmea_gsv);
		tvNmeaGSV.setText(strNmeaGSV);
		
		// Nmea GPRMC
		tvNmeaRMC = (TextView)findViewById(R.id.tv_nmea_rmc);
		tvNmeaRMC.setText(strNmeaRMC);
		
		// Nmea GPGST
		tvNmeaGST = (TextView)findViewById(R.id.tv_nmea_gst);
		tvNmeaGST.setText(strNmeaGST);
		
		// Maps
		//String url = strGoogleMaps;
		// R.id.wv_maps
		//wvMaps = (WebView)findViewById();
		//wvMaps.getSettings().setJavaScriptEnabled(true);
	    //wvMaps.setWebViewClient(new WebViewClient());
	    //wvMaps.loadUrl(url);
	    //wvMaps.loadUrl("https://www.google.co.kr/maps?saddr=20.344,34.34&daddr=20.5666,45.345");
		
		// MapView
		mapView = (MapView)findViewById(R.id.map_view);
		
		// Setting Zoom Controls on MapView
		mapView.setBuiltInZoomControls(true);
	    
	    // Set to Maps
	    btnSetToMaps = (Button)findViewById(R.id.btn_set_to_maps);
	    btnSetToMaps.setOnClickListener(this);
	}
	
	private void initHandler() {
		if (D) Log.d(TAG,"initHandler() : E");
		
		// HandlerThread
		mHandlerThread = new HandlerThread("MainAticityHandler");
		mHandlerThread.start();
		
		// Handler
		mHandler = new Handler(mHandlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what) {
				case HANDLER_EVENT_LOCATION_CHANGED:
					//
					break;
				}
			}
		};
	}
	
	private void initGPS() {
		if (D) Log.d(TAG,"initGPS(): E");
		LocationManager lm = getLocationManager();
		// LocationListener
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mLocationListener);
		
		// GpsStatus.Listener
		lm.addGpsStatusListener(mGpsStatusListener);
		
		// GpsStatus.NmeaListener
		lm.addNmeaListener(mGpsStatusNmeaListener);
		
		// get ILocationManager
		//ILocationManager ilm = getILocationManager();
	}
	
	private LocationManager getLocationManager() {
		if (D)	Log.d(TAG,"getLocationManager() : E");
		
		if (mLocationManager == null) {
			mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
		}
		return mLocationManager;
	}

/*
	private ILocationManager getILocationManager() {
		Class c = Class.forName(mLocationManager.getClass().getName());
		
		Field f = c.getDeclaredField("mService");
		
		ILocationManager mIlm = (ILocationManager)f.get(mLocationManager);
		
		return mIlm;
	}
*/

	//+LocationListener
	private LocationListener mLocationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			if (D) Log.d(TAG,"onLocationChanged()");
			
			if (D) Log.d(TAG,"onLocationChanged() : time = "+location.getTime());
			
			Calendar c = Calendar.getInstance();
			int hours= c.get(Calendar.HOUR_OF_DAY);
			int minutes = c.get(Calendar.MINUTE);
			int seconds = c.get(Calendar.SECOND);
			
			// Timestamp
			strTimestamp = getString(R.string.timestamp, hours, minutes, seconds);
			
			if (D) Log.d(TAG,"onLocationChanged() : Hours = "+hours+", Minutes = "+minutes+", Seconds = "+seconds);
			
			// Latitude
			double latitude = location.getLatitude();
			strLatitude = getString(R.string.latitude, latitude);
			if (D) Log.d(TAG,"onLocationChanged() : strLatitude = "+strLatitude);
			
			// Longitude
			double longitude = location.getLongitude();
			strLongitude = getString(R.string.longitude, longitude);
			if (D) Log.d(TAG,"onLocationChanged() : strLongitude = "+strLongitude);
			
			// Altitude
			strAltitude = getString(R.string.altitude, location.getAltitude());
			if (D) Log.d(TAG,"onLocationChanged() : strAltitude = "+strAltitude);
			
			// Send Message
			//mHandler.sendEmptyMessage(HANDLER_EVENT_LOCATION_CHANGED);
			
			strAddress = getString(R.string.address, getAddress(latitude, longitude));
			if (D) Log.d(TAG,"onLocationChanged() : strAddress = "+strAddress);
			
			// ?saddr=20.344,34.34&daddr=20.5666,45.345
			strCoordinates = "/@"+String.valueOf(latitude)+","+String.valueOf(longitude);
			showCurrentLocation(latitude, longitude);
			
			onUpdateView();
		}
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extra) {
			if (D) Log.d(TAG,"onStatusChanged()");
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			if (D) Log.d(TAG,"onProviderEnabled()");
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			if (D) Log.d(TAG,"onProviderDisabled()");
		}
	};
	//-LocationListener
	
	//+GpsStatus.Listener
	private GpsStatus.Listener mGpsStatusListener = new GpsStatus.Listener() {
		
		@Override
		public void onGpsStatusChanged(int event) {
			// TODO Auto-generated method stub
			switch(event) {
			case GpsStatus.GPS_EVENT_STARTED:
				if (D) Log.d(TAG,"onGesStatusChanged() : GPS_EVENT_STARTED");
				//Toast.makeText(mContext, "STARTED", Toast.LENGTH_SHORT).show();
				strGpsStatus = getString(R.string.gpsstatus, "STARTED");
				break;
			case GpsStatus.GPS_EVENT_STOPPED:
				if (D) Log.d(TAG,"onGesStatusChanged() : GPS_EVENT_STOPPED");
				//Toast.makeText(mContext, "STOPPED", Toast.LENGTH_SHORT).show();
				strGpsStatus = getString(R.string.gpsstatus, "STOPPED");
				break;
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				if (D) Log.d(TAG,"onGesStatusChanged() : GPS_EVENT_FIRST_FIX");
				//Toast.makeText(mContext, "FIRST_FIX", Toast.LENGTH_SHORT).show();
				strGpsStatus = getString(R.string.gpsstatus, "FIRST_FIX");
				break;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				if (D) Log.d(TAG,"onGesStatusChanged() : GPS_EVENT_SATELLITE_STATUS");
				//Toast.makeText(mContext, "SATELLITE_STATUS", Toast.LENGTH_SHORT).show();
				strGpsStatus = getString(R.string.gpsstatus, "SATELLITE_STATUS");
				break;
			}
			onUpdateView();
		}
	};
	//-GpsStatus.Listener
	
	//+GpsStatus.NmeaListener
	private GpsStatus.NmeaListener mGpsStatusNmeaListener = new GpsStatus.NmeaListener() {
		
		@Override
		public void onNmeaReceived(long timestamp, String nmea) {
			// TODO Auto-generated method stub
			if (D) Log.d(TAG,"onNmeaReceived() : timestamp = "+timestamp);
			if (D) Log.d(TAG,"onNmeaReceived() : nmea = "+nmea);
			
			String syntax = nmea.substring(3, 6);
			
			if (D)	Log.d(TAG,"onNmeaReceived() : syntax = "+syntax);
			
			if (syntax.equals(NMEA_SYNTAX_GNS)) {
				strNmeaGNS = getString(R.string.nmea_gns, nmea.trim());
			} else if (syntax.equals(NMEA_SYNTAX_GGA)) {
				strNmeaGGA = getString(R.string.nmea_gga, nmea.trim());
			} else if (syntax.equals(NMEA_SYNTAX_GSA)) {
				strNmeaGSA = getString(R.string.nmea_gsa, nmea.trim());
			} else if (syntax.equals(NMEA_SYNTAX_GSV)) {
				strNmeaGSV = getString(R.string.nmea_gsv, nmea.trim());
			} else if (syntax.equals(NMEA_SYNTAX_RMC)) {
				strNmeaRMC = getString(R.string.nmea_rmc, nmea.trim());
			} else if (syntax.equals(NMEA_SYNTAX_GST)) {
				strNmeaGST = getString(R.string.nmea_gst, nmea.trim());
			}
			
			onUpdateView();
		}
	};
	//-GpsStatus.NmeaListener
	
	private void onUpdateView() {
		if (D) Log.d(TAG,"onUpdateView() : E");
		
		// GpsStatus
		tvGpsStatus.setText(strGpsStatus);
		
		// Address
		tvAddress.setText(strAddress);
		
		// Timestamp
		tvTimestamp.setText(strTimestamp);
		
		// Latitude
		tvLatitude.setText(strLatitude);
		
		// Longitude
		tvLongitude.setText(strLongitude);
		
		// Altitude
		tvAltitude.setText(strAltitude);
		
		// Nmea --GNS
		tvNmeaGNS.setText(strNmeaGNS);
		
		// Nmea GPGGA
		tvNmeaGGA.setText(strNmeaGGA);
		
		// Nmea --GSA
		tvNmeaGSA.setText(strNmeaGSA);
		
		// Nmea --GSV
		tvNmeaGSV.setText(strNmeaGSV);
		
		// Nmea GPRMC
		tvNmeaRMC.setText(strNmeaRMC);
		
		// Nmea GPGST
		tvNmeaGST.setText(strNmeaGST);
		
		// MAP
		//wvMaps.loadUrl(strGoogleMaps+strCoordinates);
	}
	
	public String getAddress(double latitude, double longitude) {
		if (D) Log.d(TAG,"getAddress() : E");
		
		String str = "";
		
		// Geocoder
		Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
		
		List<Address> address;
		try {
			if (geocoder != null) {
				address = geocoder.getFromLocation(latitude, longitude, 1);
				if (address != null && address.size() > 0) {
					str = address.get(0).getAddressLine(0).toString();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return str;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_address:
			if (D) Log.d(TAG,"onClick() : tv_address");
			break;
		case R.id.btn_set_to_maps:
			//wvMaps.loadUrl(strGoogleMaps+strCoordinates);
			break;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_address:
			if (D) Log.d(TAG,"onLongClick() : tv_address");
			ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText(tvAddress.getText());
			return true;
		}
		return false;
	}
	
	// show curruent location in Google Maps with gps coordinates
	private void showCurrentLocation(double latitude, double longitude) {
		
		// Creating an instance of GeoPoint corresponding  to latitude and longitude
		GeoPoint point = new GeoPoint((int)(latitude * 1E6), (int)(longitude * 1E6));
		
		// Getting MapController
		MapController mapController = mapView.getController();
		
		// Applying a zoom
		mapController.setZoom(15);
		
		// Redraw the map
		mapView.invalidate();
		
		// Getting list of overlays available in the map
		List<Overlay> mapOverlays = mapView.getOverlays();
		
		// Creating a drawable object to represent the image of mark in the map
		Drawable drawable = this.getResources().getDrawable(R.drawable.cur_position);
		
		// Creating an instance of ItemizedOverlay to mark the current location in the map
		CurrentLocationOverlay currentLocationOverlay = new CurrentLocationOverlay(drawable);
		
		// Creating an item to represent a mark in the overlay
		OverlayItem currentLocation = new OverlayItem(point, "Current Location", "Latitude:" + latitude + ", Longitude:" + longitude);
		
		// Adding the mark to the overlay
		currentLocationOverlay.addOverlay(currentLocation);
		
		// Current Existing overlays in the map
		mapOverlays.clear();
		
		// Adding new overlay to map Overlay
		mapOverlays.add(currentLocationOverlay);
	}
}
