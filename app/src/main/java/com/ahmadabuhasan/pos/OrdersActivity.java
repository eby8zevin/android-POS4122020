package com.ahmadabuhasan.pos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadabuhasan.pos.adapter.OrderAdapter;
import com.ahmadabuhasan.pos.database.DBAccess;
import com.ahmadabuhasan.pos.database.DBHelper;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by Ahmad Abu Hasan on 4/12/2020
 */

public class OrdersActivity extends AppCompatActivity {

    EditText etxtSearch;
    ImageView imgNoProduct;
    private OrderAdapter orderAdapter;
    /* access modifiers changed from: private */
    public RecyclerView recyclerView;
    TextView txtNoProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        this.recyclerView = (RecyclerView) findViewById(R.id.recycler);
        this.imgNoProduct = (ImageView) findViewById(R.id.image_no_product);
        this.txtNoProducts = (TextView) findViewById(R.id.txt_no_products);
        this.etxtSearch = (EditText) findViewById(R.id.etxt_search_order);
        this.imgNoProduct.setVisibility(View.GONE);
        this.txtNoProducts.setVisibility(View.GONE);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle((int) R.string.order_history);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.recyclerView.setHasFixedSize(true);
        DBAccess dbAccess = DBAccess.getInstance(this);
        dbAccess.open();
        List<HashMap<String, String>> orderList = dbAccess.getOrderList();
        if (orderList.size() <= 0) {
            Toasty.info((Context) this, (int) R.string.no_data_found, 0).show();
            this.recyclerView.setVisibility(View.GONE);
            this.imgNoProduct.setVisibility(View.VISIBLE);
            //this.imgNoProduct.setImageResource(R.drawable.not_found);
            this.txtNoProducts.setVisibility(View.VISIBLE);
        } else {
            OrderAdapter orderAdapter2 = new OrderAdapter(this, orderList);
            this.orderAdapter = orderAdapter2;
            this.recyclerView.setAdapter(orderAdapter2);
        }
//        this.etxtSearch.addTextChangedListener(new TextWatcher() {
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                DBAccess dbAccess = DBAccess.getInstance(OrdersActivity.this);
//                dbAccess.open();
//                List<HashMap<String, String>> searchOrder = dbAccess.searchOrderList(s.toString());
//                if (searchOrder.size() <= 0) {
//                    OrdersActivity.this.recyclerView.setVisibility(View.GONE);
//                    OrdersActivity.this.imgNoProduct.setVisibility(View.VISIBLE);
//                    OrdersActivity.this.imgNoProduct.setImageResource(R.drawable.no_data);
//                    return;
//                }
//                OrdersActivity.this.recyclerView.setVisibility(View.VISIBLE);
//                OrdersActivity.this.imgNoProduct.setVisibility(View.GONE);
//                OrdersActivity.this.recyclerView.setAdapter(new OrderAdapter(OrdersActivity.this, searchOrder));
//            }
//
//            public void afterTextChanged(Editable s) {
//            }
//        });
    }

    public void onBackPressed() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
        super.onBackPressed();
    }
}
