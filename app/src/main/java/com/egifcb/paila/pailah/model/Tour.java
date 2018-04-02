package com.egifcb.paila.pailah.model;

public class Tour {
    String key;
    String judul;
    String detail;
    String fasilitas;
    String lat;
    String lag;
    String url;
    String nama;
    String email;

    public Tour(String id, String judul, String detail, String fasilitas, String lat, String lag, String url, String nama, String email) {
        this.key = id;
        this.judul = judul;
        this.detail = detail;
        this.fasilitas = fasilitas;
        this.lat = lat;
        this.lag = lag;
        this.url = url;
        this.nama = nama;
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getFasilitas() {
        return fasilitas;
    }

    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLag() {
        return lag;
    }

    public void setLag(String lag) {
        this.lag = lag;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
