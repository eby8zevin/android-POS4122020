package com.ahmadabuhasan.pos.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.pos.R;
import com.ahmadabuhasan.pos.UpdatedataActivity;
import com.ahmadabuhasan.pos.database.DBAccess;
import com.ahmadabuhasan.pos.database.DBHelper;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Ahmad Abu Hasan on 4/12/2020
 */

public class KategoriBarangAdapter extends RecyclerView.Adapter<KategoriBarangAdapter.MyViewHolder> {

    public List<HashMap<String, String>> kategoriData;
    public Context context;

    MediaPlayer player;
    RecyclerView recyclerView;
    TextView tvNoBarang;

    public KategoriBarangAdapter(Context context2, List<HashMap<String, String>> kategoriData2, RecyclerView recyclerView2, TextView tvNoBarang) {
        this.context = context2;
        this.kategoriData = kategoriData2;
        this.recyclerView = recyclerView2;
        this.player = MediaPlayer.create(context2, R.raw.delete_sound);
        this.tvNoBarang = tvNoBarang;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.kategori_barang_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String kategori_id = (String) this.kategoriData.get(position).get(DBHelper.KATEGORI_ID);
        holder.tvKategoriNama.setText((String) this.kategoriData.get(position).get(DBHelper.KATEGORI_LIST));
        holder.cardKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KategoriBarangAdapter.this.player.start();
                DBAccess dbAccess = DBAccess.getInstance(KategoriBarangAdapter.this.context);
                dbAccess.open();
                List<HashMap<String, String>> productList = dbAccess.getTabBarang(kategori_id);
                if (productList.isEmpty()) {
                    KategoriBarangAdapter.this.recyclerView.setVisibility(View.INVISIBLE);
                    KategoriBarangAdapter.this.recyclerView.setVisibility(View.GONE);
                    KategoriBarangAdapter.this.tvNoBarang.setVisibility(View.VISIBLE);
                    return;
                }
                KategoriBarangAdapter.this.recyclerView.setVisibility(View.VISIBLE);
                KategoriBarangAdapter.this.tvNoBarang.setVisibility(View.GONE);
                KategoriBarangAdapter.this.recyclerView.setAdapter(new KasirBarangAdapter(KategoriBarangAdapter.this.context, productList));
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.kategoriData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardKategori;
        TextView tvKategoriNama;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.tvKategoriNama = (TextView) itemView.findViewById(R.id.tvKategoriNama);
            this.cardKategori = (CardView) itemView.findViewById(R.id.card_kategori);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            Intent i = new Intent(KategoriBarangAdapter.this.context, UpdatedataActivity.class);
            i.putExtra(DBHelper.KATEGORI_ID, (String) ((HashMap) KategoriBarangAdapter.this.kategoriData.get(getAdapterPosition())).get(DBHelper.KATEGORI_ID));
            i.putExtra(DBHelper.KATEGORI_LIST, (String) ((HashMap) KategoriBarangAdapter.this.kategoriData.get(getAdapterPosition())).get(DBHelper.KATEGORI_LIST));
        }
    }

}
