package com.kelompok9PBP.ShoppingList;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.kelompok9PBP.ShoppingList.Fragment.DetailBarangFragment;
import com.kelompok9PBP.ShoppingList.data.model.Barang;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.ViewHolder> {
    private Context context;
    private List<Barang> barangList;

    public BarangAdapter(Context context, List<Barang> barangList) {
        this.context = context;
        this.barangList = barangList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_barang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Barang barang = barangList.get(position);

        // Logging data barang
        Log.d(TAG, "Barang ke-" + position + ": "
                + "\nNama: " + barang.getNamaBarang()
                + "\nJumlah: " + barang.getJumlah()
                + "\nHarga: " + barang.getHargaSatuan()
                + "\nKategori: " + barang.getKategori()
                + "\nWaktu: " + barang.getWaktuBelanja()
                + "\nStatus: " + (barang.isPending() ? "Belum dibeli" : "Sudah dibeli"));

        holder.tvNama.setText(barang.getNamaBarang());
        holder.tvJumlah.setText("Jumlah: " + barang.getJumlah());
        holder.tvHarga.setText("Harga: Rp " + barang.getHargaSatuan());
        holder.tvKategori.setText("Kategori: " + barang.getKategori());
        holder.tvWaktu.setText(barang.getWaktuBelanja());
        holder.tvStatus.setText(barang.isPending() ? "Belum dibeli" : "Sudah dibeli");

        holder.itemView.setOnClickListener(v -> {
            Fragment detailFragment = DetailBarangFragment.newInstance(barang);
            ((AppCompatActivity) context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.host_fragment, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return barangList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
