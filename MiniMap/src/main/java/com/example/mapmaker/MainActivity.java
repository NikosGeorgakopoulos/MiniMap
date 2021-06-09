package com.example.mapmaker;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import org.mapsforge.map.model.IMapViewPosition;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private MapView map = null;
    private MyLocationNewOverlay mLocationOverlay;
    private CompassOverlay mCompassOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    private MinimapOverlay mMinimapOverlay;
    private IMapController mapController;


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        final DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's tile servers will get you banned based on this string

        //inflate and create the map
        setContentView(R.layout.main);

        IGeoPoint centerPoint = new GeoPoint(38.128,23.759);

        map = (MapView) findViewById(R.id.map);
//        map.setTileSource(TileSourceFactory.MAPNIK);
//        map.setTileSource(TileSourceFactory.BASE_OVERLAY_NL);
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

        map.setExpectedCenter(centerPoint);

        map.setHorizontalMapRepetitionEnabled(false);
        map.setVerticalMapRepetitionEnabled(false);
        map.setMinZoomLevel(5.0);
        this.mapController = map.getController();
        this.mapController.setCenter(centerPoint);
        this.mapController.animateTo(centerPoint);

        GpsMyLocationProvider locationProvider = new GpsMyLocationProvider(ctx);

        this.mLocationOverlay = new MyLocationNewOverlay(locationProvider, map);

        locationProvider.startLocationProvider(mLocationOverlay);
        this.mLocationOverlay.enableMyLocation();
        this.mLocationOverlay.enableFollowLocation();
        this.mLocationOverlay.setDrawAccuracyEnabled(true);


        map.getOverlays().add(this.mLocationOverlay);

        this.mCompassOverlay = new CompassOverlay(ctx, new InternalCompassOrientationProvider(ctx), map);
        this.mCompassOverlay.enableCompass();
        map.getOverlays().add(this.mCompassOverlay);

//        LatLonGridlineOverlay2 overlay = new LatLonGridlineOverlay2();
//        map.getOverlays().add(overlay);

//        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(ctx, map);
//        mRotationGestureOverlay.setEnabled(true);
//        map.setMultiTouchControls(true);
//        map.getOverlays().add(this.mRotationGestureOverlay);


        MinimapOverlay mMinimapOverlay = new MinimapOverlay(ctx, map.getTileRequestCompleteHandler());
        mMinimapOverlay.setWidth(dm.widthPixels / 5);
        mMinimapOverlay.setHeight(dm.heightPixels / 5);
        map.getOverlays().add(mMinimapOverlay);



//      Creating Drawables for Icons

        Drawable adf = getResources().getDrawable(android.R.drawable.alert_dark_frame);
        Drawable auf = getResources().getDrawable(android.R.drawable.arrow_up_float);
        Drawable ph = getResources().getDrawable(android.R.drawable.progress_horizontal);
        Drawable bm = getResources().getDrawable(android.R.drawable.btn_minus);


        //your items

        GeoPoint startPoint = new GeoPoint(38.128, 23.759);
        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);

        GeoPoint aufP = new GeoPoint(38.120, 23.740);
        Marker aufm = new Marker(map);
        aufm.setPosition(aufP);
        aufm.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        aufm.setIcon(auf);
        map.getOverlays().add(aufm);

    }

    public void onResume(){
        super.onResume();
        this.mLocationOverlay.enableMyLocation();
        this.mLocationOverlay.enableFollowLocation();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause(){
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        this.mLocationOverlay.disableFollowLocation();
        this.mLocationOverlay.disableMyLocation();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }


    public IMapController getMapController(){
        return  this.mapController;
    }
}
