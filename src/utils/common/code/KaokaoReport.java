package utils.common.code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

import utils.common.utils.ConsoleIOManager;
import utils.common.utils.Constants;

public class KaokaoReport {

	private static final String DATE_FORMAT = "EEEE, MMMM d, yyyy";
	private static final String START_PATTERN = "^\\[tài\\] \\[.*\\].* finish working.*$";
	private static final String END_PATTERN = "^\\[.*\\] \\[.*\\].*$";
	
	public static void main(String[] args) throws IOException {
		run();
	}
	
	/**
	 * Đọc từ file log của Kaokao talk > lọc ra những line Finish working
	 * <br/>
	 * Input: 	> Tên log file
	 * <br/>
	 * 			> Tháng cần lọc dữ liệu
	 * @throws IOException
	 * <br/>
	 * Output: 	> Thông tin Finish working của Tài trong tháng đó
	 */
	public static void run() throws IOException {
		Scanner scanner = new Scanner(System.in);
		BufferedReader reader = null;
		
		try {
			// 0. Show list files
			ConsoleIOManager.showMessage("LIST LOG FILES");
			File[] listLogFiles =  new File(Constants.Common.KAKAO_FOLDER).listFiles();
			for (int i = 0; i < listLogFiles.length; i++) {
				if (!listLogFiles[i].isDirectory()) {
					System.out.println(listLogFiles[i].getName());
				}
			}
			String selectedLogFile = ConsoleIOManager.nextLineLoop("SELECT LOG FILES", "Pick one", "NO FILE SELECTED", scanner);
			
			// 1. Xac dinh thoi diem bat dau, thoi diem ket thuc
			int targetMonth = ConsoleIOManager.nextInt("SELECT MONTH", "Input month that you want to export data", scanner);
			
			// 2. Doc file
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(Constants.Common.KAKAO_FOLDER + File.separator + selectedLogFile)));
			String logLine = "";
			GregorianCalendar startCal = new GregorianCalendar(2018, targetMonth - 2, 23);
			Date startDate = startCal.getTime();
			GregorianCalendar endCal = new GregorianCalendar(2018, targetMonth - 1, 25);
			Date endDate = endCal.getTime();
			Date currentDate = new Date();
			boolean recordFlag = false;
			
			while((logLine = reader.readLine()) != null) {
				// Check ngay thang
				currentDate = getDateFromLine(logLine) == null ? currentDate : getDateFromLine(logLine);
				if (currentDate.after(startDate)
						&& currentDate.before(endDate)) {
					
					if (logLine.toLowerCase().matches(START_PATTERN)) {
						recordFlag = true;
					}
					
					if (recordFlag) {
						if (!logLine.toLowerCase().matches(START_PATTERN)
								&& (logLine.toLowerCase().matches(END_PATTERN) || getDateFromLine(logLine) != null)) {
							recordFlag = false;
						} else {
							System.out.println(logLine);
						}
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			scanner.close();
		}
	}
	
	private static Date getDateFromLine(String line) {
		Date resultDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String dateString = line.replaceAll("-", "").trim();
		
		try {
			resultDate = sdf.parse(dateString);
		} catch (ParseException e) {
			resultDate = null;
		}
		
		return resultDate;
	}
}
