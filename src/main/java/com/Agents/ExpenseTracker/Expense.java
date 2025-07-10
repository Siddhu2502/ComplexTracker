package com.Agents.ExpenseTracker;

import java.util.HashMap;

public class Expense {

    private String itemName;
    private Integer totalAmount;
    private String payerName;
    private Integer payerAmt;
    private HashMap<String, Integer> friendOweMuch;

    public Expense(String itemName, Integer totalAmount, String payerName, Integer payerAmt, HashMap<String, Integer> friendOweMuch){
        this.itemName = itemName;
        this.totalAmount = totalAmount;
        this.payerName = payerName;
        this.payerAmt = payerAmt;
        this.friendOweMuch = friendOweMuch;
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
        return friendOweMuch;
    }

    /**
     * @param friends the friends to set
     */
    public void setFriends(HashMap<String, Integer> friendOweMuch) {
        this.friendOweMuch = friendOweMuch;
    }

    
}
