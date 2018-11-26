package utils.cms;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import utils.common.utils.ConsoleIOManager;

public class CMSCreateAssets {

	public static void main(String[] args) throws IOException {
		run();
		ConsoleIOManager.showMessage("FINISH EXECUTION :)");
		System.exit(0);
	}
	
	/**
	 * [Tạo phisical content asset] <br/>
	 * 1. Nhập đường dẫn repository <br/>
	 * 2. Lặp lại nhiều lần | Nhập tên file để tạo <br/>
	 * @throws IOException
	 */
	public static void run() throws IOException {
		Scanner scanner = new Scanner(System.in);
		String path = ConsoleIOManager.nextLine("Input Path", StringUtils.EMPTY, scanner);
		
		boolean stopInput = false;
		File fileInput = null;
		String fileNameStr = StringUtils.EMPTY;
		do {
			scanner = new Scanner(System.in);
			fileNameStr = ConsoleIOManager.nextLine("Input file name", "Leave empty = end", scanner);
			if (fileNameStr.isEmpty()) {
				stopInput = true;
			} else {
				int assetStartId = Integer.parseInt(StringUtils.substringBefore(fileNameStr, "_"));
				fileInput = new File(path + File.separator + assetStartId + File.separator + fileNameStr);
				FileUtils.forceMkdirParent(fileInput);
				if (fileInput.createNewFile()) {
					System.out.println("Created file: " + fileInput.getAbsolutePath());
				} else {
					System.out.println("Cannot create file: " + fileInput.getAbsolutePath());
				}
				if (fileInput.getName().endsWith(".ts")) {
					String m3u8FileName = StringUtils.substringBefore(fileNameStr, "_") + "_4.m3u8";
					fileInput = new File(path + File.separator + assetStartId + File.separator + m3u8FileName);
					FileUtils.forceMkdirParent(fileInput);
					if (fileInput.createNewFile()) {
						System.out.println("Created file: " + fileInput.getAbsolutePath());
					} else {
						System.out.println("Cannot create file: " + fileInput.getAbsolutePath());
					}
				}
			}
		} while (!stopInput);
	}
}
