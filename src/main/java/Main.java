package main.java;

import main.java.service.LibraryService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        LibraryService libraryService = new LibraryService();
        Scanner scanner = new Scanner(System.in);
        boolean doesContinue = true;
        System.out.println("1. Bütün kitabları göstər\n" +
        "2. Müəllifə görə kitab axtar\n" +
        "3. Mövcud kitabları göstər\n" +
        "4. Kitab icarəyə ver\n" +
        "5. Kitabı qaytar\n" +
        "6. Janr üzrə statistika\n" +
        "0. Çıxış\n");

        while(doesContinue){
            System.out.println("Enter the proper number for operation: ");
            int op = Integer.parseInt(scanner.nextLine());

            switch (op) {
                case 1 -> {

                }
                case 2 -> {

                }
                case 3 -> {

                }
                case 4 -> {

                }
                case 5 -> {

                }
                case 6 -> {

                }
                case 0 -> {
                    doesContinue = false;
                }
                default -> {
                    System.out.println("Wrong input!");
                }
            }
        }

    }

}
