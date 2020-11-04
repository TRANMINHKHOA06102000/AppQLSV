package com.example.quanlysinhvien;

public class SinhVien {
    public int id;
    public  String ten;
    public  String sdt;
    public  byte[] anh;

    public SinhVien(int id, String ten, String sdt, byte[] anh) {
        this.id = id;
        this.ten = ten;
        this.sdt = sdt;
        this.anh = anh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public byte[] getAnh() {
        return anh;
    }

    public void setAnh(byte[] anh) {
        this.anh = anh;
    }
}
