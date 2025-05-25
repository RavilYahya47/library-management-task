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
