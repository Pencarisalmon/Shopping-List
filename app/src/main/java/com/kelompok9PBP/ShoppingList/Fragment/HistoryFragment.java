package com.kelompok9PBP.ShoppingList.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kelompok9PBP.ShoppingList.HistoryBarangAdapter; // Import HistoryBarangAdapter
import com.kelompok9PBP.ShoppingList.R;
import com.kelompok9PBP.ShoppingList.data.firestore.FirestoreHelper;
import com.kelompok9PBP.ShoppingList.data.model.Barang;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private static final String TAG = "HistoryFragment";

    private RecyclerView recyclerView;
    private HistoryBarangAdapter adapter; // Menggunakan HistoryBarangAdapter
    private List<Barang> barangList;

    private TextView tvNoHistory;
    private ProgressBar progressBar;

    private FirestoreHelper firestoreHelper;
    private FirebaseAuth mAuth;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = view.findViewById(R.id.rvHistoryBarang);
        tvNoHistory = view.findViewById(R.id.tvNoHistory);
        progressBar = view.findViewById(R.id.progressBarHistory);

        barangList = new ArrayList<>();
        // Inisialisasi HistoryBarangAdapter dengan listener yang akan membuka DetailHistoryFragment
        adapter = new HistoryBarangAdapter(getContext(), barangList, new HistoryBarangAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Barang barang) {
                if (getActivity() != null) {
                    DetailHistoryFragment detailFragment = DetailHistoryFragment.newInstance(barang);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.host_fragment, detailFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        firestoreHelper = new FirestoreHelper();
        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            ambilDataHistoryBarang();
        } else {
            Toast.makeText(getContext(), "Silakan login untuk melihat riwayat.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            tvNoHistory.setVisibility(View.VISIBLE);
            tvNoHistory.setText("Silakan login untuk melihat riwayat.");
            barangList.clear();
            adapter.notifyDataSetChanged();
        }
        if (getActivity() != null && getActivity().findViewById(R.id.bottom_navigation) != null) {
            getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        }
    }

    private void ambilDataHistoryBarang() {
        progressBar.setVisibility(View.VISIBLE);
        tvNoHistory.setVisibility(View.GONE);

        firestoreHelper.getUserBarangPending(false, new FirestoreHelper.BarangCallback() {
            @Override
            public void onSuccess(List<Barang> barangListDariRepo) {
                progressBar.setVisibility(View.GONE);
                barangList.clear();
                barangList.addAll(barangListDariRepo);

                if (barangList.isEmpty()) {
                    tvNoHistory.setVisibility(View.VISIBLE);
                    tvNoHistory.setText("Belum ada riwayat belanja.");
                } else {
                    tvNoHistory.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                progressBar.setVisibility(View.GONE);
                tvNoHistory.setVisibility(View.VISIBLE);
                tvNoHistory.setText("Gagal memuat riwayat: " + e.getMessage());
                Toast.makeText(getContext(), "Gagal memuat riwayat: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error loading history items: " + e.getMessage(), e);
            }
        });
    }
}