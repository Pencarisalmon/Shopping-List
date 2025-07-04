package com.kelompok9PBP.ShoppingList.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kelompok9PBP.ShoppingList.R;
import com.kelompok9PBP.ShoppingList.data.firestore.FirestoreHelper;
import com.kelompok9PBP.ShoppingList.data.model.Barang;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailBarangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailBarangFragment extends Fragment {

    private TextView tvNama, tvJumlah, tvHarga, tvKategori, tvWaktu, tvStatus;
    private Button btnEdit, btnHapus, btnTandaiDibeli;
    private Barang barang;
    private FirestoreHelper firestoreHelper;
    private ImageView ivBack;

    public DetailBarangFragment() {}

    public static DetailBarangFragment newInstance(Barang barang) {
        DetailBarangFragment fragment = new DetailBarangFragment();
        Bundle args = new Bundle();
        args.putSerializable("barang", barang);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_barang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivBack = view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> requireActivity().onBackPressed());

        tvNama = view.findViewById(R.id.tvNamaDetail);
        tvJumlah = view.findViewById(R.id.tvJumlahDetail);
        tvHarga = view.findViewById(R.id.tvHargaDetail);
        tvKategori = view.findViewById(R.id.tvKategoriDetail);
        tvWaktu = view.findViewById(R.id.tvWaktuDetail);
        btnTandaiDibeli = view.findViewById(R.id.btnTandaiDibeli);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnHapus = view.findViewById(R.id.btnHapus);

        firestoreHelper = new FirestoreHelper();

        if (getArguments() != null) {
            barang = (Barang) getArguments().getSerializable("barang");

            tvNama.setText(barang.getNamaBarang());
            tvJumlah.setText(String.valueOf(barang.getJumlah()));
            tvHarga.setText(String.valueOf(barang.getHargaSatuan()));
            tvKategori.setText(barang.getKategori());
            tvWaktu.setText(barang.getWaktuBelanja());
//            tvStatus.setText(barang.isPending() ? "Belum dibeli" : "Sudah dibeli");
        }

        btnEdit.setOnClickListener(v -> {
            Fragment editFragment = EditBarangFragment.newInstance(barang); // Objek Barang yang sedang tampil
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.host_fragment, editFragment)
                    .addToBackStack(null)
                    .commit();
        });

        btnHapus.setOnClickListener(v -> {
            firestoreHelper.deleteBarang(barang.getId(), new FirestoreHelper.DeleteCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getContext(), "Barang berhasil dihapus", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack(); // balik ke list
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getContext(), "Gagal hapus barang: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnTandaiDibeli.setOnClickListener(v -> {
            barang.setPending(false); // Ubah status ke "Sudah dibeli"

            firestoreHelper.updateBarang(barang, new FirestoreHelper.UpdateCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getContext(), "Barang ditandai sudah dibeli", Toast.LENGTH_SHORT).show();
                    // Optional: Update tampilan atau balik ke list
                    requireActivity().getSupportFragmentManager().popBackStack();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getContext(), "Gagal tandai dibeli: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
    }
}