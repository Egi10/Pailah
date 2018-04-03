package com.egifcb.paila.pailah.model;

import java.io.Serializable;

public class Mount implements Serializable {
    String key;
    String namaGunung;
    String tipeGunung;
    String detail;
    String ketinggian;
    String letusanTerakhir;
    String provinsi;
    String photo;
    String publish;

    public Mount(){

    }

    public Mount(String key, String namaGunung, String tipeGunung, String detail, String ketinggian, String letusanTerakhir, String provinsi, String photo, String publish){
        this.key = key;
        this.namaGunung = namaGunung;
        this.tipeGunung = tipeGunung;
        this.detail = detail;
        this.ketinggian = ketinggian;
        this.letusanTerakhir = letusanTerakhir;
        this.provinsi = provinsi;
        this.photo = photo;
        this.publish = publish;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNamaGunung() {
        return namaGunung;
    }

    public void setNamaGunung(String namaGunung) {
        this.namaGunung = namaGunung;
    }

    public String getTipeGunung() {
        return tipeGunung;
    }

    public void setTipeGunung(String tipeGunung) {
        this.tipeGunung = tipeGunung;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getKetinggian() {
        return ketinggian;
    }

    public void setKetinggian(String ketinggian) {
        this.ketinggian = ketinggian;
    }

    public String getLetusanTerakhir() {
        return letusanTerakhir;
    }

    public void setLetusanTerakhir(String letusanTerakhir) {
        this.letusanTerakhir = letusanTerakhir;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }
}
