package utils.common.utils;

import java.util.Scanner;

public class ConsoleIOManager {
	public static String nextLine(String titleMessage, String inputMessage, Scanner scanner) {
		System.out.println("===============================================================");
		System.out.println(titleMessage);
		System.out.println("===============================================================");
		System.out.println(inputMessage + " (Waiting for user input ...)");
		return scanner.nextLine();
	}
	
	public static int nextInt(String titleMessage, String inputMessage, Scanner scanner) {
		System.out.println("===============================================================");
		System.out.println(titleMessage);
		System.out.println("===============================================================");
		System.out.println(inputMessage + " (Waiting for user input ...)");
		return scanner.nextInt();
	}
	
	public static String nextLineLoop(String titleMessage, String inputMessage, String reinputMessage, Scanner scanner) {
		System.out.println("===============================================================");
		System.out.println(titleMessage);
		System.out.println("===============================================================");
		
		String outputString = "";
		
		boolean hasInput = false;
		do {
			System.out.println(inputMessage + " (Waiting for user input ...)");
			outputString = scanner.nextLine();
			if (outputString.isEmpty()) {
				System.err.println(reinputMessage + " >>> RE INPUT");
				hasInput = false;
			} else {
				hasInput = true;
			}
		} while (!hasInput);
		return outputString;
	}
	
	public static void showMessage(String message) {
		System.out.println("===============================================================");
		System.out.println(message);
		System.out.println("===============================================================");
	}
}
