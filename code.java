import java.io.*;
import java.util.*;

class Account implements Serializable {
    private String accountNumber;
    private String holderName;
    private double balance;
    private String accountType;

    public Account(String accountNumber, String holderName, String accountType, double balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.accountType = accountType;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("₹" + amount + " deposited successfully!");
        } else {
            System.out.println("Invalid deposit amount!");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("₹" + amount + " withdrawn successfully!");
        } else {
            System.out.println("Invalid withdrawal amount or insufficient balance!");
        }
    }

    public void displayAccount() {
        System.out.printf("%-10s %-15s %-10s ₹%.2f\n", accountNumber, holderName, accountType, balance);
    }
}

public class BankManagementSystem {
    private static Map<String, Account> accounts = new HashMap<>();
    private static final String DATA_FILE = "accounts.dat";
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadAccounts();
        while (true) {
            System.out.println("\n=== ADVANCED BANK MANAGEMENT SYSTEM ===");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Check Balance");
            System.out.println("5. View All Accounts");
            System.out.println("6. Delete Account");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> depositMoney();
                case 3 -> withdrawMoney();
                case 4 -> checkBalance();
                case 5 -> viewAllAccounts();
                case 6 -> deleteAccount();
                case 7 -> {
                    saveAccounts();
                    System.out.println("Thank you for using the Bank Management System!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private static void createAccount() {
        System.out.print("Enter account number: ");
        String accNo = sc.nextLine();

        if (accounts.containsKey(accNo)) {
            System.out.println("Account already exists!");
            return;
        }

        System.out.print("Enter account holder name: ");
        String name = sc.nextLine();

        System.out.print("Enter account type (Savings/Current): ");
        String type = sc.nextLine();

        System.out.print("Enter initial deposit: ");
        double balance = sc.nextDouble();
        sc.nextLine();

        Account acc = new Account(accNo, name, type, balance);
        accounts.put(accNo, acc);
        saveAccounts();
        System.out.println("✅ Account created successfully!");
    }

    private static void depositMoney() {
        System.out.print("Enter account number: ");
        String accNo = sc.nextLine();

        Account acc = accounts.get(accNo);
        if (acc != null) {
            System.out.print("Enter deposit amount: ");
            double amount = sc.nextDouble();
            acc.deposit(amount);
            saveAccounts();
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    private static void withdrawMoney() {
        System.out.print("Enter account number: ");
        String accNo = sc.nextLine();

        Account acc = accounts.get(accNo);
        if (acc != null) {
            System.out.print("Enter withdrawal amount: ");
            double amount = sc.nextDouble();
            acc.withdraw(amount);
            saveAccounts();
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    private static void checkBalance() {
        System.out.print("Enter account number: ");
        String accNo = sc.nextLine();

        Account acc = accounts.get(accNo);
        if (acc != null) {
            System.out.println("Account Holder: " + acc.getHolderName());
            System.out.println("Account Type: " + acc.getAccountType());
            System.out.println("Current Balance: ₹" + acc.getBalance());
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    private static void viewAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts available!");
            return;
        }

        System.out.printf("%-10s %-15s %-10s %s\n", "Acc No", "Holder Name", "Type", "Balance");
        System.out.println("--------------------------------------------------------");
        for (Account acc : accounts.values()) {
            acc.displayAccount();
        }
    }

    private static void deleteAccount() {
        System.out.print("Enter account number to delete: ");
        String accNo = sc.nextLine();

        if (accounts.remove(accNo) != null) {
            saveAccounts();
            System.out.println("🗑️ Account deleted successfully!");
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    private static void saveAccounts() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(accounts);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadAccounts() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            accounts = (Map<String, Account>) in.readObject();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
}
