import model.Player;
import repository.*;
import service.*;

import java.util.Scanner;

public class main {
    public static void main(String[] args) {

        AuthRepository authRepository = new AuthRepositoryImpl();
        PlayerRepository playerRepository = new PlayerRepositoryImpl();
        HistoryCreditDebitRepository historyCreditDebitRepository = new HistoryCreditDebitRepositoryImpl();
        AuditRepository auditRepository = new AuditRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();

        TransactionService transactionService = new TransactionServiceImpl(transactionRepository);
        PlayerService playerService = new PlayerServiceImpl(playerRepository, transactionService, authRepository, historyCreditDebitRepository, auditRepository);
        AuthService authService = new AuthServiceImpl(playerService, authRepository, auditRepository);

        Scanner input = new Scanner(System.in);
        int option;

        while (true) {
            menu();
            option = input.nextInt();
            switch (option) {
                case 0:
                    System.exit(0);
                case 1: {
                    System.out.println("регистрация игрока, введите Имя, Пароль");
                    String name = input.next();
                    String password = input.next();
                    Player player = playerService.create(new Player(name, password, 0));
                    System.out.println("игрок создан : " + player);
                    break;
                }
                case 2: {
                    System.out.println("авторизация игрока, введите Имя, Пароль");
                    String name = input.next();
                    String password = input.next();
                    String token = authService.doAuthorization(name, password);
                    System.out.println("ваш token : " + token);
                    break;
                }
                case 3: {
                    System.out.println("текущий баланс игрока, введите ваш token");
                    String token = input.next();
                    System.out.println("ваш баланс : " + playerService.getAccount(token));
                    break;
                }
                case 4: {
                    System.out.println("дебет/снятие средств игрока, введите ваш token, значение дебета, номер транзакции");
                    String token = input.next();
                    long valueDebit = input.nextLong();
                    String transaction = input.next();
                    try {
                        System.out.println("ваш баланс : " + playerService.debitAccount(token, valueDebit, transaction));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 5: {
                    System.out.println("кредит средств игрока, введите ваш token, значение кредита, номер транзакции");
                    String token = input.next();
                    long valueCredit = input.nextLong();
                    String transaction = input.next();
                    try {
                        System.out.println("ваш баланс: " + playerService.creditAccount(token, valueCredit, transaction));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 6: {
                    System.out.println("просмотр истории пополнения/снятия средств игрока, введите ваш token");
                    String token = input.next();
                    System.out.println("история пополнения/снятия : " + playerService.getListOperationAccount(token));
                    break;
                }
                case 7: {
                    System.out.println("аудит действий игрока, введите ваш token");
                    String token = input.next();
                    System.out.println("аудит действий : " + playerService.getListAuditAction(token));
                    break;
                }
                case 8: {
                    System.out.println("выход игрока, введите ваш token");
                    String token = input.next();
                    authService.exitAuthorization(token);
                    System.out.println("выход игрока выполнен");
                    break;
                }

            }

        }
    }

    public static void menu() {
        // Printing statements displaying menu on console
        System.out.println("MENU");
        System.out.println("1: регистрация игрока");
        System.out.println("2: авторизация игрока");
        System.out.println("3: текущий баланс игрока");
        System.out.println("4: дебет/снятие средств игрока");
        System.out.println("5: кредит на игрока");
        System.out.println("6: просмотр истории пополнения/снятия средств игроком");
        System.out.println("7: аудит действий игрока");
        System.out.println("8: выход игрока");
        System.out.println("0: выход из программы");
        System.out.print("Enter your selection : ");
    }
}
