package app.team3.t3.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import app.team3.t3.ImageDownloader;
import app.team3.t3.R;

/**
 * Created by nanden on 7/5/15.
 */
public class TestFragmentOne extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one,container,false);

        final ImageView businessIV = (ImageView) getActivity().findViewById(R.id.businessIV);
        final ImageView ratingIV = (ImageView) getActivity().findViewById(R.id.ratingIV);
        final TextView nameTV = (TextView) getActivity().findViewById(R.id.nameTV);
        final TextView countTV = (TextView) getActivity().findViewById(R.id.countTV);


        return view;

    }
}
