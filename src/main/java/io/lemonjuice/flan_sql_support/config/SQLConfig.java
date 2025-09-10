package io.lemonjuice.flan_sql_support.config;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Supplier;

@Log4j2
public class SQLConfig {
    public static final File cfgFile = new File("config/mysql.properties");

    private static final Properties properties = new Properties();

    public static final Supplier<String> HOST = () -> properties.getProperty("bot.sql.host");
    public static final Supplier<Integer> PORT = () -> Integer.valueOf(properties.getProperty("bot.sql.port"));
    public static final Supplier<String> DB_NAME = () -> properties.getProperty("bot.sql.db_name");
    public static final Supplier<String> USERNAME = () -> properties.getProperty("bot.sql.username");
    public static final Supplier<String> PASSWORD = () -> properties.getProperty("bot.sql.password");

    public static final Supplier<Integer> POOL_SIZE = () -> Integer.valueOf(properties.getProperty("bot.sql.connection_pool.size"));
    public static final Supplier<Integer> CONNECTION_TIMEOUT = () -> Integer.valueOf(properties.getProperty("bot.sql.connection.timeout_ms"));
    public static final Supplier<Integer> IDLE_TIMEOUT = () -> Integer.valueOf(properties.getProperty("bot.sql.connection.idle_timeout_ms"));

    public static final Supplier<Integer> HEARTBEAT_INTERVAL_MS = () -> Integer.valueOf(properties.getProperty("bot.sql.heartbeat.interval_ms"));

    public static final Supplier<Boolean> SQL_STRONGLY_NEEDED = () -> Boolean.valueOf(properties.getProperty("bot.sql.strongly_need"));
    public static final Supplier<Integer> MAX_FAILED_COUNT = () -> Integer.valueOf(properties.getProperty("bot.sql.max_failed_count"));

    public static void read() {
        try (InputStream input = new FileInputStream(cfgFile)) {
            properties.load(input);
        } catch (IOException e) {
            log.error("[FlandreSQLSupport] 加载mysql相关配置失败！", e);
        }
    }
}
