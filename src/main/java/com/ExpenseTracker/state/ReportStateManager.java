package com.ExpenseTracker.state;

import com.ExpenseTracker.Model.Report;

// This class manages the state of the report, ensuring that there is a single instance
// of the report throughout the application lifecycle.
public class ReportStateManager {
    private static final ReportStateManager INSTANCE = new ReportStateManager();
    private final Report report;

    private ReportStateManager() {
        this.report = new Report();
    }

    public static ReportStateManager getInstance() {
        return INSTANCE;
    }

    public Report getReport() {
        return this.report;
    }

}
