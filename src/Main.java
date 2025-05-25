import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Dream Proqraminına xoş gəlmisiniz.");

        while (true) {
            System.out.println("Aşağıdakı əməliyyatlardan birini seçin!");
            Map<Integer, String> operations = LibraryService.getAllOperations();

            operations.forEach((key, value) -> System.out.println(key + ": " + value));
            System.out.print("Seçiminiz: ");
            int num = sc.nextInt();
            BookDAOImpl bookDAO = new BookDAOImpl();
            LibraryService service = new LibraryService();
            switch (num) {
                case 0:
                    System.out.println("Proqram bağlanır...");
                    System.exit(0);
                    break;
                case 1:
                    bookDAO.findAll().forEach(System.out::println);
                    break;
                case 2:
                    System.out.print("Muellifin idsini  daxil edin :");
                    int authorId = sc.nextInt();
                    try {
                        service.findBooksByAuthor(authorId).forEach(System.out::println);
                    } catch (BookNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    service.findAvailableBooks().forEach(System.out::println);
                    break;
                case 4:
                    System.out.print("Id ni daxil edin :");
                    int id = sc.nextInt();
                    try {
                        service.borrowBook(id);
                    }catch (BookNotFoundException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    System.out.print("Id ni daxil edin :");
                    int id1 = sc.nextInt();
                    service.returnBook(id1);
                    break;
                case 6:
                    System.out.println("Janra gore statikalar");
                    System.out.println(service.getBookStatisticsByGenre());
                    break;
                default:
                    System.out.println("Yanlış seçim! Zəhmət olmasa düzgün seçim edin.");
            }
        }
    }
}
