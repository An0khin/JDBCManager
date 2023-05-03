package com.home.service;

public interface SqlPreparer {
    String prepareInsertRequest();

    String prepareDeleteRequest();

    String prepareUpdateRequest();
}
