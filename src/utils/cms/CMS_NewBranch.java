package utils.cms;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import utils.common.code.CloneDB;
import utils.common.utils.ConsoleIO;
import utils.common.utils.FileManager;
import utils.common.utils.Constants;

public class CMS_NewBranch {
	
	private static final String REFERENCES_DIR = "01_References";
	private static final String DB_SCRIPT = "02_DBScript";
	private static final String CHATLOG = "03_Chatlog";
	private static final String TEST_INPUT = "04_TestInput";
	private static final String NOTE_TXT_PREFIX = "Note";

	private static final String JDBC_FILE = "jdbc.properties";
	private static final String JDBC_AUTO_GEN_FILE = "jdbc_autogen.properties";
	private static final String JDBC_TEMPLATE = "jdbc_template.properties";
	private static final String JDBC_TEMPLATE_DBNAME = "_databaseName";
	private static final String NOTE_TEMPLATE = "note_template.txt";
	private static final String NOTE_TEMPLATE_BRANCHNAME = "_branchName";
	private static final String NOTE_TEMPLATE_DATECREATED = "_dateCreated";
	
	public static void main(String[] args) {
		run();
		ConsoleIO.showMessage("FINISH EXECUTION :)");
		System.exit(0);
	}
	
	public static void run() {
		Scanner scanner = null;
		String inputBranchName = "";
		try {
			scanner = new Scanner(System.in);
			// 1. Initialize Structure
			CMS_InitializeStructure.run();
			
			boolean hasInput = false;
			do {
				inputBranchName = ConsoleIO.nextLine("CREATE BRANCH DIRECTORIES AND SUB DIRECTORIES", "INPUT NEW BRANCH NAME", scanner);
				if (inputBranchName.isEmpty()) {
					System.err.println("NO BRANCH NAME >>> RE INPUT");
					hasInput = false;
				} else {
					hasInput = true;
				}
			} while (!hasInput);
			// 2. Create Directories
			// 2.1. Check issue folder items
			File issueDirectory = new File(Constants.CMS.BRANCHES_FOLDER);
			File[] subDirectories = issueDirectory.listFiles(new FileFilter() {
					@Override
					public boolean accept(File pathname) {
						return pathname.isDirectory();
					}
			});
			// 2.2. Create branch folder
			ConsoleIO.showMessage("CREATE BRANCH DIRECTORIES AND SUB DIRECTORIES");
			int maxIndex = FileManager.getMaxIndex(subDirectories);
			String fullBranchName = FileManager.setDirectoryName(maxIndex, inputBranchName);
			// E:\Document\03_Branches\000_branchName
			String fullBranchPath = String.format("%s%s%s", Constants.CMS.BRANCHES_FOLDER, File.separator, fullBranchName);
			System.out.println(String.format("NEW BRANCH NAME: %s", fullBranchName));
			
			// E:\Document\03_Branches\000_branchName\01_References
			createSubDirectory(fullBranchPath, REFERENCES_DIR);
			// E:\Document\03_Branches\000_branchName\02_DBScript
			createSubDirectory(fullBranchPath, DB_SCRIPT);
			// E:\Document\03_Branches\000_branchName\03_Chatlog
			createSubDirectory(fullBranchPath, CHATLOG);
			// E:\Document\03_Branches\000_branchName\04_TestInput
			createSubDirectory(fullBranchPath, TEST_INPUT);
			// E:\Tool\tomcat7\conf\cms-config\000_branchName
			String configDirectory = createSubDirectory(Constants.CMS.CONFIG_FOLDER, fullBranchName);
			
			// E:\Document\03_Branches\000_branchName\Note_000_branchName.txt
			createNoteFile(fullBranchPath, fullBranchName);
			
			// 3. Clone DB?
			inputBranchName = ConsoleIO.nextLine("CLONE DATABASE?", Constants.Message.NO_INPUT, scanner);
			boolean bCloneDB = true;
			if (!inputBranchName.isEmpty() && StringUtils.equalsIgnoreCase("N", inputBranchName)) {
				bCloneDB = false;
				System.out.println(">>> NO");
			} else {
				bCloneDB = true;
				System.out.println(">>> YES");
			}
			// 4. Clone DB
			if (bCloneDB) {
				CloneDB.run(fullBranchName);
			}
			
			// 5.Create configuration file
			// E:\Tool\tomcat7\conf\cms-config\000_branchName\jdbc.properties
			createConfigurationFile(configDirectory, fullBranchName);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}
	
	private static String createSubDirectory(String path, String directoryName) {
		String fullPath = String.format("%s%s%s", path, File.separator, directoryName);
		try {
			FileUtils.forceMkdir(new File(fullPath));
			System.out.println(String.format("Create subdirectory: %s", fullPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fullPath;
	}
	
	private static String createConfigurationFile(String configDirectory, String dbName) {
		// 1. Check if this configuration file is exists. If yes >>> Create jdbc_autogen.properties
		String destPath = String.format("%s%s%s", configDirectory, File.separator, JDBC_FILE);
		File file = new File(destPath);
		if (file.exists() && !file.isDirectory()) {
			System.out.println(String.format("CONFIGFILE ALREADY EXISTS: %s >>> CREATE jdbc_autogen.properties", destPath));
			destPath = String.format("%s%s%s", configDirectory, File.separator, JDBC_AUTO_GEN_FILE);
		}
		
		String sourcePath = CMS_NewBranch.class.getClassLoader().getResource(JDBC_TEMPLATE).getPath();
		FileManager.modifyFile(sourcePath, destPath, JDBC_TEMPLATE_DBNAME, dbName);
		System.out.println(String.format("Create JDBC configuration file: %s", destPath));
		return destPath;
	}
	
	private static String createNoteFile(String fullBranchPath, String fullBranchName) {
		String notePath = String.format("%s%s%s_%s.txt", fullBranchPath, File.separator, NOTE_TXT_PREFIX, fullBranchName);
		String sourcePath = CMS_NewBranch.class.getClassLoader().getResource(NOTE_TEMPLATE).getPath();
		Date currentDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		String currentDateString = sdf.format(currentDate);
		
		Map<String, String> mapReplacement = new HashMap<>();
		mapReplacement.put(NOTE_TEMPLATE_BRANCHNAME, fullBranchName);
		mapReplacement.put(NOTE_TEMPLATE_DATECREATED, currentDateString);
		
		FileManager.modifyFile(sourcePath, notePath, mapReplacement);
		System.out.println(notePath);
		return notePath;
	}
}
