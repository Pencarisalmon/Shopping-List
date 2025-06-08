package com.kelompok9PBP.ShoppingList.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kelompok9PBP.ShoppingList.R;
import com.kelompok9PBP.ShoppingList.data.firestore.FirestoreHelper;

import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etNama, etTanggal;
    private Button btnSaveProfile;
    private FirebaseFirestore db;
    private String uid;
    private ImageView ivBack;
    private FirestoreHelper firestoreHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());

        // Inisialisasi
        etNama = findViewById(R.id.editTextNamaBarang);
        etTanggal = findViewById(R.id.editTextWaktuBelanja);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firestoreHelper = new FirestoreHelper();

        // Ambil data user dari Firestore
        firestoreHelper.getUserProfile(
                doc -> {
                    if (doc.exists()) {
                        etNama.setText(doc.getString("nama"));
                        etTanggal.setText(doc.getString("tanggal_lahir"));
                    }
                },
                e -> Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
        );

        // Tanggal: buka DatePicker
        etTanggal.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String tanggal = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etTanggal.setText(tanggal);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Simpan ke Firestore
        btnSaveProfile.setOnClickListener(v -> {
            String namaBaru = etNama.getText().toString();
            String tanggalBaru = etTanggal.getText().toString();

            if (namaBaru.isEmpty() || tanggalBaru.isEmpty()) {
                Toast.makeText(this, "Nama atau tanggal tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            firestoreHelper.updateUserProfile(
                    namaBaru,
                    tanggalBaru,
                    unused -> {
                        Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        finish();
                    },
                    e -> Toast.makeText(this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show()
            );
        });
    }
}
