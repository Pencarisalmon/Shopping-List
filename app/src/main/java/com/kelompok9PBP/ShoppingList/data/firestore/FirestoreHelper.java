package com.kelompok9PBP.ShoppingList.data.firestore;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Task;
import com.kelompok9PBP.ShoppingList.data.model.Barang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreHelper {

    private FirebaseFirestore db;
    private final FirebaseUser currentUser;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public interface BarangCallback {
        void onSuccess(List<Barang> barangList);
        void onFailure(Exception e);
    }

    public void getUserBarang(BarangCallback callback) {

        if (currentUser == null) {
            callback.onFailure(new Exception("User belum login"));
            return;
        }

        String userId = currentUser.getUid();
        db.collection("users")
                .document(userId)
                .collection("shopping_items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Barang> barangList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Barang barang = doc.toObject(Barang.class);
                        if (barang != null) {
                            barang.setId(doc.getId());
                            barangList.add(barang);
                        }
                    }
                    callback.onSuccess(barangList);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public interface DeleteCallback {
        void onSuccess();
        void onFailure(Exception e);
    }
    public void deleteBarang(String barangId, DeleteCallback callback) {
        if (currentUser == null) {
            callback.onFailure(new Exception("User belum login"));
            return;
        }

        String userId = currentUser.getUid();
        db.collection("users")
                .document(userId)
                .collection("shopping_items")
                .document(barangId)
                .delete()
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(callback::onFailure);
    }

    // Contoh method ambil collection reference
    public CollectionReference getCollection(String collectionName) {
        return db.collection(collectionName);
    }

    // Contoh method untuk dapatkan data dengan query
    public Task<QuerySnapshot> getDataWithQuery(String collectionName, Query query) {
        return query.get();
    }

    // Contoh method untuk insert data
    public Task<Void> addDocument(String collectionName, Object data) {
        return db.collection(collectionName).add(data).continueWith(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return null;
        });
    }

    // Contoh method update document by id
    public Task<Void> updateDocument(String collectionName, String documentId, Object data) {
        return db.collection(collectionName).document(documentId).set(data);
    }

    // Contoh method hapus document by id
    public Task<Void> deleteDocument(String collectionName, String documentId) {
        return db.collection(collectionName).document(documentId).delete();
    }

    public void getUserProfile(OnSuccessListener<DocumentSnapshot> onSuccess, OnFailureListener onFailure) {
        if (currentUser == null) {
            onFailure.onFailure(new Exception("User belum login"));
            return;
        }

        db.collection("users").document(currentUser.getUid())
                .get()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public void updateUserProfile(String nama, String tanggalLahir,
                                  OnSuccessListener<Void> onSuccessListener,
                                  OnFailureListener onFailureListener) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> updates = new HashMap<>();
        updates.put("nama", nama);
        updates.put("tanggal_lahir", tanggalLahir);

        db.collection("users").document(uid)
                .update(updates)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void saveUserData(String uid, String nama, String tanggalLahir, String email,
                             OnSuccessListener<Void> onSuccessListener,
                             OnFailureListener onFailureListener) {

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("uid", uid);
        userMap.put("nama", nama);
        userMap.put("tanggal_lahir", tanggalLahir);
        userMap.put("email", email);

        db.collection("users").document(uid)
                .set(userMap)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

}
