package com.agents.ExpenseTracker;

import java.util.HashMap;

public class Expense {

    private String itemName;
    private Integer totalAmount;
    private String payerName;
    private Integer payerAmt;
    private HashMap<String, Integer> friend_oweMuch;

    public Expense(String itemName, Integer totalAmount, String payerName, Integer payerAmt, HashMap<String, Integer> friend_oweMuch){
        this.itemName = itemName;
        this.totalAmount = totalAmount;
        this.payerName = payerName;
        this.payerAmt = payerAmt;
        this.friend_oweMuch = friend_oweMuch;
    }


    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the totalAmount
     */
    public Integer getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return the payerName
     */
    public String getPayerName() {
        return payerName;
    }

    /**
     * @param payerName the payerName to set
     */
    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public Integer getPayerAmt(){
        return payerAmt;
    }

    public void setPayerAmt(Integer payerAmt){
        this.payerAmt = payerAmt;
    }

    /**
     * @return the friends
     */
    public HashMap<String, Integer> getFriends() {
        return friend_oweMuch;
    }

    /**
     * @param friends the friends to set
     */
    public void setFriends(HashMap<String, Integer> friend_oweMuch) {
        this.friend_oweMuch = friend_oweMuch;
    }

    
}
