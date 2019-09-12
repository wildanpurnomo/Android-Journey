package com.example.dzakiyh.simpel.model;

public class Upload {

    private String usernamePengirim, uploadName, uploadImageUrl, jenisAplikasi;

    public Upload(){

    }

    public Upload(String usernamePengirim, String uploadName, String uploadImageUrl, String jenisAplikasi) {
        this.usernamePengirim = usernamePengirim;
        this.uploadName = uploadName;
        this.uploadImageUrl = uploadImageUrl;
        this.jenisAplikasi = jenisAplikasi;
    }

    public String getUsernamePengirim() {
        return usernamePengirim;
    }

    public void setUsernamePengirim(String usernamePengirim) {
        this.usernamePengirim = usernamePengirim;
    }

    public String getUploadName() {
        return uploadName;
    }

    public void setUploadName(String uploadName) {
        this.uploadName = uploadName;
    }

    public String getUploadImageUrl() {
        return uploadImageUrl;
    }

    public void setUploadImageUrl(String uploadImageUrl) {
        this.uploadImageUrl = uploadImageUrl;
    }

    public String getJenisAplikasi() {
        return jenisAplikasi;
    }

    public void setJenisAplikasi(String jenisAplikasi) {
        this.jenisAplikasi = jenisAplikasi;
    }
}
