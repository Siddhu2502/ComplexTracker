package com.Agents.ExpenseTrackerAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ExpenseTracker.Expense;
import com.ExpenseTracker.ExpenseTrackerCore;
import com.ExpenseTracker.ExpenseTrackerStateManager;
import com.ExpenseTracker.Person;
import com.ExpenseTracker.Report;
import com.ExpenseTracker.ReportStateManager;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;
import com.google.adk.tools.Annotations.Schema;

public class ExpenseTrackerAgent {
    private static final String AGENT_NAME = "ExpenseTrackerAgent";
    private static final String AGENT_DESCRIPTION = "An agent that tracks expenses and balances among friends.";
    private static final String GEMINI_MODEL = "gemini-1.5-flash";
    private static final String STATUS = "status";
    private static final String SUCCESS = "success";
    private static final String REPORT = "report";
    private static final String LOG_EMPTY = "The transaction log is empty!.";


    public static final BaseAgent rootAgent = iniAgent();

    private static String agentInstruction = """
            You are an expense tracking assistant.
            When the user provides details about an expense, call the `addExpense` function.
            When the user asks for the REPORT, "balances", or "who owes what", call the `getDetailedBalances` function.
            When the user asks for the "log" or "transaction history", call the `getSessionLog` function.
            Present the data you receive from the tools clearly to the user.

            You can only call the functions provided to you.
            You cannot create new functions or modify existing ones.
            You cannot access any external databases or APIs.

            When a instruction is given by the user to you break it down into the following steps:
            1. Identify the type of request (add expense, get balances, get log).
            2. If it's an add expense request, extract the item name, total amount, payer name, and amounts owed by friends.
                a. In case when the payer's amount is not specified but the other friends' amounts are specified,
                   calculate the payer's amount as the total minus the sum of friends' amounts.
                b. If in case the payer's amount is not specified and no friends' amounts are specified then set the payer's amount to the total amount.
                c. If in case the payer's amount is not specified and and there are more than one friend but only one of the friends's amount is specified
                   ask the user to specify the details more clearly. (e.g. "Please specify the payer's amount or the amounts owed by all friends as only <friend_name> has been given but there are <number_of_friends>.")
                d. In case of absense of the details such as the occasion, payer, or amounts owed by friends, ask the user to provide those details if point a, b, c is not satisfied.
            3. If it's a get balances request, call the `getDetailedBalances` function.
            4. If it's a get log request, call the `getSessionLog` function.
            5. Always return the results in a clear and concise format TABULATE IT

            
            """;

    public static BaseAgent iniAgent() {
        return LlmAgent.builder()
                .name(AGENT_NAME)
                .model(GEMINI_MODEL)
                .description(AGENT_DESCRIPTION)
                .instruction(agentInstruction)
                .tools(
                        FunctionTool.create(ExpenseTrackerAgent.class, "addExpense"),
                        FunctionTool.create(ExpenseTrackerAgent.class, "getDetailedBalances"),
                        FunctionTool.create(ExpenseTrackerAgent.class, "getSessionLog")
                )
                .build();
    }

    // This tool correctly has parameters because it's receiving new information.
    public static void addExpense(
            @Schema(description = "The name or description of the expense item, e.g., 'Coffee' or 'Lunch'.") String itemName,
            @Schema(description = "The total cost of the item as a whole number.") Integer totalAmount,
            @Schema(description = "The name of the person who paid for the item.") String payerName,
            @Schema(description = "The portion of the total amount that the payer is personally responsible for, as a whole number.") Integer payerAmt,
            @Schema(description = "A Map of friends who participated and the amount each one owes as a whole number.") HashMap<String, Integer> friend_oweMuch
    ) {
        ExpenseTrackerCore etCore = ExpenseTrackerStateManager.getInstance().getExpenseTrackerCore();
        etCore.addExpense(new Expense(itemName, totalAmount, payerName, payerAmt, friend_oweMuch));
        System.out.println("SUCCESS: Tool 'addExpense' was called for item: " + itemName);
    }

    /**
     * Retrieves a detailed transaction history of all expenses added during the session.
     * Call this when the user asks for the "log" or "history".
     */
    public static Map<String, String> getSessionLog() {
        ExpenseTrackerCore etCore = ExpenseTrackerStateManager.getInstance().getExpenseTrackerCore();
        ArrayList<Expense> log = etCore.getExpenseSession();

        Report logReport = ReportStateManager.getInstance().getReport();


        if (log.isEmpty()) {
            return Map.of(STATUS, SUCCESS, REPORT, LOG_EMPTY);
        }

        List<String> logEntries = new ArrayList<>();
        for (Expense expense : log) {
            String fact = String.format("Item: %s, Amount: %d, Paid by: %s",
                    expense.getItemName(), expense.getTotalAmount(), expense.getPayerName());
            logEntries.add(fact);
        }

        logEntries.add(logReport.getExpenseLog(log));

        return Map.of(STATUS, SUCCESS, REPORT, String.join("\n", logEntries));
    }

    /**
     * Calculates and retrieves the final balance report, showing exactly who owes money to whom.
     * Call this when the user asks for the REPORT or "balances".
     */
    public static Map<String, String> getDetailedBalances() {
        ExpenseTrackerCore etCore = ExpenseTrackerStateManager.getInstance().getExpenseTrackerCore();
        ArrayList<Person> balances = etCore.getCandidateExpenses();

        Report report = ReportStateManager.getInstance().getReport();


        if (balances.isEmpty()) {
            return Map.of(STATUS, SUCCESS, REPORT, "All debts are settled. No data found.");
        }

        List<String> debtList = new ArrayList<>();
        for (Person personWhoIsOwed : balances) {
            // Ensure your Person class uses Map<String, Integer> to match the constraint
            for (Map.Entry<String, Integer> entry : personWhoIsOwed.getBalanceWithOthers().entrySet()) {
                if (entry.getValue() > 0) {
                    String debtFact = String.format("%s owes %s %d",
                            entry.getKey(), personWhoIsOwed.getName(), entry.getValue());
                    debtList.add(debtFact);
                }
            }
        }

        debtList.add(report.getDetailedBalances(balances));

        if (debtList.isEmpty()) {
            return Map.of(STATUS, SUCCESS, REPORT, "All debts are settled.");
        } else {
            return Map.of(STATUS, SUCCESS, REPORT, String.join("\n", debtList));
        }
    }
}