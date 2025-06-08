package com.kelompok9PBP.ShoppingList.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kelompok9PBP.ShoppingList.R;
import com.kelompok9PBP.ShoppingList.data.firestore.FirestoreHelper;
import com.kelompok9PBP.ShoppingList.data.model.Barang;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailHistoryFragment extends Fragment {
    private static final String TAG = "DetailHistoryFragment";

    private TextView tvNama, tvJumlah, tvHarga, tvKategori, tvWaktu, tvStatus;
    private Button btnPulihkan, btnHapus; // ID tombol yang sesuai di layout fragment_detail_history.xml
    private Barang barang;
    private FirestoreHelper firestoreHelper;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db; // Untuk logging aktivitas
    private ImageView ivBack;

    public DetailHistoryFragment() {
        // Required empty public constructor
    }

    public static DetailHistoryFragment newInstance(Barang barang) {
        DetailHistoryFragment fragment = new DetailHistoryFragment();
        Bundle args = new Bundle();
        args.putSerializable("barang", barang); // Pastikan Barang implements Serializable
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_history, container, false); // Pastikan nama layout ini benar
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivBack = view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> requireActivity().onBackPressed());

        // --- UBAH ID DI SINI AGAR SESUAI DENGAN XML YANG BARU ---
        tvNama = view.findViewById(R.id.history_tvNamaDetail);
        tvJumlah = view.findViewById(R.id.history_tvJumlahDetail);
        tvHarga = view.findViewById(R.id.history_tvHargaDetail);
        tvKategori = view.findViewById(R.id.history_tvKategoriDetail);
        tvWaktu = view.findViewById(R.id.history_tvWaktuDetail);
        tvStatus = view.findViewById(R.id.history_tvStatusDetail); // Tampilkan status
        btnPulihkan = view.findViewById(R.id.history_btnPulihkan); // Sesuaikan dengan ID di layout Anda
        btnHapus = view.findViewById(R.id.history_btnHapus); // Sesuaikan dengan ID di layout Anda
        // --- AKHIR PERUBAHAN ID ---


        firestoreHelper = new FirestoreHelper();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Inisialisasi Firestore untuk log aktivitas

        if (getArguments() != null) {
            barang = (Barang) getArguments().getSerializable("barang");

            if (barang != null) {
                tvNama.setText(barang.getNamaBarang());
                tvJumlah.setText(String.valueOf(barang.getJumlah()));
                tvHarga.setText("Rp " + String.format(Locale.getDefault(), "%,.0f", barang.getHargaSatuan())); // Format harga
                tvKategori.setText(barang.getKategori());
                tvWaktu.setText(barang.getWaktuBelanja()); // `waktuBelanja` masih String
                tvStatus.setText(barang.isPending() ? "Belum dibeli" : "Sudah dibeli");
            }
        }

        btnPulihkan.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(requireContext(), "User belum login.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (barang != null && barang.getId() != null) {
                barang.setPending(true); // Ubah status menjadi "Belum dibeli"

                firestoreHelper.updateBarang(barang, new FirestoreHelper.UpdateCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(requireContext(), "Barang berhasil dipulihkan ke daftar belanja!", Toast.LENGTH_SHORT).show();
                        tvStatus.setText("Belum dibeli"); // Update UI secara langsung
                        // Kembali ke HistoryFragment atau ke HomeFragment jika ingin pengguna langsung melihat daftar belanja
                        requireActivity().getSupportFragmentManager().popBackStack();

                        // Tambahkan log aktivitas
                        addActivityLog(currentUser.getUid(), currentUser.getEmail(), "Pulihkan Status Barang", barang.getId(), barang.getNamaBarang(), null);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(requireContext(), "Gagal memulihkan status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error restoring status: " + e.getMessage(), e);
                    }
                });
            } else {
                Toast.makeText(requireContext(), "Barang tidak ditemukan untuk dipulihkan.", Toast.LENGTH_SHORT).show();
            }
        });

        btnHapus.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(requireContext(), "User belum login.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (barang != null && barang.getId() != null) {
                firestoreHelper.deleteBarang(barang.getId(), new FirestoreHelper.DeleteCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(requireContext(), "Barang berhasil dihapus dari riwayat!", Toast.LENGTH_SHORT).show();
                        // Kembali ke HistoryFragment setelah menghapus
                        requireActivity().getSupportFragmentManager().popBackStack();

                        // Tambahkan log aktivitas
                        addActivityLog(currentUser.getUid(), currentUser.getEmail(), "Hapus Belanjaan dari Riwayat", barang.getId(), barang.getNamaBarang(), null);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(requireContext(), "Gagal menghapus barang: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error deleting barang: " + e.getMessage(), e);
                    }
                });
            } else {
                Toast.makeText(requireContext(), "Barang tidak ditemukan untuk dihapus.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metode helper untuk menambahkan log aktivitas (sama seperti yang kita diskusikan sebelumnya)
    private void addActivityLog(String userId, String userEmail, String action, String itemId, String itemName, Map<String, Object> details) {
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("userId", userId);
        logEntry.put("userEmail", userEmail);
        logEntry.put("action", action);
        logEntry.put("itemId", itemId);
        logEntry.put("itemName", itemName);
        logEntry.put("timestamp", com.google.firebase.firestore.FieldValue.serverTimestamp());
        if (details != null) {
            logEntry.put("details", details);
        }

        db.collection("activity_logs")
                .add(logEntry)
                .addOnSuccessListener(logDocRef -> Log.d(TAG, "Activity log added with ID: " + logDocRef.getId()))
                .addOnFailureListener(e -> Log.e(TAG, "Error adding activity log", e));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Sembunyikan bottom navigation saat di detail history
        if (getActivity() != null && getActivity().findViewById(R.id.bottom_navigation) != null) {
            getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // Tampilkan kembali bottom navigation saat meninggalkan detail history
        if (getActivity() != null && getActivity().findViewById(R.id.bottom_navigation) != null) {
            getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        }
    }
}