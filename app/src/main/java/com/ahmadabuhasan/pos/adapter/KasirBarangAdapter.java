package com.ahmadabuhasan.pos.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.internal.view.SupportMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.pos.KasirActivity;
import com.ahmadabuhasan.pos.R;
import com.ahmadabuhasan.pos.UpdatedataActivity;
import com.ahmadabuhasan.pos.database.DBAccess;
import com.ahmadabuhasan.pos.database.DBHelper;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by Ahmad Abu Hasan on 4/12/2020
 */

public class KasirBarangAdapter extends RecyclerView.Adapter<KasirBarangAdapter.MyViewHolder> {

    //KasirActivity
    public static int count;
    public Context context;
    MediaPlayer player;
    private List<HashMap<String, String>> barangData;

    public KasirBarangAdapter(Context context2, List<HashMap<String, String>> productData2) {
        this.context = context2;
        this.barangData = productData2;
        this.player = MediaPlayer.create(context2, R.raw.delete_sound);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.kasir_barang_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int getStock;
        MyViewHolder myViewHolder = holder;
        int i = position;

        final DBAccess dbAccess = DBAccess.getInstance(this.context);
        dbAccess.open();

        String currency = dbAccess.getCurrency();
        final String barang_id = (String) this.barangData.get(i).get(DBHelper.BARANG_ID);
        String NamaBarang = (String) this.barangData.get(i).get(DBHelper.BARANG_NAMA);
        String Stock = (String) this.barangData.get(i).get(DBHelper.BARANG_STOCK);
        String Bobot = (String) this.barangData.get(i).get(DBHelper.BARANG_BOBOT); //weight
        String Satuan_Id = (String) this.barangData.get(i).get(DBHelper.BARANG_SATUAN); //unit
        String Harga = (String) this.barangData.get(i).get(DBHelper.BARANG_HARGA);

        dbAccess.open();
        String satuan_nama = dbAccess.getSatuanNama(Satuan_Id);
        myViewHolder.tvNamaBarang.setText(NamaBarang);
        int getStock2 = Integer.parseInt(Stock);
        if (getStock2 > 5) {
            TextView textView = myViewHolder.tvStock;
            getStock = getStock2;
            StringBuilder sb = new StringBuilder();
            sb.append(this.context.getString(R.string.stock));
            sb.append(" : ");
            sb.append(Stock);
            textView.setText(sb.toString());
        } else {
            getStock = getStock2;
            TextView textView2 = myViewHolder.tvStock;
            textView2.setText(this.context.getString(R.string.stock) + " : " + Stock);
            myViewHolder.tvStock.setTextColor(SupportMenu.CATEGORY_MASK);
        }

        TextView textView3 = myViewHolder.tvSatuan;
        textView3.setText(Bobot + " " + satuan_nama);
        TextView textView4 = myViewHolder.tvHarga;
        textView4.setText(currency + " : " + Harga);
        myViewHolder.cardBarang.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                KasirBarangAdapter.this.player.start();
                Intent intent = new Intent(KasirBarangAdapter.this.context, UpdatedataActivity.class);
                intent.putExtra(DBHelper.BARANG_ID, barang_id);
                KasirBarangAdapter.this.context.startActivity(intent);
            }
        });

        final String Kasir_Barang = barang_id;
        final String Kasir_Bobot = Bobot;
        final String Kasir_Satuan = Satuan_Id;
        String Harga1 = Harga;
        final String Kasir_Harga = Harga1;
        String Stock1 = Stock;
        final String Kasir_Stock = Stock1;
        final int i2 = getStock;

        final DBAccess dbAccess1 = dbAccess;

        myViewHolder.btnAddToKasir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i2 <= 0) {
                    Toasty.warning(KasirBarangAdapter.this.context, (int) R.string.stock_is_low_please_update_stock, 0).show();
                    return;
                }
                Log.d("s_id", Kasir_Satuan);

                dbAccess1.open();
                int check = dbAccess1.addToKasir(Kasir_Barang, Kasir_Bobot, Kasir_Satuan, Kasir_Harga, 1, Kasir_Stock);
                dbAccess1.open();
                int count = dbAccess1.getKasirItemCount();
                if (count == 0) {
                    KasirActivity.txtCount.setVisibility(View.INVISIBLE);
                } else {
                    KasirActivity.txtCount.setVisibility(View.VISIBLE);
                    KasirActivity.txtCount.setText(String.valueOf(count));
                }
                if (check == 1) {
                    Toasty.success(KasirBarangAdapter.this.context, (int) R.string.product_added_to_cart, 0).show();
                    KasirBarangAdapter.this.player.start();
                } else if (check == 2) {
                    Toasty.info(KasirBarangAdapter.this.context, (int) R.string.product_already_added_to_cart, 0).show();
                } else {
                    Toasty.error(KasirBarangAdapter.this.context, (int) R.string.product_added_to_cart_failed_try_again, 0).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.barangData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button btnAddToKasir;
        CardView cardBarang;
        TextView tvNamaBarang;
        TextView tvStock;
        TextView tvSatuan;
        TextView tvHarga;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.tvNamaBarang = (TextView) itemView.findViewById(R.id.tvNamaBarang);
            this.tvStock = (TextView) itemView.findViewById(R.id.tvStock);
            this.tvSatuan = itemView.findViewById(R.id.tvSatuan);
            this.tvHarga = (TextView) itemView.findViewById(R.id.tvHarga);
            this.btnAddToKasir = (Button) itemView.findViewById(R.id.btn_addKasir);
            this.cardBarang = (CardView) itemView.findViewById(R.id.card_Barang);
        }
    }

}
