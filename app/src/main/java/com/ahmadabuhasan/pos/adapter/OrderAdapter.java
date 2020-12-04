package com.ahmadabuhasan.pos.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.pos.OrderDetailsActivity;
import com.ahmadabuhasan.pos.R;
import com.ahmadabuhasan.pos.database.DBAccess;
import com.ahmadabuhasan.pos.database.DBHelper;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by Ahmad Abu Hasan on 4/12/2020
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    Context context;
    /* access modifiers changed from: private */
    public List<HashMap<String, String>> orderData;

    public OrderAdapter(Context context2, List<HashMap<String, String>> orderData2) {
        this.context = context2;
        this.orderData = orderData2;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final String invoice_id = (String) this.orderData.get(position).get(DBHelper.ORDER_INVOICE);
        String orderStatus = (String) this.orderData.get(position).get(DBHelper.ORDER_STATUS);
        holder.txt_customer_name.setText((String) this.orderData.get(position).get(DBHelper.ORDER_CUSTOMER));
        TextView textView = holder.txt_order_id;
        textView.setText(this.context.getString(R.string.order_id) + invoice_id);
        TextView textView2 = holder.txt_payment_method;
        textView2.setText(this.context.getString(R.string.payment_method) + ((String) this.orderData.get(position).get(DBHelper.ORDER_PAYMENT)));
        TextView textView3 = holder.txt_order_type;
        textView3.setText(this.context.getString(R.string.order_type) + ((String) this.orderData.get(position).get(DBHelper.ORDER_TYPE)));
        TextView textView4 = holder.txt_date;
        textView4.setText(((String) this.orderData.get(position).get(DBHelper.ORDER_TIME)) + " " + ((String) this.orderData.get(position).get(DBHelper.ORDER_DATE)));
        holder.txt_order_status.setText(orderStatus);
        if (orderStatus.equals("COMPLETED")) {
            holder.txt_order_status.setBackgroundColor(Color.parseColor("#43a047"));
            holder.txt_order_status.setTextColor(-1);
            holder.imgStatus.setVisibility(View.GONE);
        } else if (orderStatus.equals("CANCEL")) {
            holder.txt_order_status.setBackgroundColor(Color.parseColor("#e53935"));
            holder.txt_order_status.setTextColor(-1);
            holder.imgStatus.setVisibility(View.GONE);
        }
        holder.imgStatus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(OrderAdapter.this.context);
                dialogBuilder.withTitle(OrderAdapter.this.context.getString(R.string.change_order_status)).withMessage((CharSequence) OrderAdapter.this.context.getString(R.string.please_change_order_status_to_complete_or_cancel)).withEffect(Effectstype.Slidetop).withDialogColor("#01baef").withButton1Text(OrderAdapter.this.context.getString(R.string.order_completed)).withButton2Text(OrderAdapter.this.context.getString(R.string.cancel_order)).setButton1Click(new View.OnClickListener() {
                    public void onClick(View v) {
                        DBAccess databaseAccess = DBAccess.getInstance(OrderAdapter.this.context);
                        databaseAccess.open();
                        if (databaseAccess.updateOrder(invoice_id, "COMPLETED")) {
                            Toasty.success(OrderAdapter.this.context, (int) R.string.order_updated, 0).show();
                            holder.txt_order_status.setText("COMPLETED");
                            holder.txt_order_status.setBackgroundColor(Color.parseColor("#43a047"));
                            holder.txt_order_status.setTextColor(-1);
                            holder.imgStatus.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(OrderAdapter.this.context, R.string.failed, Toast.LENGTH_SHORT).show();
                        }
                        dialogBuilder.dismiss();
                    }
                }).setButton2Click(new View.OnClickListener() {
                    public void onClick(View v) {
                        DBAccess databaseAccess = DBAccess.getInstance(OrderAdapter.this.context);
                        databaseAccess.open();
                        if (databaseAccess.updateOrder(invoice_id, "CANCEL")) {
                            Toasty.error(OrderAdapter.this.context, (int) R.string.order_updated, 0).show();
                            holder.txt_order_status.setText("CANCEL");
                            holder.txt_order_status.setBackgroundColor(Color.parseColor("#e53935"));
                            holder.txt_order_status.setTextColor(-1);
                            holder.imgStatus.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(OrderAdapter.this.context, R.string.failed, Toast.LENGTH_SHORT).show();
                        }
                        dialogBuilder.dismiss();
                    }
                }).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.orderData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgStatus;
        TextView txt_customer_name;
        TextView txt_date;
        TextView txt_order_id;
        TextView txt_order_status;
        TextView txt_order_type;
        TextView txt_payment_method;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.txt_customer_name = (TextView) itemView.findViewById(R.id.txt_customer_name);
            this.txt_order_id = (TextView) itemView.findViewById(R.id.txt_order_id);
            this.txt_order_type = (TextView) itemView.findViewById(R.id.txt_order_type);
            this.txt_payment_method = (TextView) itemView.findViewById(R.id.txt_payment_method);
            this.txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            this.txt_order_status = (TextView) itemView.findViewById(R.id.txt_order_status);
            this.imgStatus = (ImageView) itemView.findViewById(R.id.img_status);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            Intent i = new Intent(OrderAdapter.this.context, OrderDetailsActivity.class);
            i.putExtra(DBHelper.ORDER_ID, (String) ((HashMap) OrderAdapter.this.orderData.get(getAdapterPosition())).get(DBHelper.ORDER_INVOICE));
            i.putExtra(DBHelper.ORDER_CUSTOMER, (String) ((HashMap) OrderAdapter.this.orderData.get(getAdapterPosition())).get(DBHelper.ORDER_CUSTOMER));
            i.putExtra(DBHelper.ORDER_DATE, (String) ((HashMap) OrderAdapter.this.orderData.get(getAdapterPosition())).get(DBHelper.ORDER_DATE));
            i.putExtra(DBHelper.ORDER_TIME, (String) ((HashMap) OrderAdapter.this.orderData.get(getAdapterPosition())).get(DBHelper.ORDER_TIME));
            i.putExtra(DBHelper.ORDER_TAX, (String) ((HashMap) OrderAdapter.this.orderData.get(getAdapterPosition())).get(DBHelper.ORDER_TAX));
            i.putExtra(DBHelper.ORDER_DISCOUNT, (String) ((HashMap) OrderAdapter.this.orderData.get(getAdapterPosition())).get(DBHelper.ORDER_DISCOUNT));
            OrderAdapter.this.context.startActivity(i);
        }
    }
}
