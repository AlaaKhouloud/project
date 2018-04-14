package com.example.lenovo.geolocalisationsafi;

/**
 * Created by lenovo on 24/03/2018.
 */

public class PointInterest {
    String id;
    String nom;
    Double longtitude;
    Double latitude;
    public PointInterest(){

    }
    public PointInterest(String id,String nom,Double longtitude,Double latitude ){
        this.id=id;
        this.nom=nom;
        this.longtitude=longtitude;
        this.latitude=latitude;
    }

    public String getNom() {
        return nom;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "PointInterest{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", longtitude=" + longtitude +
                ", latitude=" + latitude +
                '}';
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
