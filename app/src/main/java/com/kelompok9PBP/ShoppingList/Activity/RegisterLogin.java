package com.kelompok9PBP.ShoppingList.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.kelompok9PBP.ShoppingList.MainActivity;
import com.kelompok9PBP.ShoppingList.R;

import com.kelompok9PBP.ShoppingList.data.auth.AuthHelper;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterLogin extends AppCompatActivity {
    private boolean isRegister = true;
    private View registerTabIndicator, loginTabIndicator;
    private LinearLayout registerTab, loginTab, layoutRegister, layoutLogin;
    private Button btnForm;
    private EditText etName, etRegisterEmail, etTanggal, etRegisterPassword, etConfirmPassword, etLoginEmail, etLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(RegisterLogin.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

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
            isRegister = true;
            registerTabIndicator.setVisibility(View.VISIBLE);
            loginTabIndicator.setVisibility(View.INVISIBLE);
            layoutRegister.setVisibility(View.VISIBLE);
            layoutLogin.setVisibility(View.GONE);
            btnForm.setText(R.string.daftar);
            clearLoginFields();
        });

        loginTab.setOnClickListener(v -> {
            isRegister = false;
            loginTabIndicator.setVisibility(View.VISIBLE);
            registerTabIndicator.setVisibility(View.INVISIBLE);
            layoutLogin.setVisibility(View.VISIBLE);
            layoutRegister.setVisibility(View.GONE);
            btnForm.setText(R.string.masuk);
            clearRegisterFields();
        });

        etTanggal.setOnClickListener(v -> showDatePicker());

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
        String email = etLoginEmail.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etLoginEmail.setError("Email harus diisi!");
            etLoginEmail.requestFocus();
            return;
        }

        if (!isEmailFormatValid(email)) {
            etLoginEmail.setError("Format email tidak valid!");
            etLoginEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etLoginPassword.setError("Password harus diisi!");
            etLoginPassword.requestFocus();
            return;
        }

        AuthHelper authHelper = new AuthHelper(this);
        authHelper.loginUser(email, password, new AuthHelper.LoginCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterLogin.this, "Login berhasil!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterLogin.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(RegisterLogin.this, "Login gagal: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerData() {
        String nama = etName.getText().toString().trim();
        String tanggal = etTanggal.getText().toString().trim();
        String email = etRegisterEmail.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();
        String konfirmasiPassword = etConfirmPassword.getText().toString().trim();

        if (nama.isEmpty()) {
            etName.setError("Nama lengkap harus diisi!");
            etName.requestFocus();
            return;
        }

        if (tanggal.isEmpty()) {
            etTanggal.setError("Tanggal lahir harus diisi!");
            etTanggal.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etRegisterEmail.setError("Email harus diisi!");
            etRegisterEmail.requestFocus();
            return;
        }

        if (!isEmailFormatValid(email)) {
            etRegisterEmail.setError("Format email tidak valid!");
            etRegisterEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etRegisterPassword.setError("Password harus diisi!");
            etRegisterPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etRegisterPassword.setError("Password minimal 6 karakter!");
            etRegisterPassword.requestFocus();
            return;
        }

        if (!password.equals(konfirmasiPassword)) {
            etConfirmPassword.setError("Password dan konfirmasi tidak cocok!");
            etConfirmPassword.requestFocus();
            return;
        }

        AuthHelper authHelper = new AuthHelper(this);
        authHelper.registerUser(nama, tanggal, email, password, new AuthHelper.RegisterCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterLogin.this, "Registrasi berhasil. Silakan login.", Toast.LENGTH_SHORT).show();
                // Beralih ke form login
                isRegister = false;
                loginTabIndicator.setVisibility(View.VISIBLE);
                registerTabIndicator.setVisibility(View.INVISIBLE);
                layoutLogin.setVisibility(View.VISIBLE);
                layoutRegister.setVisibility(View.GONE);
                btnForm.setText(R.string.masuk);
                clearRegisterFields();
                clearLoginFields();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(RegisterLogin.this, "Registrasi gagal: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isEmailFormatValid(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
