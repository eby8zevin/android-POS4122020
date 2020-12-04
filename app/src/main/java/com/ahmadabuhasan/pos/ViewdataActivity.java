package com.ahmadabuhasan.pos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ahmadabuhasan.pos.adapter.BarangAdapter;
import com.ahmadabuhasan.pos.database.DBAccess;
import com.ahmadabuhasan.pos.database.DBHelper;
import com.ajts.androidmads.library.SQLiteToExcel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by Ahmad Abu Hasan on 4/12/2020
 */

public class ViewdataActivity extends AppCompatActivity {
    EditText etSearch;
    ProgressDialog loading;
    FloatingActionButton fabAdd;
    BarangAdapter barangAdapter;

    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdata);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Data Barang");

        etSearch = findViewById(R.id.etSearch);
        this.fabAdd = findViewById(R.id.fab_add);
        this.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewdataActivity.this, AdddataActivity.class));
            }
        });

        recyclerView = findViewById(R.id.data_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        DBAccess dbAccess = DBAccess.getInstance(this);
        dbAccess.open();
        List<HashMap<String, String>> barangData = dbAccess.getBarang();
        Log.d("data", "" + barangData.size());
        if (barangData.size() <= 0) {
            Toasty.info(this, R.string.no_data_found, 0).show();
        } else {
            BarangAdapter barangAdapter2 = new BarangAdapter(this, barangData);
            this.barangAdapter = barangAdapter2;
            this.recyclerView.setAdapter(barangAdapter2);
        }

        this.etSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DBAccess dbAccess1 = DBAccess.getInstance(ViewdataActivity.this);
                dbAccess1.open();
                List<HashMap<String, String>> searchBarangList = dbAccess1.getSearchBarang(s.toString());
                if (searchBarangList.size() <= 0) {
                    ViewdataActivity.this.recyclerView.setVisibility(View.GONE);
                    return;
                }
                ViewdataActivity.this.recyclerView.setVisibility(View.VISIBLE);
                ViewdataActivity barangActivity = ViewdataActivity.this;
                barangActivity.barangAdapter = new BarangAdapter(barangActivity, searchBarangList);
                ViewdataActivity.this.recyclerView.setAdapter(ViewdataActivity.this.barangAdapter);
            }

            public void afterTextChanged(Editable s) {
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_barang_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            Intent i = new Intent(this, DashboardActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;
        } else if (itemId != R.id.menu_export) {
            return super.onOptionsItemSelected(item);
        } else {
            folderChooser();
            return true;
        }
    }

    public void folderChooser() {
        new ChooserDialog(ViewdataActivity.this)
                .displayPath(true)
                .withFilter(true, false, new String[0])
                .withChosenListener(new ChooserDialog.Result() {
                    public void onChoosePath(String path, File pathFile) {
                        ViewdataActivity.this.onExport(path);
                        Log.d("path", path);
                    }
                }).build().show();
    }

    public void onExport(String path) {
        String directory_path = path;
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        new SQLiteToExcel(getApplicationContext(), DBHelper.DATABASE_NAME, directory_path)
                .exportSingleTable("barang", "barang.xls", new SQLiteToExcel.ExportListener() {
                    public void onStart() {
                        ViewdataActivity.this.loading = new ProgressDialog(ViewdataActivity.this);
                        ViewdataActivity.this.loading.setMessage(ViewdataActivity.this.getString(R.string.data_exporting_please_wait));
                        ViewdataActivity.this.loading.setCancelable(false);
                        ViewdataActivity.this.loading.show();
                    }

                    public void onCompleted(String filePath) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                ViewdataActivity.this.loading.dismiss();
                                Toasty.success((Context) ViewdataActivity.this, (int) R.string.data_successfully_exported, 0).show();
                            }
                        }, 5000);
                    }

                    public void onError(Exception e) {
                        ViewdataActivity.this.loading.dismiss();
                        Toasty.error((Context) ViewdataActivity.this, (int) R.string.data_export_fail, 0).show();
                    }
                });
    }

}
