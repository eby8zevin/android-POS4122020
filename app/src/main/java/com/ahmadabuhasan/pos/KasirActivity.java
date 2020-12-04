package com.ahmadabuhasan.pos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadabuhasan.pos.adapter.KasirBarangAdapter;
import com.ahmadabuhasan.pos.adapter.KategoriBarangAdapter;
import com.ahmadabuhasan.pos.database.DBAccess;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by Ahmad Abu Hasan on 4/12/2020
 */

public class KasirActivity extends AppCompatActivity {

    public static EditText etSearch;
    public static TextView txtCount;

    KategoriBarangAdapter kategoriAdapter;

    ImageView imgBack;
    ImageView imgKasir;
    ImageView imgScanCode;

    KasirBarangAdapter barangAdapter;

    /* access modifiers changed from: private */
    public RecyclerView recyclerView;

    int spanCount = 2;

    TextView tvNoBarang;
    TextView tvReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kasir);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();

        etSearch = findViewById(R.id.etSearch);
        this.recyclerView = findViewById(R.id.data_recyclerView);
        this.tvNoBarang = findViewById(R.id.tvNoBarang);
        this.imgScanCode = findViewById(R.id.img_scan_code);
        this.tvReset = findViewById(R.id.tvReset);
        txtCount = findViewById(R.id.txt_count);
        this.imgBack = findViewById(R.id.img_Back);
        this.imgKasir = findViewById(R.id.img_Kasir);
        RecyclerView kategoriRecyclerView = findViewById(R.id.kategori_recyclerView);

        final DBAccess dbAccess = DBAccess.getInstance(this);
        this.imgScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KasirActivity.this.startActivity(new Intent(KasirActivity.this, ScannerkasirActivity.class));
            }
        });

        this.tvNoBarang.setVisibility(View.GONE);
        if ((getResources().getConfiguration().screenLayout & 15) == 3) {
            this.spanCount = 4;
        } else if ((getResources().getConfiguration().screenLayout & 15) == 2) {
            this.spanCount = 2;
        } else if ((getResources().getConfiguration().screenLayout & 15) == 1) {
            this.spanCount = 2;
        } else {
            this.spanCount = 4;
        }

        this.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        this.recyclerView.setHasFixedSize(true);

        this.tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbAccess.open();
                List<HashMap<String, String>> productList = dbAccess.getBarang();
                if (productList.isEmpty()) {
                    KasirActivity.this.recyclerView.setVisibility(View.GONE);
                    KasirActivity.this.tvNoBarang.setVisibility(View.VISIBLE);
                    return;
                }
                KasirActivity.this.recyclerView.setVisibility(View.VISIBLE);
                KasirActivity kasirActivity = KasirActivity.this;
                kasirActivity.barangAdapter = new KasirBarangAdapter(kasirActivity, productList);
                KasirActivity.this.recyclerView.setAdapter(KasirActivity.this.barangAdapter);
            }
        });

        kategoriRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        kategoriRecyclerView.setHasFixedSize(true);
        dbAccess.open();
        List<HashMap<String, String>> categoryData = dbAccess.getBarangKategori();
        Log.d("data", "" + categoryData.size());
        if (categoryData.isEmpty()) {
            Toasty.info((Context) this, (int) R.string.no_data_found, 0).show();
        } else {
            KategoriBarangAdapter productCategoryAdapter = new KategoriBarangAdapter(this, categoryData, this.recyclerView, this.tvNoBarang);
            this.kategoriAdapter = productCategoryAdapter;
            kategoriRecyclerView.setAdapter(productCategoryAdapter);
        }

        dbAccess.open();
        int count = dbAccess.getKasirItemCount();
        if (count == 0) {
            txtCount.setVisibility(View.INVISIBLE);
        } else {
            txtCount.setVisibility(View.VISIBLE);
            txtCount.setText(String.valueOf(count));
        }
        this.imgKasir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                KasirActivity.this.startActivity(new Intent(KasirActivity.this, BarangKasirActivity.class));
            }
        });
        this.imgBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                KasirActivity.this.startActivity(new Intent(KasirActivity.this, DashboardActivity.class));
                KasirActivity.this.finish();
            }
        });

        dbAccess.open();
        List<HashMap<String, String>> barangList = dbAccess.getBarang();
        if (barangList.size() <= 0) {
            this.recyclerView.setVisibility(View.GONE);
            this.tvNoBarang.setVisibility(View.VISIBLE);
        } else {
            this.recyclerView.setVisibility(View.VISIBLE);
            this.tvNoBarang.setVisibility(View.GONE);
            KasirBarangAdapter kasirBarangAdapter = new KasirBarangAdapter(this, barangList);
            this.barangAdapter = kasirBarangAdapter;
            this.recyclerView.setAdapter(kasirBarangAdapter);
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dbAccess.open();
                List<HashMap<String, String>> searchBarangList = dbAccess.getSearchBarang(s.toString());
                if (searchBarangList.size() <= 0) {
                    KasirActivity.this.recyclerView.setVisibility(View.GONE);
                    KasirActivity.this.tvNoBarang.setVisibility(View.VISIBLE);
                    return;
                }
                KasirActivity.this.recyclerView.setVisibility(View.VISIBLE);
                KasirActivity.this.tvNoBarang.setVisibility(View.GONE);
                KasirActivity kasirActivity = KasirActivity.this;
                kasirActivity.barangAdapter = new KasirBarangAdapter(kasirActivity, searchBarangList);
                KasirActivity.this.recyclerView.setAdapter(KasirActivity.this.barangAdapter);
            }

            public void afterTextChanged(Editable s) {
            }
        });
    }

}
