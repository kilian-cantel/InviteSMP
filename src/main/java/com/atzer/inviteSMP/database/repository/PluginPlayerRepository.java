package com.atzer.inviteSMP.database.repository;

import com.zaxxer.hikari.HikariDataSource;

public class PluginPlayerRepository {

    private final HikariDataSource dataSource;

    public PluginPlayerRepository(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }



}
