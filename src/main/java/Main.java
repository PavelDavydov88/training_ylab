import config.ConnectionUtils;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import repository.*;
import service.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException, DatabaseException {
        Connection connection = null;
        Database database = null;
        Statement statement = null;
        try {
            connection = ConnectionUtils.getConnection();
            database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            String sql = "CREATE SCHEMA IF NOT EXISTS liquibase";
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            database.setDefaultSchemaName("liquibase");
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Миграции успешно выполнены!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("connection from main close=" + connection.isClosed());

        PlayerRepository playerRepository = new PlayerRepositoryImpl();
        AuthRepository authRepository = new AuthRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();
        HistoryCreditDebitRepository historyCreditDebitRepository = new HistoryCreditDebitRepositoryImpl();
        AuditRepository auditRepository = new AuditRepositoryImpl();

        AuditService auditService = new AuditServiceImpl(auditRepository);
        AuthService authService = new AuthServiceImpl(playerRepository, authRepository, auditRepository);
        TransactionService transactionService = new TransactionServiceImpl(transactionRepository);
        PlayerService playerService = new PlayerServiceImpl(playerRepository, transactionService, authService, historyCreditDebitRepository, auditService);

        Scanner input = new Scanner(System.in);
        int option;

        while (true) {
            menu();
            option = input.nextInt();
            switch (option) {
                case 0:
                    connection.close();
                    database.close();
                    statement.close();
                    System.out.println("connection from main close=" + connection.isClosed());
                    System.exit(0);
                case 1: {
                    System.out.println("registration player, input Name, Password");
                    String name = input.next();
                    String password = input.next();
                    playerService.create(name, password);
                    System.out.println("player created : ");
                    break;
                }
                case 2: {
                    System.out.println("authorization player, input Name, Password");
                    String name = input.next();
                    String password = input.next();
                    String token = authService.doAuthorization(name, password);
                    System.out.println("your token : " + token);
                    break;
                }
                case 3: {
                    System.out.println("current account of the player, input your token");
                    String token = input.next();
                    System.out.println("your current account : " + playerService.getAccount(token));
                    break;
                }
                case 4: {
                    System.out.println("debit operation of the player, input your token,value of credit, ID transaction");
                    String token = input.next();
                    long valueDebit = input.nextLong();
                    String transaction = input.next();
                    try {
                        System.out.println("your current account : " + playerService.debitAccount(token, valueDebit, transaction));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 5: {
                    System.out.println("credit operation of the player, input your token, value of debit, ID transaction");
                    String token = input.next();
                    long valueCredit = input.nextLong();
                    String transaction = input.next();
                    try {
                        System.out.println("your current account: " + playerService.creditAccount(token, valueCredit, transaction));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 6: {
                    System.out.println("listing of the history debit/credit operation of the player, input your token");
                    String token = input.next();
                    System.out.println("your history debit/credit operation : " + playerService.getListOperationAccount(token));
                    break;
                }
                case 7: {
                    System.out.println("audit of the player, input your token");
                    String token = input.next();
                    System.out.println("your audit : " + playerService.getListAuditAction(token));
                    break;
                }
                case 8: {
                    System.out.println("exit of authorization, input your token");
                    String token = input.next();
                    authService.exitAuthorization(token);
                    System.out.println("exit of authorization player done");
                    break;
                }
            }
        }
    }

    public static void menu() {
        // Printing statements displaying menu on console
        System.out.println("MENU");
        System.out.println("1: registration player");
        System.out.println("2: authorization player");
        System.out.println("3: current account of the player");
        System.out.println("4: debit operation of the player");
        System.out.println("5: credit operation of the player");
        System.out.println("6: listing of the history debit/credit operation of the player");
        System.out.println("7: audit of the player");
        System.out.println("8: exit of authorization");
        System.out.println("0: exit of program");
        System.out.print("Enter your selection : ");
    }
}
