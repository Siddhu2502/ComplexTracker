package com.Agents.ExpenseTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExpenseTrackerCore {

    private ArrayList<Expense> expenseSession;
    private ArrayList<Person> candidates;

    public ExpenseTrackerCore() {
        this.expenseSession = new ArrayList<>();
        this.candidates = new ArrayList<>();
    }

    
    private Person getPersonByName(String name) {
        for (Person person : candidates) {
            if (person.getName().equals(name)) {
                return person;
            }
        }
        Person newPerson = new Person(name);
        candidates.add(newPerson);
        return newPerson;
    }

    /**
     * Adds a new expense and performs a symmetrical update on the pairwise balances
     * for everyone involved.
     */
    public void addExpense(Expense expenseItem) {
        // Log the transaction
        this.expenseSession.add(expenseItem);

        String payerName = expenseItem.getPayerName();
        HashMap<String, Integer> friendsWhoOwe = expenseItem.getFriends();

        // Loop through each debt in this single transaction
        for (Map.Entry<String, Integer> debtEntry : friendsWhoOwe.entrySet()) {
            String friendName = debtEntry.getKey();
            Integer amountOwed = debtEntry.getValue();

            // Get the Person objects involved in this specific debt
            Person payer = getPersonByName(payerName);
            Person friend = getPersonByName(friendName);

            // Perform the symmetrical update:
            // 1. Credit the payer: The payer is now owed 'amountOwed' BY the friend.
            payer.updateBalanceWith(friendName, amountOwed);

            // 2. Debit the friend: The friend now owes 'amountOwed' TO the payer.
            friend.updateBalanceWith(payerName, -amountOwed);
        }
    }
    
    // Getter to see the final list of people and their detailed balances
    public ArrayList<Person> getCandidateExpenses() {
        return candidates;
    }

    public ArrayList<Expense> getExpenseSession() {
        return expenseSession;
    }
    
    /**
     * Resets the expense session and clears all candidates.
     * This is useful for starting a new session without any previous data.
     */
    public void reset() {
        this.expenseSession.clear();
        this.candidates.clear();
    }
}