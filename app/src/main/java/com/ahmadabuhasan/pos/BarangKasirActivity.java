package com.ahmadabuhasan.pos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ahmadabuhasan.pos.adapter.KasirAdapter;
import com.ahmadabuhasan.pos.database.DBAccess;
import com.ahmadabuhasan.pos.database.DBHelper;
import com.github.mikephil.charting.utils.Utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

/**
 * Created by Ahmad Abu Hasan on 4/12/2020
 */

public class BarangKasirActivity extends AppCompatActivity {

    ArrayAdapter<String> customerAdapter;
    List<String> customerNames;

    /* renamed from: f */
    DecimalFormat f;

    LinearLayout linearLayout;
    ArrayAdapter<String> orderTypeAdapter;
    List<String> orderTypeNames;
    ArrayAdapter<String> paymentMethodAdapter;
    List<String> paymentMethodNames;
    KasirAdapter productCartAdapter;
    private RecyclerView recyclerView;
    ImageView imgNoBarang;
    TextView tvNotBarang;
    TextView tvTotalHarga;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang_kasir);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Keranjang Barang");

        this.f = new DecimalFormat("#0.00");

        this.linearLayout = findViewById(R.id.linear_layout);
        this.recyclerView = findViewById(R.id.kasir_recyclerView);
        this.imgNoBarang = findViewById(R.id.image_no_barang);
        this.tvNotBarang = findViewById(R.id.tvNotBarang);
        this.tvTotalHarga = findViewById(R.id.tvTotalHarga);
        btnSubmit = findViewById(R.id.test);

        this.tvNotBarang.setVisibility(View.GONE);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView.setHasFixedSize(true);

        DBAccess dbAccess = DBAccess.getInstance(this);
        dbAccess.open();
        List<HashMap<String, String>> keranjangBarangList = dbAccess.getKeranjangBarang();

        if (keranjangBarangList.isEmpty()) {
            this.imgNoBarang.setImageResource(R.drawable.empty_cart);
            this.imgNoBarang.setVisibility(View.VISIBLE);
            this.tvNotBarang.setVisibility(View.VISIBLE);
            this.btnSubmit.setVisibility(View.GONE);
            this.recyclerView.setVisibility(View.GONE);
            this.linearLayout.setVisibility(View.GONE);
            this.tvTotalHarga.setVisibility(View.GONE);
        } else {
            this.imgNoBarang.setVisibility(View.GONE);
            KasirAdapter kasirAdapter = new KasirAdapter(this, keranjangBarangList, this.tvTotalHarga, this.btnSubmit, this.imgNoBarang, this.tvNotBarang);
            this.productCartAdapter = kasirAdapter;
            this.recyclerView.setAdapter(kasirAdapter);
        }

        this.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarangKasirActivity.this.dialog();
            }
        });
    }

    public void dialog() {
        DBAccess dbAccess = DBAccess.getInstance(this);
        dbAccess.open();
        List<HashMap<String, String>> shopData = dbAccess.getShopInformation();
        final String shop_currency = shopData.get(0).get(DBHelper.INFO_CURRENCY);
        String tax = shopData.get(0).get(DBHelper.INFO_TAX);
        double getTax = Double.parseDouble(tax);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_payment, (ViewGroup) null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        final Button dialog_btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
        ImageButton dialog_btn_close = (ImageButton) dialogView.findViewById(R.id.btn_close);
        final TextView dialog_order_payment_method = (TextView) dialogView.findViewById(R.id.dialog_order_status);
        final TextView dialog_order_type = (TextView) dialogView.findViewById(R.id.dialog_order_type);
        final TextView dialog_customer = (TextView) dialogView.findViewById(R.id.dialog_customer);
        TextView dialog_txt_total = (TextView) dialogView.findViewById(R.id.dialog_txt_total);
        TextView dialog_txt_total_tax = (TextView) dialogView.findViewById(R.id.dialog_txt_total_tax);
        final TextView dialog_txt_total_cost = (TextView) dialogView.findViewById(R.id.dialog_txt_total_cost);
        final EditText dialog_etxt_discount = (EditText) dialogView.findViewById(R.id.etxt_dialog_discount);
        ImageButton dialog_img_customer = (ImageButton) dialogView.findViewById(R.id.img_select_customer);
        ImageButton dialog_img_order_payment_method = (ImageButton) dialogView.findViewById(R.id.img_order_payment_method);
        ImageButton dialog_img_order_type = (ImageButton) dialogView.findViewById(R.id.img_order_type);
        ((TextView) dialogView.findViewById(R.id.dialog_level_tax)).setText(getString(R.string.total_tax) + "( " + tax + "%) : ");
        final double total_cost = KasirAdapter.total_harga.doubleValue();
        StringBuilder sb = new StringBuilder();
        sb.append(shop_currency);
        sb.append(this.f.format(total_cost));
        dialog_txt_total.setText(sb.toString());
        final double calculated_tax = (total_cost * getTax) / 100.0d;
        dialog_txt_total_tax.setText(shop_currency + this.f.format(calculated_tax));
        double calculated_total_cost = (total_cost + calculated_tax) - Utils.DOUBLE_EPSILON;
        dialog_txt_total_cost.setText(shop_currency + this.f.format(calculated_total_cost));
        dialog_etxt_discount.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String get_discount = s.toString();
                if (!get_discount.isEmpty()) {
                    double discount = Double.parseDouble(get_discount);
                    if (discount > total_cost + calculated_tax) {
                        dialog_etxt_discount.setError(BarangKasirActivity.this.getString(R.string.discount_cant_be_greater_than_total_price));
                        dialog_etxt_discount.requestFocus();
                        dialog_btn_submit.setVisibility(View.INVISIBLE);
                        return;
                    }
                    dialog_btn_submit.setVisibility(View.VISIBLE);
                    TextView textView = dialog_txt_total_cost;
                    textView.setText(shop_currency + BarangKasirActivity.this.f.format((total_cost + calculated_tax) - discount));
                    return;
                }
                double calculated_total_cost = (total_cost + calculated_tax) - Utils.DOUBLE_EPSILON;
                TextView textView2 = dialog_txt_total_cost;
                textView2.setText(shop_currency + BarangKasirActivity.this.f.format(calculated_total_cost));
            }

            public void afterTextChanged(Editable s) {
            }
        });

        this.customerNames = new ArrayList();
//        dbAccess.open();
//        List<HashMap<String, String>> customer = dbAccess.getCustomers();
//        for (int i = 0; i < customer.size(); i++) {
//            this.customerNames.add(customer.get(i).get(Constant.CUSTOMER_NAME));
//        }
        this.orderTypeNames = new ArrayList();
//        dbAccess.open();
//        List<HashMap<String, String>> order_type = dbAccess.getOrderType();
//        for (int i2 = 0; i2 < order_type.size(); i2++) {
//            this.orderTypeNames.add(order_type.get(i2).get(DBHelper.ORDER_TYPE));
//        }
        this.paymentMethodNames = new ArrayList();
//        dbAccess.open();
//        List<HashMap<String, String>> payment_method = dbAccess.getPaymentMethod();
//        for (int i3 = 0; i3 < payment_method.size(); i3++) {
//            this.paymentMethodNames.add(payment_method.get(i3).get(Constant.PAYMENT_METHOD_NAME));
//        }

        dialog_img_order_payment_method.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BarangKasirActivity.this.paymentMethodAdapter = new ArrayAdapter<>(BarangKasirActivity.this, android.R.layout.simple_list_item_1);
                BarangKasirActivity.this.paymentMethodAdapter.addAll(BarangKasirActivity.this.paymentMethodNames);
                AlertDialog.Builder dialog = new AlertDialog.Builder(BarangKasirActivity.this);
                View dialogView = BarangKasirActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, (ViewGroup) null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = (ListView) dialogView.findViewById(R.id.dialog_list);
                //((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.select_payment_method);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter(BarangKasirActivity.this.paymentMethodAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        BarangKasirActivity.this.paymentMethodAdapter.getFilter().filter(charSequence);
                    }

                    public void afterTextChanged(Editable s) {
                    }
                });
                final AlertDialog alertDialog = dialog.create();
                ((Button) dialogView.findViewById(R.id.dialog_button)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        alertDialog.dismiss();
                        dialog_order_payment_method.setText(BarangKasirActivity.this.paymentMethodAdapter.getItem(position));
                    }
                });
            }
        });

        dialog_img_order_type.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BarangKasirActivity.this.orderTypeAdapter = new ArrayAdapter<>(BarangKasirActivity.this, android.R.layout.simple_list_item_1);
                BarangKasirActivity.this.orderTypeAdapter.addAll(BarangKasirActivity.this.orderTypeNames);
                AlertDialog.Builder dialog = new AlertDialog.Builder(BarangKasirActivity.this);
                View dialogView = BarangKasirActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, (ViewGroup) null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = (ListView) dialogView.findViewById(R.id.dialog_list);
                //((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.select_order_type);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter(BarangKasirActivity.this.orderTypeAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        BarangKasirActivity.this.orderTypeAdapter.getFilter().filter(charSequence);
                    }

                    public void afterTextChanged(Editable s) {
                    }
                });
                final AlertDialog alertDialog = dialog.create();
                ((Button) dialogView.findViewById(R.id.dialog_button)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    /* class com.app.smartpos.pos.ProductCart.AnonymousClass4.AnonymousClass3 */

                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        alertDialog.dismiss();
                        dialog_order_type.setText(BarangKasirActivity.this.orderTypeAdapter.getItem(position));
                    }
                });
            }
        });

        dialog_img_customer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BarangKasirActivity.this.customerAdapter = new ArrayAdapter<>(BarangKasirActivity.this, android.R.layout.simple_list_item_1);
                BarangKasirActivity.this.customerAdapter.addAll(BarangKasirActivity.this.customerNames);
                AlertDialog.Builder dialog = new AlertDialog.Builder(BarangKasirActivity.this);
                View dialogView = BarangKasirActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, (ViewGroup) null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                ListView dialog_list = (ListView) dialogView.findViewById(R.id.dialog_list);
                //((TextView) dialogView.findViewById(R.id.dialog_title)).setText(R.string.select_customer);
                dialog_list.setVerticalScrollBarEnabled(true);
                dialog_list.setAdapter(BarangKasirActivity.this.customerAdapter);
                ((EditText) dialogView.findViewById(R.id.dialog_input)).addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        BarangKasirActivity.this.customerAdapter.getFilter().filter(charSequence);
                    }

                    public void afterTextChanged(Editable s) {
                    }
                });
                final AlertDialog alertDialog = dialog.create();
                ((Button) dialogView.findViewById(R.id.dialog_button)).setOnClickListener(new View.OnClickListener() {
                    /* class com.app.smartpos.pos.ProductCart.AnonymousClass5.AnonymousClass2 */

                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        alertDialog.dismiss();
                        dialog_customer.setText(BarangKasirActivity.this.customerAdapter.getItem(position));
                    }
                });
            }
        });

        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        dialog_btn_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String discount;
                String order_type = dialog_order_type.getText().toString().trim();
                String order_payment_method = dialog_order_payment_method.getText().toString().trim();
                String customer_name = dialog_customer.getText().toString().trim();
                String discount2 = dialog_etxt_discount.getText().toString().trim();
                if (discount2.isEmpty()) {
                    discount = "0.00";
                } else {
                    discount = discount2;
                }
                BarangKasirActivity.this.proceedOrder(order_type, order_payment_method, customer_name, calculated_tax, discount);
                alertDialog.dismiss();
            }
        });

        dialog_btn_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void proceedOrder(String type, String payment_method, String customer_name, double calculated_tax, String discount) {
        JSONException e;
        String str = DBHelper.BARANG_ID;
        DBAccess dbAccess = DBAccess.getInstance(this);
        dbAccess.open();
        int itemCount = dbAccess.getKasirItemCount();
        if (itemCount > 0) {
            dbAccess.open();
            List<HashMap<String, String>> lines = dbAccess.getKeranjangBarang();
            if (lines.isEmpty()) {
                Toasty.error((Context) this, (int) R.string.no_data_found, 0).show();
                DBAccess dbAccess1 = dbAccess;
                int i = itemCount;
                return;
            }
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
            String currentTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date());
            Long tsLong = Long.valueOf(System.currentTimeMillis() / 1000);
            String timeStamp = tsLong.toString();
            Log.d("Time", timeStamp);
            JSONObject obj = new JSONObject();
            try {
                obj.put(DBHelper.ORDER_DATE, currentDate);
                obj.put(DBHelper.ORDER_TIME, currentTime);
                obj.put(DBHelper.ORDER_TYPE, type);
                int i2 = itemCount;
                try {
                    obj.put(DBHelper.ORDER_PAYMENT, payment_method);
                    obj.put(DBHelper.ORDER_CUSTOMER, customer_name);
                    String str2 = currentTime;
                    Long l = tsLong;
                    try {
                        obj.put(DBHelper.ORDER_TAX, calculated_tax);
                        obj.put(DBHelper.ORDER_DISCOUNT, discount);
                        JSONArray array = new JSONArray();
                        int i3 = 0;
                        while (i3 < lines.size()) {
                            dbAccess.open();
                            String product_id = (String) lines.get(i3).get(str);
                            String product_name = dbAccess.getBarangNama(product_id);
                            dbAccess.open();
                            String timeStamp2 = timeStamp;
                            String weight_unit = dbAccess.getSatuanNama((String) lines.get(i3).get(DBHelper.KASIR_SATUAN)); //try
                            //dbAccess.open();
                            //String product_image = dbAccess.getProductImage(product_id);
                            DBAccess dbAccess2 = dbAccess;
                            JSONObject objp = new JSONObject();
                            try {
                                objp.put(str, product_id);
                                String str3 = str;
                                objp.put(DBHelper.REKAP_NAMA, product_name);
                                StringBuilder sb = new StringBuilder();
                                String str4 = product_id;
                                sb.append((String) lines.get(i3).get(DBHelper.REKAP_BOBOT));
                                sb.append(" ");
                                sb.append(weight_unit);
                                objp.put(DBHelper.REKAP_BOBOT, sb.toString());
                                objp.put(DBHelper.REKAP_QTY, lines.get(i3).get(DBHelper.KASIR_QTY));
                                objp.put(DBHelper.KASIR_STOCK, lines.get(i3).get(DBHelper.KASIR_STOCK));
                                objp.put(DBHelper.REKAP_HARGA, lines.get(i3).get(DBHelper.KASIR_HARGA));
                                //objp.put(Constant.PRODUCT_IMAGE, product_image);
                                objp.put(DBHelper.REKAP_DATE, currentDate);
                                array.put(objp);
                                i3++;
                                String str5 = type;
                                double d = calculated_tax;
                                dbAccess = dbAccess2;
                                timeStamp = timeStamp2;
                                str = str3;
                            } catch (JSONException e1) {
                                e = e1;
                                e.printStackTrace();
                                saveOrderInOfflineDb(obj);
                                return;
                            }
                        }
                        String str6 = timeStamp;
                        obj.put("lines", array);
                    } catch (JSONException e3) {
                        e = e3;
                        DBAccess dbAccess4 = dbAccess;
                        String str7 = timeStamp;
                        e.printStackTrace();
                        saveOrderInOfflineDb(obj);
                        return;
                    }
                } catch (JSONException e4) {
                    e = e4;
                    DBAccess dbAccess5 = dbAccess;
                    String str8 = currentTime;
                    Long l2 = tsLong;
                    String str9 = timeStamp;
                    e.printStackTrace();
                    saveOrderInOfflineDb(obj);
                    return;
                }
            } catch (JSONException e5) {
                e = e5;
                DBAccess dbAccess6 = dbAccess;
                int i4 = itemCount;
                String str10 = currentTime;
                Long l3 = tsLong;
                String str11 = timeStamp;
                e.printStackTrace();
                saveOrderInOfflineDb(obj);
                return;
            }
            saveOrderInOfflineDb(obj);
            return;
        }
        int i5 = itemCount;
        Toasty.error((Context) this, (int) R.string.no_product_in_cart, 0).show();
    }

    private void saveOrderInOfflineDb(JSONObject obj) {
        String timeStamp = Long.valueOf(System.currentTimeMillis() / 1000).toString();
        DBAccess dbAccess = DBAccess.getInstance(this);
        dbAccess.open();
        dbAccess.insertOrder(timeStamp, obj);
        Toasty.success((Context) this, (int) R.string.order_done_successful, 0).show();
        startActivity(new Intent(this, OrdersActivity.class));
        finish();
    }


}