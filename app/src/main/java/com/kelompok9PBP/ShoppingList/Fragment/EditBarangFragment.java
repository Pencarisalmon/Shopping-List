package com.kelompok9PBP.ShoppingList.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.kelompok9PBP.ShoppingList.R;
import com.kelompok9PBP.ShoppingList.data.firestore.FirestoreHelper;
import com.kelompok9PBP.ShoppingList.data.model.Barang;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditBarangFragment extends Fragment {

    private EditText etNama, etJumlah, etHarga, etWaktu;
    private AutoCompleteTextView etKategori;
    private Button btnSave, btnCancel;

    private Barang barang;
    private final Calendar myCalendar = Calendar.getInstance();

    String[] kategoriArray = new String[]{"Makanan", "Minuman", "Pakaian", "Elektronik", "Rumah Tangga", "Lainnya"};
    private FirestoreHelper firestoreHelper;

    public EditBarangFragment() {
        // Required empty public constructor
    }

    public static EditBarangFragment newInstance(Barang barang) {
        EditBarangFragment fragment = new EditBarangFragment();
        Bundle args = new Bundle();
        args.putSerializable("barang", barang);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_barang, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNama = view.findViewById(R.id.etNama);
        etJumlah = view.findViewById(R.id.etJumlah);
        etHarga = view.findViewById(R.id.etHarga);
        etKategori = view.findViewById(R.id.etKategori);
        etWaktu = view.findViewById(R.id.etWaktu);
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);

        firestoreHelper = new FirestoreHelper();

        if (getArguments() != null) {
            barang = (Barang) getArguments().getSerializable("barang");

            if (barang != null) {
                etNama.setText(barang.getNamaBarang());
                etJumlah.setText(String.valueOf(barang.getJumlah()));
                etHarga.setText(String.valueOf(barang.getHargaSatuan()));
                etKategori.setText(barang.getKategori());
                etWaktu.setText(barang.getWaktuBelanja());
            }
        }

        btnSave.setOnClickListener(v -> saveBarang());
        btnCancel.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        dropdown();
        tanggalPicker();
    }

    private void saveBarang() {
        String nama = etNama.getText().toString().trim();
        String jumlahStr = etJumlah.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();
        String kategori = etKategori.getText().toString().trim();
        String waktu = etWaktu.getText().toString().trim();

        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(jumlahStr) ||
                TextUtils.isEmpty(hargaStr) || TextUtils.isEmpty(kategori) || TextUtils.isEmpty(waktu)) {
            Toast.makeText(requireContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        int jumlah;
        double harga;
        try {
            jumlah = Integer.parseInt(jumlahStr);
            harga = Double.parseDouble(hargaStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Jumlah dan Harga harus angka yang valid", Toast.LENGTH_SHORT).show();
            return;
        }

        barang.setNamaBarang(nama);
        barang.setJumlah(jumlah);
        barang.setHargaSatuan(harga);
        barang.setKategori(kategori);
        barang.setWaktuBelanja(waktu);

        // Panggil FirestoreHelper update method
        firestoreHelper.updateBarang(barang, new FirestoreHelper.UpdateCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(requireContext(), "Barang berhasil diupdate", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack(); // balik ke halaman sebelumnya
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(requireContext(), "Gagal update barang: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tanggalPicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                etWaktu.setText(sdf.format(myCalendar.getTime()));
            }
        };

        etWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(requireContext(), dateSetListener,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void dropdown() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                kategoriArray
        );
        etKategori.setAdapter(adapter);
    }
}
