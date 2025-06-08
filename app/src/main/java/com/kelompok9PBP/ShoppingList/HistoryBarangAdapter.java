package com.kelompok9PBP.ShoppingList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompok9PBP.ShoppingList.data.model.Barang;

import java.util.List;
import java.util.Locale;

public class HistoryBarangAdapter extends RecyclerView.Adapter<HistoryBarangAdapter.ViewHolder> {
    private static final String TAG = "HistoryBarangAdapter"; // Tag untuk Log

    private Context context;
    private List<Barang> barangList;
    private OnItemClickListener listener; // Interface listener untuk klik item

    // Interface untuk menangani klik item di History
    public interface OnItemClickListener {
        void onItemClick(Barang barang);
    }

    // Konstruktor
    public HistoryBarangAdapter(Context context, List<Barang> barangList, OnItemClickListener listener) {
        this.context = context;
        this.barangList = barangList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Menggunakan layout item_barang.xml yang sama karena strukturnya mirip
        // Jika Anda ingin tampilan item yang berbeda untuk history, buat layout XML baru
        View view = LayoutInflater.from(context).inflate(R.layout.item_barang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Barang barang = barangList.get(position);

        // Logging data barang (opsional, untuk debugging)
        Log.d(TAG, "History Item " + position + ": "
                + "\nID: " + barang.getId()
                + "\nNama: " + barang.getNamaBarang()
                + "\nJumlah: " + barang.getJumlah()
                + "\nHarga: " + barang.getHargaSatuan()
                + "\nKategori: " + barang.getKategori()
                + "\nWaktu: " + barang.getWaktuBelanja()
                + "\nStatus: " + (barang.isPending() ? "Belum dibeli" : "Sudah dibeli"));

        holder.tvNama.setText(barang.getNamaBarang());
        holder.tvJumlah.setText("Jumlah: " + barang.getJumlah());
        // Format harga menjadi "Rp xxx"
        holder.tvHarga.setText("Harga: Rp " + String.format(Locale.getDefault(), "%,.0f", barang.getHargaSatuan()));
        holder.tvKategori.setText("Kategori: " + barang.getKategori());
        holder.tvWaktu.setText(barang.getWaktuBelanja());
        // Status akan selalu "Sudah dibeli" di history, tapi tetap bisa ditampilkan untuk kejelasan
        holder.tvStatus.setText(barang.isPending() ? "Belum dibeli" : "Sudah dibeli");


        // Set OnClickListener untuk item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(barang); // Panggil listener saat item diklik
            }
        });
    }

    @Override
    public int getItemCount() {
        return barangList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvJumlah, tvHarga, tvKategori, tvWaktu, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNamaBarang);
            tvJumlah = itemView.findViewById(R.id.tvJumlah);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            tvKategori = itemView.findViewById(R.id.tvKategori);
            tvWaktu = itemView.findViewById(R.id.tvTanggal);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}