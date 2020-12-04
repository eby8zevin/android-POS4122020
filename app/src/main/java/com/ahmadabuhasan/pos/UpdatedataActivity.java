package com.ahmadabuhasan.pos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.internal.view.SupportMenu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadabuhasan.pos.database.DBAccess;
import com.ahmadabuhasan.pos.database.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by Ahmad Abu Hasan on 4/12/2020
 */

public class UpdatedataActivity extends AppCompatActivity {

    ArrayAdapter<String> kategoriAdapter;
    List<String> kategoriNames;
    String kategori_Id;

    ArrayAdapter<String> satuanAdapter;
    List<String> satuanNames;
    String satuan_Id;

    ArrayAdapter<String> supplierAdapter;
    List<String> supplierNames;
    String supplier_Id;

    EditText etNamaBarang;
    @SuppressLint("StaticFieldLeak")
    public static EditText etKodeBarang;
    ImageView imgScanCode;
    EditText etKategori;
    EditText etBeli;
    EditText etStock;
    EditText etHarga;
    EditText etBobot;
    EditText etSatuan;
    EditText etLastUpdate;
    EditText etKeterangan;
    EditText etSupplier;

    TextView tvEditData;
    TextView tvUpdate;

    String barangID;
    private String datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatedata);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Edit Barang");

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss aa");
        datetime = dateformat.format(c.getTime());

        etNamaBarang = findViewById(R.id.etNamaBarang);
        etKodeBarang = findViewById(R.id.etKodeBarang);
        imgScanCode = findViewById(R.id.img_scan_code);
        etKategori = findViewById(R.id.etKategori);
        etBeli = findViewById(R.id.etBeli);
        etStock = findViewById(R.id.etStock);
        etHarga = findViewById(R.id.etHarga);
        etBobot = findViewById(R.id.etBobot);
        etSatuan = findViewById(R.id.etSatuan);
        etLastUpdate = findViewById(R.id.etLastUpdate);
        //etLastUpdate.setText(date);
        etKeterangan = findViewById(R.id.etKeterangan);
        etSupplier = findViewById(R.id.etSupplier);

        tvEditData = findViewById(R.id.tvEdit);
        tvUpdate = findViewById(R.id.tvUpdate);

        this.barangID = getIntent().getExtras().getString(DBHelper.BARANG_ID);

        this.etNamaBarang.setEnabled(false);
        etKodeBarang.setEnabled(false);
        this.imgScanCode.setEnabled(false);
        this.etKategori.setEnabled(false);
        this.etBeli.setEnabled(false);
        this.etStock.setEnabled(false);
        this.etHarga.setEnabled(false);
        this.etBobot.setEnabled(false);
        this.etSatuan.setEnabled(false);
        this.etLastUpdate.setEnabled(false);
        this.etKeterangan.setEnabled(false);
        this.etSupplier.setEnabled(false);

        this.tvUpdate.setVisibility(View.GONE);

        this.imgScanCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UpdatedataActivity.this.imgScanCode.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        UpdatedataActivity.this.startActivity(new Intent(UpdatedataActivity.this, UpdatedataScannerviewActivity.class));
                    }
                });
            }
        });

        this.kategoriNames = new ArrayList<>();
        this.satuanNames = new ArrayList<>();
        this.supplierNames = new ArrayList<>();

        DBAccess dbAccess = DBAccess.getInstance(this);
        dbAccess.open();
        ArrayList<HashMap<String, String>> barangInfo = dbAccess.getBarangInfo(this.barangID);
        String NamaBarang = barangInfo.get(0).get(DBHelper.BARANG_NAMA);
        String KodeBarang = barangInfo.get(0).get(DBHelper.BARANG_KODE);
        String Kategori_Id = barangInfo.get(0).get(DBHelper.BARANG_KATEGORI);
        String Beli = barangInfo.get(0).get(DBHelper.BARANG_BELI);
        String Stock = barangInfo.get(0).get(DBHelper.BARANG_STOCK);
        String Harga = barangInfo.get(0).get(DBHelper.BARANG_HARGA);
        String Bobot = barangInfo.get(0).get(DBHelper.BARANG_BOBOT);
        String Satuan_Id = barangInfo.get(0).get(DBHelper.BARANG_SATUAN);
        String LastUpdate = barangInfo.get(0).get(DBHelper.BARANG_LASTUPDATE);
        String Keterangan = barangInfo.get(0).get(DBHelper.BARANG_KETERANGAN);
        String Supplier_Id = barangInfo.get(0).get(DBHelper.BARANG_SUPPLIER);

        this.etNamaBarang.setText(NamaBarang);
        etKodeBarang.setText(KodeBarang);
        dbAccess.open();
        this.etKategori.setText(dbAccess.getKategoriNama(Kategori_Id));
        this.etBeli.setText(Beli);
        this.etStock.setText(Stock);
        this.etHarga.setText(Harga);
        this.etBobot.setText(Bobot);
        dbAccess.open();
        this.etSatuan.setText(dbAccess.getSatuanNama(Satuan_Id));
        this.etLastUpdate.setText(LastUpdate);
        this.etKeterangan.setText(Keterangan);
        dbAccess.open();
        this.etSupplier.setText(dbAccess.getSupplierNama(Supplier_Id));

        this.kategori_Id = Kategori_Id; //selectedID
        this.satuan_Id = Satuan_Id;
        this.supplier_Id = Supplier_Id;

        dbAccess.open();
        final List<HashMap<String, String>> barangKategori = dbAccess.getBarangKategori();
        dbAccess.open();
        final List<HashMap<String, String>> barangSatuan = dbAccess.getSatuan();
        dbAccess.open();
        final List<HashMap<String, String>> barangSupplier = dbAccess.getSupplier();

        int i = 0;
        while (true) {
            DBAccess dbAccess1 = dbAccess;
            if (i >= barangKategori.size()) {
                break;
            }
            this.kategoriNames.add(barangKategori.get(i).get(DBHelper.KATEGORI_LIST));
            i++;
            dbAccess = dbAccess1;
        }
        for (int i3 = 0; i3 < barangSatuan.size(); i3++) {
            this.satuanNames.add(barangSatuan.get(i3).get(DBHelper.SATUAN_LIST));
        }
        for (int i2 = 0; i2 < barangSupplier.size(); i2++) {
            this.supplierNames.add(barangSupplier.get(i2).get(DBHelper.SUPPLIER_NAMA));
        }

        this.etKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatedataActivity.this.kategoriAdapter = new ArrayAdapter<>(UpdatedataActivity.this, android.R.layout.simple_list_item_1);
                UpdatedataActivity.this.kategoriAdapter.addAll(UpdatedataActivity.this.kategoriNames);
                AlertDialog.Builder dialog = new AlertDialog.Builder(UpdatedataActivity.this);
                View dialogView = UpdatedataActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = dialogView.findViewById(R.id.dialog_list);
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.kategori);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter(UpdatedataActivity.this.kategoriAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        UpdatedataActivity.this.kategoriAdapter.getFilter().filter(charSequence);
                    }

                    public void afterTextChanged(Editable s) {
                    }
                });
                final AlertDialog alertDialog = dialog.create();
                dialogView.findViewById(R.id.dialog_button).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        alertDialog.dismiss();
                        String str2 = UpdatedataActivity.this.kategoriAdapter.getItem(position);
                        String str1 = "0";
                        UpdatedataActivity.this.etKategori.setText(str2);
                        for (position = 0; position < UpdatedataActivity.this.kategoriNames.size(); position++) {
                            if (UpdatedataActivity.this.kategoriNames.get(position).equalsIgnoreCase(str2))
                                str1 = (String) ((HashMap) barangKategori.get(position)).get("id");
                        }
                        UpdatedataActivity.this.kategori_Id = str1;
                        Log.d("id", str1);
                    }
                });
            }
        });

        this.etSatuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatedataActivity.this.satuanAdapter = new ArrayAdapter<>(UpdatedataActivity.this, android.R.layout.simple_list_item_1);
                UpdatedataActivity.this.satuanAdapter.addAll(UpdatedataActivity.this.satuanNames);
                AlertDialog.Builder dialog = new AlertDialog.Builder(UpdatedataActivity.this);
                View dialogView = UpdatedataActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = dialogView.findViewById(R.id.dialog_list);
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.satuan);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter(UpdatedataActivity.this.satuanAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        UpdatedataActivity.this.satuanAdapter.getFilter().filter(charSequence);
                    }

                    public void afterTextChanged(Editable s) {
                    }
                });
                final AlertDialog alertDialog = dialog.create();
                (dialogView.findViewById(R.id.dialog_button)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        alertDialog.dismiss();
                        String str2 = UpdatedataActivity.this.satuanAdapter.getItem(position);
                        String str1 = "0";
                        UpdatedataActivity.this.etSatuan.setText(str2);
                        for (position = 0; position < UpdatedataActivity.this.satuanNames.size(); position++) {
                            if ((UpdatedataActivity.this.satuanNames.get(position)).equalsIgnoreCase(str2))
                                str1 = (String) ((HashMap) barangSatuan.get(position)).get("id");
                        }
                        UpdatedataActivity.this.satuan_Id = str1;
                        Log.d("List", UpdatedataActivity.this.satuan_Id);
                    }
                });
            }
        });

        this.etSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatedataActivity.this.supplierAdapter = new ArrayAdapter<>(UpdatedataActivity.this, android.R.layout.simple_list_item_1);
                UpdatedataActivity.this.supplierAdapter.addAll(UpdatedataActivity.this.supplierNames);
                AlertDialog.Builder dialog = new AlertDialog.Builder(UpdatedataActivity.this);
                View dialogView = UpdatedataActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = dialogView.findViewById(R.id.dialog_list);
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.suppliers);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter(UpdatedataActivity.this.supplierAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        UpdatedataActivity.this.supplierAdapter.getFilter().filter(charSequence);
                    }

                    public void afterTextChanged(Editable s) {
                    }
                });
                final AlertDialog alertDialog = dialog.create();
                dialogView.findViewById(R.id.dialog_button).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        alertDialog.dismiss();
                        String str2 = UpdatedataActivity.this.supplierAdapter.getItem(position);
                        String str1 = "0";
                        UpdatedataActivity.this.etSupplier.setText(str2);
                        for (position = 0; position < UpdatedataActivity.this.supplierNames.size(); position++) {
                            if (UpdatedataActivity.this.supplierNames.get(position).equalsIgnoreCase(str2))
                                str1 = (String) ((HashMap) barangSupplier.get(position)).get("id");
                        }
                        UpdatedataActivity.this.supplier_Id = str1;
                    }
                });
            }
        });

        this.tvEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatedataActivity.this.etNamaBarang.setEnabled(true);
                UpdatedataActivity.etKodeBarang.setEnabled(true);
                UpdatedataActivity.this.imgScanCode.setEnabled(true);
                UpdatedataActivity.this.etKategori.setEnabled(true);
                UpdatedataActivity.this.etBeli.setEnabled(true);
                UpdatedataActivity.this.etStock.setEnabled(true);
                UpdatedataActivity.this.etHarga.setEnabled(true);
                UpdatedataActivity.this.etBobot.setEnabled(true);
                UpdatedataActivity.this.etSatuan.setEnabled(true);
                //UpdatedataActivity.this.etLastUpdate.setEnabled(true);
                UpdatedataActivity.this.etKeterangan.setEnabled(true);
                UpdatedataActivity.this.etSupplier.setEnabled(true);

                UpdatedataActivity.this.etNamaBarang.setTextColor(SupportMenu.CATEGORY_MASK);
                UpdatedataActivity.etKodeBarang.setTextColor(SupportMenu.CATEGORY_MASK);
                UpdatedataActivity.this.etKategori.setTextColor(SupportMenu.CATEGORY_MASK);
                UpdatedataActivity.this.etBeli.setTextColor(SupportMenu.CATEGORY_MASK);
                UpdatedataActivity.this.etStock.setTextColor(SupportMenu.CATEGORY_MASK);
                UpdatedataActivity.this.etHarga.setTextColor(SupportMenu.CATEGORY_MASK);
                UpdatedataActivity.this.etBobot.setTextColor(SupportMenu.CATEGORY_MASK);
                UpdatedataActivity.this.etSatuan.setTextColor(SupportMenu.CATEGORY_MASK);
                //UpdatedataActivity.this.etLastUpdate.setTextColor(SupportMenu.CATEGORY_MASK);
                UpdatedataActivity.this.etKeterangan.setTextColor(SupportMenu.CATEGORY_MASK);
                UpdatedataActivity.this.etSupplier.setTextColor(SupportMenu.CATEGORY_MASK);

                UpdatedataActivity.this.tvEditData.setVisibility(View.GONE);
                UpdatedataActivity.this.tvUpdate.setVisibility(View.VISIBLE);
            }
        });

        this.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NamaBarang = UpdatedataActivity.this.etNamaBarang.getText().toString();
                String KodeBarang = UpdatedataActivity.etKodeBarang.getText().toString();
                String Kategori = UpdatedataActivity.this.kategori_Id;
                String Beli = UpdatedataActivity.this.etBeli.getText().toString();
                String Stock = UpdatedataActivity.this.etStock.getText().toString();
                String Harga = UpdatedataActivity.this.etHarga.getText().toString();
                String Bobot = UpdatedataActivity.this.etBobot.getText().toString();
                String Satuan = UpdatedataActivity.this.satuan_Id;
                //String LastUpdate = UpdatedataActivity.this.etLastUpdate.getText().toString();
                String LastUpdate = UpdatedataActivity.this.datetime;
                String Keterangan = UpdatedataActivity.this.etKeterangan.getText().toString();
                String Supplier = UpdatedataActivity.this.supplier_Id;

                if (NamaBarang.isEmpty() || Beli.isEmpty() || Stock.isEmpty() || Harga.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Nama Barang, Beli, Stock, Harga\nTidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else {
                    DBAccess dbAccess = DBAccess.getInstance(UpdatedataActivity.this);
                    dbAccess.open();
                    if (dbAccess.updateBarang(NamaBarang, KodeBarang, Kategori, Beli, Stock, Harga, Bobot, Satuan, LastUpdate, Keterangan, Supplier, UpdatedataActivity.this.barangID)) {
                        Toasty.success(UpdatedataActivity.this, R.string.update_successfully, 0).show();
                        Intent intent = new Intent(UpdatedataActivity.this, ViewdataActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        UpdatedataActivity.this.startActivity(intent);
                        return;
                    }
                    Toasty.error(UpdatedataActivity.this, R.string.failed, 0).show();
                }
            }
        });
    }

}
