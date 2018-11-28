package utils.common.code;

import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import utils.common.utils.ConsoleIO;
import utils.common.utils.Constants;
import utils.common.utils.DBManager;

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
		String sourceHost = "";
		String sourceUser = "";
		String sourcePasword = "";
		
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
				sourceHost = Constants.REMOTE_HOST;
				sourceUser = Constants.REMOTE_USER;
				sourcePasword = Constants.REMOTE_PASSWORD;
			} else {
				sourceHost = Constants.LOCAL_HOST;
				sourceUser = Constants.LOCAL_USER;
				sourcePasword = Constants.LOCAL_PASSWORD;
			}
			exitValue = DBManager.showDB(sourceHost, sourceUser, sourcePasword);
			if (exitValue != 0) {
				return;
			}
			
			sourcedbName = ConsoleIO.nextLineExpectInput("INPUT SOURCE DATABASE NAME", scanner);
			
			// 2. Create DB
			// 2.1. Local DB
			exitValue = DBManager.createDB(Constants.LOCAL_HOST, Constants.LOCAL_USER, Constants.LOCAL_PASSWORD, newdbName);
			
			if (exitValue != 0) {
				System.out.println(String.format("[ERROR] Failed to create local DB: %s - %s", Constants.LOCAL_HOST, newdbName));
				return;
			} else {
				System.out.println(String.format("Created local DB: %s - %s", Constants.LOCAL_HOST, newdbName));
			}
			// 2.2. Remote DB
			exitValue = DBManager.createDB(Constants.REMOTE_HOST, Constants.REMOTE_USER, Constants.REMOTE_PASSWORD, newdbName);
			
			if (exitValue != 0) {
				System.out.println(String.format("[ERROR] Failed to create remote DB: %s - %s", Constants.REMOTE_HOST, newdbName));
				return;
			} else {
				System.out.println(String.format("Created remote DB: %s - %s", Constants.REMOTE_HOST, newdbName));
			}
			// 3. Clone DB
			// 3.1. Local DB
			exitValue = DBManager.cloneDB(sourceHost, sourceUser, sourcePasword, sourcedbName,
										Constants.LOCAL_HOST, Constants.LOCAL_USER, Constants.LOCAL_PASSWORD, newdbName);
			
			if (exitValue != 0) {
				System.out.println(String.format("[ERROR] Failed to clone local DB: %s - %s", Constants.LOCAL_HOST, newdbName));
			} else {
				System.out.println(String.format("Clone local DB succesful: %s - %s", Constants.LOCAL_HOST, newdbName));
			}
			// 3.2. Remote DB
			exitValue = DBManager.cloneDB(sourceHost, sourceUser, sourcePasword, sourcedbName,
										Constants.REMOTE_HOST, Constants.REMOTE_USER, Constants.REMOTE_PASSWORD, newdbName);
			
			if (exitValue != 0) {
				System.out.println(String.format("[ERROR] Failed to clone remote DB: %s - %s", Constants.REMOTE_HOST, newdbName));
			} else {
				System.out.println(String.format("Clone remote DB succesful: %s - %s", Constants.REMOTE_HOST, newdbName));;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}
}
