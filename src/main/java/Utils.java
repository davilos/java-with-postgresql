import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
        
    }
}
