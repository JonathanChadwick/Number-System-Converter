package NumberConverter;

import java.util.Scanner;


public class Main {
    static Scanner sc = new Scanner(System.in);
    static boolean finished = false;

    public static void main(String[] args) {
        //First level of menu
        do {
            System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit)");
            if (sc.hasNextInt()) {
                runConverter();
            } else {
                if ("/exit".equals(sc.nextLine())) {
                    finished = true;
                }
            }
        } while (!finished);
    }

    public static void runConverter() {
        //Grab user input for source and target bases.
        int sourceBase = sc.nextInt();
        int targetBase = sc.nextInt();
        sc.nextLine(); // Spacer to move the Scanner forward
        Converter.setBases(sourceBase, targetBase); //Set base attributes in the Convert class
        boolean convertLoop = true;
        while (convertLoop) {
            System.out.printf("Enter number in base %d to convert to base %d (To go back type /back)", sourceBase, targetBase);
            String sourceNumber = sc.nextLine();
            if ("/back".equals(sourceNumber)) {
                System.out.println();
                return;
            }
            //Run converter
            String convertedNumber = Converter.run(sourceNumber);
            System.out.printf("Conversion result: %s%n%n", convertedNumber);
        }
    }
}
