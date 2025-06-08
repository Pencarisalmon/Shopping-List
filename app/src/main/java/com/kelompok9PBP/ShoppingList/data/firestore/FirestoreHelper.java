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
import java.util.List;

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
                            barangList.add(barang);
                        }
                    }
                    callback.onSuccess(barangList);
                })
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
}
