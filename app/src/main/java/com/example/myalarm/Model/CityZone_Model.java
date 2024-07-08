package com.example.myalarm.Model;

public class CityZone_Model {
    int id;
    String tenThanhPho;
    String quocGia;
    String zone;

    public CityZone_Model() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String time;

    public CityZone_Model(int id, String quocGia, String tenThanhPho, String zone) {
        this.id = id;
        this.quocGia = quocGia;
        this.tenThanhPho = tenThanhPho;
        this.zone = zone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuocGia() {
        return quocGia;
    }

    public void setQuocGia(String quocGia) {
        this.quocGia = quocGia;
    }

    public String getTenThanhPho() {
        return tenThanhPho;
    }

    public void setTenThanhPho(String tenThanhPho) {
        this.tenThanhPho = tenThanhPho;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}






