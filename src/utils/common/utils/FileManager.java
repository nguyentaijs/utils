package utils.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class FileManager {
	
	
	/**
	 * Thư mục có dạng: 000_[tên thư mục]
	 * <br/>
	 * VD: 001_cms4k >>> 1 + 1 = 2
	 * <br/>
	 * Hàm này sẽ đọc danh sách thư mục trong @param directories
	 * @param directories Thư mục cha dùng để quét danh sách
	 * @return Index tối đa + 1
	 */
	public static int getMaxIndex(File[] directories) {
		int maxIndex = -1;
		for (int i = 0; i < directories.length; i++) {
			int index = NumberUtils.toInt(StringUtils.split(directories[i].getName(), "_")[0], -1);
			if (maxIndex < index) {
				maxIndex = index;
			}
		}
		return maxIndex + 1;
	}

	
	/**
	 * Tạo ra Folder Name có dạng: [000]_[tên thư mục]
	 * <br/>
	 * VD: 001_cms4k
	 * @param maxIndex
	 * @param directoryName
	 * @return [000]_[tên thư mục]
	 */
	public static String setDirectoryName(int maxIndex, String directoryName) {
		return String.format("%03d_%s", maxIndex, directoryName);
	}
	
	public static void modifyFile(String sourcePath, String destinationPath, String oldString, String newString) {
		File sourceFile = new File(sourcePath);
		File destinationFile = new File(destinationPath);

		String oldContent = StringUtils.EMPTY;
		BufferedReader reader = null;
		FileWriter writer = null;

		try {
			reader = new BufferedReader(new FileReader(sourceFile));

			// Reading all the lines of input text file into oldContent
			String line = reader.readLine();
			while (line != null) {
				oldContent = oldContent + line + System.lineSeparator();
				line = reader.readLine();
			}

			// Replacing oldString with newString in the oldContent
			String newContent = oldContent.replaceAll(oldString, newString);
			System.out.println(newContent);
			// Rewriting the input text file with newContent
			writer = new FileWriter(destinationFile);
			writer.write(newContent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// Closing the resources
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void modifyFile(String sourcePath, String destinationPath, Map<String, String> mapReplacement) {
		File sourceFile = new File(sourcePath);
		File destinationFile = new File(destinationPath);

		String oldContent = StringUtils.EMPTY;
		BufferedReader reader = null;
		FileWriter writer = null;

		try {
			reader = new BufferedReader(new FileReader(sourceFile));

			// Reading all the lines of input text file into oldContent
			String line = reader.readLine();
			while (line != null) {
				oldContent = oldContent + line + System.lineSeparator();
				line = reader.readLine();
			}

			// Replacing oldString with newString in the oldContent
			String newContent = oldContent;
			for (String key : mapReplacement.keySet()) {				
				newContent = newContent.replaceAll(key, mapReplacement.get(key));
			}
			System.out.println(newContent);
			// Rewriting the input text file with newContent
			writer = new FileWriter(destinationFile);
			writer.write(newContent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// Closing the resources
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
