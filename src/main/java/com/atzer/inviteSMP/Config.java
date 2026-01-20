package com.atzer.inviteSMP;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public enum Config {

    DATABASE_HOST("database.host", "localhost"),
    DATABASE_PORT("database.port", 3306),
    DATABASE_NAME("database.name", "database_name"),
    DATABASE_USER("database.user", "database_user"),
    DATABASE_PASSWORD("database.password", "database_password"),
    DATABASE_POOL_MAX_POOL_SIZE("database.pool.max_pool_size", 10),
    DATABASE_POOL_MINIMUM_IDLE("database.pool.minimum_idle", 2),
    DATABASE_POOL_MAX_LIFETIME("database.pool.max_lifetime", 1800000),
    DATABASE_POOL_CONNECTION_TIMEOUT("database.pool.connection_timeout", 5000),
    MESSAGE_FIRST_JOIN_WELCOME("message.first_join_welcome", "<green>Welcome to the server !<newline> <gray> do you have any accounts ? <newline> <green><click:run_command:/ac connect>[yes]</click><red><click:run_command:/ac register>[no]</click></red>"),
    MESSAGE_JOIN_WELCOME("message.join_welcome", "<green>Welcome back !");

    private static final FileConfiguration config = InviteSMP.getInstance().getConfig();
    private static final File file = new File(InviteSMP.getInstance().getDataFolder(), "config.yml");

    private final String path;
    private final Object defaultValue;

    Config(String path, Object defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }

    public String getPath() {
        return this.path;
    }

    public Object getDefaultValue() {
        return this.defaultValue;
    }

    public Object get() throws IOException {
        Object value = config.get(path);

        if (value == null) {
            return this.getBecauseNull(config, file);
        }

        return value;
    }

    public Object getBecauseNull(FileConfiguration config, File file) throws IOException {
        config.set(this.path, this.defaultValue);
        config.save(file);
        return this.defaultValue;
    }

    public String getString() throws IOException {
        if (this.defaultValue instanceof String) {
            return (String) this.get();
        }

        throw new RuntimeException(this.path + " is not a string");
    }

    public int getInt() throws IOException {
        if (this.defaultValue instanceof Integer) {
            return (Integer) this.get();
        }

        throw new RuntimeException(this.path + " is not an integer");
    }

    public boolean getBoolean() throws IOException {
        if (this.defaultValue instanceof Boolean) {
            return (Boolean) this.get();
        }

        throw new RuntimeException(this.path + " is not a boolean");
    }
}