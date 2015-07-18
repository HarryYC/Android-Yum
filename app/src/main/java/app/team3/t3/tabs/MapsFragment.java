package app.team3.t3.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import app.team3.t3.R;
import app.team3.t3.yelp.Restaurant;

/**
 * Created by nanden on 7/5/15.
 */
public class MapsFragment extends Fragment {

    private static final double LATITUDE_TEST = 17.385044;
    private static final double LONGITUDE_TEST = 78.486671;

    private static ViewPager mViewPager;

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private MarkerOptions markerOptions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mViewPager = (ViewPager) container.findViewById(R.id.view_pager);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        //ResDatabaseHelper resDatabaseHelper = new ResDatabaseHelper(getActivity().getApplicationContext());
        mMapView.onCreate(savedInstanceState);
        Bundle intent = getActivity().getIntent().getExtras();
        //Restaurant restaurant = resDatabaseHelper.getRestaurant(intent.getInt("restaurant_picked"));
        Restaurant restaurant = intent.getParcelable("restaurant_picked");
        markerOptions = new MarkerOptions().position(
                new LatLng(restaurant.getLatitude(), restaurant.getLongitude())).title(restaurant.getName());
        mGoogleMap = mMapView.getMap();

        markerOptions.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        mGoogleMap.addMarker(markerOptions);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerOptions.getPosition(), 16.0f));

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
