package com.example.lms.Service;

import com.example.lms.Model.BorrowRecord;
import com.example.lms.Model.ReturnRecord;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReturnService {

    private static final String DATA_FILE = "src/main/resources/data/return.txt";
    private final List<ReturnRecord> returnRecords = new ArrayList<>();
    private final BorrowService borrowService;

    public ReturnService(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostConstruct
    public void init() {
        File file = new File(DATA_FILE);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            loadReturnRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadReturnRecords() {
        returnRecords.clear();
        File file = new File(DATA_FILE);
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            String line;
            while ((line = raf.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length == 11) {
                    returnRecords.add(new ReturnRecord(
                            p[0], p[1], p[2], p[3], p[4],
                            p[5], p[6], Integer.parseInt(p[7]),
                            p[8], p[9], Integer.parseInt(p[10])
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveReturnRecords() {
        File file = new File(DATA_FILE);
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.setLength(0);
            for (ReturnRecord r : returnRecords) {
                String line = r.getStudentName() + "|" + r.getStudentId() + "|" +
                        r.getBatch() + "|" + r.getDepartment() + "|" + r.getPhone() + "|" +
                        r.getBookName() + "|" + r.getAuthorName() + "|" + r.getQuantity() + "|" +
                        r.getBorrowDate() + "|" + r.getReturnDate() + "|" + r.getPenalty() + "\n";
                raf.writeBytes(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String returnBook(BorrowRecord borrowRecord, String actualReturnDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dueDate = LocalDate.parse(borrowRecord.getReturnDate(), formatter);
        LocalDate returnDate = LocalDate.parse(actualReturnDate, formatter);

        long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
        int penalty = daysLate > 0 ? (int) (daysLate * 100) : 0;

        ReturnRecord record = new ReturnRecord(
                borrowRecord.getStudentName(),
                borrowRecord.getStudentId(),
                borrowRecord.getBatch(),
                borrowRecord.getDepartment(),
                borrowRecord.getPhone(),
                borrowRecord.getBookName(),
                borrowRecord.getAuthorName(),
                borrowRecord.getQuantity(),
                borrowRecord.getBorrowDate(),
                actualReturnDate,
                penalty
        );

        returnRecords.add(record);
        saveReturnRecords();



        return "success";
    }

    public List<ReturnRecord> getAllReturnRecords() {
        loadReturnRecords();
        return returnRecords;
    }
}
