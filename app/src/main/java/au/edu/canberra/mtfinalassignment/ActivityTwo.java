package au.edu.canberra.mtfinalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityTwo extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng google = new LatLng(37.3981617,-122.1220645);
    LatLng ibm = new LatLng(41.1308344,-73.7315235);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setTitle("Google and IBM Headquarters");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in USA and move the camera
        final LatLng usa = new LatLng(40.788077, -98.627419);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usa, 2));

        //Google marker
        final Marker googleMarker = mMap.addMarker(new MarkerOptions()
                .title("Google Headquarters")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_google_round))
                .snippet("Mountain View, California, USA")
                .position(google)
                .draggable(false));

        //IBM marker
        final Marker ibmMarker = mMap.addMarker(new MarkerOptions()
                .title("IBM Headquarters")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ibm_round))
                .snippet("Yorktown Height, New York, USA")
                .position(ibm)
                .draggable(false));

        //custom info window layout for different markers on map
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            //inflate selected marker and fill with related information
            @Override
            public View getInfoContents(Marker marker) {
                //added suppression to remove "avoid passing 'null' as the view root" warning
                @SuppressLint("InflateParams") View infoWindow = getLayoutInflater().inflate(R.layout.infowindow_with_image, null);

                TextView title = infoWindow.findViewById(R.id.title);
                TextView snippet = infoWindow.findViewById(R.id.snippet);
                ImageView image = infoWindow.findViewById(R.id.info_image);

                //Google custom info window
                if (marker.getId().equals(googleMarker.getId())) {
                    title.setText(marker.getTitle());
                    snippet.setText(marker.getSnippet());
                    image.setImageDrawable(getResources()
                            .getDrawable(R.drawable.google, getTheme()));
                }

                //IBM custom info window
                if (marker.getId().equals(ibmMarker.getId())) {
                    title.setText(marker.getTitle());
                    snippet.setText(marker.getSnippet());
                    image.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ibm, getTheme()));
                }
                return infoWindow;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                if (marker.getId().equals(googleMarker.getId())) {
                    Intent intent = new Intent(getApplicationContext(), ActivityThree.class);
                    intent.putExtra("title", "Google Firebase ML Cloud Services");
                    startActivity(intent);
                }
                if (marker.getId().equals(ibmMarker.getId())) {
                    Intent intent = new Intent(getApplicationContext(), ActivityThree.class);
                    intent.putExtra("title", "IBM Watson ML Cloud Services");
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.map_normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.map_satellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}