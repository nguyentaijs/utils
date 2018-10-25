package utils.common.code;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import utils.common.utils.FileManager;

public class OneTimeUsedMain {

	private static final String incorrect = "system.baseMultiRepositoryPath=C:/NAS_INGEST01/cms/repository,E:/NAS_INGEST02/cms/repository";
	private static final String correct = "system.baseMultiRepositoryPath=C:/NAS_INGEST01,E:/NAS_INGEST02";
	private static final String parrentPath = "E:\\Tool\\tomcat7\\conf\\cms-config";

	public static void main(String[] args) {
		modifyAllConfigProperties();
	}

	public static void modifyAllConfigProperties() {
		// 1. find all properties files
		File parrentFile = new File(parrentPath);

		Collection<File> fileCol = FileUtils.listFiles(parrentFile, new IOFileFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return false;
			}

			@Override
			public boolean accept(File file) {
				return file.getName().endsWith("config.properties");
			}
		}, TrueFileFilter.INSTANCE);

		List<File> resultFiles = new ArrayList<>(fileCol);
		
		Iterator<File> itr = fileCol.iterator();
		while(itr.hasNext()) {
			System.out.println(itr.next().getAbsolutePath());
		}
		
		// 2. Modify
		for (File file : resultFiles) {
			System.out.println("===================================================");
			System.out.println(file.getPath());
			FileManager.modifyFile(file.getPath(), file.getPath(), incorrect, correct);
		}
	}

}
