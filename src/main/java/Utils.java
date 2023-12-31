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
            System.out.println("\nAté logo!");
            connection.close();
            System.exit(0);
        }
    }

    private static void delete() {
        String countQuery = "SELECT COUNT(*) AS count FROM produtos WHERE id=?";
        String deleteQuery = "DELETE FROM produtos WHERE id=?";

        System.out.print("\nDigite o código do produto: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            PreparedStatement countStatement = connection.prepareStatement(countQuery);
            countStatement.setInt(1, id);

            ResultSet countSet = countStatement.executeQuery();
            countSet.next();

            if (countSet.getInt("count") > 0) {
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, id);

                deleteStatement.executeUpdate();
                deleteStatement.close();

                System.out.printf("%nO produto foi removido com sucesso!%n");
            } else {
                System.out.println("\nNão existe um produto com o id informado!");
            }
            countStatement.close();
            countSet.close();
        } catch (Exception e ) {
            e.printStackTrace();
            System.err.println("\nErro ao deletar produto!");
            System.exit(-42);
        }
    }

    private static void update() {
        String countQuery = "SELECT COUNT(*) AS count FROM produtos WHERE id=?";

        System.out.print("\nInforme o código do produto: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            PreparedStatement countStatement = connection.prepareStatement(countQuery);
            countStatement.setInt(1, id);

            ResultSet countSet = countStatement.executeQuery();

            countSet.next();

            if (countSet.getInt("count") > 0) {
                String updateQuery = "UPDATE produtos SET nome=?, preco=?, estoque=? WHERE id=?";

                System.out.print("\nInforme o nome do produto: ");
                String name = scanner.nextLine();

                System.out.print("\nInforme o preço: R$");
                float price = scanner.nextFloat();

                System.out.print("\nInforme a quantidade em estoque: ");
                int number = scanner.nextInt();

                scanner.nextLine(); // Consume a quebra de linha, para evitar que o menu apareça sozinho.

                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                updateStatement.setString(1, name);
                updateStatement.setFloat(2, price);
                updateStatement.setInt(3, number);
                updateStatement.setInt(4, id);

                updateStatement.executeUpdate();
                updateStatement.close();

                System.out.printf("%nO produto '%s' foi atualizado com sucesso!%n", name);
            } else {
                System.out.println("\nNão existe produto com o id informado!");
            }
            countStatement.close();
            countSet.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("\nErro ao atualizar o produto!");
            System.exit(-42);
        }
    }

    private static void insert() {
        System.out.print("\nInforme o nome do produto: ");
        String name = scanner.nextLine();

        System.out.print("\nInforme o preço: R$");
        float price = scanner.nextFloat();

        System.out.print("\nInforme a quantidade em estoque: ");
        int number = scanner.nextInt();

        scanner.nextLine(); // Consume a quebra de linha, para evitar que o menu apareça sozinho.

        try {
            String insertQuery = "INSERT INTO produtos (nome, preco, estoque) VALUES (?, ?, ?)";

            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

            insertStatement.setString(1, name);
            insertStatement.setFloat(2, price);
            insertStatement.setInt(3, number);

            insertStatement.executeUpdate();
            insertStatement.close();

            System.out.printf("%nO produto '%s' foi inserido com sucesso!%n", name);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("\nErro ao inserir produto!");
            System.exit(-42);
        }
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
