package com.kelompok9PBP.ShoppingList.Fragment;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kelompok9PBP.ShoppingList.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class AddFragment extends Fragment {
    private TextInputEditText editTextNamaBarang, editTextJumlah, editTextHarga, editTextWaktuBelanja;
    private AutoCompleteTextView autoCompleteKategori;
    private MaterialButton buttonSimpan;
    private final Calendar myCalendar = Calendar.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String[] kategoriArray = new String[]{"Makanan", "Minuman", "Pakaian", "Elektronik", "Rumah Tangga", "Lainnya"};

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        editTextNamaBarang = view.findViewById(R.id.editTextNamaBarang);
        editTextJumlah = view.findViewById(R.id.editTextJumlah);
        autoCompleteKategori = view.findViewById(R.id.autoCompleteKategori);
        editTextHarga = view.findViewById(R.id.editTextHarga);
        editTextWaktuBelanja = view.findViewById(R.id.editTextWaktuBelanja);
        buttonSimpan = view.findViewById(R.id.buttonSimpan);

        dropdown();
        tanggalPicker();

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextNamaBarang.setError(null);
                editTextJumlah.setError(null);
                autoCompleteKategori.setError(null);
                editTextHarga.setError(null);
                editTextWaktuBelanja.setError(null);

                String namaBarang = editTextNamaBarang.getText().toString().trim();
                String jumlahStr = editTextJumlah.getText().toString().trim();
                String kategori = autoCompleteKategori.getText().toString().trim();
                String hargaStr = editTextHarga.getText().toString().trim();
                String waktuBelanja = editTextWaktuBelanja.getText().toString().trim();

                boolean isValid = true;

                if (TextUtils.isEmpty(namaBarang)) {
                    editTextNamaBarang.setError("Nama barang tidak boleh kosong");
                    isValid = false;
                }

                if (TextUtils.isEmpty(jumlahStr)) {
                    editTextJumlah.setError("Jumlah tidak boleh kosong");
                    isValid = false;
                } else {
                    try {
                        Integer.parseInt(jumlahStr);
                    } catch (NumberFormatException e) {
                        editTextJumlah.setError("Jumlah tidak valid");
                        isValid = false;
                    }
                }

                if (TextUtils.isEmpty(kategori)) {
                    autoCompleteKategori.setError("Kategori harus dipilih", null);
                    isValid = false;
                }

                if (TextUtils.isEmpty(hargaStr)) {
                    editTextHarga.setError("Harga tidak boleh kosong");
                    isValid = false;
                } else {
                    try {
                        Double.parseDouble(hargaStr);
                    } catch (NumberFormatException e) {
                        editTextHarga.setError("Harga tidak valid");
                        isValid = false;
                    }
                }

                if (TextUtils.isEmpty(waktuBelanja)) {
                    editTextWaktuBelanja.setError("Waktu belanja harus dipilih");
                    isValid = false;
                }

                if (isValid) {
                    int jumlah = Integer.parseInt(jumlahStr);
                    double harga = Double.parseDouble(hargaStr);

                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    String userId = currentUser.getUid();

                    Map<String, Object> belanjaan = new HashMap<>();
                    belanjaan.put("namaBarang", namaBarang);
                    belanjaan.put("jumlah", jumlah);
                    belanjaan.put("kategori", kategori);
                    belanjaan.put("hargaSatuan", harga);
                    belanjaan.put("waktuBelanja", waktuBelanja);
                    belanjaan.put("pending", true);

                    db.collection("users")
                            .document(userId)
                            .collection("shopping_items")
                            .add(belanjaan)
                            .addOnSuccessListener(documentReference -> {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                editTextNamaBarang.setText("");
                                editTextJumlah.setText("");
                                autoCompleteKategori.setText("", false);
                                editTextHarga.setText("");
                                editTextWaktuBelanja.setText("");
                                myCalendar.setTimeInMillis(System.currentTimeMillis());

                                editTextNamaBarang.clearFocus();
                                editTextJumlah.clearFocus();
                                autoCompleteKategori.clearFocus();
                                editTextHarga.clearFocus();
                                editTextWaktuBelanja.clearFocus();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(requireContext(), "Gagal menyimpan belanjaan: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                Log.w(TAG, "Error adding document", e);
                            });
                }
            }
        });
        return view;
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
                editTextWaktuBelanja.setText(sdf.format(myCalendar.getTime()));
            }
        };

        editTextWaktuBelanja.setOnClickListener(new View.OnClickListener() {
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
        autoCompleteKategori.setAdapter(adapter);
    }

}