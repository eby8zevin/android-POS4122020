package com.ahmadabuhasan.pos.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.pos.KasirActivity;
import com.ahmadabuhasan.pos.R;
import com.ahmadabuhasan.pos.database.DBAccess;
import com.ahmadabuhasan.pos.database.DBHelper;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by Ahmad Abu Hasan on 4/12/2020
 */

public class KasirAdapter extends RecyclerView.Adapter<KasirAdapter.MyViewHolder> {

    //BarangKasirActivity
    public static Double total_harga;
    public List<HashMap<String, String>> kasir_barang;
    public Context context;

    MediaPlayer player;
    DecimalFormat f = new DecimalFormat("#0.00");

    ImageView imgNoBarang;
    TextView tvNotBarang;
    TextView tvTotalHarga;
    Button btnSubmitOrder;

    public KasirAdapter(Context context1, List<HashMap<String, String>> kasir_barang1, TextView tvTotalHarga1, Button btnSubmitOrder1, ImageView imgNoBarang1, TextView tvNotBarang1) {
        this.context = context1;
        this.kasir_barang = kasir_barang1;
        this.player = MediaPlayer.create(context1, R.raw.delete_sound);
        this.tvTotalHarga = tvTotalHarga1;
        this.btnSubmitOrder = btnSubmitOrder1;
        this.imgNoBarang = imgNoBarang1;
        this.tvNotBarang = tvNotBarang1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.keranjang_barang_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final MyViewHolder myViewHolder = holder;
        int i = position;

        final DBAccess dbAccess = DBAccess.getInstance(this.context);
        dbAccess.open();

        final String Id = (String) this.kasir_barang.get(i).get(DBHelper.KASIR_ID);
        String Barang_Id = (String) this.kasir_barang.get(i).get(DBHelper.KASIR_BARANGID);
        String BarangNama = dbAccess.getBarangNama(Barang_Id);
        String Bobot = (String) this.kasir_barang.get(i).get(DBHelper.KASIR_BOBOT);
        dbAccess.open();
        String Satuan = dbAccess.getSatuanNama(this.kasir_barang.get(i).get(DBHelper.KASIR_SATUAN));
        final String Harga = (String) this.kasir_barang.get(i).get(DBHelper.KASIR_HARGA);
        String Qty = (String) this.kasir_barang.get(i).get(DBHelper.KASIR_QTY);
        final int getStock = Integer.parseInt((String) this.kasir_barang.get(i).get(DBHelper.KASIR_STOCK));

        dbAccess.open();
        final String currency = dbAccess.getCurrency();

        dbAccess.open();
        total_harga = Double.valueOf(dbAccess.getTotalHarga());
        TextView textView = this.tvTotalHarga;
        StringBuilder sb = new StringBuilder();
        DBAccess dbAccess1 = DBAccess.getInstance(this.context);
        dbAccess1.open();
        sb.append(this.context.getString(R.string.total_price));
        sb.append(currency);
        sb.append(this.f.format(total_harga));
        textView.setText(sb.toString());

        double parseDouble = Double.parseDouble(Harga);
        double parseInt = (double) Integer.parseInt(Qty);
        Double.isNaN(parseInt);
        double getHarga = parseInt * parseDouble;

        myViewHolder.tvNamaBarang.setText(BarangNama);
        TextView textView2 = myViewHolder.tvBobot;
        textView2.setText(Bobot + " " + Satuan);
        TextView textView3 = myViewHolder.tvHarga;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(currency);
        sb2.append(this.f.format(getHarga));
        textView3.setText(sb2.toString());
        myViewHolder.tvQtyNumber.setText(Qty);

        myViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBAccess dbAccess2 = DBAccess.getInstance(KasirAdapter.this.context);
                dbAccess2.open();

                if (dbAccess2.deleteBarangDariKeranjang(Id)) {
                    Toasty.success(KasirAdapter.this.context, (CharSequence) KasirAdapter.this.context.getString(R.string.product_removed_from_cart), 0).show();
                    KasirAdapter.this.player.start();
                    KasirAdapter.this.kasir_barang.remove(myViewHolder.getAdapterPosition());
                    KasirAdapter.this.notifyItemRemoved(myViewHolder.getAdapterPosition());
                    dbAccess2.open();
                    KasirAdapter.total_harga = Double.valueOf(dbAccess2.getTotalHarga());
                    TextView textView = KasirAdapter.this.tvTotalHarga;
                    textView.setText(KasirAdapter.this.context.getString(R.string.total_price) + currency + KasirAdapter.this.f.format(KasirAdapter.total_harga));
                } else {
                    Toasty.error(KasirAdapter.this.context, (CharSequence) KasirAdapter.this.context.getString(R.string.failed), 0).show();
                }
                dbAccess2.open();
                int itemCount = dbAccess2.getKeranjangItemCount();
                Log.d("itemCount", "" + itemCount);
                if (itemCount <= 0) {
                    KasirAdapter.this.tvTotalHarga.setVisibility(View.GONE);
                    KasirAdapter.this.btnSubmitOrder.setVisibility(View.GONE);
                    KasirAdapter.this.imgNoBarang.setVisibility(View.VISIBLE);
                    KasirAdapter.this.tvNotBarang.setVisibility(View.VISIBLE);
                }
            }
        });

        myViewHolder.tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int get_qty = Integer.parseInt(myViewHolder.tvQtyNumber.getText().toString());
                if (get_qty >= getStock) {
                    Context access$000 = KasirAdapter.this.context;
                    Toasty.error(access$000, (CharSequence) KasirAdapter.this.context.getString(R.string.available_stock) + " " + getStock, 0).show();
                    return;
                }
                int get_qty2 = get_qty + 1;
                double parseDouble = Double.parseDouble(Harga);
                double d = (double) get_qty2;
                Double.isNaN(d);
                double cost = parseDouble * d;
                TextView textView = myViewHolder.tvHarga;
                textView.setText(currency + KasirAdapter.this.f.format(cost));
                TextView textView2 = myViewHolder.tvQtyNumber;
                textView2.setText("" + get_qty2);
                DBAccess dbAccess3 = DBAccess.getInstance(KasirAdapter.this.context);
                dbAccess3.open();
                String str = Id;
                dbAccess3.updateBarangQty(str, "" + get_qty2);
                KasirAdapter.total_harga = Double.valueOf(KasirAdapter.total_harga.doubleValue() + Double.valueOf(Harga).doubleValue());
                TextView textView3 = KasirAdapter.this.tvTotalHarga;
                textView3.setText(KasirAdapter.this.context.getString(R.string.total_price) + currency + KasirAdapter.this.f.format(KasirAdapter.total_harga));
            }
        });

        myViewHolder.tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int get_qty = Integer.parseInt(myViewHolder.tvQtyNumber.getText().toString());
                if (get_qty >= 2) {
                    int get_qty2 = get_qty - 1;
                    double parseDouble = Double.parseDouble(Harga);
                    double d = (double) get_qty2;
                    Double.isNaN(d);
                    double cost = parseDouble * d;
                    myViewHolder.tvHarga.setText(currency + KasirAdapter.this.f.format(cost));
                    myViewHolder.tvQtyNumber.setText("" + get_qty2);
                    DBAccess dbAccess4 = DBAccess.getInstance(KasirAdapter.this.context);
                    dbAccess4.open();
                    dbAccess4.updateBarangQty(Id, "" + get_qty2);
                    KasirAdapter.total_harga = Double.valueOf(KasirAdapter.total_harga.doubleValue() - Double.valueOf(Harga).doubleValue());
                    KasirAdapter.this.tvTotalHarga.setText(KasirAdapter.this.context.getString(R.string.total_price) + currency + KasirAdapter.this.f.format(KasirAdapter.total_harga));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.kasir_barang.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDelete;
        ImageView imgProduct;
        TextView tvNamaBarang;
        TextView tvBobot;
        TextView tvHarga;
        TextView tvMinus;
        TextView tvQtyNumber;
        TextView tvPlus;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imgProduct = (ImageView) itemView.findViewById(R.id.cart_product_image);
            this.tvNamaBarang = (TextView) itemView.findViewById(R.id.tvNamaBarang);
            this.tvBobot = (TextView) itemView.findViewById(R.id.tvBobot);
            this.tvHarga = (TextView) itemView.findViewById(R.id.tvHarga);
            this.tvMinus = (TextView) itemView.findViewById(R.id.tvMinus);
            this.tvQtyNumber = (TextView) itemView.findViewById(R.id.tvNumber);
            this.tvPlus = (TextView) itemView.findViewById(R.id.tvPlus);
            this.imgDelete = (ImageView) itemView.findViewById(R.id.img_delete);
        }
    }
}
