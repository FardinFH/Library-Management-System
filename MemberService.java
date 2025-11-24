package com.example.lms.Service;

import com.example.lms.Model.Member;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    private static final String DATA_FILE = "src/main/resources/data/members.txt";
    private final List<Member> members = new ArrayList<>();

    @PostConstruct
    public void init() {
        File file = new File(DATA_FILE);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            loadMembers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMember(Member member) {
        members.add(member);
        saveMembers();
    }

    private void saveMembers() {
        File file = new File(DATA_FILE);
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.setLength(0);
            for (Member m : members) {
                String line = m.getId() + "|" + m.getName() + "|" + m.getPhone() + "|" +
                        m.getEmail() + "|" + m.getBatch() + "|" + m.getDepartment() + "\n";
                raf.writeBytes(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Member> getAllMembers() {
        loadMembers();
        return members;
    }

    private void loadMembers() {
        members.clear();
        File file = new File(DATA_FILE);
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            String line;
            while ((line = raf.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length == 6) {
                    members.add(new Member(p[0], p[1], p[2], p[3], p[4], p[5]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isMember(String studentId) {
        loadMembers();
        return members.stream()
                .anyMatch(m -> m.getId().equals(studentId));
    }

    public long countAllMembers() {
        loadMembers();
        return members.size();
    }
}
