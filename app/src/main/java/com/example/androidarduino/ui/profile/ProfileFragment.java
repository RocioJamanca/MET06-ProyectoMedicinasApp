//package com.example.androidarduino.ui.profile;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.example.androidarduino.MainMenu;
//import com.example.androidarduino.User;
//import com.example.androidarduino.databinding.FragmentProfileBinding;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//
//public class ProfileFragment extends Fragment {
//    User user;
//    private ProfileViewModel profileViewModel;
//    private FragmentProfileBinding binding;
//    String userEmail;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//
//
//
//        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
//
//        binding = FragmentProfileBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        userEmail = getActivity().getIntent().getStringExtra("email");
//
//        final TextView nameProfile = binding.textNameProfile;
//        final TextView surnameProfile = binding.textSurnameProfile;
//        final TextView ageProfile = binding.textAgeProfile;
//        final TextView emailProfile = binding.textEmailProfile;
//
//        final Button changePass = binding.btnChangePassProfile;
//        final Button editProfile = binding.btnEditProfileProfile;
//
//
//
//        //Datos de usuario
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myUserReference = database.getReference("usuarios");
//
//        Query query = myUserReference.orderByChild("email").equalTo(userEmail);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                for (DataSnapshot data : snapshot.getChildren()) {
//                    String key = data.getKey();
//                    String path = "usuarios/" + key;
//                    DatabaseReference myUser = database.getReference("path");
//                    myUser.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                            User user1 = snapshot.getValue(User.class);
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
//
//
//        nameProfile.setText(user.name);
//
//
//
//        changePass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        editProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//
//
//
//
//
//
//
//        return root;
//
//
//
//
//    }// End On Create
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//
//
//
//
//}
//
