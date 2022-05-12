package com;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class LeetCodeN4 {
    private static boolean result = true;
    public static void main(String[] args) {
        char[][] board = {
                {'5','3','.','.','7','.','.','.','.'},
                {'6','.','.','1','9','5','.','.','.'},
                {'.','9','.','.','.','.','.','6','.'},
                {'8','.','.','.','6','.','.','.','3'},
                {'4','.','.','8','.','3','.','.','1'},
                {'7','.','.','.','2','.','.','.','6'},
                {'.','6','.','.','.','.','2','8','.'},
                {'.','.','.','4','1','9','.','.','5'},
                {'.','.','.','.','8','.','.','7','9'}
        };

        System.out.println(isValidSudoku(board));
    }

    public static boolean isValidSudoku(char[][] board) {
        /*for (int i = 0; i < 9; i++) {
            if (!isRowOrColumnCorrect(board[i])) {
                System.out.println("Row " + i + " incorrect");
                return false;
            } else {
                System.out.println("Row " + i + " correct");
            }
        }

        char[] column = new char[9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                column[j] = board[j][i];
            }
            if (!isRowOrColumnCorrect(column)) {
                System.out.println("Column " + i + " incorrect");
                return false;
            } else {
                System.out.println("Column " + i + " correct");
            }
        }*/

        List<List<String>> squareList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            squareList.add(new ArrayList<>());
        }

        int rowIndex;
        int columnIndex;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                rowIndex = i / 3;
                if (rowIndex == 1) {
                    rowIndex = 3;
                } else if (rowIndex == 2) {
                    rowIndex = 6;
                }
                columnIndex = j / 3;
                squareList.get(rowIndex + columnIndex).add(String.valueOf(board[i][j]));
            }
        }
        System.out.println(squareList);

        squareList.forEach(square -> {
            char[] chars = square.stream().collect(Collectors.joining()).toCharArray();
            if (!isRowOrColumnCorrect(chars)) {
                System.out.println("Square incorrect");
                result = false;
            } else {
                System.out.println("Square correct");
            }
        });

        return result;
    }

    private static boolean isRowOrColumnCorrect(char[] row) {
        Map<String, Integer> uniqueElementsWithCount = new HashMap<>();
        //System.out.println(row);
        for (char c : row) {
            String key = String.valueOf(c);
            if (uniqueElementsWithCount.containsKey(key)) {
                uniqueElementsWithCount.put(key, uniqueElementsWithCount.get(key) + 1);
            } else {
                uniqueElementsWithCount.put(key, 1);
            }
        }
        //System.out.println(uniqueElementsWithCount);
        uniqueElementsWithCount.remove(".");
        List<Integer> duplicatesList = uniqueElementsWithCount.values().stream().filter(d -> d > 1).collect(Collectors.toList());
        //System.out.println(duplicatesList);
        return duplicatesList.isEmpty();
    }
}
