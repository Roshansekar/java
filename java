CODE:
Account.java(Base Class)
package javaassignment1;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public abstract class Account {
    private static int nextAccountNo = 1001;
    protected int accountNo;
    protected String holderName;
    protected double balance;
    protected LocalDate openedDate;
    protected List<String> transactions;

    public Account(String holderName, double initialDeposit) {
        this.accountNo = nextAccountNo++;
        this.holderName = holderName;
        this.balance = initialDeposit;
        this.openedDate = LocalDate.now();
        this.transactions = new ArrayList<>();
        transactions.add("Account opened with balance: " + initialDeposit);
    }
    public abstract void withdraw(double amount);
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactions.add("Deposited: " + amount); }     
    }
    public void transfer(Account toAccount, double amount) {
        transfer(toAccount, amount, "No remark"); }
    public void transfer(Account toAccount, double amount, String remark) {
        if (amount <= this.balance) {
            this.balance -= amount;
            toAccount.deposit(amount);
            transactions.add("Transferred " + amount + " to Acc#" + toAccount.getAccountNo() + " | Remark: " + remark);
        } else {
            transactions.add("Failed transfer of " + amount + " to Acc#" + toAccount.getAccountNo() + " | Insufficient funds"); }    
    }
    public void miniStatement() {
        System.out.println("---- Mini Statement for Account #" + accountNo + " ----");
        transactions.stream().limit(5).forEach(System.out::println); }
    public void accountSummary() {
        System.out.println("---- Account Summary ----");
        System.out.println("Account No: " + accountNo);
        System.out.println("Holder Name: " + holderName);
        System.out.println("Balance: " + balance);
        System.out.println("Opened On: " + openedDate);
    }
    public int getAccountNo() {
        return accountNo; }
    public String getHolderName() {
        return holderName; }
    public double getBalance() {
        return balance; }
    public LocalDate getOpenedDate() {
        return openedDate; }
    public List<String> getTransactions() {
        return transactions; }    
}
SavingsAccount.java (Extends Account)
package javaassignment1;
public class SavingsAccount extends Account {
	    private double interestRate;
	    private double minBalance;
	    public SavingsAccount(String holderName, double initialDeposit, double interestRate, double minBalance) {
	        super(holderName, initialDeposit);
	        this.interestRate = interestRate;
	        this.minBalance = minBalance; }
	    @Override
	    public void withdraw(double amount) {
	        if (balance - amount >= minBalance) {
	            balance -= amount;
	            transactions.add("Withdrawn: " + amount);
	        } else {
	            transactions.add("Withdrawal failed. Balance can't go below minimum."); }
	    }
	    public void addInterest() {
	        double interest = balance * interestRate / 100;
	        deposit(interest);
	        transactions.add("Interest added: " + interest); }
	    public double getInterestRate() {
	        return interestRate; }
	    public double getMinBalance() {
	        return minBalance; }
	}
CurrentAccount.java (Extends Account)
package javaassignment1;
public class CurrentAccount extends Account {
	    private double overdraftLimit;
	    private double serviceCharge;
	    public CurrentAccount(String holderName, double initialDeposit, double overdraftLimit, double serviceCharge) {
	        super(holderName, initialDeposit);
	        this.overdraftLimit = overdraftLimit;
	        this.serviceCharge = serviceCharge;
	    }
	    @Override
	    public void withdraw(double amount) {
	        if (balance - amount >= -overdraftLimit) {
	            balance -= amount;
	            transactions.add("Withdrawn: " + amount);
	            if (balance < 0) {
	                balance -= serviceCharge;
	                transactions.add("Service charge applied: " + serviceCharge);
	            }
	        } else {
	            transactions.add("Withdrawal failed. Exceeds overdraft limit.");
	        }
	    }
	    public double getOverdraftLimit() {
	        return overdraftLimit; }
	    public double getServiceCharge() {
	        return serviceCharge; }    
	}

BankService.java
package javaassignment1;
import java.util.ArrayList;
import java.util.List;
public class BankService {
	 private List<Account> accounts = new ArrayList<>();
	    public void addAccount(Account account) {
	        accounts.add(account); }
	    public Account findAccount(int accNo) {
	        for (Account acc : accounts) {
	            if (acc.getAccountNo() == accNo)
	                return acc; }
	        return null; }
	    public void listAccounts() {
	        System.out.println("---- All Accounts ----");
	        for (Account acc : accounts) {
	            System.out.println(acc.getAccountNo() + " - " + acc.getHolderName() + " - " + acc.getBalance()); }
	    }
	    public void printDailyTransactionReport() {
	        System.out.println("---- Daily Transaction Report ----");
	        for (Account acc : accounts) {
	            System.out.println("Account #" + acc.getAccountNo() + ":");
	            for (String txn : acc.getTransactions()) {
	                System.out.println("  " + txn); }
	        }
	    }
	}

BankAppMain.java (Main Class)
package javaassignment1;
import java.util.Scanner;
public class BankAppMain {
	 public static void main(String[] args) {
	        BankService bankService = new BankService();
	        Scanner scanner = new Scanner(System.in);
	        SavingsAccount s1 = new SavingsAccount("Alice", 5000, 3.5, 1000);
	        CurrentAccount c1 = new CurrentAccount("Bob", 10000, 2000, 50);
	        bankService.addAccount(s1);
	        bankService.addAccount(c1);
	        int choice;
	        do {
	            System.out.println("\n=== Bank Menu ===");
	            System.out.println("1. List Accounts");
	            System.out.println("2. Deposit");
	            System.out.println("3. Withdraw");
	            System.out.println("4. Transfer");
	            System.out.println("5. Mini Statement");
	            System.out.println("6. Account Summary");
	            System.out.println("7. Daily Transaction Report");
	            System.out.println("0. Exit");
	            System.out.print("Enter choice: ");
	            choice = scanner.nextInt();
	            switch (choice) {
	                case 1:
	                    bankService.listAccounts();
	                    break;
	                case 2:
	                    System.out.print("Enter Account No: ");
	                    int dAccNo = scanner.nextInt();
	                    System.out.print("Amount to Deposit: ");
	                    double depAmt = scanner.nextDouble();
	                    Account dAcc = bankService.findAccount(dAccNo);
	                    if (dAcc != null) {
	                        dAcc.deposit(depAmt);
	                    }
	                    break;
	                case 3:
	                    System.out.print("Enter Account No: ");
	                    int wAccNo = scanner.nextInt();
	                    System.out.print("Amount to Withdraw: ");
	                    double wAmt = scanner.nextDouble();
	                    Account wAcc = bankService.findAccount(wAccNo);
	                    if (wAcc != null) {
	                        wAcc.withdraw(wAmt);
	                    }
	                    break;
	                case 4:
	                    System.out.print("From Account No: ");
	                    int fromAccNo = scanner.nextInt();
	                    System.out.print("To Account No: ");
	                    int toAccNo = scanner.nextInt();
	                    System.out.print("Amount to Transfer: ");
	                    double tAmt = scanner.nextDouble();
	                    Account fromAcc = bankService.findAccount(fromAccNo);
	                    Account toAcc = bankService.findAccount(toAccNo);
	                    if (fromAcc != null && toAcc != null) {
	                        fromAcc.transfer(toAcc, tAmt);
	                    }
	                    break;
	                case 5:
	                    System.out.print("Enter Account No: ");
	                    int mAccNo = scanner.nextInt();
	                    Account mAcc = bankService.findAccount(mAccNo);
	                    if (mAcc != null) {
	                        mAcc.miniStatement();
	                    }
	                    break;
	                case 6:
	                    System.out.print("Enter Account No: ");
	                    int sAccNo = scanner.nextInt();
	                    Account sAcc = bankService.findAccount(sAccNo);
	                    if (sAcc != null) {
	                        sAcc.accountSummary();
	                    }
	                    break;
	                case 7:
	                    bankService.printDailyTransactionReport();
	                    break;
	                case 0:
	                    System.out.println("Exiting app.");
	                    break;
	                default:
	                    System.out.println("Invalid choice.");
	            }
	        } while (choice != 0);
	        scanner.close(); }
	}
