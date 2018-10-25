package utils.common.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class ProcessManager {
	
	/**
	 * Chạy command dựa vào @param command
	 * <br/>
	 * Nếu để @param os = null >>> Không xuất ra output
	 * <br/>
	 * Nếu để @param os = FileOutputStream >>> Xuất ra file
	 * <br/>
	 * Nếu để @param os = System.out >>> Xuất ra màn hình console
	 * <br/>
	 * @param commandName
	 * @param command
	 * @param os
	 * @return exit value của @param command
	 */
	public static int processCommand(String commandName, String command, OutputStream os) {
		ConsoleIOManager.showMessage(commandName);
		int exitValue = runCommand(command, os);
		if (exitValue != 0) {
			System.err.println(String.format("%s FAILED !!! EXITVALUE = %d", commandName, exitValue));			
		}
		return exitValue;
	}
	
	private static int runCommand(String command, OutputStream os) {
		int exitVal = Integer.MIN_VALUE;
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(command);
			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
			errorGobbler.start();

			if (os != null) {
				// any output?
				StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT", os);
				outputGobbler.start();
			}
			System.out.println("Processing ...");
			// any error???
			exitVal = proc.waitFor();
		} catch (Exception t) {
			t.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.flush();
					if (!(os instanceof PrintStream)) {
						os.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return exitVal;
	}
}
