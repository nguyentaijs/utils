package utils.bms;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import utils.common.utils.ConsoleIOManager;
import utils.common.utils.FixedValues;

public class BMSInitializeStructure {

	public static void main(String[] args) throws IOException {
		run();
	}
	
	public static void run() throws IOException {
		ConsoleIOManager.showMessage("CREATE ROOT DIRECTORIES IF NOT EXISTS");
		makeDirectory(FixedValues.BMS.BRANCHES_FOLDER);
		makeDirectory(FixedValues.BMS.RELEASES_FOLDER);
		makeDirectory(FixedValues.BMS.INPUT_FOLDER);
		makeDirectory(FixedValues.BMS.REF_FOLDER);
		makeDirectory(FixedValues.BMS.OTHERS_FOLDER);
	}
	
	private static void makeDirectory(String path) throws IOException {
		FileUtils.forceMkdir(new File(path));
		System.out.println(path);
	}
}
