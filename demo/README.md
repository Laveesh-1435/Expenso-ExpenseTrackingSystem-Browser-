expense-tracker-web/
├── pom.xml
└── src/
    └── main/
        ├── java/com/example/expense/
        │   ├── ExpenseTrackerApplication.java  (New Main App)
        │   ├── model/Expense.java              (Updated Model)
        │   ├── service/ExpenseService.java     (Your old logic combined)
        │   └── controller/ExpenseController.java (New REST API)
        └── resources/
            └── static/
                ├── index.html                  (New Frontend UI)
                └── style.css