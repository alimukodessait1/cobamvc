package com.mycompany.cobamvc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mobil")
public class Mobil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "kode_mobil", nullable = false, length = 20)
    private String kodeMobil;

    @Column(name = "nama_mobil", nullable = false, length = 100)
    private String namaMobil;

    @Column(name = "merk", length = 50)
    private String merk;

    @Column(name = "tarif")
    private double tarif;

    @Column(name = "status", length = 20)
    private String status;

    public Mobil() {
    }

    public Mobil(String kodeMobil, String namaMobil, String merk, double tarif, String status) {
        this.kodeMobil = kodeMobil;
        this.namaMobil = namaMobil;
        this.merk = merk;
        this.tarif = tarif;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKodeMobil() {
        return kodeMobil;
    }

    public void setKodeMobil(String kodeMobil) {
        this.kodeMobil = kodeMobil;
    }

    public String getNamaMobil() {
        return namaMobil;
    }

    public void setNamaMobil(String namaMobil) {
        this.namaMobil = namaMobil;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public double getTarif() {
        return tarif;
    }

    public void setTarif(double tarif) {
        this.tarif = tarif;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
