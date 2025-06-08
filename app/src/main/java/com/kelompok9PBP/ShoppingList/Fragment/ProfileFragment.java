package com.kelompok9PBP.ShoppingList.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.kelompok9PBP.ShoppingList.Activity.EditProfileActivity;
import com.kelompok9PBP.ShoppingList.Activity.RegisterLogin;
import com.kelompok9PBP.ShoppingList.R;

import com.kelompok9PBP.ShoppingList.data.firestore.FirestoreHelper;

import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {

    private TableRow trLogout;
    private TextView tvName, tvEmail, tvDob;
    private final FirestoreHelper firestoreHelper = new FirestoreHelper();
    private String cachedName = null;
    private String cachedEmail = null;
    private String cachedDob = null;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        trLogout = view.findViewById(R.id.tr_logout);
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvDob = view.findViewById(R.id.tvDob);

        MaterialButton btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        // Kalau data sudah ada di cache, langsung tampilkan dulu
        if (cachedName != null && cachedEmail != null && cachedDob != null) {
            tvName.setText(cachedName);
            tvEmail.setText(cachedEmail);
            tvDob.setText(cachedDob);
        } else {
            loadDataFromFirestore();
        }

        trLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), RegisterLogin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }

    private void loadDataFromFirestore() {
        firestoreHelper.getUserProfile(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                cachedName = documentSnapshot.getString("nama");
                cachedEmail = documentSnapshot.getString("email");
                cachedDob = documentSnapshot.getString("tanggal_lahir");

                tvName.setText(cachedName);
                tvEmail.setText(cachedEmail);
                tvDob.setText("🎂 " + cachedDob);
            }
        }, e -> {
            tvName.setText("Gagal ambil data");
            tvEmail.setText("-");
            tvDob.setText("-");
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataFromFirestore(); // Selalu reload data dari Firestore pas Fragment muncul lagi
    }

}
