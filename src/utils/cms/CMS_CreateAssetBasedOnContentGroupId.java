package utils.cms;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import utils.common.utils.ConsoleIO;
import utils.common.utils.Constants;
import utils.common.utils.DBManager;

public class CMS_CreateAssetBasedOnContentGroupId {
	
	public static void main(String[] args) {
		run();
		ConsoleIO.showMessage("FINISH EXECUTION :)");
		System.exit(0);
	}

	public static void run() {
		Scanner scanner = new Scanner(System.in);
		File fileInput = null;
		boolean isRemoteServer = false;
		int exitValue = Integer.MIN_VALUE;
		String sourcedbName = "";
		try {
			
			// 0.1: Remote server?
			String cloneRemoteServer = ConsoleIO.nextLine("USING REMOTE SERVER?", Constants.Message.YES_INPUT, scanner);

			if ( StringUtils.equalsIgnoreCase("Y", cloneRemoteServer)) {
				isRemoteServer = true;
				ConsoleIO.showMessage(String.format("DEFAULT REMOTE >>> HOST: %s, USER: %s, PASSWORD: %s",
						Constants.REMOTE_HOST, Constants.LOCAL_USER, Constants.REMOTE_PASSWORD));
			} else {
				isRemoteServer = false;
			}

			// 1.1. Read user input target db name
			if (isRemoteServer) {
				exitValue = DBManager.showDB(Constants.REMOTE_HOST, Constants.LOCAL_USER, Constants.REMOTE_PASSWORD);
			} else {
				exitValue = DBManager.showDB(Constants.LOCAL_HOST, Constants.LOCAL_USER, Constants.LOCAL_PASSWORD);
			}
			if (exitValue != 0) {
				return;
			}

			sourcedbName = ConsoleIO.nextLineExpectInput("INPUT SOURCE DATABASE NAME", scanner);
			
			String contentGroupId = ConsoleIO.nextLineExpectInput("INPUT CONTENT GROUP ID", scanner);
			ResultSet filePathResultSet = null;
			if (isRemoteServer) {
				filePathResultSet = getFilePathBasedOnContentGroupId(Constants.REMOTE_HOST, Constants.REMOTE_USER, Constants.REMOTE_PASSWORD,
																		sourcedbName, contentGroupId);
			} else {
				filePathResultSet = getFilePathBasedOnContentGroupId(Constants.LOCAL_HOST, Constants.LOCAL_USER, Constants.LOCAL_PASSWORD,
																		sourcedbName, contentGroupId);
			}
			List<String> filePathList = new ArrayList<>();
			while (filePathResultSet.next()) {
				filePathList.add(filePathResultSet.getString(1));
			}
			
			if (filePathList.size() <= 0) {
				System.out.println("No asset for content group " + contentGroupId);
			}
			
			for (String filePath : filePathList) {
				fileInput = new File(filePath);
				FileUtils.forceMkdirParent(fileInput);
				if (fileInput.createNewFile()) {
					System.out.println("Created file: " + fileInput.getAbsolutePath());
				} else {
					System.out.println("Cannot create file: " + fileInput.getAbsolutePath());
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			scanner.close();
		}
	}
	
	private static ResultSet getFilePathBasedOnContentGroupId(String host, String user, String password, String sourceDbName, String contentGroupId) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(String.format("jdbc:mysql://%s:3306/%s", host, sourceDbName), user, password);
		Statement statement = con.createStatement();
		
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(" SELECT asset.fileName                                                             ");
		queryBuilder.append(" FROM contentgroup AS contentgrp JOIN contentsubset AS subset                      ");
		queryBuilder.append(" ON contentgrp.movieSubsetId = subset.contentSubsetId                              ");
		queryBuilder.append(" OR contentgrp.subtitleSubsetId = subset.contentSubsetId                           ");
		queryBuilder.append(" OR contentgrp.previewSubsetId = subset.contentSubsetId                            ");
		queryBuilder.append(" OR contentgrp.posterSubsetId = subset.contentSubsetId                             ");
		queryBuilder.append(" OR contentgrp.thumbnailSubsetId = subset.contentSubsetId                          ");
		queryBuilder.append(" JOIN contentasset AS asset ON subset.contentSubsetId = asset.contentSubsetId      ");
		queryBuilder.append(" WHERE 1                                                                           ");
		queryBuilder.append(" AND contentgrp.contentGroupId = %s                                                ");
		
		return statement.executeQuery(String.format(queryBuilder.toString(), contentGroupId));
	}
}
