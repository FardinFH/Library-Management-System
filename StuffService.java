package com.example.lms.Service;

import com.example.lms.Model.Stuff;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

@Service
public class StuffService {

    private static final String DATA_FILE = "src/main/resources/data/stuff.txt";
    private final List<Stuff> stuffs = new ArrayList<>();

    @PostConstruct
    public void loadStuffs() {
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
                    if (p.length == 5) {
                        stuffs.add(new Stuff(
                                p[0],
                                p[1],
                                p[2],
                                p[3],
                                Double.parseDouble(p[4])
                        ));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveStuffs() {
        File file = new File(DATA_FILE);
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.setLength(0);
            for (Stuff s : stuffs) {
                String line = s.getName() + "|" + s.getNid() + "|" + s.getPhone() + "|" +
                        s.getAddress() + "|" + s.getSalary() + "\n";
                raf.writeBytes(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addStuff(Stuff stuff) {
        stuffs.add(stuff);
        saveStuffs();
    }

    public List<Stuff> getAllStuffs() {
        return stuffs;
    }

    public long countAllStuffs() {
        return stuffs.size();
    }
}
