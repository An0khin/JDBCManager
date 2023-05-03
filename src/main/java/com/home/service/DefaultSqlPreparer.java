package com.home.service;

import java.util.List;

public class DefaultSqlPreparer implements SqlPreparer {
    private final List<String> fieldsNames;
    private final String tableName;

    public DefaultSqlPreparer(final List<String> fieldsNames, final String tableName) {
        this.fieldsNames = fieldsNames;
        this.tableName = tableName;
    }

    @Override
    public String prepareInsertRequest() {
        StringBuilder requestBuilder = new StringBuilder(String.format("INSERT INTO %s VALUES(", tableName));

        requestBuilder.append("?,".repeat(fieldsNames.size()));

        requestBuilder.deleteCharAt(requestBuilder.length() - 1);
        requestBuilder.append(");");

        return requestBuilder.toString();
    }

    @Override
    public String prepareDeleteRequest() {
        return String.format("DELETE FROM %s ", tableName) + buildWhereSequence(fieldsNames) + ";";
    }

    @Override
    public String prepareUpdateRequest() {
        return String.format("UPDATE %s ", tableName) + buildSetSequence(fieldsNames) +
                " " +
                buildWhereSequence(fieldsNames) +
                ";";
    }

    private String buildWhereSequence(List<String> fields) {
        StringBuilder whereSequenceBuilder = new StringBuilder("WHERE ");

        int size = fields.size();

        for(int i = 0; i < size; i++) {
            whereSequenceBuilder.append(String.format("%s = ? ", fields.get(i)));
            if(i != size - 1) {
                whereSequenceBuilder.append("AND ");
            }
        }

        return whereSequenceBuilder.toString();
    }

    private String buildSetSequence(List<String> fields) {
        StringBuilder setSequenceBuilder = new StringBuilder("SET ");

        int size = fields.size();

        for(int i = 0; i < size; i++) {
            setSequenceBuilder.append(String.format("%s = ?", fields.get(i)));
            if(i != size - 1) {
                setSequenceBuilder.append(", ");
            }
        }

        return setSequenceBuilder.toString();
    }
}
