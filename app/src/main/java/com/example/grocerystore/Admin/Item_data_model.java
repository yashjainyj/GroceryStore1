package com.example.grocerystore.Admin;

public class Item_data_model {
    private String shopId;
    private String itemId;
    private String itemName;
    private String weight;
    private String quantity;
    private String itemPrice;
    private String description;
    private String category;
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String imageUrl;
    public Item_data_model() {
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Item_data_model(String shopId, String itemId, String itemName, String weight, String quantity, String itemPrice, String description, String category, String imageUrl) {
        this.shopId = shopId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.weight = weight;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
