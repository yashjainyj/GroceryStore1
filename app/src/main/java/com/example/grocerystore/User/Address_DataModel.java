package com.example.grocerystore.User;

import android.content.Context;

public class Address_DataModel {
    private String salutation;
    private String key;
    private String name;
    private String flat;
    private String street;
    private String locality;
    private String nickname;
public  Address_DataModel(){}
    public Address_DataModel(String salutation, String name, String flat, String street, String locality, String nickname) {
        this.salutation = salutation;
        this.name = name;
        this.flat = flat;
        this.street = street;
        this.locality = locality;
        this.nickname = nickname;
    }
    public Address_DataModel(String key,String salutation, String name, String flat, String street, String locality, String nickname) {
        this.key = key;
        this.salutation = salutation;
        this.name = name;
        this.flat = flat;
        this.street = street;
        this.locality = locality;
        this.nickname = nickname;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
