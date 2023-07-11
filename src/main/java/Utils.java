import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Utils {
    static Scanner scanner = new Scanner(System.in);
    static Connection connection;
    public static void menu() {
        System.out.println("\n==================Gerenciamento de Produtos===============");
        System.out.println("Selecione uma opção: ");
        System.out.println("1 - Listar produtos.");
        System.out.println("2 - Inserir produtos.");
        System.out.println("3 - Atualizar produtos.");
        System.out.println("4 - Deletar produtos.");
        System.out.println("5 - Sair do sistema.");
        System.out.println("Para listar o menu novamente digite 'm' ou pressione 'enter'");

        while (true) {
            System.out.print("\n-> ");
            switch (scanner.nextLine()) {
                case "1" -> list();
                case "2" -> insert();
                case "3" -> update();
                case "4" -> delete();
                case "5" -> {
                    try {
                        disconnect();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "m", "M", "" -> menu();
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    public static void connect() {
        Properties properties = new Properties();

        System.out.print("Digite o seu usuário: ");
        String user = scanner.nextLine();

        System.out.print("Informe a senha do seu usuário: ");
        String password = scanner.nextLine();

        properties.setProperty("user", user);
        properties.setProperty("password", password);
        properties.setProperty("ssl", "false");

        String SERVER_URL = "jdbc:postgresql://localhost:5432/jpostgresql";

        try {
            connection = DriverManager.getConnection(SERVER_URL, properties);
            menu();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Verifique se o servidor está ativo!");
            System.exit(-42);
            connection = null;
        }
    }

    private static void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
            System.exit(0);
        }
    }

    private static void delete() {
        
    }

    private static void update() {
        
    }

    private static void insert() {
        
    }

    private static void list() {
        try {
            String countQuery = "SELECT COUNT(*) AS count FROM produtos";
            Statement statement = connection.createStatement();
            ResultSet countSet = statement.executeQuery(countQuery);

            countSet.next();

            if (countSet.getInt("count") > 0) {
                String selectAllQuery = "SELECT * FROM produtos";
                ResultSet selectAllSet = statement.executeQuery(selectAllQuery);

                System.out.print("\n--------------------------------------------------");
                System.out.printf("%n%-" + 5 + "s %-" + 20 + "s %-" + 15 + "s %-" + 5 + "s%n",
                        "id", "Nome", "Preço", "Estoque");
                System.out.println("--------------------------------------------------");

                while (selectAllSet.next()) {
                    System.out.printf("%-" + 5 + "d %-" + 20 + "s %-" + 15 + ".2f %-" + 5 + "d%n",
                            selectAllSet.getInt(1), selectAllSet.getString(2),
                            selectAllSet.getFloat(3), selectAllSet.getInt(4));
                }
                System.out.println("--------------------------------------------------");

                selectAllSet.close();
            } else {
                System.out.println("\nNão existem produtos cadastrados!");
            }
            statement.close();
            countSet.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("\nErro buscando todos os produtos!");
            System.exit(-42);
        }
    }
}
