package br.lauriavictor.wmb.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import br.lauriavictor.wmb.R;
import br.lauriavictor.wmb.model.FetchDataType;

public class TypeFragment extends Fragment {

    private View view;
    private Button mButtonLoadType;
    public static TextView mContent;


    public TypeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.type_fragment, container, false);

        mButtonLoadType = (Button) view.findViewById(R.id.buttonShowJsonType);
        mContent = (TextView) view.findViewById(R.id.jsonContentType);

        mButtonLoadType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchDataType fetchDataType = new FetchDataType();
                fetchDataType.execute();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
