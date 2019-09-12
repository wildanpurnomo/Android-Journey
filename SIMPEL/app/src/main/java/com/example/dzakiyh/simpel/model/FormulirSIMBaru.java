package com.example.dzakiyh.simpel.model;

public class FormulirSIMBaru {

    private String jenisFormulir, tanggalKirim, idPengirim, namaLengkap, alamatPribadi, noTeleponPribadi, namaOrtu, noTeleponOrtu;
    public FormulirSIMBaru(){

    }

    public FormulirSIMBaru(String jenisFormulir,String tanggalKirim,String idPengirim,String namaLengkap, String alamatPribadi, String noTeleponPribadi, String namaOrtu, String noTeleponOrtu) {
        this.jenisFormulir = jenisFormulir;
        this.tanggalKirim = tanggalKirim;
        this.idPengirim = idPengirim;
        this.namaLengkap = namaLengkap;
        this.alamatPribadi = alamatPribadi;
        this.noTeleponPribadi = noTeleponPribadi;
        this.namaOrtu = namaOrtu;
        this.noTeleponOrtu = noTeleponOrtu;
    }

    public String getJenisFormulir() {
        return jenisFormulir;
    }

    public void setJenisFormulir(String jenisFormulir) {
        this.jenisFormulir = jenisFormulir;
    }

    public String getTanggalKirim() {
        return tanggalKirim;
    }

    public void setTanggalKirim(String tanggalKirim) {
        this.tanggalKirim = tanggalKirim;
    }

    public String getIdPengirim() {
        return idPengirim;
    }

    public void setIdPengirim(String idPengirim) {
        this.idPengirim = idPengirim;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getAlamatPribadi() {
        return alamatPribadi;
    }

    public void setAlamatPribadi(String alamatPribadi) {
        this.alamatPribadi = alamatPribadi;
    }

    public String getNoTeleponPribadi() {
        return noTeleponPribadi;
    }

    public void setNoTeleponPribadi(String noTeleponPribadi) {
        this.noTeleponPribadi = noTeleponPribadi;
    }

    public String getNamaOrtu() {
        return namaOrtu;
    }

    public void setNamaOrtu(String namaOrtu) {
        this.namaOrtu = namaOrtu;
    }

    public String getNoTeleponOrtu() {
        return noTeleponOrtu;
    }

    public void setNoTeleponOrtu(String noTeleponOrtu) {
        this.noTeleponOrtu = noTeleponOrtu;
    }
}
