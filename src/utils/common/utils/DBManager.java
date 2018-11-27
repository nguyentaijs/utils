package utils.common.utils;

public class DBManager {

	public static int showDB(String host, String user, String password) {
		String command = String.format("cmd.exe /c mysql -h %s -u %s -p%s -e \"SHOW DATABASES\"",
				host, user, password);
		return ProcessManager.processCommand("Database List", command, System.out);
	}
	
	public static int createDB(String host, String user, String password, String newdbName) {
		String command = String.format("cmd.exe /c mysql -h %s -u %s -p%s -e \"CREATE DATABASE %s /*!40100 COLLATE 'utf8_general_ci' */\"",
				host, user, password, newdbName);
		return ProcessManager.processCommand("Create DB", command, null);
	}
	
	public static int cloneDB(String sourceHost, String sourceUser, String sourcePassword, String sourceDB,
							  String targetHost, String targetUser, String targetPassword, String targetDB) {
		String command = String.format("cmd.exe /c mysqldump -h %s -u %s -p%s %s | mysql -h %s -u %s -p%s %s",
							sourceHost, sourceUser, sourcePassword, sourceDB,
							targetHost, targetUser, targetPassword, targetDB);
		return ProcessManager.processCommand(String.format("CLONE %s >>> %s", sourceDB, targetDB), command, System.out);
	}
}
