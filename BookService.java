package com.example.lms.Service;

import com.example.lms.Model.Book;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private static final String DATA_FILE = "src/main/resources/data/books.txt";
    private final List<Book> books = new ArrayList<>();

    @PostConstruct
    public void loadBooks() {
        File file = new File(DATA_FILE);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                String line;
                while ((line = raf.readLine()) != null) {
                    String[] p = line.split("\\|");
                    if (p.length == 6) {
                        books.add(new Book(
                                p[0],
                                p[1],
                                p[2],
                                p[3],
                                Integer.parseInt(p[4]),
                                Double.parseDouble(p[5])
                        ));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBooks() {
        File file = new File(DATA_FILE);
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.setLength(0);
            for (Book b : books) {
                String line = b.getId() + "|" + b.getVendorName() + "|" + b.getTitle() + "|" +
                        b.getAuthor() + "|" + b.getQuantity() + "|" + b.getPrice() + "\n";
                raf.writeBytes(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBook(Book book) {
        books.add(book);
        saveBooks();
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public long countAllBooks() {
        return books.stream().mapToInt(Book::getQuantity).sum();
    }
}
