package com.kelompok9PBP.ShoppingList.data.auth;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kelompok9PBP.ShoppingList.data.firestore.FirestoreHelper;

public class AuthHelper {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Context context;

    public AuthHelper(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void registerUser(String name, String birthDate, String email, String password, RegisterCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        FirestoreHelper firestoreHelper = new FirestoreHelper();
                        firestoreHelper.saveUserData(
                                user.getUid(),
                                name,
                                birthDate,
                                email,
                                unused -> callback.onSuccess(),
                                e -> callback.onFailure(e.getMessage())
                        );

                    } else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }

    public void loginUser(String email, String password, LoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }

    public interface RegisterCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public interface LoginCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
