package java;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /*
        === Kitab İdarəetmə Sistemi ===
        1. Bütün kitabları göstər
        2. Müəllifə görə kitab axtar
        3. Mövcud kitabları göstər
        4. Kitab icarəyə ver
        5. Kitabı qaytart
        6. Janr üzrə statistika
        0. Çıxış

Seçiminizi daxil edin:
*/

        while (true) {
            int selection =fromScanner();
            switch (selection) {
                case 0: return;
                case 1: ;
                case 2: ;
                case 3: ;
                case 4:;
                case 5: ;
                case 6: ;
            }
        }
    }

    public static int fromScanner() {
        Scanner scanner = new Scanner(System.in);
        String string = "=== Kitab İdarəetmə Sistemi ===\n" +
                "        1. Bütün kitabları göstər\n" +
                "        2. Müəllifə görə kitab axtar\n" +
                "        3. Mövcud kitabları göstər\n" +
                "        4. Kitab icarəyə ver\n" +
                "        5. Kitabı qaytart\n" +
                "        6. Janr üzrə statistika\n" +
                "        0. Çıxış\n" +
                "\nSeciminizi daxil edin : ";

        return scanner.nextInt();
    }
}
