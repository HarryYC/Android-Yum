package app.team3.t3.tabs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import app.team3.t3.MainActivity;
import app.team3.t3.R;
import app.team3.t3.ResDatabaseHelper;
import app.team3.t3.yelp.Restaurant;

/**
 * Created by nanden on 7/5/15.
 */
public class MapsFragment extends Fragment {

    private Button mGoButton;
    private Button mTryAgainButton;

    private MapView mMapView;
    private GoogleMap mGoogleMap;

    private Restaurant mRestaurant;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mGoButton = (Button) view.findViewById(R.id.map_go_bt);
        mTryAgainButton = (Button)view.findViewById(R.id.map_try_again_bt);

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        Bundle intent = getActivity().getIntent().getExtras();
        mRestaurant = intent.getParcelable("restaurant_picked");

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ResDatabaseHelper(getActivity().getApplicationContext()).saveRestaurant(mRestaurant);
                Intent directionIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + mRestaurant.getAddress()));
                directionIntent.setPackage("com.google.android.apps.maps");
                startActivity(directionIntent);
            }
        });

        mTryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        MarkerOptions markerOptions = new MarkerOptions().position(
                new LatLng(mRestaurant.getLatitude(), mRestaurant.getLongitude())).title(mRestaurant.getName());

        markerOptions.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        mGoogleMap = mMapView.getMap();
        mGoogleMap.addMarker(markerOptions);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerOptions.getPosition(), 16.0f));

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
