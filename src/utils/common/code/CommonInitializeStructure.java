package utils.common.code;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import utils.common.utils.ConsoleIO;
import utils.common.utils.Constants;

public class CommonInitializeStructure {

	public static void main(String[] args) throws IOException {
		run();
	}
	
	public static void run() throws IOException {
		ConsoleIO.showMessage("CREATE ROOT DIRECTORIES IF NOT EXISTS");
		makeDirectory(Constants.Common.KAKAO_FOLDER);
	}
	
	private static void makeDirectory(String path) throws IOException {
		FileUtils.forceMkdir(new File(path));
		System.out.println(path);
	}
}
