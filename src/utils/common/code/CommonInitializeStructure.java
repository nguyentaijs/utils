package utils.common.code;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import utils.common.utils.ConsoleIOManager;
import utils.common.utils.FixedValues;

public class CommonInitializeStructure {

	public static void main(String[] args) throws IOException {
		run();
	}
	
	public static void run() throws IOException {
		ConsoleIOManager.showMessage("CREATE ROOT DIRECTORIES IF NOT EXISTS");
		makeDirectory(FixedValues.Common.KAKAO_FOLDER);
	}
	
	private static void makeDirectory(String path) throws IOException {
		FileUtils.forceMkdir(new File(path));
		System.out.println(path);
	}
}