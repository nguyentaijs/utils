package utils.cms;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import utils.common.utils.ConsoleIO;
import utils.common.utils.Constants;

public class CMS_InitializeStructure {

	public static void main(String[] args) throws IOException {
		run();
	}

	public static void run() throws IOException {
		ConsoleIO.showMessage("CREATE ROOT DIRECTORIES IF NOT EXISTS");
		makeDirectory(Constants.CMS.BRANCHES_FOLDER);
		makeDirectory(Constants.CMS.RELEASES_FOLDER);
		makeDirectory(Constants.CMS.INPUT_FOLDER);
		makeDirectory(Constants.CMS.REF_FOLDER);
		makeDirectory(Constants.CMS.OTHERS_FOLDER);
	}

	private static void makeDirectory(String path) throws IOException {
		FileUtils.forceMkdir(new File(path));
		System.out.println(path);
	}
}
