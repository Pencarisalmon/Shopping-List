package com.kelompok9PBP.ShoppingList.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kelompok9PBP.ShoppingList.R;
import com.kelompok9PBP.ShoppingList.data.model.Barang;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailBarangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailBarangFragment extends Fragment {

    private TextView tvNama, tvJumlah, tvHarga, tvKategori, tvWaktu, tvStatus;
    private Button btnEdit, btnHapus;
    private Barang barang;

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

        tvNama = view.findViewById(R.id.tvNamaDetail);
        tvJumlah = view.findViewById(R.id.tvJumlahDetail);
        tvHarga = view.findViewById(R.id.tvHargaDetail);
        tvKategori = view.findViewById(R.id.tvKategoriDetail);
        tvWaktu = view.findViewById(R.id.tvWaktuDetail);
//        tvStatus = view.findViewById(R.id.tvStatusDetail);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnHapus = view.findViewById(R.id.btnHapus);

        if (getArguments() != null) {
            barang = (Barang) getArguments().getSerializable("barang");

            tvNama.setText(barang.getNamaBarang());
            tvJumlah.setText(barang.getJumlah());
            tvHarga.setText(barang.getHargaSatuan());
            tvKategori.setText(barang.getKategori());
            tvWaktu.setText(barang.getWaktuBelanja());
//            tvStatus.setText(barang.isPending() ? "Belum dibeli" : "Sudah dibeli");
        }

        btnEdit.setOnClickListener(v -> {
            // Nanti tambahin logic edit
        });

        btnHapus.setOnClickListener(v -> {
            // Nanti tambahin logic hapus
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