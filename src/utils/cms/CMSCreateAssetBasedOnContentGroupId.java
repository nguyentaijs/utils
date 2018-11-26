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

import utils.common.utils.ConsoleIOManager;
import utils.common.utils.Constants;

public class CMSCreateAssetBasedOnContentGroupId {
	
	private static final String DB_NAME = "cms_1_12_testbed_subtitle";

	public static void main(String[] args) {
		run();
		ConsoleIOManager.showMessage("FINISH EXECUTION :)");
		System.exit(0);
	}

	private static void run() {
		Scanner scanner = new Scanner(System.in);
		File fileInput = null;
		try {
			String contentGroupId = ConsoleIOManager.nextLine("INPUT CONTENT GROUP ID", "DO NOT LEAVE EMPTY", scanner); 
			ResultSet filePathResultSet = getFilePathBasedOnContentGroupId(contentGroupId);
			List<String> filePathList = new ArrayList<>();
			while (filePathResultSet.next()) {
				filePathList.add(filePathResultSet.getString(1));
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
	
	private static ResultSet getFilePathBasedOnContentGroupId(String contentGroupId) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB_NAME, Constants.LOCAL_USER, Constants.LOCAL_PASSWORD);
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
