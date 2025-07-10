package com.Agents.ExpenseTracker;

// This class ensures we only ever have ONE instance of our expense tracker core logic.
public final class ExpenseTrackerStateManager {

    private static final ExpenseTrackerStateManager INSTANCE_ETS = new ExpenseTrackerStateManager();
    private final ExpenseTrackerCore expenseTrackerCore;

    private ExpenseTrackerStateManager() {
        this.expenseTrackerCore = new ExpenseTrackerCore();
    }

    public static ExpenseTrackerStateManager getInstance() {
        return INSTANCE_ETS;
    }

    // 5. A public method to get the actual tracker object.
    public ExpenseTrackerCore getExpenseTrackerCore() {
        return this.expenseTrackerCore;
    }
}