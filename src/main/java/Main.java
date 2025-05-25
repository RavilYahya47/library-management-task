package main.java;

import main.java.model.Book;
import main.java.service.LibraryService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        LibraryService libraryService = new LibraryService();
        Scanner scanner = new Scanner(System.in);
        boolean doesContinue = true;
        System.out.println("1. Bütün kitabları göstər\n" +
        "2. Müəllifə görə kitab axtar\n" +
        "3. Mövcud kitabları göstər\n" +
        "4. Kitab icarəyə ver\n" +
        "5. Kitabı qaytar\n" +
        "6. Janr üzrə statistika\n" +
        "0. Çıxış\n");

        while(doesContinue){
            System.out.println("Enter the proper number for operation: ");
            int op = Integer.parseInt(scanner.nextLine());

            switch (op) {
                case 1 -> {
                    libraryService.getBookDAO()
                            .findAll()
                            .forEach(System.out::println);
                    System.out.println("---------------------------------------------------------------------------");
                }
                case 2 -> {
                    System.out.print("Enter the name of author: ");
                    String authorName = scanner.nextLine();
                    List<Book> booksByAuthor = libraryService.findBooksByAuthor(authorName);
                    if(booksByAuthor.isEmpty()){
                        System.out.println("No book by this author!");
                    } else{
                        System.out.println("Books by this author:");
                        booksByAuthor.forEach(System.out::println);
                    }
                    System.out.println("---------------------------------------------------------------------------");
                }
                case 3 -> {
                    List<Book> availableBooks = libraryService.findAvailableBooks();
                    if(availableBooks.isEmpty()){
                        System.out.println("Not any available book!");
                    } else{
                        System.out.println("Available books:");
                        availableBooks.forEach(System.out::println);
                    }
                    System.out.println("---------------------------------------------------------------------------");
                }
                case 4 -> {
                    System.out.println("Enter the id of book you want to borrow:");
                    int id = Integer.parseInt(scanner.nextLine());
                    libraryService.borrowBook(id);
                    System.out.println("---------------------------------------------------------------------------");

                }
                case 5 -> {
                    System.out.println("Enter the id of book you want to return:");
                    int id = Integer.parseInt(scanner.nextLine());
                    libraryService.returnBook(id);
                    System.out.println("---------------------------------------------------------------------------");

                }
                case 6 -> {
                    Map<String, Long> booksByGenre = libraryService.getBookStatisticsByGenre();
                    System.out.println(booksByGenre);
                    System.out.println("---------------------------------------------------------------------------");

                }
                case 0 -> {
                    doesContinue = false;
                    System.out.println("Bye!");
                    System.out.println("---------------------------------------------------------------------------");
                }
                default -> {
                    System.out.println("Wrong input!");
                    System.out.println("---------------------------------------------------------------------------");
                }
            }
        }

    }

}
