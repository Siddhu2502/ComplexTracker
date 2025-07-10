package com.Agents.ExpenseTracker;

import java.util.HashMap;
import java.util.Map;

public class Person {

    private String name;
    private Map<String, Integer> balanceWithOthers;

    public Person(String name) {
        this.name = name;
        this.balanceWithOthers = new HashMap<>();
    }

    // A helper method to cleanly update the balance with another person
    public void updateBalanceWith(String otherPersonName, Integer amount) {
        Integer currentBalance = this.balanceWithOthers.getOrDefault(otherPersonName, 0);
        this.balanceWithOthers.put(otherPersonName, currentBalance + amount);
    }

    // Getters
    public String getName() {
        return name;
    }

    public Map<String, Integer> getBalanceWithOthers() {
        return balanceWithOthers;
    }
}