package com.ahmadabuhasan.pos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.ajts.androidmads.library.ExcelToSQLite;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by Ahmad Abu Hasan on 4/12/2020
 */

public class AdddataActivity extends AppCompatActivity {

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

    TextView tvAddData;

    ProgressDialog loading;

    private String datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddata);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Tambah Barang");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss aa");
        datetime = dateFormat.format(calendar.getTime());

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
        etLastUpdate.setEnabled(false);
        etKeterangan = findViewById(R.id.etKeterangan);
        etSupplier = findViewById(R.id.etSupplier);

        tvAddData = findViewById(R.id.tvAddData);

        imgScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdddataActivity.this, ScannerviewActivity.class));
            }
        });

        this.kategoriNames = new ArrayList<>();
        this.satuanNames = new ArrayList<>();
        this.supplierNames = new ArrayList<>();

        DBAccess dbAccess = DBAccess.getInstance(this);
        dbAccess.open();
        final List<HashMap<String, String>> kategori = dbAccess.getKategori();
        dbAccess.open();
        final List<HashMap<String, String>> satuan = dbAccess.getSatuan();
        dbAccess.open();
        final List<HashMap<String, String>> supplier = dbAccess.getSupplier();

        for (int i = 0; i < kategori.size(); i++) {
            this.kategoriNames.add(kategori.get(i).get(DBHelper.KATEGORI_LIST));
        }
        for (int i3 = 0; i3 < satuan.size(); i3++) {
            this.satuanNames.add(satuan.get(i3).get(DBHelper.SATUAN_LIST));
        }
        for (int i2 = 0; i2 < supplier.size(); i2++) {
            this.supplierNames.add(supplier.get(i2).get(DBHelper.SUPPLIER_NAMA));
        }

        this.etKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdddataActivity.this.kategoriAdapter = new ArrayAdapter<>(AdddataActivity.this, android.R.layout.simple_list_item_1);
                AdddataActivity.this.kategoriAdapter.addAll(AdddataActivity.this.kategoriNames);
                AlertDialog.Builder dialog = new AlertDialog.Builder(AdddataActivity.this);
                View dialogView = AdddataActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = dialogView.findViewById(R.id.dialog_list);
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.kategori);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter(AdddataActivity.this.kategoriAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        AdddataActivity.this.kategoriAdapter.getFilter().filter(charSequence);
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
                        String str2 = AdddataActivity.this.kategoriAdapter.getItem(position);
                        String str1 = "0";
                        AdddataActivity.this.etKategori.setText(str2);
                        for (position = 0; position < AdddataActivity.this.kategoriNames.size(); position++) {
                            if (AdddataActivity.this.kategoriNames.get(position).equalsIgnoreCase(str2))
                                str1 = (String) ((HashMap) kategori.get(position)).get("id");
                        }
                        AdddataActivity.this.kategori_Id = str1;
                        Log.d("id", str1);
                    }
                });
            }
        });

        this.etSatuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdddataActivity.this.satuanAdapter = new ArrayAdapter<>(AdddataActivity.this, android.R.layout.simple_list_item_1);
                AdddataActivity.this.satuanAdapter.addAll(AdddataActivity.this.satuanNames);
                AlertDialog.Builder dialog = new AlertDialog.Builder(AdddataActivity.this);
                View dialogView = AdddataActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = dialogView.findViewById(R.id.dialog_list);
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.satuan);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter(AdddataActivity.this.satuanAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        AdddataActivity.this.satuanAdapter.getFilter().filter(charSequence);
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
                        String str2 = AdddataActivity.this.satuanAdapter.getItem(position);
                        String str1 = "0";
                        AdddataActivity.this.etSatuan.setText(str2);
                        for (position = 0; position < AdddataActivity.this.satuanNames.size(); position++) {
                            if ((AdddataActivity.this.satuanNames.get(position)).equalsIgnoreCase(str2))
                                str1 = (String) ((HashMap) satuan.get(position)).get("id");
                        }
                        AdddataActivity.this.satuan_Id = str1;
                        Log.d("List", AdddataActivity.this.satuan_Id);
                    }
                });
            }
        });

        this.etSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdddataActivity.this.supplierAdapter = new ArrayAdapter<>(AdddataActivity.this, android.R.layout.simple_list_item_1);
                AdddataActivity.this.supplierAdapter.addAll(AdddataActivity.this.supplierNames);
                AlertDialog.Builder dialog = new AlertDialog.Builder(AdddataActivity.this);
                View dialogView = AdddataActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = dialogView.findViewById(R.id.dialog_list);
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.suppliers);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter(AdddataActivity.this.supplierAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        AdddataActivity.this.supplierAdapter.getFilter().filter(charSequence);
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
                        String str2 = AdddataActivity.this.supplierAdapter.getItem(position);
                        String str1 = "0";
                        AdddataActivity.this.etSupplier.setText(str2);
                        for (position = 0; position < AdddataActivity.this.supplierNames.size(); position++) {
                            if (AdddataActivity.this.supplierNames.get(position).equalsIgnoreCase(str2))
                                str1 = (String) ((HashMap) supplier.get(position)).get("id");
                        }
                        AdddataActivity.this.supplier_Id = str1;
                    }
                });
            }
        });

        this.tvAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NamaBarang = AdddataActivity.this.etNamaBarang.getText().toString();
                String KodeBarang = AdddataActivity.etKodeBarang.getText().toString();
                //String Kategori = AdddataActivity.this.etKategori.getText().toString();
                String Kategori_Id = AdddataActivity.this.kategori_Id;
                String Beli = AdddataActivity.this.etBeli.getText().toString();
                String Stock = AdddataActivity.this.etStock.getText().toString();
                String Harga = AdddataActivity.this.etHarga.getText().toString();
                String Bobot = AdddataActivity.this.etBobot.getText().toString();
                //String Satuan = AdddataActivity.this.etSatuan.getText().toString();
                String Satuan_Id = AdddataActivity.this.satuan_Id;
                //String LastUpdate = AdddataActivity.this.etLastUpdate.getText().toString();
                String LastUpdate = AdddataActivity.this.datetime;
                String Keterangan = AdddataActivity.this.etKeterangan.getText().toString();
                //String Supplier = AdddataActivity.this.etSupplier.getText().toString();
                String Supplier_Id = AdddataActivity.this.supplier_Id;

                if (NamaBarang.isEmpty() || Beli.isEmpty() || Stock.isEmpty() || Harga.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Nama Barang, Beli, Stock, Harga\nTidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else {
                    DBAccess dbAccess = DBAccess.getInstance(AdddataActivity.this);
                    dbAccess.open();
                    if (dbAccess.addBarang(NamaBarang, KodeBarang, Kategori_Id, Beli, Stock, Harga, Bobot, Satuan_Id, LastUpdate, Keterangan, Supplier_Id)) {
                        Toasty.success(AdddataActivity.this, R.string.data_successfully_added, 0).show();
                        Intent intent = new Intent(AdddataActivity.this, ViewdataActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        AdddataActivity.this.startActivity(intent);
                        return;
                    }
                    Toasty.error(AdddataActivity.this, R.string.failed, 0).show();
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_barang_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId != R.id.menu_import) {
            return super.onOptionsItemSelected(item);
        } else {
            fileChooser();
            return true;
        }
    }

    public void fileChooser() {
        new ChooserDialog((Activity) this).displayPath(true).withFilter(false, false, "xls").withChosenListener(new ChooserDialog.Result() {
            public void onChoosePath(String path, File pathFile) {
                AdddataActivity.this.onImport(path);
            }
        }).withOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Log.d("CANCEL", "CANCEL");
                dialog.cancel();
            }
        }).build().show();
    }

    public void onImport(String path) {
        String directory_path = path;
        DBAccess.getInstance(this).open();
        if (!new File(directory_path).exists()) {
            Toast.makeText(this, R.string.no_file_found, Toast.LENGTH_SHORT).show();
        } else {
            new ExcelToSQLite(getApplicationContext(), DBHelper.DATABASE_NAME, false).importFromFile(directory_path, (ExcelToSQLite.ImportListener) new ExcelToSQLite.ImportListener() {
                public void onStart() {
                    AdddataActivity.this.loading = new ProgressDialog(AdddataActivity.this);
                    AdddataActivity.this.loading.setMessage(AdddataActivity.this.getString(R.string.data_importing_please_wait));
                    AdddataActivity.this.loading.setCancelable(false);
                    AdddataActivity.this.loading.show();
                }

                public void onCompleted(String dbName) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            AdddataActivity.this.loading.dismiss();
                            Toasty.success((Context) AdddataActivity.this, (int) R.string.data_successfully_imported, 0).show();
                            AdddataActivity.this.startActivity(new Intent(AdddataActivity.this, DashboardActivity.class));
                            AdddataActivity.this.finish();
                        }
                    }, 5000);
                }

                public void onError(Exception e) {
                    AdddataActivity.this.loading.dismiss();
                    Log.d("Error : ", "" + e.getMessage());
                    Toasty.error((Context) AdddataActivity.this, (int) R.string.data_import_fail, 0).show();
                }
            });
        }
    }

}
