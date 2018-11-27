package utils.common.code;

import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import utils.common.utils.ConsoleIO;
import utils.common.utils.Constants;
import utils.common.utils.DBManager;
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
				newdbName = ConsoleIO.nextLine("INPUT NEW DB NAME", "* CAUTION: Cannot leave empty", scanner);
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
		
		ConsoleIO.showMessage("FINISH EXECUTION :)");
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
		String sourcedbName = "";
		boolean bCloneRemoteServer = false;
		int exitValue = Integer.MIN_VALUE;
		
		try {
			scanner = new Scanner(System.in);
			
			// 0.1: Clone remote server?
			String cloneRemoteServer = ConsoleIO.nextLine("CLONE REMOTE SERVER?", Constants.Message.YES_INPUT, scanner);
			
			if (StringUtils.equalsIgnoreCase("Y", cloneRemoteServer)) {
				bCloneRemoteServer = true;
				ConsoleIO.showMessage(String.format("DEFAULT REMOTE >>> HOST: %s, USER: %s, PASSWORD: %s",
															Constants.REMOTE_HOST, Constants.LOCAL_USER, Constants.REMOTE_PASSWORD));
			} else {
				bCloneRemoteServer = false;
			}
			
			// 1.1. Read user input target db name
			if (bCloneRemoteServer) {
				exitValue = DBManager.showDB(Constants.REMOTE_HOST, Constants.LOCAL_USER, Constants.REMOTE_PASSWORD);
			} else {
				exitValue = DBManager.showDB(Constants.LOCAL_HOST, Constants.LOCAL_USER, Constants.LOCAL_PASSWORD);
			}
			if (exitValue != 0) {
				return;
			}
			
			sourcedbName = ConsoleIO.nextLineExpectInput("INPUT SOURCE DATABASE NAME", scanner);
			
			// 2. Create DB
			exitValue = DBManager.createDB(Constants.LOCAL_HOST, Constants.LOCAL_USER, Constants.LOCAL_PASSWORD, newdbName);
			if (exitValue != 0) {
				return;
			}
			// 3. Clone DB
			if (bCloneRemoteServer) {
				exitValue = DBManager.cloneDB(Constants.REMOTE_HOST, Constants.REMOTE_USER, Constants.REMOTE_PASSWORD, sourcedbName,
											  Constants.LOCAL_HOST, Constants.LOCAL_USER, Constants.LOCAL_PASSWORD, newdbName);
			} else {
				exitValue = DBManager.cloneDB(Constants.LOCAL_HOST, Constants.LOCAL_USER, Constants.LOCAL_PASSWORD, sourcedbName,
						  Constants.LOCAL_HOST, Constants.LOCAL_USER, Constants.LOCAL_PASSWORD, newdbName);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}
}
