package com.ahmadabuhasan.pos.model;

public class barang {

    private int id;
    private String namabarang;
    private String kodebarang;
    private String kategori;
    private String beli;
    private String stock;
    private String harga;
    private String satuan;
    private String lastupdate;
    private String keterangan;
    private String supplier;

    public barang() {

    }

    public barang(String namabarang,
                  String kodebarang,
                  String kategori,
                  String beli,
                  String stock,
                  String harga,
                  String satuan,
                  String lastupdate,
                  String keterangan,
                  String supplier) {

        this.namabarang = namabarang;
        this.kodebarang = kodebarang;
        this.kategori = kategori;
        this.beli = beli;
        this.stock = stock;
        this.harga = harga;
        this.satuan = satuan;
        this.lastupdate = lastupdate;
        this.keterangan = keterangan;
        this.supplier = supplier;
    }

    public barang(int id,
                  String namabarang,
                  String kodebarang,
                  String kategori,
                  String beli,
                  String stock,
                  String harga,
                  String satuan,
                  String lastupdate,
                  String keterangan,
                  String supplier) {

        this.id = id;
        this.namabarang = namabarang;
        this.kodebarang = kodebarang;
        this.kategori = kategori;
        this.beli = beli;
        this.stock = stock;
        this.harga = harga;
        this.satuan = satuan;
        this.lastupdate = lastupdate;
        this.keterangan = keterangan;
        this.supplier = supplier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamabarang() {
        return namabarang;
    }

    public void setNamabarang(String namabarang) {
        this.namabarang = namabarang;
    }

    public String getKodebarang() {
        return kodebarang;
    }

    public void setKodebarang(String kodebarang) {
        this.kodebarang = kodebarang;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getBeli() {
        return beli;
    }

    public void setBeli(String beli) {
        this.beli = beli;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

}
