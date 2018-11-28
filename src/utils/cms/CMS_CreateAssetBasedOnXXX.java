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

public class CMS_CreateAssetBasedOnXXX {
	
	private static String host, user, password, sourceDbName;

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
		String contentGroupId = "";
		String type ="";
		String key = "";
		
		
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
				host = Constants.REMOTE_HOST;
				user = Constants.REMOTE_USER;
				password = Constants.REMOTE_PASSWORD;
			} else {
				host = Constants.LOCAL_HOST;
				user = Constants.LOCAL_USER;
				password = Constants.LOCAL_PASSWORD;
			}
			exitValue = DBManager.showDB(host, user, password);
			if (exitValue != 0) {
				return;
			}

			sourceDbName = ConsoleIO.nextLineExpectInput("INPUT SOURCE DATABASE NAME", scanner);
			
			showKeyMenu();
			type = ConsoleIO.nextLineExpectInput("Select type | Default = Content group ID", scanner);
			switch (type) {
			case "1":
				key = ConsoleIO.nextLineExpectInput("Input Content group Id", scanner);
				contentGroupId = key;
				break;
			case "2":
				key = ConsoleIO.nextLineExpectInput("Input Offer Id", scanner);
				ResultSet contentGroupIds = getContentGrpIdByOfferId(key);
				if(contentGroupIds.next()) {
					contentGroupId = contentGroupIds.getString(1);
				}
				break;
			default:
				key = ConsoleIO.nextLineExpectInput("Input Content group Id", scanner);
				contentGroupId = key;
				break;
			}
			
			ResultSet filePathResultSet = getFilePathBasedOnContentGroupId(contentGroupId);
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
				if (fileInput.exists()) {
					System.out.println("File already exists: " + fileInput.getAbsolutePath());
				} else {
					if (fileInput.createNewFile()) {
						System.out.println("Created file: " + fileInput.getAbsolutePath());
					} else {
						System.out.println("Cannot create file: " + fileInput.getAbsolutePath());
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			scanner.close();
		}
	}

	private static void showKeyMenu() {
		String[] keys = new String[] {
				"1. Content group Id",
				"2. OfferId"
		};
		ConsoleIO.showMessage("MENU");
		for (int i = 0; i < keys.length; i++) {
			System.out.println(keys[i]);
		}
	}

	private static ResultSet getFilePathBasedOnContentGroupId(String contentGroupId) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(String.format("jdbc:mysql://%s:3306/%s", host, sourceDbName), user, password);
		Statement statement = con.createStatement();
		
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(" SELECT asset.fileName                                                                    ");
		queryBuilder.append(" FROM contentgroup AS contentgrp JOIN contentsubset AS subset                             ");
		queryBuilder.append(" ON contentgrp.movieSubsetId = subset.contentSubsetId                                     ");
		queryBuilder.append(" OR contentgrp.subtitleSubsetId = subset.contentSubsetId                                  ");
		queryBuilder.append(" OR contentgrp.previewSubsetId = subset.contentSubsetId                                   ");
		queryBuilder.append(" OR contentgrp.posterSubsetId = subset.contentSubsetId                                    ");
		queryBuilder.append(" OR contentgrp.thumbnailSubsetId = subset.contentSubsetId                                 ");
		queryBuilder.append(" JOIN contentasset AS asset ON subset.contentSubsetId = asset.contentSubsetId             ");
		queryBuilder.append(" WHERE 1                                                                                  ");
		queryBuilder.append(" AND contentgrp.contentGroupId = %s                                                       ");
		queryBuilder.append(" UNION                                                                                    ");
		queryBuilder.append(" SELECT asset.fileName                                                                    ");
		queryBuilder.append(" FROM contentgroup AS contentgrp JOIN postergroup AS postergrp                            ");
		queryBuilder.append(" ON contentgrp.contentGroupId = postergrp.contentGroupId                                  ");
		queryBuilder.append(" JOIN contentsubset AS subset ON subset.contentSubsetId = postergrp.posterSubsetId        ");
		queryBuilder.append(" JOIN contentasset AS asset ON subset.contentSubsetId = asset.contentSubsetId             ");
		queryBuilder.append(" WHERE 1                                                                                  ");
		queryBuilder.append(" AND contentgrp.contentGroupId = %s                                                       ");
		
		return statement.executeQuery(String.format(queryBuilder.toString(), contentGroupId, contentGroupId));
	}
	
	private static ResultSet getContentGrpIdByOfferId(String offerId) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(String.format("jdbc:mysql://%s:3306/%s", host, sourceDbName), user, password);
		Statement statement = con.createStatement();
		
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(" SELECT cg.contentGroupId                                             ");
		queryBuilder.append(" FROM offer o JOIN offercontentgroup ocg ON o.offerId = ocg.offerId   ");
		queryBuilder.append(" JOIN contentgroup cg ON ocg.contentGroupId = cg.contentGroupId       ");
		queryBuilder.append(" WHERE o.offerId = %s                                                 ");
		
		return statement.executeQuery(String.format(queryBuilder.toString(), offerId));
	}
}
