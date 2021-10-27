package com.magnumbillparser;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Strings;

public class BillParser {
    public static void main(String[] args) {
        //rc1
        List<String> allText = new ArrayList<>();
        try {
            Stream<String> fileStrings = Files.lines(Paths.get("src/main/resources/Bill.txt"));
            allText = fileStrings.collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("Parsing exception: " + e.getMessage());
        }

        List<String> withNumbers = allText.stream()
                .filter(line -> !Strings.isNullOrEmpty(line))
                .filter(line -> (Character.isDigit(line.charAt(0)) && (line.charAt(1) == '.' || line.charAt(2) == '.'))).collect(Collectors.toList());

        //withNumbers.forEach(System.out::println);

        for (String line: withNumbers) {
            String lineWithoutFirstNumbers = line.substring(line.indexOf(" ") + 1);
            //System.out.println(lineWithoutFirstNumbers);

            //Getting price
            String lineBeforeCurrency = lineWithoutFirstNumbers.substring(0, lineWithoutFirstNumbers.indexOf("₸"));
            //System.out.println(lineBeforeCurrency);
            List<String> tokens = Arrays.asList(lineBeforeCurrency.split(" "));
            String secondFromEnd = tokens.get(tokens.size() - 2);
            String last = tokens.get(tokens.size() - 1);
            StringBuilder sb = new StringBuilder();
            int price;
            if (!Character.isDigit(secondFromEnd.charAt(secondFromEnd.length() - 1))) {
                if (last.contains("ГР")) {
                    last = last.substring(last.indexOf("ГР"));
                } else if (last.contains("М")) {
                    last = last.substring(last.indexOf("М"));
                } else if (last.contains("Г")) {
                    last = last.substring(last.indexOf("Г"));
                }

                char[] array = last.toCharArray();
                for (char a : array) {
                    if (Character.isDigit(a)) {
                        sb.append(a);
                    }
                }
            } else {
                char[] array = secondFromEnd.toCharArray();
                for (char a: array) {
                    if (Character.isDigit(a)) {
                        sb.append(a);
                    }
                }
                sb.append(last);
            }
            price = Integer.parseInt(sb.toString().substring(1));
            //System.out.println("Price: " + price);

            //Getting quantity
            String lineForQuantity = lineWithoutFirstNumbers.substring(lineWithoutFirstNumbers.indexOf("₸"), lineWithoutFirstNumbers.lastIndexOf(","));
            String quantity;
            if (!lineForQuantity.contains(".")) {
                quantity = String.valueOf(lineForQuantity.charAt(1));

                String stringPrice = lineForQuantity.substring(2).replace(" ", "");
                if (Long.parseLong(stringPrice) != Math.round(price * Double.parseDouble(quantity))) {
                    quantity = lineForQuantity.substring(1, 3);
                }
            } else {
                quantity = lineForQuantity.substring(1, lineForQuantity.indexOf(".") + 4);
            }
            //System.out.println("Quantity: " + quantity);

            //Getting name
            String positionName = lineWithoutFirstNumbers.substring(0, lineWithoutFirstNumbers.indexOf(" ", lineWithoutFirstNumbers.indexOf(" ") + 1));
            //System.out.println("Position name: " + positionName);

            double sum = price * Double.parseDouble(quantity);
            Long roundedSum = Math.round(sum);

            switch ((int) (roundedSum%10)){
                case 1:
                case 6:
                    roundedSum = roundedSum - 1;
                    break;
                case 2:
                case 7:
                    roundedSum = roundedSum - 2;
                    break;
                case 3:
                case 8:
                    roundedSum = roundedSum + 2;
                    break;
                case 4:
                case 9:
                    roundedSum = roundedSum + 1;
                    break;
            }
            quantity = quantity.replace(".", ",");

            String result = positionName + "\t " + price + "\t " + quantity + "\t" + roundedSum;
            System.out.println(result);
        }
    }
}
