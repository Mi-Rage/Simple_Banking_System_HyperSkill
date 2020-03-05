package banking;

import java.util.Scanner;

public class Bank {
    Scanner scanner = new Scanner(System.in);
    DataBase dataBase;


    public Bank(String fileName) {
        this.dataBase = new DataBase(fileName);
    }

    //Главное меню
    public void init() {
        int choice;

        do {
            System.out.println("1. Create account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    exit();
                    break;
                case 1:
                    createCard();
                    break;
                case 2:
                    logIntoAccount();
                    break;
                default:
                    System.out.println("ERROR: Не верный номер.");
            }
        } while (choice != 0);
    }

    public void createCard(){
        BankCard bankCard = new BankCard();

        System.out.println("\nYour card have been created");
        System.out.println("Your card number:\n" + bankCard.getCardNumber());
        System.out.println("Your card PIN:\n" + bankCard.getCardPin() + "\n");

        dataBase.insert(bankCard.getCardNumber(),bankCard.getCardPin(),0);
        dataBase.selectAll();
    }

    //Логин в свой аккаунт
    private void logIntoAccount() {
        String inputCardNumber;                       //Введенный номер карты
        String inputCardPin;                          //Введенный pin
        BankCard currentCard;                         //Полученная карта

        scanner.nextLine();  //Очистим сканер
        System.out.println("\nEnter your card number:");
        inputCardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        inputCardPin = scanner.nextLine();
        currentCard = dataBase.selectCard(inputCardNumber, inputCardPin);
        if(currentCard.getCardNumber().equals(inputCardNumber) || currentCard.getCardPin().equals(inputCardPin)){
            System.out.println("\nYou have successfully logged in!\n");
            operationAcc(currentCard);
        } else {
            System.out.println("\nWrong card number or PIN!\n");
        }
//        System.out.println("log N=" + currentCard.getCardNumber() + "log pin=" + currentCard.getCardPin() + " bal=" + currentCard.getBalance());

    }

    //Операции в личном кабинете
    private void operationAcc(BankCard currentCard) {
        int choice;

        do {
            System.out.println("1. Balance");
            System.out.println("2. Log out");
            System.out.println("0. Exit");
            choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    exit();
                    break;
                case 1:
                    viewBalance(currentCard);
                    break;
                case 2:
                    System.out.println("\nYou have successfully logged out!\n");
                    return;
                default:
                    System.out.println("ERROR: Не верный номер.");
            }
        } while (choice != 0);

    }

    //Запрос баланса
    private void viewBalance(BankCard currentCard) {
        System.out.println("\nBalance: " + currentCard.getBalance() + "\n");
    }

    //Безопасный выход
    private void exit() {
        System.out.println("\nBye!");
        System.exit(0);
    }
}
