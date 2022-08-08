package com;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class test3 {
    public static void main(String[] args) {
        List<String> table1 = new ArrayList<>();
        try {
            Stream<String> fileStrings = Files.lines(Paths.get("src/main/resources/table1.txt"));
            table1 = fileStrings.collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("Parsing exception: " + e.getMessage());
        }

        List<String> table2 = new ArrayList<>();
        try {
            Stream<String> fileStrings = Files.lines(Paths.get("src/main/resources/table2.txt"));
            table2 = fileStrings.collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("Parsing exception: " + e.getMessage());
        }

        System.out.println(table1);
        System.out.println(table2);

        List<String> missedT1 = new ArrayList<>();
        for (String t2 : table2) {
            if (!table1.contains(t2)) {
                missedT1.add(t2);
            }
        }

        List<String> missedT2 = new ArrayList<>();
        for (String t1 : table1) {
            if (!table2.contains(t1)) {
                missedT2.add(t1);
            }
        }

        System.out.println("Нет в столбце1: " + missedT1);
        System.out.println("Нет в столбце2: " + missedT2);
    }
}
