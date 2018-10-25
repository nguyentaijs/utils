package utils.cms;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import utils.common.utils.ConsoleIOManager;
import utils.common.utils.FixedValues;

public class CMSInitializeStructure {

	public static void main(String[] args) throws IOException {
		run();
	}

	public static void run() throws IOException {
		ConsoleIOManager.showMessage("CREATE ROOT DIRECTORIES IF NOT EXISTS");
		makeDirectory(FixedValues.CMS.BRANCHES_FOLDER);
		makeDirectory(FixedValues.CMS.RELEASES_FOLDER);
		makeDirectory(FixedValues.CMS.INPUT_FOLDER);
		makeDirectory(FixedValues.CMS.REF_FOLDER);
		makeDirectory(FixedValues.CMS.OTHERS_FOLDER);
	}

	private static void makeDirectory(String path) throws IOException {
		FileUtils.forceMkdir(new File(path));
		System.out.println(path);
	}
}
