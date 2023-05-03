package com.home.service;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementPreparer {
    PreparedStatement prepareGetAllStatement() throws SQLException;

    PreparedStatement prepareInsertStatement() throws SQLException;

    PreparedStatement prepareDeleteStatement() throws SQLException;

    PreparedStatement prepareUpdateStatement() throws SQLException;

    PreparedStatement prepareClearStatement() throws SQLException;

    PreparedStatement prepareGetColumnsNamesStatement() throws SQLException;
}
