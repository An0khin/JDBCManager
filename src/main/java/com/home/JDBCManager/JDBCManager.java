package com.home.JDBCManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
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
			
			int size = row.size();
			
			for(int i = 0; i < size; i++) {
				request += "\'" + row.get(i) + "\'";
				if(i != size - 1) {
					request += ", ";
				}
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
	
	public boolean deleteRowFromTable(List<String> row, String tableName) {
		try(Connection connection = DriverManager.getConnection(url, login, password)) {
			Statement stat = connection.createStatement();
			
			List<String> columnNames = getColumnsNamesFromTable(tableName);
			
			String request = "DELETE FROM " + tableName + " WHERE ";
			
			int size = columnNames.size();
			
			for(int i = 0; i < size; i++) {
				request += columnNames.get(i) + " = \'" + row.get(i) + "\' ";
				if(i != size - 1) {
					request += "AND ";
				}
			}
			
			request += ";";
			
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
	
	public boolean editRowFromTable(List<String> oldRow, List<String> newRow, String tableName) {
		try(Connection connection = DriverManager.getConnection(url, login, password)) {
			
			Statement stat = connection.createStatement();
			
			List<String> columnNames = getColumnsNamesFromTable(tableName);
			
			String request = "UPDATE " + tableName + " SET ";
			
			int size = columnNames.size();
			
			for(int i = 0; i < size; i++) {
				request += columnNames.get(i) + " = \'" + newRow.get(i) + "\'";
				
				if(i != size - 1) {
					request += ", ";
				}
			}
			
			request += " WHERE ";
			
			for(int i = 0; i < size; i++) {
				request += columnNames.get(i) + " = \'" + oldRow.get(i) + "\'";
				
				if(i != size - 1) {
					request += "AND ";
				}
			}
			
			request += ";";
			
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
	
	public boolean clearTable(String tableName) {
		try(Connection connection = DriverManager.getConnection(url, login, password)) {
			
			Statement stat = connection.createStatement();
			
			String request = "DELETE FROM " + tableName + ";";
			
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
	
	private List<String> getColumnsNamesFromTable(String tableName) {
		List<String> columnNames = new ArrayList<>();
		
		try(Connection connection = DriverManager.getConnection(url, login, password)) {
			Statement stat = connection.createStatement();
						
			String requestForNamesColumns = "SELECT * FROM " + tableName + ";"; //getting columns' names
			ResultSet rs = stat.executeQuery(requestForNamesColumns);
			ResultSetMetaData rsMetaData = rs.getMetaData();
			int countColumns = rsMetaData.getColumnCount();
			
			for(int i = 1; i <= countColumns; i++) { //columns' names to List
				columnNames.add(rsMetaData.getColumnName(i));
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return columnNames;
	}
}
