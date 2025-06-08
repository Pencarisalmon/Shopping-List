package com.kelompok9PBP.ShoppingList.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kelompok9PBP.ShoppingList.BarangAdapter;
import com.kelompok9PBP.ShoppingList.R;
import com.kelompok9PBP.ShoppingList.data.firestore.FirestoreHelper;
import com.kelompok9PBP.ShoppingList.data.model.Barang;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private BarangAdapter adapter;
    private List<Barang> barangList;

    private TextView tvTotalItem, tvTotalHarga;
    private FirebaseFirestore db;
    private FirestoreHelper firestoreHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.rvBarang);
        tvTotalItem = view.findViewById(R.id.tvTotalItem);
        tvTotalHarga = view.findViewById(R.id.tvTotalHarga);

        barangList = new ArrayList<>();
        adapter = new BarangAdapter(getContext(), barangList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        firestoreHelper = new FirestoreHelper();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        if(currentUser!=null){
            ambilDataBarang();
        }

        return view;
    }

    private void ambilDataBarang() {
        firestoreHelper.getUserBarang(new FirestoreHelper.BarangCallback() {
            @Override
            public void onSuccess(List<Barang> barangListDariRepo) {
                barangList.clear();
                barangList.addAll(barangListDariRepo);

                int totalJumlah = 0;
                int totalHarga = 0;

                for (Barang barang : barangList) {
                    totalJumlah += barang.getJumlah();
                    totalHarga += barang.getJumlah() * barang.getHargaSatuan();

                    Log.d("FirestoreBarang", "Nama: " + barang.getNamaBarang() + ", Jumlah: " + barang.getJumlah() + ", HargaSatuan: " + barang.getHargaSatuan());
                }


                tvTotalItem.setText(String.valueOf(totalJumlah));
                tvTotalHarga.setText("Rp " + totalHarga);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Gagal ambil data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

