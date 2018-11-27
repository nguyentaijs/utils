package utils.common.utils;

import java.util.Scanner;

public class ConsoleIO {
	public static String nextLine(String titleMessage, String inputMessage, Scanner scanner) {
		System.out.println("===============================================================");
		System.out.println(String.format("%s %s (Waiting for user input ...)", titleMessage, inputMessage));
		System.out.println("===============================================================");
		return scanner.nextLine();
	}
	
	public static int nextInt(String titleMessage, String inputMessage, Scanner scanner) {
		System.out.println("===============================================================");
		System.out.println(titleMessage);
		System.out.println("===============================================================");
		System.out.println(inputMessage + " (Waiting for user input ...)");
		return scanner.nextInt();
	}
	
	public static String nextLineExpectInput(String titleMessage, Scanner scanner) {
		System.out.println("===============================================================");
		System.out.println(titleMessage + " (Waiting for user input ...)");
		System.out.println("===============================================================");
		
		String outputString = "";
		
		boolean hasInput = false;
		do {
			outputString = scanner.nextLine();
			if (outputString.isEmpty()) {
				System.out.println("NO INPUT VALUE >>> RE INPUT (Waiting for user input ...)");
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
