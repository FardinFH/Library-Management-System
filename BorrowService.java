package com.example.lms.Service;

import com.example.lms.Model.BorrowRecord;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

@Service
public class BorrowService {

    private static final String DATA_FILE = "src/main/resources/data/borrow.txt";
    private final List<BorrowRecord> borrowRecords = new ArrayList<>();
    private final MemberService memberService;

    public BorrowService(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostConstruct
    public void loadBorrowRecords() {
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
                    if (p.length == 10) {
                        borrowRecords.add(new BorrowRecord(
                                p[0], p[1], p[2], p[3], p[4],
                                p[5], p[6], Integer.parseInt(p[7]),
                                p[8], p[9]
                        ));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBorrowRecords() {
        File file = new File(DATA_FILE);
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.setLength(0);
            for (BorrowRecord b : borrowRecords) {
                String line = b.getStudentName() + "|" + b.getStudentId() + "|" +
                        b.getBatch() + "|" + b.getDepartment() + "|" + b.getPhone() + "|" +
                        b.getBookName() + "|" + b.getAuthorName() + "|" + b.getQuantity() + "|" +
                        b.getBorrowDate() + "|" + b.getReturnDate() + "\n";
                raf.writeBytes(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String addBorrowRecord(BorrowRecord record) {

        boolean isMember = memberService.isMember(record.getStudentId());
        if (!isMember) {
            return "error_member";
        }

        int currentBooks = borrowRecords.stream()
                .filter(r -> r.getStudentId().equals(record.getStudentId()))
                .mapToInt(BorrowRecord::getQuantity)
                .sum();

        if (currentBooks + record.getQuantity() > 3) {
            return "error_limit";
        }

        borrowRecords.add(record);
        saveBorrowRecords();
        return "success";
    }

    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecords;
    }

    public void removeBorrowRecord(BorrowRecord record) {
        borrowRecords.removeIf(r ->
                r.getStudentId().equals(record.getStudentId()) &&
                        r.getBookName().equals(record.getBookName())
        );
        saveBorrowRecords();
    }
}
