package com.ahmadabuhasan.pos.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ahmad Abu Hasan on 4/12/2020
 */

public class DBHelper extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    public static final String DATABASE_NAME = "skripsi.db";

    //Table Name
    public static final String TABLE_BARANG = "barang";
    public static final String TABLE_KATEGORI = "kategori";
    public static final String TABLE_PELANGGAN = "pelanggan";
    public static final String TABLE_KASIR = "kasir";
    public static final String TABLE_SATUAN = "satuan";
    public static final String TABLE_SUPPLIER = "supplier";
    public static final String TABLE_INFO = "info";
    public static final String TABLE_REKAP = "rekap";
    public static final String TABLE_ORDER_LIST = "order_list";

    //Column Barang
    public static final String BARANG_ID = "id";
    public static final String BARANG_NAMA = "NamaBarang";
    public static final String BARANG_KODE = "KodeBarang";
    public static final String BARANG_KATEGORI = "Kategori";
    public static final String BARANG_BELI = "Beli";
    public static final String BARANG_STOCK = "Stock";
    public static final String BARANG_HARGA = "Harga";
    public static final String BARANG_BOBOT = "Bobot";
    public static final String BARANG_SATUAN = "Satuan";
    public static final String BARANG_LASTUPDATE = "LastUpdate";
    public static final String BARANG_KETERANGAN = "Keterangan";
    public static final String BARANG_SUPPLIER = "Supplier";

    //Column Kategori
    public static final String KATEGORI_ID = "id";
    public static final String KATEGORI_LIST = "List";
    public static final String KATEGORI_LASTUPDATE = "LastUpdate";

    //Column Pelanggan
    public static final String PELANGGAN_ID = "id";
    public static final String PELANGGAN_NAMA = "Nama";
    public static final String PELANGGAN_ALAMAT = "Alamat";
    public static final String PELANGGAN_TELEPON = "Telepon";
    public static final String PELANGGAN_HP = "HP";
    public static final String PELANGGAN_REKENING = "Rekening";
    public static final String PELANGGAN_KETERANGAN = "Keterangan";
    public static final String PELANGGAN_LASTUPDATE = "LastUpdate";

    //Column Kasir
    public static final String KASIR_ID = "id";
    public static final String KASIR_BARANGID = "Barang_ID";
    public static final String KASIR_BOBOT = "Bobot";
    public static final String KASIR_SATUAN = "Satuan";
    public static final String KASIR_HARGA = "Harga";
    public static final String KASIR_QTY = "QTY";
    public static final String KASIR_STOCK = "Stock";


    //Column Satuan
    public static final String SATUAN_ID = "id";
    public static final String SATUAN_LIST = "List";
    public static final String SATUAN_LASTUPDATE = "LastUpdate";

    //Column Supplier
    public static final String SUPPLIER_ID = "id";
    public static final String SUPPLIER_NAMA = "Nama";
    public static final String SUPPLIER_ALAMAT = "Alamat";
    public static final String SUPPLIER_TELEPON = "Telepon";
    public static final String SUPPLIER_HP = "HP";
    public static final String SUPPLIER_SALES = "Sales";
    public static final String SUPPLIER_REKENING = "Rekening";
    public static final String SUPPLIER_KETERANGAN = "Keterangan";
    public static final String SUPPLIER_LASTUPDATE = "LastUpdate";

    //Column Info
    public static final String INFO_ID = "id";
    public static final String INFO_CURRENCY = "Currency";
    public static final String INFO_TAX = "Tax";

    //Column Rekap
    public static final String REKAP_ID = "id";
    public static final String REKAP_INVOICE = "INVOICE";
    public static final String REKAP_NAMA = "NamaBarang";
    public static final String REKAP_BOBOT = "Bobot";
    public static final String REKAP_QTY = "QTY";
    public static final String REKAP_HARGA = "Harga";
    public static final String REKAP_DATE = "Date";
    public static final String REKAP_STATUS = "Status";

    //Column Order List
    public static final String ORDER_ID = "id";
    public static final String ORDER_INVOICE = "INVOICE";
    public static final String ORDER_DATE = "Date";
    public static final String ORDER_TIME = "Time";
    public static final String ORDER_TYPE = "Type";
    public static final String ORDER_PAYMENT = "PaymentMethod";
    public static final String ORDER_CUSTOMER = "NamaCustomer";
    public static final String ORDER_TAX = "Tax";
    public static final String ORDER_DISCOUNT = "Discount";
    public static final String ORDER_STATUS = "Status";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Table Create Statement
    // Barang
    private static final String CREATE_TABLE_BARANG = "CREATE TABLE " + TABLE_BARANG +
            "(" + BARANG_ID + " INTEGER PRIMARY KEY,"
            + BARANG_NAMA + " TEXT,"
            + BARANG_KODE + " TEXT,"
            + BARANG_KATEGORI + " TEXT,"
            + BARANG_BELI + " TEXT,"
            + BARANG_STOCK + " TEXT,"
            + BARANG_HARGA + " TEXT,"
            + BARANG_BOBOT + " TEXT,"
            + BARANG_SATUAN + " TEXT,"
            + BARANG_LASTUPDATE + " TEXT,"
            + BARANG_KETERANGAN + " TEXT,"
            + BARANG_SUPPLIER + " TEXT"
            + ")";

    // Kategori
    private static final String CREATE_TABLE_KATEGORI = "CREATE TABLE " + TABLE_KATEGORI +
            "(" + KATEGORI_ID + " INTEGER PRIMARY KEY,"
            + KATEGORI_LIST + " TEXT,"
            + KATEGORI_LASTUPDATE + " TEXT"
            + ")";

    // Pelanggan
    private static final String CREATE_TABLE_PELANGGAN = "CREATE TABLE " + TABLE_PELANGGAN +
            "(" + PELANGGAN_ID + " INTEGER PRIMARY KEY,"
            + PELANGGAN_NAMA + " TEXT,"
            + PELANGGAN_ALAMAT + " TEXT,"
            + PELANGGAN_TELEPON + " TEXT,"
            + PELANGGAN_HP + " TEXT,"
            + PELANGGAN_REKENING + " TEXT,"
            + PELANGGAN_KETERANGAN + " TEXT,"
            + PELANGGAN_LASTUPDATE + " TEXT"
            + ")";

    // Kasir
    private static final String CREATE_TABLE_KASIR = "CREATE TABLE " + TABLE_KASIR +
            "(" + KASIR_ID + " INTEGER PRIMARY KEY,"
            + KASIR_BARANGID + " TEXT,"
            + KASIR_BOBOT + " TEXT,"
            + KASIR_SATUAN + " TEXT,"
            + KASIR_HARGA + " TEXT,"
            + KASIR_QTY + " INTEGER,"
            + KASIR_STOCK + " TEXT"
            + ")";

    // Satuan
    private static final String CREATE_TABLE_SATUAN = "CREATE TABLE " + TABLE_SATUAN +
            "(" + SATUAN_ID + " INTEGER PRIMARY KEY,"
            + SATUAN_LIST + " TEXT,"
            + SATUAN_LASTUPDATE + " TEXT"
            + ")";

    // Supplier
    private static final String CREATE_TABLE_SUPPLIER = "CREATE TABLE " + TABLE_SUPPLIER +
            "(" + SUPPLIER_ID + " INTEGER PRIMARY KEY,"
            + SUPPLIER_NAMA + " TEXT,"
            + SUPPLIER_ALAMAT + " TEXT,"
            + SUPPLIER_TELEPON + " TEXT,"
            + SUPPLIER_HP + " TEXT,"
            + SUPPLIER_SALES + " TEXT,"
            + SUPPLIER_REKENING + " TEXT,"
            + SUPPLIER_KETERANGAN + " TEXT,"
            + SUPPLIER_LASTUPDATE + " TEXT"
            + ")";

    private static final String CREATE_TABLE_ORDER = "CREATE TABLE " + TABLE_ORDER_LIST +
            "(" + ORDER_ID + " INTEGER PRIMARY KEY,"
            + ORDER_INVOICE + " TEXT,"
            + ORDER_DATE + " TEXT,"
            + ORDER_TIME + " TEXT,"
            + ORDER_TYPE + " TEXT,"
            + ORDER_PAYMENT + " TEXT,"
            + ORDER_CUSTOMER + " TEXT,"
            + ORDER_TAX + " TEXT,"
            + ORDER_DISCOUNT + " TEXT,"
            + ORDER_STATUS + " TEXT"
            + ")";

    private static final String CREATE_TABLE_REKAP = " CREATE TABLE " + TABLE_REKAP +
            "(" + REKAP_ID + " INTEGER PRIMARY KEY,"
            + REKAP_INVOICE + " TEXT,"
            + REKAP_NAMA + " TEXT,"
            + REKAP_BOBOT + " TEXT,"
            + REKAP_QTY + " TEXT,"
            + REKAP_HARGA + " TEXT,"
            + REKAP_DATE + " TEXT,"
            + REKAP_STATUS + " TEXT"
            + ")";


    @Override
    public void onCreate(SQLiteDatabase db) {

        //script sql
        db.execSQL(CREATE_TABLE_BARANG);
        //db.execSQL(CREATE_TABLE_KATEGORI);
        db.execSQL(CREATE_TABLE_PELANGGAN);
        db.execSQL(CREATE_TABLE_KASIR);
        //db.execSQL(CREATE_TABLE_SATUAN);
        //db.execSQL(CREATE_TABLE_SUPPLIER);
        db.execSQL(CREATE_TABLE_ORDER);
        db.execSQL(CREATE_TABLE_REKAP);

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_KATEGORI + "(id INTEGER PRIMARY KEY, List TEXT, LastUpdate TEXT)");
        db.execSQL("INSERT INTO " + TABLE_KATEGORI + "(id, List, LastUpdate) VALUES (1, 'Cat', '2/12/2020')");
        db.execSQL("INSERT INTO " + TABLE_KATEGORI + "(id, List, LastUpdate) VALUES (2, 'Lem', '2/12/2020')");
        db.execSQL("INSERT INTO " + TABLE_KATEGORI + "(id, List, LastUpdate) VALUES (3, 'Kunci', '2/12/2020')");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SATUAN + "(id INTEGER PRIMARY KEY, List TEXT, LastUpdate TEXT)");
        db.execSQL("INSERT INTO " + TABLE_SATUAN + "(id, List, LastUpdate) VALUES (1, 'pcs', '1/12/2020')");
        db.execSQL("INSERT INTO " + TABLE_SATUAN + "(id, List, LastUpdate) VALUES (2, 'kg', '1/12/2020')");
        db.execSQL("INSERT INTO " + TABLE_SATUAN + "(id, List, LastUpdate) VALUES (3, 'cm', '1/12/2020')");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SUPPLIER + "(id INTEGER PRIMARY KEY, Nama TEXT, Alamat TEXT, Telepon TEXT, HP TEXT, Sales TEXT, Rekening TEXT, Keterangan TEXT, LastUpdate TEXT)");
        db.execSQL("INSERT INTO " + TABLE_SUPPLIER + "(id, Nama, Alamat, Telepon, HP, Sales, Rekening, Keterangan, LastUpdate) VALUES (1, 'Demo', 'Bangil', '0343-123456', '08123456789', 'John', '123456789', 'Manusia', '2/12/2020')");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_INFO + "(id INTEGER PRIMARY KEY, Currency TEXT, Pajak TEXT)");
        db.execSQL("INSERT INTO " + TABLE_INFO + "(id, Currency, Pajak) VALUES (1, 'Rp', 10)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BARANG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KATEGORI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PELANGGAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KASIR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SATUAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPLIER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REKAP);
        onCreate(db);

    }

}
