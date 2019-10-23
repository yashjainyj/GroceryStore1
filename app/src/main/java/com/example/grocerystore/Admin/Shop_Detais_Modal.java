package com.example.grocerystore.Admin;

public class Shop_Detais_Modal {
    private String shop_Id;
    private String shop_Name;
    private String shop_Address;
    private String shop_rating;
    private String image_Url;
    private String contact_number;

    public Shop_Detais_Modal() {
    }

    public Shop_Detais_Modal(String shop_Name, String shop_Address, String shop_rating, String image_Url, String contact_number) {
        this.shop_Name = shop_Name;
        this.shop_Address = shop_Address;
        this.shop_rating = shop_rating;
        this.image_Url = image_Url;
        this.contact_number = contact_number;
    }
    public Shop_Detais_Modal(String shop_Id, String shop_Name, String shop_Address, String shop_rating, String image_Url, String contact_number) {
        this.shop_Id = shop_Id;
        this.shop_Name = shop_Name;
        this.shop_Address = shop_Address;
        this.shop_rating = shop_rating;
        this.image_Url = image_Url;
        this.contact_number = contact_number;
    }

    public String getShop_Id() {
        return shop_Id;
    }

    public void setShop_Id(String shop_Id) {
        this.shop_Id = shop_Id;
    }

    public String getShop_Name() {
        return shop_Name;
    }

    public void setShop_Name(String shop_Name) {
        this.shop_Name = shop_Name;
    }

    public String getShop_Address() {
        return shop_Address;
    }

    public void setShop_Address(String shop_Address) {
        this.shop_Address = shop_Address;
    }

    public String getShop_rating() {
        return shop_rating;
    }

    public void setShop_rating(String shop_rating) {
        this.shop_rating = shop_rating;
    }

    public String getImage_Url() {
        return image_Url;
    }

    public void setImage_Url(String image_Url) {
        this.image_Url = image_Url;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }
}
