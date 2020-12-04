package com.ahmadabuhasan.pos.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

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

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.MyViewHolder> {

    //ViewDataActivity
    public Context context;
    public List<HashMap<String, String>> barangData;

    public BarangAdapter(Context context1, List<HashMap<String, String>> barangData1) {
        this.context = context1;
        this.barangData = barangData1;
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.barang_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final DBAccess dbAccess = DBAccess.getInstance(this.context);
        final String barang_id = this.barangData.get(position).get(DBHelper.BARANG_ID);
        dbAccess.open();

        String currency = dbAccess.getCurrency();
        dbAccess.open();

        //String supplier_name = dbAccess.getSupplierName((String) this.barangData.get(position).get(DBHelper.BARANG_SUPPLIER));
        holder.tvNamaBarang.setText(this.barangData.get(position).get(DBHelper.BARANG_NAMA));

        TextView textView = holder.tvBeli;
        textView.setText(this.context.getString(R.string.beli) + " : " + currency + ((String) this.barangData.get(position).get(DBHelper.BARANG_BELI)));
        //textView.setText(this.context.getString(R.string.beli) + ": " + (this.barangData.get(position).get(DBHelper.BARANG_BELI)));

        TextView textView2 = holder.tvStock;
        textView2.setText(this.context.getString(R.string.stock) + ": " + (this.barangData.get(position).get(DBHelper.KASIR_STOCK)));

        TextView textView3 = holder.tvHarga;
        textView3.setText(this.context.getString(R.string.harga) + " : " + currency + ((String) this.barangData.get(position).get(DBHelper.BARANG_HARGA)));
        //textView3.setText(this.context.getString(R.string.harga) + ": " + (this.barangData.get(position).get(DBHelper.BARANG_HARGA)));

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(BarangAdapter.this.context).setMessage(R.string.barang_delete).setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbAccess.open();
                        if (dbAccess.deleteBarang(barang_id)) {
                            Toasty.error(BarangAdapter.this.context, R.string.barang_deleted, 0).show();
                            BarangAdapter.this.barangData.remove(holder.getAdapterPosition());
                            BarangAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                        } else {
                            Toast.makeText(BarangAdapter.this.context, R.string.failed, Toast.LENGTH_LONG).show();
                        }
                        dialog.cancel();
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
            }
        });

    }

    public int getItemCount() {
        return this.barangData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgDelete;
        TextView tvNamaBarang;
        TextView tvBeli;
        TextView tvStock;
        TextView tvHarga;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.tvNamaBarang = itemView.findViewById(R.id.tvNamaBarang);
            this.tvBeli = itemView.findViewById(R.id.tvBeli);
            this.tvStock = itemView.findViewById(R.id.tvStock);
            this.tvHarga = itemView.findViewById(R.id.tvHarga);
            this.imgDelete = itemView.findViewById(R.id.img_delete);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            Intent i = new Intent(BarangAdapter.this.context, UpdatedataActivity.class);
            i.putExtra(DBHelper.BARANG_ID, (String) ((HashMap) BarangAdapter.this.barangData.get(getAdapterPosition())).get(DBHelper.BARANG_ID));
            BarangAdapter.this.context.startActivity(i);
        }
    }

}
