package com.kelompok9PBP.ShoppingList.data.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties; // Opsional, tapi berguna

import java.io.Serializable;

@IgnoreExtraProperties // Opsional: Abaikan properti ekstra di Firestore yang tidak ada di kelas ini

public class Barang implements Serializable {
    private String id;
    private String namaBarang;

    private int jumlah;
    private double hargaSatuan;
    private String kategori;
    private String waktuBelanja;
    private boolean pending;

    public Barang() {
        // Konstruktor kosong yang diperlukan Firebase
    }

//    public Barang(String nama_barang, int jumlah, int harga_satuan, String kategori, String waktu_belanja, boolean pending) {
//        this.nama_barang = nama_barang;
//        this.jumlah = jumlah;
//        this.harga_satuan = harga_satuan;
//        this.kategori = kategori;
//        this.waktu_belanja = waktu_belanja;
//        this.pending = pending;
//    }

    public void setId(String id) {
        this.id = id;
    }
    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }
    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public void setHargaSatuan(double hargaSatuan) {
        this.hargaSatuan = hargaSatuan;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setWaktuBelanja(String waktuBelanja) {
        this.waktuBelanja = waktuBelanja;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public String getId() {
        return id;
    }
    public String getNamaBarang() { return namaBarang; }
    public int getJumlah() { return jumlah; }
    public double getHargaSatuan() { return hargaSatuan; }
    public String getKategori() { return kategori; }
    public String getWaktuBelanja() { return waktuBelanja; }
    public boolean isPending() { return pending; }
}


