package com.ExpenseTracker.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ExpenseTracker.Core.Expense;

public class Report {
    private ArrayList<Person> balances;
    @SuppressWarnings("unused")
    private ArrayList<Expense> expensesLog;

    public Report(){
        this.balances = new ArrayList<>();
        this.expensesLog = new ArrayList<>();
    }

    public String getDetailedBalances(ArrayList<Person> balances) {
        // --- This top header part is fine ---
        String output = """
                        +----------------+-------------+--------------+
                        | Who Owes       | Amount      | Owed To      |
                        +----------------+-------------+--------------+
                        """;


        boolean debtsFound = false; // A flag to check if we print any debt lines

        for (Person personWhoIsOwed : balances) {
            
            for (Map.Entry<String, Integer> entry : personWhoIsOwed.getBalanceWithOthers().entrySet()) {
                
                String personWhoOwes = entry.getKey();
                Integer balance = entry.getValue();

                // The key trick: Only print a line when the balance is positive.
                // This means we are looking at it from the perspective of the person
                // who is owed money, which prints each debt only once.
                if (balance > 0) {
                    debtsFound = true; 
                    
                    output += String.format("| %-14s | %11d | %-12s |\n",
                            personWhoOwes,      
                            balance,            
                            personWhoIsOwed.getName()
                    );
                    output += "+----------------+-------------+--------------+\n";
                }
            }
        }

        if (!debtsFound) {    
            output += "|            All debts are settled!           |\n";
            output += "+----------------+-------------+--------------+\n";
        }
                
        output += """
                        +----------------+-------------+--------------+
                        """;

        output += "Total People Involved: " + balances.size() + "\n";
        
        // Note: This sum will always be 0.0 in a balanced system,
        // because for every +25 (A is owed by B), there is a -25 (B owes A).
        // It's a good sanity check!
        // if its not 0, then there is an error in the logic.
        output += "Total Amount Owed: " + balances.stream()
                .flatMap(person -> person.getBalanceWithOthers().values().stream().filter(amount -> amount > 0))
                .mapToInt(Integer::intValue)
                .sum() + "\n";
        
        output += """
                        +----------------+-------------+--------------+
                        """;
        output += "End of Report\n";
        output += """
                        +----------------+-------------+--------------+
                        """;
        output += "Generated on: " + java.time.LocalDateTime.now() + "\n";
        output += """
                        +----------------+-------------+--------------+
                        """;
        output += "Thank you for using the Expense Tracker!\n";
        
        return output;
    }



    public String getExpenseLog(ArrayList<Expense> expensesLog) {
        // A slightly cleaned up header for clarity
        String output = """
                        +----------------+-------------+--------------+-------------+---------------------------------+
                        |    Item Name   |  Total Amt  |  Payer Name  |  Payer Amt  |         Friends Who Owe         |
                        |                |             |              |             |       Name      |     Amount    |
                        +----------------+-------------+--------------+-------------+-----------------+---------------+
                        """;

        // Loop through each expense record
        for (Expense expense : expensesLog) {
            // It's easier to work with an indexed list for this problem
            List<Map.Entry<String, Integer>> friendsList = new ArrayList<>(expense.getFriends().entrySet());

            // --- Case 1: The expense has friends who owe money ---
            if (!friendsList.isEmpty()) {
                // Get the VERY FIRST friend to print on the main line
                Map.Entry<String, Integer> firstFriend = friendsList.get(0);

                // Print the main expense details AND the first friend on one line
                output += String.format("| %-14s | %11d | %-12s | %11d | %-15s | %13d |\n",
                        expense.getItemName(),
                        expense.getTotalAmount(),
                        expense.getPayerName(),
                        expense.getPayerAmt(),
                        firstFriend.getKey(),   // First friend's name
                        firstFriend.getValue()  // First friend's amount
                );

                // Now, loop through THE REST of the friends (starting from the second one)
                for (int i = 1; i < friendsList.size(); i++) {
                    Map.Entry<String, Integer> nextFriend = friendsList.get(i);
                    // Print subsequent friends on new lines, with padding to align them correctly
                    output += String.format("|                |             |              |             | %-15s | %13d |\n",
                            nextFriend.getKey(),   // Next friend's name
                            nextFriend.getValue()  // Next friend's amount
                    );
                }

            } 
            // --- Case 2: The expense has no friends who owe (e.g., payer bought for self) ---
            else {
                // Print just the main details with an empty friends column
                output += String.format("| %-14s | %11d | %-12s | %11d |                 |               |\n",
                        expense.getItemName(),
                        expense.getTotalAmount(),
                        expense.getPayerName(),
                        expense.getPayerAmt()
                );
            }
        }

        output += """
                        +----------------+-------------+--------------+-------------+---------------------------------+
                        """;
                        
        output += "Total Expenses: " + expensesLog.stream()
                .mapToInt(Expense::getTotalAmount)
                .sum() + "\n";
        output += "Total Payer Amount: " + expensesLog.stream()
                .mapToInt(Expense::getPayerAmt)
                .sum() + "\n";
        output += "Total Friends Owe Amount: " + expensesLog.stream()
                .flatMap(expense -> expense.getFriends().values().stream())
                .mapToInt(Integer::intValue)
                .sum() + "\n";
        output += """
                        +----------------+-------------+--------------+-------------+---------------------------------+
                        """;
        output += "Total Expenses: " + expensesLog.size() + "\n";
        output += "Total People Involved: " + balances.size() + "\n";
        output += "Total Amount Owed: " + balances.stream()
                .flatMap(person -> person.getBalanceWithOthers().values().stream().filter(amount -> amount > 0))
                .mapToInt(Integer::intValue)
                .sum() + "\n"; 
        output += """
                        +----------------+-------------+--------------+-------------+---------------------------------+
                        """;
        output += "End of Report\n";
        output += """
                        +----------------+-------------+--------------+-------------+---------------------------------+
                        """;
        output += "Generated on: " + java.time.LocalDateTime.now() + "\n";
        output += """
                        +----------------+-------------+--------------+-------------+---------------------------------+
                        """;
        output += "Thank you for using the Expense Tracker!\n";
        output += """
                        +----------------+-------------+--------------+-------------+---------------------------------+
                        """;

        return output;
                         
    }
}
