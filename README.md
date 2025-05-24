# Kitab İdarəetmə Sistemi - Layihə Tapşırığı

## Layihə Haqqında
Bu layihədə siz sadə bir **Kitab İdarəetmə Sistemi** yaradacaqsınız. Sistem kitabları PostgreSQL verilənlər bazasında saxlayacaq və Java vasitəsilə idarə edəcəkdir. Bu tapşırıq sizin öyrəndiyiniz Java, SQL və Git biliklərinizi praktikada tətbiq etmək üçün hazırlanmışdır.

## Texniki Tələblər
- Java 17 və ya daha yeni versiya
- PostgreSQL verilənlər bazası
- JDBC driver
- Git version control

## Layihə Strukturu
```
library-management-task/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── model/
│   │       ├── dao/
│   │       ├── service/
│   │       └── Main.java
│   └── resources/
│       └── database.sql
└── README.md
```

## Tapşırıq 1: Verilənlər Bazası Qurulumu

### 1.1 PostgreSQL Connection
`database.sql` faylında aşağıdaki cədvəlləri yaradın:

```sql
-- Müəlliflər cədvəli
CREATE TABLE authors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    birth_year INTEGER,
    nationality VARCHAR(50)
);

-- Kitablar cədvəli
CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author_id INTEGER REFERENCES authors(id),
    publication_year INTEGER,
    genre VARCHAR(50),
    pages INTEGER,
    is_available BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 1.2 Test Məlumatları
Cədvəllərə test məlumatları əlavə edin:

```sql
-- Müəlliflər
INSERT INTO authors (name, birth_year, nationality) VALUES
('Nizami Gəncəvi', 1141, 'Azərbaycan'),
('Füzuli', 1494, 'Azərbaycan'),
('Gabriel García Márquez', 1927, 'Kolumbiya'),
('J.K. Rowling', 1965, 'İngiltərə');

-- Kitablar
INSERT INTO books (title, author_id, publication_year, genre, pages) VALUES
('Xəmsə', 1, 1200, 'Poeziya', 500),
('Leyli və Məcnun', 2, 1536, 'Romantik', 300),
('Yüz İllik Tənhalıq', 3, 1967, 'Maqik Realizm', 417),
('Harry Potter və Fəlsəfə Daşı', 4, 1997, 'Fantasy', 223);
```

## Tapşırıq 2: Model Class-ları

### 2.1 Author Class
`model/Author.java` faylında:

```java
public class Author {
    private int id;
    private String name;
    private Integer birthYear;
    private String nationality;
    
    // Constructors (default və parametrli)
    // Getter və Setter methodları
    // toString() methodu
    // equals() və hashCode() methodları
}
```

### 2.2 Book Class
`model/Book.java` faylında:

```java
public class Book {
    private int id;
    private String title;
    private int authorId;
    private Integer publicationYear;
    private String genre;
    private Integer pages;
    private boolean isAvailable;
    private LocalDateTime createdAt;
    
    // Constructors, getters, setters, toString, equals, hashCode
}
```

## Tapşırıq 3: Database Connection

### 3.1 DatabaseConnection Class
`dao/DatabaseConnection.java` faylında:

```java
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/library_db";
    private static final String USERNAME = "your_username";
    private static final String PASSWORD = "your_password";
    
    public static Connection getConnection() throws SQLException {
        // Connection qaytaran method yazın
        // Exception handling əlavə edin
    }
}
```

## Tapşırıq 4: DAO Layer

### 4.1 AuthorDAO Interface
`dao/AuthorDAO.java`:

```java
public interface AuthorDAO {
    List<Author> findAll() throws SQLException;
    Optional<Author> findById(int id) throws SQLException;
    Author save(Author author) throws SQLException;
    void update(Author author) throws SQLException;
    void deleteById(int id) throws SQLException;
}
```

### 4.2 AuthorDAOImpl Class
`dao/impl/AuthorDAOImpl.java` - Interface-i implement edin:
- `PreparedStatement` istifadə edin
- `ResultSet`-dən Author obyektləri yaradın
- Exception handling əlavə edin
- Connection-ları düzgün bağlayın

### 4.3 BookDAO və BookDAOImpl
Eyni şəkildə Book üçün də DAO layer yaradın.

## Tapşırıq 5: Service Layer

### 5.1 LibraryService Class
`service/LibraryService.java`:

```java
public class LibraryService {
    private AuthorDAO authorDAO;
    private BookDAO bookDAO;
    
    // Constructor dependency injection
    
    // Business logic methodları:
    public List<Book> findBooksByAuthor(String authorName) throws SQLException;
    public List<Book> findAvailableBooks() throws SQLException;
    public void borrowBook(int bookId) throws SQLException;
    public void returnBook(int bookId) throws SQLException;
    public Map<String, Long> getBookStatisticsByGenre() throws SQLException;
}
```

## Tapşırıq 6: Main Application

### 6.1 Main Class
`Main.java` faylında console-based menu yaradın:

```
=== Kitab İdarəetmə Sistemi ===
1. Bütün kitabları göstər
2. Müəllifə görə kitab axtar
3. Mövcud kitabları göstər
4. Kitab icarəyə ver
5. Kitabı qaytart
6. Janr üzrə statistika
0. Çıxış

Seçiminizi daxil edin:
```

## Bonus Tapşırıqlar

### Bonus 1: Stream API istifadəsi
- Kitabları janr üzrə qruplaşdırın
- Ən çox səhifəli kitabı tapın
- Müəyyən ildən sonra çap olunan kitabları filtirləyin

### Bonus 2: Custom Exception-lar
```java
public class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }
}

public class BookNotAvailableException extends Exception {
    // Implementation
}
```

### Bonus 3: Lambda Expression-lar
Service layer-də Functional Interface-lər istifadə edin:
```java
public List<Book> findBooksBy(Predicate<Book> condition) throws SQLException;
```

## Qiymətləndirmə Meyarları

### Əsas Tələblər (70 bal)
- ✅ Model class-ları düzgün yaradılması (10 bal)
- ✅ Database connection qurulması (10 bal)
- ✅ DAO pattern-in düzgün tətbiqi (20 bal)
- ✅ Service layer biznes məntiqinin yazılması (15 bal)
- ✅ Main aplikasiyanın işləməsi (15 bal)

### Kod Keyfiyyəti (20 bal)
- ✅ Exception handling (5 bal)
- ✅ Code organization və package structure (5 bal)
- ✅ Naming convention-lar (5 bal)
- ✅ Comment-lər və documentation (5 bal)

### Git Workflow (10 bal)
- ✅ Düzgün branch yaradılması (3 bal)
- ✅ Meaningful commit message-ları (4 bal)
- ✅ Code push edilməsi (3 bal)

## Git Workflow Təlimatları

### 1. Repository Clone
```bash
git clone <repository-url>
cd library-management-task
```

### 2. Yeni Branch Yarat
```bash
git checkout -b feature/your-name-library-system
# Nümunə: git checkout -b feature/ali-library-system
```

### 3. Kodu Yaz və Commit Et
```bash
git add .
git commit -m "feat: implement Author and Book models"
git commit -m "feat: add database connection setup"
git commit -m "feat: implement AuthorDAO with CRUD operations"
# və s.
```

### 4. Branch-ı Push Et
```bash
git push origin feature/your-name-library-system
```

## Faydalı Məsləhətlər

1. **SQLException Handling**: `try-catch` blokları istifadə edin
2. **Resource Management**: `try-with-resources` istifadə edin
3. **PreparedStatement**: SQL injection-dan qorunmaq üçün
4. **Optional**: `null` dəyərlər üçün
5. **Streams**: Collections ilə işləyərkən
6. **Git Commits**: Hər feature üçün ayrı commit

## Test Etmək Üçün
Aplikasiyanızı işə salın və aşağıdaki əməliyyatları yoxlayın:
- Kitabların siyahısını görmək
- Müəllifə görə axtarış
- Kitab icarəyə vermək və qaytarmaq
- Statistika görmək

**Uğurlar! 🚀**

---
*Bu layihə Java Backend kursu üçün hazırlanmışdır. Suallarınız olduqda müəllimə müraciət edin.*