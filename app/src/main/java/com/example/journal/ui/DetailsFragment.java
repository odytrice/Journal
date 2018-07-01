package com.example.journal.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.journal.databinding.*;

import com.example.journal.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ENTRYID = "entryId";

    // TODO: Rename and change types of parameters
    private int entryId;
    private DetailsViewModel _model;


    public DetailsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(int entryId) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ENTRYID, entryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entryId = getArguments().getInt(ARG_ENTRYID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentDetailsBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_details,container,false);

        _model = ViewModelProviders.of(getActivity(), new ViewModelFactory(getContext())).get(DetailsViewModel.class);

        binding.btnEdit.setOnClickListener(v ->{
            CreateFragment fragment = CreateFragment.newInstance(entryId);
            ((INavigationController)getActivity()).moveToFragment(fragment);
        });

        return binding.getRoot();
    }

}
