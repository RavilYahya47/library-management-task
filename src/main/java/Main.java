package main.java;

import main.java.model.Author;
import main.java.model.Book;
import main.java.service.LibraryService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final LibraryService libraryService = new LibraryService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            try {
                showMenu();
                int choice = getChoice();

                if (choice == 0) {
                    System.out.println("Proqramdan çıxılır... Sağ olun!");
                    break;
                }

                handleMenuChoice(choice);

            } catch (Exception e) {
                System.err.println("Xəta baş verdi: " + e.getMessage());
                System.out.println("Zəhmət olmasa yenidən cəhd edin.\n");
            }
        }

        scanner.close();

    }

    private static void showMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("!! KİTAB İDARƏETMƏ SİSTEMİ !!");
        System.out.println("=".repeat(50));
        System.out.println("1  Bütün kitabları göstər");
        System.out.println("2  Müəllifə görə kitab axtar");
        System.out.println("3  Mövcud kitabları göstər");
        System.out.println("4  Kitab icarəyə ver");
        System.out.println("5  Kitabı qaytar");
        System.out.println("6  Janr üzrə statistika");
        System.out.println("7  Bütün müəllifləri göstər");
        System.out.println("8  Yeni müəllif əlavə et");
        System.out.println("9  Müəllif məlumatlarını yenilə");
        System.out.println("10 Müəllifi sil");
        System.out.println("11 Yeni kitab əlavə et");
        System.out.println("00 Çıxış");
        System.out.println("=".repeat(50));
        System.out.print("Seçiminizi daxil edin (0-10): ");
    }

    private static int getChoice() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Yalnış daxiletmə! Zəhmət olmasa rəqəm daxil edin.");
            return -1;
        }
    }

    private static void handleMenuChoice(int choice) throws SQLException {
        switch (choice) {
            case 1:
                showAllBooks();
                break;
            case 2:
                searchBooksByAuthor();
                break;
            case 3:
                showAvailableBooks();
                break;
            case 4:
                borrowBook();
                break;
            case 5:
                returnBook();
                break;
            case 6:
                showBookStatistics();
                break;
            case 7:
                showAllAuthors();
                break;
            case 8:
                addNewAuthor();
                break;
            case 9:
                updateAuthor();
                break;
            case 10:
                deleteAuthor();
                break;
            case 11:
                addNewBook();
            default:
                System.out.println("Yanlış seçim! Zəhmət olmasa 0-10 arası rəqəm daxil edin.");
        }
    }

    private static void addNewBook() throws SQLException {
        System.out.println("\nYeni Kitab Əlavə Et");
        System.out.println("-".repeat(30));

        System.out.print("Kitab başlığı: ");
        String title = scanner.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println("Kitab başlığı boş ola bilməz!");
            return;
        }

        System.out.print("Müəllif ID-si: ");
        try {
            int authorId = Integer.parseInt(scanner.nextLine().trim());
            Optional<Author> author = libraryService.getAuthorById(authorId);
            if (author.isEmpty()) {
                System.out.println("Bu ID ilə müəllif tapılmadı: " + authorId);
                return;
            }

            System.out.print("Nəşr ili (boş buraxmaq üçün Enter basın): ");
            String publicationYearStr = scanner.nextLine().trim();
            Integer publicationYear = null;

            if (!publicationYearStr.isEmpty()) {
                try {
                    publicationYear = Integer.parseInt(publicationYearStr);
                } catch (NumberFormatException e) {
                    System.out.println("Yanlış il formatı! Kitab onsuz əlavə ediləcək.");
                }
            }

            System.out.print("Janr (boş buraxmaq üçün Enter basın): ");
            String genre = scanner.nextLine().trim();
            if (genre.isEmpty()) {
                genre = null;
            }

            System.out.print("Səhifə sayı (boş buraxmaq üçün Enter basın): ");
            String pagesStr = scanner.nextLine().trim();
            Integer pages = null;

            if (!pagesStr.isEmpty()) {
                try {
                    pages = Integer.parseInt(pagesStr);
                } catch (NumberFormatException e) {
                    System.out.println("Yanlış səhifə sayı formatı! Kitab onsuz əlavə ediləcək.");
                }
            }

            Book newBook = new Book(0, title, authorId, publicationYear, genre, pages, true, null);

            System.out.println("Kitab əlavə edilir...");
            Book savedBook = libraryService.saveBook(newBook);
            System.out.println("Kitab uğurla əlavə edildi!");
            System.out.printf("ID: %d | Başlıq: %s | Müəllif: %s | İl: %s | Janr: %s | Səhifələr: %s%n",
                    savedBook.getId(),
                    savedBook.getTitle(),
                    author.get().getName(),
                    savedBook.getPublicationYear() != null ? savedBook.getPublicationYear().toString() : "Naməlum",
                    savedBook.getGenre() != null ? savedBook.getGenre() : "Naməlum",
                    savedBook.getPages() != null ? savedBook.getPages().toString() : "Naməlum"
            );

        } catch (NumberFormatException e) {
            System.out.println("Yanlış müəllif ID formatı! Zəhmət olmasa rəqəm daxil edin.");
        }
    }

    private static void showAllBooks() throws SQLException {
        System.out.println("\nBütün kitabları yükləyir...");
        List<Book> availableBooks = libraryService.findAvailableBooks();
        libraryService.printBooks(availableBooks, "Sistemdə Mövcud Olan Bütün Kitablar");

        if (!availableBooks.isEmpty()) {
            System.out.println("\nCəmi kitab sayı: " + availableBooks.size());
        }
    }

    private static void searchBooksByAuthor() throws SQLException {
        System.out.print("\n Müəllif adını daxil edin: ");
        String authorName = scanner.nextLine().trim();

        if (authorName.isEmpty()) {
            System.out.println("Müəllif adı boş ola bilməz!");
            return;
        }

        System.out.println("🔍 '" + authorName + "' müəllifinin kitabları axtarılır...");
        List<Book> books = libraryService.findBooksByAuthor(authorName);
        libraryService.printBooks(books, "'" + authorName + "' müəllifinin kitabları");
    }

    private static void showAvailableBooks() throws SQLException {
        System.out.println("\nMövcud kitablar yüklənir...");
        List<Book> books = libraryService.findAvailableBooks();
        libraryService.printBooks(books, "İcarəyə Verilə Bilən Kitablar");

        if (!books.isEmpty()) {
            System.out.println("\nMövcud kitab sayı: " + books.size());
        }
    }

    private static void borrowBook() throws SQLException {
        System.out.print("\nİcarəyə vermək istədiyiniz kitabın ID-sini daxil edin: ");

        try {
            int bookId = Integer.parseInt(scanner.nextLine().trim());

            System.out.println("Kitab icarəyə verilir...");
            libraryService.borrowBook(bookId);
            System.out.println("Kitab uğurla icarəyə verildi! (ID: " + bookId + ")");

        } catch (NumberFormatException e) {
            System.out.println("Yanlış format! Zəhmət olmasa rəqəm daxil edin.");
        } catch (RuntimeException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    private static void returnBook() throws SQLException {
        System.out.print("\n Qaytarmaq istədiyiniz kitabın ID-sini daxil edin: ");

        try {
            int bookId = Integer.parseInt(scanner.nextLine().trim());

            System.out.println("Kitab qaytarılır...");
            libraryService.returnBook(bookId);
            System.out.println("Kitab uğurla qaytarıldı! (ID: " + bookId + ")");

        } catch (NumberFormatException e) {
            System.out.println("Yanlış format! Zəhmət olmasa rəqəm daxil edin.");
        } catch (RuntimeException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    private static void showBookStatistics() throws SQLException {
        System.out.println("\nStatistika hazırlanır...");
        libraryService.printBookStatistics();
    }

    private static void showAllAuthors() throws SQLException {
        System.out.println("\nMüəlliflər yüklənir...");
        libraryService.printAllAuthors();
    }

    private static void addNewAuthor() throws SQLException {
        System.out.println("\nYeni Müəllif Əlavə Et");
        System.out.println("-".repeat(30));

        System.out.print("Müəllif adı: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Müəllif adı boş ola bilməz!");
            return;
        }

        System.out.print("Doğum ili (boş buraxmaq üçün Enter basın): ");
        String birthYearStr = scanner.nextLine().trim();
        Integer birthYear = null;

        if (!birthYearStr.isEmpty()) {
            try {
                birthYear = Integer.parseInt(birthYearStr);
            } catch (NumberFormatException e) {
                System.out.println("Yanlış il formatı! Müəllif onsuz əlavə ediləcək.");
            }
        }

        System.out.print("Milliyyəti (boş buraxmaq üçün Enter basın): ");
        String nationality = scanner.nextLine().trim();
        if (nationality.isEmpty()) {
            nationality = null;
        }

        Author newAuthor = new Author(0, name, birthYear, nationality);

        System.out.println("Müəllif əlavə edilir...");
        Author savedAuthor = libraryService.saveAuthor(newAuthor);
        System.out.println("Müəllif uğurla əlavə edildi!");
        System.out.printf("ID: %d | Ad: %s | Doğum ili: %s | Milliyyət: %s%n",
                savedAuthor.getId(),
                savedAuthor.getName(),
                savedAuthor.getBirthYear() != null ? savedAuthor.getBirthYear().toString() : "Naməlum",
                savedAuthor.getNationality() != null ? savedAuthor.getNationality() : "Naməlum"
        );
    }

    private static void updateAuthor() throws SQLException {
        System.out.println("\n✏️ Müəllif Məlumatlarını Yenilə");
        System.out.println("-".repeat(35));

        System.out.print("Yeniləmək istədiyiniz müəllifin ID-sini daxil edin: ");

        try {
            int authorId = Integer.parseInt(scanner.nextLine().trim());

            // Əvvəlcə müəllifin mövcud olub olmadığını yoxlayırıq
            Optional<Author> existingAuthor = libraryService.getAuthorById(authorId);

            if (existingAuthor.isEmpty()) {
                System.out.println("❌ Bu ID ilə müəllif tapılmadı: " + authorId);
                return;
            }

            Author author = existingAuthor.get();
            System.out.println("Hazırkı məlumatlar:");
            System.out.printf("   Ad: %s%n", author.getName());
            System.out.printf("   Doğum ili: %s%n", author.getBirthYear() != null ? author.getBirthYear() : "Naməlum");
            System.out.printf("   Milliyyət: %s%n", author.getNationality() != null ? author.getNationality() : "Naməlum");

            System.out.println("\nYeni məlumatları daxil edin (boş buraxsanız köhnəsi qalacaq):");

            System.out.print("Yeni ad: ");
            String newName = scanner.nextLine().trim();
            if (!newName.isEmpty()) {
                author.setName(newName);
            }

            System.out.print("Yeni doğum ili: ");
            String newBirthYearStr = scanner.nextLine().trim();
            if (!newBirthYearStr.isEmpty()) {
                try {
                    author.setBirthYear(Integer.parseInt(newBirthYearStr));
                } catch (NumberFormatException e) {
                    System.out.println("Yanlış il formatı! Köhnə qiymət saxlanıldı.");
                }
            }

            System.out.print("Yeni milliyyət: ");
            String newNationality = scanner.nextLine().trim();
            if (!newNationality.isEmpty()) {
                author.setNationality(newNationality);
            }

            System.out.println("Məlumatlar yenilənir...");
            libraryService.updateAuthor(author);
            System.out.println("Müəllif məlumatları uğurla yeniləndi!");

        } catch (NumberFormatException e) {
            System.out.println("Yanlış ID formatı! Zəhmət olmasa rəqəm daxil edin.");
        }
    }

    private static void deleteAuthor() throws SQLException {
        System.out.println("\n🗑️ Müəllifi Sil");
        System.out.println("-".repeat(20));
        System.out.print("Silmək istədiyiniz müəllifin ID-sini daxil edin: ");
        try {
            int authorId = Integer.parseInt(scanner.nextLine().trim());
            Optional<Author> existingAuthor = libraryService.getAuthorById(authorId);

            if (existingAuthor.isEmpty()) {
                System.out.println("Bu ID ilə müəllif tapılmadı: " + authorId);
                return;
            }

            Author author = existingAuthor.get();
            System.out.println("Silinəcək müəllif:");
            System.out.printf("   ID: %d%n", author.getId());
            System.out.printf("   Ad: %s%n", author.getName());
            System.out.printf("   Doğum ili: %s%n", author.getBirthYear() != null ? author.getBirthYear() : "Naməlum");
            System.out.printf("   Milliyyət: %s%n", author.getNationality() != null ? author.getNationality() : "Naməlum");

            System.out.print("\nBu müəllifi silmək istədiyinizdən əminsiniz? (y/n): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("y") || confirmation.equals("yes") || confirmation.equals("bəli")) {
                System.out.println("Müəllif silinir...");
                libraryService.deleteAuthor(authorId);
                System.out.println("Müəllif uğurla silindi!");
            } else {
                System.out.println("Əməliyyat ləğv edildi.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Yanlış ID formatı! Zəhmət olmasa rəqəm daxil edin.");
        }
    }

    private static void waitForEnter() {
        System.out.print("\nDavam etmək üçün Enter basın...");
        scanner.nextLine();
    }
}
