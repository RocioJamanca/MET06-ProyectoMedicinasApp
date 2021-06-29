package com.example.androidarduino.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigator;

import com.example.androidarduino.R;
import com.example.androidarduino.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView nameProfile = binding.textNameProfile;
        final TextView surnameProfile = binding.textSurnameProfile;
        final TextView ageProfile = binding.textAgeProfile;
        final TextView emailProfile = binding.textEmailProfile;

        final Button changePass = binding.btnChangePassProfile;


        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });







        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                nameProfile.setText(s);
            }
        });
        return root;




    }// End On Create

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}