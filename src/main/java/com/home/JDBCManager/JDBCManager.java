package com.home.JDBCManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

public class JDBCManager {
	
	private String login;
	private String password;
	private String url;
	
	public JDBCManager(String login, String password, String url) {
		super();
		this.login = login;
		this.password = password;
		this.url = url;
	}

	public boolean insertRowToTable(List<String> row, String tableName) {
		try(Connection connection = DriverManager.getConnection(url, login, password)) {
			Statement stat = connection.createStatement();
			
			String request = "insert into " + tableName + " values(";
			
			for(String field : row) {
				request += " \'" + field + "\'";
			}
			
			request += ");";
			
			int result = stat.executeUpdate(request);
			
			if(result > 0) {
				return true;
			} else {
				return false;
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
