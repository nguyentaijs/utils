package utils.common.code;

import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import utils.common.utils.ConsoleIOManager;
import utils.common.utils.FixedValues;
import utils.common.utils.ProcessManager;

public class CloneDB {
	
	public static void main(String[] args) {
		Scanner scanner = null;
		String newdbName = StringUtils.EMPTY;
		boolean hasInput = true;
		
		try {
			// 1. Get new database name
			scanner = new Scanner(System.in);
			do {
				newdbName = ConsoleIOManager.nextLine("INPUT NEW DB NAME", "* CAUTION: Cannot leave empty", scanner);
				if (newdbName.isEmpty()) {
					System.err.println("EMPTY DB NAME!");
					hasInput = false;
				} else {
					hasInput = true;
				}
			} while(!hasInput);
			
			// 2. Clone db
			run(newdbName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		
		ConsoleIOManager.showMessage("FINISH EXECUTION :)");
		System.exit(0);
	}
	
	/**
	 * Input: <br/>
	 *  > Clone remote server? <br/>
	 *  > Source database name <br/>
	 * Output: clone source db > @newdbName <br/>
	 * @param newdbName new database name
	 * @throws IOException
	 */
	public static void run(String newdbName) throws IOException {
		Scanner scanner = null;
		String targetDBName = "";
		boolean bCloneRemoteServer = false;
		int exitValue = Integer.MIN_VALUE;
		String command = StringUtils.EMPTY;
		
		try {
			scanner = new Scanner(System.in);
			
			// 0.1: Clone remote server?
			String cloneRemoteServer = ConsoleIOManager.nextLine("CLONE REMOTE SERVER?", FixedValues.Message.YES_INPUT, scanner);
			
			if (!cloneRemoteServer.isEmpty() && StringUtils.equalsIgnoreCase("Y", cloneRemoteServer)) {
				bCloneRemoteServer = true;
				System.out.println(">>> YES");
				ConsoleIOManager.showMessage(String.format("DEFAULT REMOTE >>> HOST: %s, USER: %s, PASSWORD: %s",
															FixedValues.REMOTE_HOST, FixedValues.LOCAL_USER, FixedValues.REMOTE_PASSWORD));
			} else {
				bCloneRemoteServer = false;
				System.out.println(">>> NO");
			}
			
			// 1.1. Read user input target db name
			if (bCloneRemoteServer) {
				command = String.format("cmd.exe /c mysql -h %s -u %s -p%s -e \"SHOW DATABASES\"",
										FixedValues.REMOTE_HOST, FixedValues.LOCAL_USER, FixedValues.REMOTE_PASSWORD);
			} else {
				command = String.format("cmd.exe /c mysql -h %s -u %s -p%s -e \"SHOW DATABASES\"",
						FixedValues.LOCAL_HOST, FixedValues.LOCAL_USER, FixedValues.LOCAL_PASSWORD);
			}
			exitValue = ProcessManager.processCommand("DB LIST (PICK ONE)", command, System.out);
			if (exitValue != 0) {
				return;
			}
			
			targetDBName = ConsoleIOManager.nextLine("INPUT SOURCE DATABASE NAME", "* CAUTION: CANNOT LEAVE EMPTY", scanner);
			if (targetDBName.isEmpty()) {
				System.err.println("No TARGET DB NAME !!!");
				return;
			}
			// 2. Create DB
			command = String.format("cmd.exe /c mysql -u %s -p%s -e \"CREATE DATABASE %s /*!40100 COLLATE 'utf8_general_ci' */\"",
									FixedValues.LOCAL_USER, FixedValues.LOCAL_PASSWORD, newdbName);
			exitValue = ProcessManager.processCommand("CREATE DB", command, null);
			if (exitValue != 0) {
				return;
			}
			// 3. Clone DB
			if (bCloneRemoteServer) {
				command = String.format("cmd.exe /c mysqldump -h %s -u %s -p%s %s | mysql -u %s -p%s %s",
										FixedValues.REMOTE_HOST, FixedValues.REMOTE_USER, FixedValues.REMOTE_PASSWORD, targetDBName,
										FixedValues.LOCAL_USER, FixedValues.LOCAL_PASSWORD, newdbName);
			} else {
				command = String.format("cmd.exe /c mysqldump -h %s -u %s -p%s %s | mysql -u %s -p%s %s",
										FixedValues.LOCAL_HOST, FixedValues.LOCAL_USER, FixedValues.LOCAL_PASSWORD, targetDBName,
										FixedValues.LOCAL_USER, FixedValues.LOCAL_PASSWORD, newdbName);
			}
			exitValue = ProcessManager.processCommand(String.format("CLONE %s >>> %s", targetDBName, newdbName), command, System.out);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}
}
