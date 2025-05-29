package com.example.buildyourownmeal;

import java.util.ArrayList;

public class orderModel {
    private String contactNumber, orderDate, orderStatus, orderGroupId, pickUp, paymentMethod;
    private int orderTotalPrice, orderCount, userId;

    public orderModel(String contactNumber, String orderDate, String orderStatus, String orderGroupId, String pickUp, String paymentMethod, int orderTotalPrice, int orderCount, int userId) {
        this.contactNumber = contactNumber;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderGroupId = orderGroupId;
        this.pickUp = pickUp;
        this.paymentMethod = paymentMethod;
        this.orderTotalPrice = orderTotalPrice;
        this.orderCount = orderCount;
        this.userId = userId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderGroupId() {
        return orderGroupId;
    }

    public void setOrderGroupId(String orderGroupId) {
        this.orderGroupId = orderGroupId;
    }

    public String getPickUp() {
        return pickUp;
    }

    public void setPickUp(String pickUp) {
        this.pickUp = pickUp;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(int orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
