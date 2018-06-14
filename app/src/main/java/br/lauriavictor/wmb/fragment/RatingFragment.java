package br.lauriavictor.wmb.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import br.lauriavictor.wmb.R;
import br.lauriavictor.wmb.model.FetchData;

public class RatingFragment extends Fragment {

    private View view;
    private Button mButtonLoadRating;
    public static TextView mContent;


    public RatingFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.rating_fragment, container, false);

        mButtonLoadRating = (Button) view.findViewById(R.id.buttonShowJson);
        mContent = (TextView) view.findViewById(R.id.jsonContent);

        mButtonLoadRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchData fetchData = new FetchData();
                fetchData.execute();
            }
        });
        return view;
    }
}
