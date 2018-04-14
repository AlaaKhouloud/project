package com.example.lenovo.geolocalisationsafi;

import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements LocationListener {
    LocationManager lm;
    private DatabaseReference databaseTeachers;
private static final  int PERMS_CALL_ID=1234;
    private MapFragment mapFragment;
   private DatabaseReference databasepoints;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager=getFragmentManager();
       mapFragment=(MapFragment)fragmentManager.findFragmentById(R.id.map);
        databaseTeachers = FirebaseDatabase.getInstance().getReference().child("points");
        databaseTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.child("points").getChildren()) {
                    PointInterest lokasi3 = dataSnapshot.getValue(PointInterest.class);
                    String latitude = child.child("latitude").getValue().toString();
                    String longitude = child.child("longitude").getValue().toString();
                    double loclatitude = Double.parseDouble(latitude);
                    double loclongitude = Double.parseDouble(longitude);
                    LatLng cod = new LatLng(loclatitude, loclongitude);
                    googleMap.addMarker(new MarkerOptions().position(cod).title(""));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();

    }
private  void checkPermissions(){

    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},PERMS_CALL_ID);
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return;
    }   lm = (LocationManager) getSystemService(LOCATION_SERVICE);

    if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
    }
    if(lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)){

        lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,10000,0,this);
    }
    if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,0,this);
    }
    loadMap();
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMS_CALL_ID){
            checkPermissions();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(lm!=null){
            lm.removeUpdates(this);
        }
    }
    @SuppressWarnings("MissingPermission")
private void loadMap(){

    mapFragment.getMapAsync(new OnMapReadyCallback() {
        @Override
        public void onMapReady(final GoogleMap googleMap) {
MainActivity.this.googleMap=googleMap;
            googleMap.moveCamera(CameraUpdateFactory.zoomBy(15));
            googleMap.setMyLocationEnabled(true);
       //     googleMap.addMarker(new MarkerOptions().position(new LatLng(43.799345,6.7254267)).title("infini"));



       /*     databaseTeachers = FirebaseDatabase.getInstance().getReference().child("points");
            databaseTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.child("points").getChildren()) {
                        PointInterest lokasi3 = dataSnapshot.getValue(PointInterest.class);
                        String latitude = child.child("latitude").getValue().toString();
                        String longitude = child.child("longitude").getValue().toString();
                        double loclatitude = Double.parseDouble(latitude);
                        double loclongitude = Double.parseDouble(longitude);
                        LatLng cod = new LatLng(loclatitude, loclongitude);
                        googleMap.addMarker(new MarkerOptions().position(cod).title(""));

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



*/





            googleMap.setOnPoiClickListener(new GoogleMap.OnPoiClickListener(){


                @Override
                public void onPoiClick(PointOfInterest pointOfInterest) {
                    googleMap.addMarker(new MarkerOptions().position(pointOfInterest.latLng).title(pointOfInterest.name));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(pointOfInterest.latLng));
                    final MediaPlayer mediaPlayer= MediaPlayer.create(MainActivity.this,R.raw.classic);
                   mediaPlayer.start();

                    databasepoints= FirebaseDatabase.getInstance().getReference("points");
                    String id=databasepoints.push().getKey();

                    //<LatLng lg=pointOfInterest.latLng;
                    PointInterest point=new PointInterest(id,pointOfInterest.name,pointOfInterest.latLng.longitude,pointOfInterest.latLng.latitude);
                    databasepoints.child(id).setValue(point);
                }
            } );
        }
    });
}
    @Override
    public void onLocationChanged(Location location) {

       double latitude=location.getLatitude();
      double longtitude=location.getLongitude();


if(googleMap!=null){

    LatLng googleLocation=new LatLng(latitude,longtitude);
    googleMap.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));
    //googleMap.addMarker(new MarkerOptions().position(googleLocation).title("infini"));
}

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

