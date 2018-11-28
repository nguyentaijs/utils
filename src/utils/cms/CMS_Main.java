package utils.cms;

import java.util.Scanner;

import utils.common.utils.ConsoleIO;

public class CMS_Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String commandInput = "";
		try {
			String[] commands = new String[] {
					"0. Exit",
					"1. Initialize Structure",
					"2. Create new branch",
					"3. Create asset based on Content Group Id",
					"4. Create asset by user input PATH & File Name"
			};
			
			showMenu(commands);
			commandInput = ConsoleIO.nextLineExpectInput("SELECT COMMAND | OTHERS = EXIT", scanner);
			
			switch (commandInput) {
			case "0":
				break;
			case "1":
				CMS_InitializeStructure.run();
				break;
			case "2":
				CMS_NewBranch.run();
				break;
			case "3":
				CMS_CreateAssetBasedOnContentGroupId.run();
				break;
			case "4":
				CMS_CreateAssets.run();
				break;
			default:
				break;
			}
			
			
			ConsoleIO.showMessage("FINISH EXECUTION :)");
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		
	}
	
	private static void showMenu(String[] commands) {
		ConsoleIO.showMessage("MENU");
		for (int i = 0; i < commands.length; i++) {
			System.out.println(commands[i]);
		}
	}
}
