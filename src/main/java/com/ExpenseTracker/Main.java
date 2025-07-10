package com.ExpenseTracker;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

public class Main {
    static ExpenseTrackerCore etCore = new ExpenseTrackerCore();
    static Report report = new Report();

    public static void main(String[] args) {



        // Dynamic generation of random transactions
        // Prepare 10 unique people
        List<String> names = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            names.add("Person" + i);
        }

        // Track how many times each person has paid
        Map<String, Integer> payCount = new HashMap<>();
        for (String name : names)
            payCount.put(name, 0);

        Random rand = new Random();
        int transactionId = 1;
        int maxPayments = 10;
        int totalGroups = 50; // number of payer groups

        // Generate transactions with random group sizes
        for (int g = 0; g < totalGroups; g++) {
            // Select eligible payers
            List<String> eligible = new ArrayList<>();
            for (String name : names) {
                if (payCount.get(name) < maxPayments)
                    eligible.add(name);
            }
            if (eligible.isEmpty())
                break;
            String payer = eligible.get(rand.nextInt(eligible.size()));

            // Random number of friends to involve (at least 10)
            int groupSize = Math.min(rand.nextInt(10) + 10, names.size() - 1);
            Set<String> friends = new HashSet<>();

            while (friends.size() < groupSize) {
                String friend = names.get(rand.nextInt(names.size()));
                if (!friend.equals(payer))
                    friends.add(friend);
            }

            // Assign random amounts: payer and each friend
            int payerAmtVal = rand.nextInt(100) + 1;
            HashMap<String, Integer> friendMap = new HashMap<>();
            int sumFriendsAmt = 0;
            for (String friend : friends) {
                int fAmt = rand.nextInt(100) + 1;
                friendMap.put(friend, fAmt);
                sumFriendsAmt += fAmt;
            }

            int totalAmt = payerAmtVal + sumFriendsAmt;
            String item = "Expense" + transactionId++;
            etCore.addExpense(new Expense(item, totalAmt, payer, payerAmtVal, friendMap));

            // Count this group payment
            payCount.put(payer, payCount.get(payer) + 1);
        }
        // Generate the report
        System.out.println(report.getExpenseLog(etCore.getExpenseSession()));
        System.out.println("\n\n\n\n");
        System.out.println(report.getDetailedBalances(etCore.getCandidateExpenses()));

        
    }

}
