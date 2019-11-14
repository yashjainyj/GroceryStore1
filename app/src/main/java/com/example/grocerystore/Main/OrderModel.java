package com.example.grocerystore.Main;

public class OrderModel {
    private String orderId;
    private String ItemId;
    private String orderDate;
    private String referenceNo;
    private String amount;
    private String addressId;

    public OrderModel(String orderId, String itemId, String orderDate, String referenceNo, String amount, String addressId) {
        this.orderId = orderId;
        ItemId = itemId;
        this.orderDate = orderDate;
        this.referenceNo = referenceNo;
        this.amount = amount;
        this.addressId = addressId;
    }

    public OrderModel() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}
