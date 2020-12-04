package com.ahmadabuhasan.pos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.mikephil.charting.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ahmad Abu Hasan on 4/12/2020
 */

public class DBAccess {

    private static DBAccess instance;
    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;

    private DBAccess(Context context) {
        this.openHelper = new DBHelper(context);
    }

    public static DBAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DBAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = this.openHelper.getWritableDatabase();
    }

    public void close() {
        SQLiteDatabase sQLiteDatabase = this.database;
        if (sQLiteDatabase != null) {
            sQLiteDatabase.close();
        }
    }

    //adddataactivity
    public boolean addBarang(String NamaBarang,
                             String KodeBarang,
                             String Kategori_Id,
                             String Beli,
                             String Stock,
                             String Harga,
                             String Bobot,
                             String Satuan_Id,
                             String LastUpdate,
                             String Keterangan,
                             String Supplier_Id) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.BARANG_NAMA, NamaBarang);
        values.put(DBHelper.BARANG_KODE, KodeBarang);
        values.put(DBHelper.BARANG_KATEGORI, Kategori_Id);
        values.put(DBHelper.BARANG_BELI, Beli);
        values.put(DBHelper.BARANG_STOCK, Stock);
        values.put(DBHelper.BARANG_BOBOT, Bobot);
        values.put(DBHelper.BARANG_HARGA, Harga);
        values.put(DBHelper.BARANG_SATUAN, Satuan_Id);
        values.put(DBHelper.BARANG_LASTUPDATE, LastUpdate);
        values.put(DBHelper.BARANG_KETERANGAN, Keterangan);
        values.put(DBHelper.BARANG_SUPPLIER, Supplier_Id);

        long check = this.database.insert("barang", null, values);
        this.database.close();
        return check != -1;
    }

    //barangadapter,kasirbarangadapter,barangkasiractivity,kasiradapter
    public String getCurrency() {
        String currency = "n/a";
        Cursor cursor = this.database.rawQuery("SELECT * FROM info", (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                currency = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return currency;
    }

    //viewdataactivity,kasiractivity
    public ArrayList<HashMap<String, String>> getBarang() {
        ArrayList<HashMap<String, String>> barang = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM barang ORDER BY id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DBHelper.BARANG_ID, cursor.getString(0));
                map.put(DBHelper.BARANG_NAMA, cursor.getString(1));
                map.put(DBHelper.BARANG_KODE, cursor.getString(2));
                map.put(DBHelper.BARANG_KATEGORI, cursor.getString(3));
                map.put(DBHelper.BARANG_BELI, cursor.getString(4));
                map.put(DBHelper.BARANG_STOCK, cursor.getString(5));
                map.put(DBHelper.BARANG_HARGA, cursor.getString(6));
                map.put(DBHelper.BARANG_BOBOT, cursor.getString(7));
                map.put(DBHelper.BARANG_SATUAN, cursor.getString(8));
                map.put(DBHelper.BARANG_LASTUPDATE, cursor.getString(9));
                map.put(DBHelper.BARANG_KETERANGAN, cursor.getString(10));
                map.put(DBHelper.BARANG_SUPPLIER, cursor.getString(11));
                barang.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return barang;
    }

    //viewdataactivity,kasiractivity
    public ArrayList<HashMap<String, String>> getSearchBarang(String s) {
        ArrayList<HashMap<String, String>> barang = new ArrayList<>();
        SQLiteDatabase sQLiteDatabase = this.database;
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM barang WHERE NamaBarang LIKE '%" + s + "%' OR KodeBarang LIKE '%" + s + "%' ORDER BY id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DBHelper.BARANG_ID, cursor.getString(0));
                map.put(DBHelper.BARANG_NAMA, cursor.getString(1));
                map.put(DBHelper.BARANG_KODE, cursor.getString(2));
                map.put(DBHelper.BARANG_KATEGORI, cursor.getString(3));
                map.put(DBHelper.BARANG_BELI, cursor.getString(4));
                map.put(DBHelper.BARANG_STOCK, cursor.getString(5));
                map.put(DBHelper.BARANG_HARGA, cursor.getString(6));
                map.put(DBHelper.BARANG_BOBOT, cursor.getString(7));
                map.put(DBHelper.BARANG_SATUAN, cursor.getString(8));
                map.put(DBHelper.BARANG_LASTUPDATE, cursor.getString(9));
                map.put(DBHelper.BARANG_KETERANGAN, cursor.getString(10));
                map.put(DBHelper.BARANG_SUPPLIER, cursor.getString(11));
                barang.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return barang;
    }

    //viewdataactivity
    public boolean deleteBarang(String barang_id) {
        long check = this.database.delete("barang", "id" + "=?", new String[]{barang_id});
        this.database.close();
        return check == 1;
    }

    //updatedataactivity
    public ArrayList<HashMap<String, String>> getBarangInfo(String barang_id) {
        ArrayList<HashMap<String, String>> barang = new ArrayList<>();
        SQLiteDatabase sQLiteDatabase = this.database;
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM barang WHERE id='" + barang_id + "'", (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DBHelper.BARANG_ID, cursor.getString(0));
                map.put(DBHelper.BARANG_NAMA, cursor.getString(1));
                map.put(DBHelper.BARANG_KODE, cursor.getString(2));
                map.put(DBHelper.BARANG_KATEGORI, cursor.getString(3));
                map.put(DBHelper.BARANG_BELI, cursor.getString(4));
                map.put(DBHelper.BARANG_STOCK, cursor.getString(5));
                map.put(DBHelper.BARANG_HARGA, cursor.getString(6));
                map.put(DBHelper.BARANG_BOBOT, cursor.getString(7));
                map.put(DBHelper.BARANG_SATUAN, cursor.getString(8));
                map.put(DBHelper.BARANG_LASTUPDATE, cursor.getString(9));
                map.put(DBHelper.BARANG_KETERANGAN, cursor.getString(10));
                map.put(DBHelper.BARANG_SUPPLIER, cursor.getString(11));
                barang.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return barang;
    }

    //updatedataactivity
    public String getKategoriNama(String kategori_id) {
        String barang_kategori = "n/a";
        SQLiteDatabase sQLiteDatabase = this.database;
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM kategori WHERE id=" + kategori_id + "", (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                barang_kategori = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return barang_kategori;
    }

    //updatedataactivity,kasirbarangadapter
    public String getSatuanNama(String satuan_id) {
        String satuan_nama = "n/a";
        SQLiteDatabase sQLiteDatabase = this.database;
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM satuan WHERE id=" + satuan_id + "", (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                satuan_nama = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return satuan_nama;
    }

    //updatedataactivity
    public String getSupplierNama(String supplier_id) {
        String supplier_nama = "n/a";
        SQLiteDatabase sQLiteDatabase = this.database;
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM supplier WHERE id=" + supplier_id + "", (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                supplier_nama = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return supplier_nama;
    }

    //updatedataactivity,kasiractivity
    public ArrayList<HashMap<String, String>> getBarangKategori() {
        ArrayList<HashMap<String, String>> barang_kategori = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM kategori ORDER BY id DESC", (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DBHelper.KATEGORI_ID, cursor.getString(0));
                map.put(DBHelper.KATEGORI_LIST, cursor.getString(1));
                barang_kategori.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return barang_kategori;
    }

    //adddataactivity
    public ArrayList<HashMap<String, String>> getKategori() {
        ArrayList<HashMap<String, String>> barang_kategori = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM kategori ORDER BY id DESC", (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DBHelper.KATEGORI_ID, cursor.getString(0));
                map.put(DBHelper.KATEGORI_LIST, cursor.getString(1));
                barang_kategori.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return barang_kategori;
    }

    //adddataactivity,updatedataactivity
    public ArrayList<HashMap<String, String>> getSatuan() {
        ArrayList<HashMap<String, String>> barang_satuan = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM satuan", (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(0));
                map.put("List", cursor.getString(1));
                barang_satuan.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return barang_satuan;
    }

    //adddataactivity,updatedataactivity
    public ArrayList<HashMap<String, String>> getSupplier() {
        ArrayList<HashMap<String, String>> barang_supplier = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM supplier", (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DBHelper.SUPPLIER_ID, cursor.getString(0));
                map.put(DBHelper.SUPPLIER_NAMA, cursor.getString(1));
                barang_supplier.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return barang_supplier;
    }

    //updatedataactivity
    public Boolean updateBarang(String NamaBarang,
                                String KodeBarang,
                                String Kategori,
                                String Beli,
                                String Stock,
                                String Harga,
                                String Bobot,
                                String Satuan,
                                String LastUpdate,
                                String Keterangan,
                                String Supplier,
                                String barang_id) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.BARANG_NAMA, NamaBarang);
        values.put(DBHelper.BARANG_KODE, KodeBarang);
        values.put(DBHelper.BARANG_KATEGORI, Kategori);
        values.put(DBHelper.BARANG_BELI, Beli);
        values.put(DBHelper.BARANG_STOCK, Stock);
        values.put(DBHelper.BARANG_HARGA, Harga);
        values.put(DBHelper.BARANG_BOBOT, Bobot);
        values.put(DBHelper.BARANG_SATUAN, Satuan);
        values.put(DBHelper.BARANG_LASTUPDATE, LastUpdate);
        values.put(DBHelper.BARANG_KETERANGAN, Keterangan);
        values.put(DBHelper.BARANG_SUPPLIER, Supplier);

        SQLiteDatabase sqLiteDatabase = this.database;
        String[] strArr = {barang_id};
        this.database.isOpen();
        if (((long) sqLiteDatabase.update("barang", values, "id=?", strArr)) == -1) {
            return false;
        }
        return true;
    }

    //kasirbarangadapter
    public int addToKasir(String barang_id, String bobot, String satuan, String harga, int qty, String stock) {
        SQLiteDatabase sQLiteDatabase = this.database;
        if (sQLiteDatabase.rawQuery("SELECT * FROM kasir WHERE id='" + barang_id + "'", (String[]) null).getCount() >= 1) {
            return 2;
        }
        ContentValues values = new ContentValues();
        values.put(DBHelper.KASIR_BARANGID, barang_id);
        values.put(DBHelper.KASIR_BOBOT, bobot);
        values.put(DBHelper.KASIR_SATUAN, satuan);
        values.put(DBHelper.KASIR_HARGA, harga);
        values.put(DBHelper.KASIR_QTY, Integer.valueOf(qty));
        values.put(DBHelper.KASIR_STOCK, stock);
        long check = this.database.insert("kasir", (String) null, values);
        this.database.close();
        if (check == -1) {
            return -1;

        }
        return 1;
    }

    //kasiractivity
    public int getKasirItemCount() {
        Cursor cursor = this.database.rawQuery("SELECT * FROM kasir", (String[]) null);
        int itemCount = cursor.getCount();
        cursor.close();
        this.database.close();
        return itemCount;
    }

    //kategoribarangadapter
    public ArrayList<HashMap<String, String>> getTabBarang(String kategori_id) {
        ArrayList<HashMap<String, String>> product = new ArrayList<>();
        SQLiteDatabase sQLiteDatabase = this.database;
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM barang WHERE Kategori = '" + kategori_id + "' ORDER BY id DESC", (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DBHelper.BARANG_ID, cursor.getString(0));
                map.put(DBHelper.BARANG_NAMA, cursor.getString(1));
                map.put(DBHelper.BARANG_KODE, cursor.getString(2));
                map.put(DBHelper.BARANG_KATEGORI, cursor.getString(3));
                map.put(DBHelper.BARANG_BELI, cursor.getString(4));
                map.put(DBHelper.BARANG_STOCK, cursor.getString(5));
                map.put(DBHelper.BARANG_HARGA, cursor.getString(6));
                map.put(DBHelper.BARANG_SATUAN, cursor.getString(7));
                map.put(DBHelper.BARANG_LASTUPDATE, cursor.getString(8));
                map.put(DBHelper.BARANG_KETERANGAN, cursor.getString(9));
                map.put(DBHelper.BARANG_SUPPLIER, cursor.getString(10));

                product.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return product;
    }

    //barangkasiractivity
    public ArrayList<HashMap<String, String>> getKeranjangBarang() {
        ArrayList<HashMap<String, String>> barang = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM kasir", (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DBHelper.KASIR_ID, cursor.getString(0));
                map.put(DBHelper.KASIR_BARANGID, cursor.getString(1));
                map.put(DBHelper.KASIR_BOBOT, cursor.getString(2));
                map.put(DBHelper.KASIR_SATUAN, cursor.getString(3));
                map.put(DBHelper.KASIR_HARGA, cursor.getString(4));
                map.put(DBHelper.KASIR_QTY, cursor.getString(5));
                map.put(DBHelper.KASIR_STOCK, cursor.getString(6));
                barang.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return barang;
    }

    //kasiradapter
    public String getBarangNama(String barang_id) {
        String barang_nama = "n/a";
        SQLiteDatabase sQLiteDatabase = this.database;
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM barang WHERE id='" + barang_id + "'", (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                barang_nama = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return barang_nama;
    }

    //kasiradapter
    public double getTotalHarga() {
        double total_harga = Utils.DOUBLE_EPSILON;
        Cursor cursor = this.database.rawQuery("SELECT * FROM kasir", (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                double harga = Double.parseDouble(cursor.getString(4));
                double parseInt = (double) Integer.parseInt(cursor.getString(5));
                Double.isNaN(parseInt);
                total_harga += parseInt * harga;
            } while (cursor.moveToNext());
        } else {
            total_harga = Utils.DOUBLE_EPSILON;
        }
        cursor.close();
        this.database.close();
        return total_harga;
    }

    //kasiradapter
    public boolean deleteBarangDariKeranjang(String id) {
        long check = (long) this.database.delete("kasir", "id=?", new String[]{id});
        this.database.close();
        return check == 1;
    }

    //kasiradapter
    public int getKeranjangItemCount() {
        Cursor cursor = this.database.rawQuery("SELECT * FROM kasir", (String[]) null);
        int itemCount = cursor.getCount();
        cursor.close();
        this.database.close();
        return itemCount;
    }

    //kasiradapter
    public void updateBarangQty(String id, String qty) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.KASIR_QTY, qty);
        long update = (long) this.database.update("kasir", values, "id=?", new String[]{id});
    }

    //barangkasiractivity
    public void insertOrder(String paramString, JSONObject paramJSONObject) {
        String str1 = "Pending";
        String str2 = "Status"; //rekap
        String str3 = "Date";
        //String str6 = "product_image";
        String str4 = "Harga";

        ContentValues contentValues2 = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        ContentValues contentValues3 = new ContentValues();
        String str5 = "QTY";
        try {

            String str7 = paramJSONObject.getString(DBHelper.ORDER_DATE);
            String str8 = paramJSONObject.getString(DBHelper.ORDER_TIME);
            String str9 = paramJSONObject.getString(DBHelper.ORDER_TYPE);
            String str10 = paramJSONObject.getString(DBHelper.ORDER_PAYMENT);
            String str11 = paramJSONObject.getString(DBHelper.ORDER_CUSTOMER);
            String str12 = paramJSONObject.getString(DBHelper.ORDER_TAX);
            String str13 = paramJSONObject.getString(DBHelper.ORDER_DISCOUNT);

            ContentValues contentValues = contentValues2;

            contentValues.put(DBHelper.ORDER_INVOICE, paramString);
            contentValues.put(DBHelper.ORDER_DATE, str7);
            contentValues.put(DBHelper.ORDER_TIME, str8);
            contentValues.put(DBHelper.ORDER_TYPE, str9);
            contentValues.put(DBHelper.ORDER_PAYMENT, str10);
            contentValues.put(DBHelper.ORDER_CUSTOMER, str11);
            contentValues.put(DBHelper.ORDER_TAX, str12);
            contentValues.put(DBHelper.ORDER_DISCOUNT, str13);
            contentValues.put(DBHelper.ORDER_STATUS, "Pending");

            this.database.insert("order_list", null, contentValues);
            this.database.delete("kasir", null, null);

        } catch (JSONException jSONException) {
            jSONException.printStackTrace();

        }
    }

    //barangkasiractivity
    public ArrayList<HashMap<String, String>> getShopInformation() {
        ArrayList<HashMap<String, String>> shop_info = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM info", (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DBHelper.INFO_CURRENCY, cursor.getString(1));
                map.put(DBHelper.INFO_TAX, cursor.getString(2));
                shop_info.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return shop_info;
    }

    public ArrayList<HashMap<String, String>> getOrderList() {
        ArrayList<HashMap<String, String>> orderList = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM order_list ORDER BY id DESC", (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(DBHelper.ORDER_INVOICE, cursor.getString(1));
                map.put(DBHelper.ORDER_DATE, cursor.getString(2));
                map.put(DBHelper.ORDER_TIME, cursor.getString(3));
                map.put(DBHelper.ORDER_TYPE, cursor.getString(4));
                map.put(DBHelper.ORDER_PAYMENT, cursor.getString(5));
                map.put(DBHelper.ORDER_CUSTOMER, cursor.getString(6));
                map.put(DBHelper.ORDER_TAX, cursor.getString(7));
                map.put(DBHelper.ORDER_DISCOUNT, cursor.getString(8));
                map.put(DBHelper.ORDER_STATUS, cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_STATUS)));
                orderList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return orderList;
    }

    public boolean updateOrder(String invoiceId, String orderStatus) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.ORDER_STATUS, orderStatus);
        this.database.update("rekap", contentValues, "INVOICE=?", new String[]{invoiceId});
        long l = this.database.update("order_list", contentValues, "INVOICE=?", new String[]{invoiceId});
        this.database.close();
        return (l == 1L);
    }

    public float getMonthlySalesAmount(String month, String getYear) {
        float total_price = 0.0f;
        Cursor cursor = this.database.rawQuery("SELECT * FROM rekap WHERE Status='Completed'  AND  strftime('%m', Date) = '" + month + "' AND strftime('%Y', Date) = '" + getYear + "'  ", (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                total_price += ((float) Integer.parseInt(cursor.getString(4))) * Float.parseFloat(cursor.getString(5));
            } while (cursor.moveToNext());
        } else {
            total_price = 0.0f;
        }
        cursor.close();
        this.database.close();
        Log.d("total_price", "" + total_price);
        return total_price;
    }

    public double getTotalOrderPrice(String type) {
        Cursor cursor;
        double total_price = Utils.DOUBLE_EPSILON;
        if (type.equals("MONTHLY")) {
            String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
            cursor = this.database.rawQuery("SELECT * FROM rekap WHERE Status='Completed'  AND strftime('%m', Date) = '" + currentMonth + "' ", (String[]) null);
        } else if (type.equals("YEARLY")) {
            String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
            cursor = this.database.rawQuery("SELECT * FROM rekap WHERE Status='Completed'  AND  strftime('%Y', Date) = '" + currentYear + "' ", (String[]) null);
        } else if (type.equals("DAILY")) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
            SQLiteDatabase sQLiteDatabase = this.database;
            cursor = sQLiteDatabase.rawQuery("SELECT * FROM rekap WHERE Status='Completed'  AND   Date='" + currentDate + "' ORDER BY id DESC", (String[]) null);
        } else {
            cursor = this.database.rawQuery("SELECT * FROM rekap WHERE Status='Completed' ", (String[]) null);
        }
        if (cursor.moveToFirst()) {
            do {
                double price = Double.parseDouble(cursor.getString(5));
                double parseInt = (double) Integer.parseInt(cursor.getString(4));
                Double.isNaN(parseInt);
                total_price += parseInt * price;
            } while (cursor.moveToNext());
        } else {
            total_price = Utils.DOUBLE_EPSILON;
        }
        cursor.close();
        this.database.close();
        return total_price;
    }

    public double getTotalTax(String type) {
        Cursor cursor;
        double total_tax = Utils.DOUBLE_EPSILON;
        if (type.equals("MONTHLY")) {
            String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
            cursor = this.database.rawQuery("SELECT * FROM order_list WHERE Status='Completed'  AND  strftime('%m', Date) = '" + currentMonth + "' ", (String[]) null);
        } else if (type.equals("YEARLY")) {
            String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
            cursor = this.database.rawQuery("SELECT * FROM order_list WHERE Status='Completed'  AND strftime('%Y', Date) = '" + currentYear + "' ", (String[]) null);
        } else if (type.equals("DAILY")) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
            SQLiteDatabase sQLiteDatabase = this.database;
            cursor = sQLiteDatabase.rawQuery("SELECT * FROM order_list WHERE Status='Completed'  AND   Date='" + currentDate + "' ORDER BY order_id DESC", (String[]) null);
        } else {
            cursor = this.database.rawQuery("SELECT * FROM order_list WHERE Status='Completed' ", (String[]) null);
        }
        if (cursor.moveToFirst()) {
            do {
                total_tax += Double.parseDouble(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_TAX)));
            } while (cursor.moveToNext());
        } else {
            total_tax = Utils.DOUBLE_EPSILON;
        }
        cursor.close();
        this.database.close();
        return total_tax;
    }

    public double getTotalDiscount(String type) {
        Cursor cursor;
        double total_discount = Utils.DOUBLE_EPSILON;
        if (type.equals("MONTHLY")) {
            String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
            cursor = this.database.rawQuery("SELECT * FROM order_list WHERE Status='Completed'  AND strftime('%m', Date) = '" + currentMonth + "' ", (String[]) null);
        } else if (type.equals("YEARLY")) {
            String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
            cursor = this.database.rawQuery("SELECT * FROM order_list WHERE Status='Completed'  AND strftime('%Y', Date) = '" + currentYear + "' ", (String[]) null);
        } else if (type.equals("DAILY")) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
            SQLiteDatabase sQLiteDatabase = this.database;
            cursor = sQLiteDatabase.rawQuery("SELECT * FROM order_list WHERE Status='Completed'  AND   Date='" + currentDate + "' ORDER BY id DESC", (String[]) null);
        } else {
            cursor = this.database.rawQuery("SELECT * FROM order_list WHERE Status='Completed'", (String[]) null);
        }
        if (cursor.moveToFirst()) {
            do {
                total_discount += Double.parseDouble(cursor.getString(cursor.getColumnIndex(DBHelper.ORDER_DISCOUNT)));
            } while (cursor.moveToNext());
        } else {
            total_discount = Utils.DOUBLE_EPSILON;
        }
        cursor.close();
        this.database.close();
        return total_discount;
    }

}
