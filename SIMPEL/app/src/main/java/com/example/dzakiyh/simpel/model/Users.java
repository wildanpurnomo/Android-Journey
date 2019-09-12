package com.example.dzakiyh.simpel.model;

public class Users {

    //account managemenet properties
    private String username;
    private String email;
    private String password;

    //general info properties
    private String namaLengkap;
    private String noKTP;

    //utils
    private String sudahFormBaru;
    private String sudahFormPerpanjangan;
    private String sudahUploadKTP;
    private String sudahUploadKK;
    private String sudahUploadSuratSehat;
    private String sudahKTPPerpanj;
    private String sudahKKPerpanj;
    private String sudahSuratSehatPerpanj;
    private String jadwalAmbil;
    private String jadwalUjian;

    public Users(){

    }

    public Users(String username, String email, String password, String namaLengkap, String noKTP, String sudahFormBaru, String sudahFormPerpanjangan, String sudahUploadKTP, String sudahUploadKK, String sudahUploadSuratSehat, String sudahKTPPerpanj, String sudahKKPerpanj, String sudahSuratSehatPerpanj, String jadwalAmbil, String jadwalUjian) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.namaLengkap = namaLengkap;
        this.noKTP = noKTP;

        this.sudahFormBaru = sudahFormBaru;
        this.sudahFormPerpanjangan = sudahFormPerpanjangan;
        this.sudahUploadKTP = sudahUploadKTP;
        this.sudahUploadKK = sudahUploadKK;
        this.sudahUploadSuratSehat = sudahUploadSuratSehat;
        this.sudahKTPPerpanj = sudahKTPPerpanj;
        this.sudahKKPerpanj = sudahKKPerpanj;
        this.sudahSuratSehatPerpanj = sudahSuratSehatPerpanj;
        this.jadwalAmbil = jadwalAmbil;
        this.jadwalUjian = jadwalUjian;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getNoKTP() {
        return noKTP;
    }

    public void setNoKTP(String noKTP) {
        this.noKTP = noKTP;
    }

    public String getSudahFormBaru() {
        return sudahFormBaru;
    }

    public void setSudahFormBaru(String sudahFormBaru) {
        this.sudahFormBaru = sudahFormBaru;
    }

    public String getSudahFormPerpanjangan() {
        return sudahFormPerpanjangan;
    }

    public void setSudahFormPerpanjangan(String sudahFormPerpanjangan) {
        this.sudahFormPerpanjangan = sudahFormPerpanjangan;
    }

    public String getSudahUploadKTP() {
        return sudahUploadKTP;
    }

    public void setSudahUploadKTP(String sudahUploadKTP) {
        this.sudahUploadKTP = sudahUploadKTP;
    }

    public String getSudahUploadKK() {
        return sudahUploadKK;
    }

    public void setSudahUploadKK(String sudahUploadKK) {
        this.sudahUploadKK = sudahUploadKK;
    }

    public String getSudahUploadSuratSehat() {
        return sudahUploadSuratSehat;
    }

    public void setSudahUploadSuratSehat(String sudahUploadSuratSehat) {
        this.sudahUploadSuratSehat = sudahUploadSuratSehat;
    }

    public String getSudahKTPPerpanj() {
        return sudahKTPPerpanj;
    }

    public void setSudahKTPPerpanj(String sudahKTPPerpanj) {
        this.sudahKTPPerpanj = sudahKTPPerpanj;
    }

    public String getSudahKKPerpanj() {
        return sudahKKPerpanj;
    }

    public void setSudahKKPerpanj(String sudahKKPerpanj) {
        this.sudahKKPerpanj = sudahKKPerpanj;
    }

    public String getSudahSuratSehatPerpanj() {
        return sudahSuratSehatPerpanj;
    }

    public void setSudahSuratSehatPerpanj(String sudahSuratSehatPerpanj) {
        this.sudahSuratSehatPerpanj = sudahSuratSehatPerpanj;
    }

    public String getJadwalAmbil() {
        return jadwalAmbil;
    }

    public void setJadwalAmbil(String jadwalAmbil) {
        this.jadwalAmbil = jadwalAmbil;
    }

    public String getJadwalUjian() {
        return jadwalUjian;
    }

    public void setJadwalUjian(String jadwalUjian) {
        this.jadwalUjian = jadwalUjian;
    }
}
