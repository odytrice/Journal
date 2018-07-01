package com.example.journal.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.journal.core.models.Entry;
import com.example.journal.core.models.User;
import com.example.journal.databinding.FragmentCreateBinding;
import com.example.journal.R;

import java.util.Date;

public class CreateFragment extends Fragment {


    private int entryId = 0;
    private DetailsViewModel _model;
    private User _user;

    public CreateFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CreateFragment newInstance(int entryId) {
        CreateFragment fragment = new CreateFragment();
        Bundle args = new Bundle();
        args.putInt("entryId", entryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entryId = getArguments().getInt("entryId");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentCreateBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create, container, false);

        _model = ViewModelProviders.of(getActivity(), new ViewModelFactory(getContext())).get(DetailsViewModel.class);

        _model.getUser().observe(this, user -> {
            _user = user;
        });



        if (entryId > 0) {
            _model.getEntry(entryId).observe(this, entry -> {
                if (entry != null) {
                    ((INavigationController)getActivity()).setTitle(entry.title);
                    binding.txtTitle.setText(entry.title);
                    binding.txtText.setText(entry.text);
                }
            });
        }
        else {
            ((INavigationController)getActivity()).setTitle("New Entry");
        }

        _model.fetchUser();

        binding.btnSave.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.txtText.getText())) {
                Toast.makeText(getContext(), "The Description Field is Required", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.txtTitle.getText())) {
                Toast.makeText(getContext(), "The Title Field is Required", Toast.LENGTH_SHORT).show();
            } else {
                Entry entry = new Entry();


                if (_user == null) {
                    Toast.makeText(getContext(), "User is not Logged In", Toast.LENGTH_SHORT).show();
                }
                entry.entryId = entryId;
                entry.userId = _user.userId;
                entry.title = binding.txtTitle.getText().toString();
                entry.text = binding.txtText.getText().toString();
                entry.createdDate = new Date();

                _model.addEntry(entry);
                getActivity().finish();
            }
        });

        return binding.getRoot();
    }
}
