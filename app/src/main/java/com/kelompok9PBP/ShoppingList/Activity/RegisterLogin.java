package com.kelompok9PBP.ShoppingList.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kelompok9PBP.ShoppingList.R;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterLogin extends AppCompatActivity {
    private final Boolean isRegister = true;
    private View registerTabIndicator, loginTabIndicator;
    private LinearLayout registerTab, loginTab, layoutRegister, layoutLogin;
    private Button btnForm;
    private EditText etName, etRegisterEmail, etTanggal, etRegisterPassword, etConfirmPassword, etLoginEmail, etLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        setupListeners();
    }

    private void setupListeners() {
        registerTab.setOnClickListener(v -> {
            registerTabIndicator.setVisibility(View.VISIBLE);
            loginTabIndicator.setVisibility(View.INVISIBLE);
            layoutRegister.setVisibility(View.VISIBLE);
            layoutLogin.setVisibility(View.GONE);
            btnForm.setText(R.string.daftar);
            clearLoginFields();
        });
        etTanggal.setOnClickListener(v -> showDatePicker());

        loginTab.setOnClickListener(v -> {
            loginTabIndicator.setVisibility(View.VISIBLE);
            registerTabIndicator.setVisibility(View.INVISIBLE);
            layoutLogin.setVisibility(View.VISIBLE);
            layoutRegister.setVisibility(View.GONE);
            btnForm.setText(R.string.masuk);
            clearRegisterFields();
        });
        btnForm.setOnClickListener(v -> {
            if (isRegister) {
                registerData();
            } else {
                loginData();
            }
        });
    }

    private void clearLoginFields() {
        etLoginEmail.setText("");
        etLoginPassword.setText("");
    }

    private void clearRegisterFields() {
        etName.setText("");
        etTanggal.setText("");
        etRegisterEmail.setText("");
        etRegisterPassword.setText("");
        etConfirmPassword.setText("");
    }

    private void loginData() {
        etLoginEmail.setError(null);
        etLoginPassword.setError(null);
        String email = etLoginEmail.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();

        boolean isValid = true;

        if (email.isEmpty()) {
            etLoginEmail.setError("Email harus diisi!");
            isValid = false;
        } else if (isValidEmail(email)) {
            etLoginEmail.setError("Format email tidak valid!");
            isValid = false;
        }
        if (password.isEmpty()) {
            etLoginPassword.setError("Password harus diisi!");
            isValid = false;
        } else if (password.length() < 6) {
            etLoginPassword.setError("Password minimal 6 karakter!");
            isValid = false;
        }

        if (isValid) {
            Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show();
            //TODO: kirim data register
        } else {
            if (email.isEmpty() || isValidEmail(email)) etLoginEmail.requestFocus();
            else if (password.isEmpty() || password.length() < 6) etLoginPassword.requestFocus();

            Toast.makeText(this, "Periksa kembali input Anda.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etTanggal.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void registerData() {
        etName.setError(null);
        etTanggal.setError(null);
        etRegisterEmail.setError(null);
        etRegisterPassword.setError(null);
        etConfirmPassword.setError(null);
        String nama = etName.getText().toString().trim();
        String tanggal = etTanggal.getText().toString().trim();
        String email = etRegisterEmail.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();
        String konfirmasiPassword = etConfirmPassword.getText().toString().trim();

        boolean isValid = true;

        if (nama.isEmpty()) {
            etName.setError("Nama lengkap harus diisi!");
            isValid = false;
        }
        if (tanggal.isEmpty()) {
            etTanggal.setError("Tanggal lahir harus diisi!");
            isValid = false;
        }
        if (email.isEmpty()) {
            etRegisterEmail.setError("Email harus diisi!");
            isValid = false;

        } else if (isValidEmail(email)) {
            etRegisterEmail.setError("Format email tidak valid!");
            isValid = false;
        }
        if (password.isEmpty()) {
            etRegisterPassword.setError("Password harus diisi!");
            isValid = false;
        } else if (password.length() < 6) {
            etRegisterPassword.setError("Password minimal 6 karakter!");
            isValid = false;
        }
        if (konfirmasiPassword.isEmpty()) {
            etConfirmPassword.setError("Konfirmasi password harus diisi!");
            isValid = false;
        } else if (!password.equals(konfirmasiPassword)) {
            etConfirmPassword.setError("Password dan Konfirmasi Password tidak cocok!");
            isValid = false;
        }

        if (isValid) {
            Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show();
            //TODO: kirim data register
        } else {
            if (nama.isEmpty()) etName.requestFocus();
            else if (tanggal.isEmpty()) etTanggal.requestFocus();
            else if (email.isEmpty() || isValidEmail(email)) etRegisterEmail.requestFocus();
            else if (password.isEmpty() || password.length() < 6) etRegisterPassword.requestFocus();
            else if (konfirmasiPassword.isEmpty() || !password.equals(konfirmasiPassword))
                etConfirmPassword.requestFocus();

            Toast.makeText(this, "Periksa kembali input Anda.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }


    private void initViews() {
        registerTab = findViewById(R.id.register_tab);
        loginTab = findViewById(R.id.login_tab);
        registerTabIndicator = findViewById(R.id.view_register_tab);
        loginTabIndicator = findViewById(R.id.view_login_tab);

        layoutRegister = findViewById(R.id.layout_register);
        layoutLogin = findViewById(R.id.layout_login);

        btnForm = findViewById(R.id.btn_form);

        etName = findViewById(R.id.edt_nama_lengkap);
        etTanggal = findViewById(R.id.edt_tanggal_lahir);
        etRegisterEmail = findViewById(R.id.edt_email_register);
        etRegisterPassword = findViewById(R.id.edt_password_register);
        etConfirmPassword = findViewById(R.id.edt_konfirmasi_password);

        etLoginEmail = findViewById(R.id.edt_email_login);
        etLoginPassword = findViewById(R.id.edt_password_login);
    }
}