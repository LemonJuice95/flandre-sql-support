package io.lemonjuice.flan_sql_support.config;

import io.lemonjuice.flandre_bot_framework.FlandreBot;
import io.lemonjuice.flandre_bot_framework.config.BotConfig;
import io.lemonjuice.flandre_bot_framework.config.ConfigItem;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Supplier;

@Log4j2
public class SQLConfig {
    public static final BotConfig CONFIG = FlandreBot.registerConfig(new BotConfig("./config/mysql.properties", "config/mysql.properties")
            .failWhenExport()
            .description("MySQL插件相关配置"));

    public static final ConfigItem<String> HOST = CONFIG.register(CONFIG::getString, "bot.sql.host", "");
    public static final ConfigItem<Integer> PORT = CONFIG.register(CONFIG::getInt, "bot.sql.port", 3306);
    public static final ConfigItem<String> DB_NAME = CONFIG.register(CONFIG::getString, "bot.sql.db_name", "");
    public static final ConfigItem<String> USERNAME = CONFIG.register(CONFIG::getString, "bot.sql.username", "");
    public static final ConfigItem<String> PASSWORD = CONFIG.register(CONFIG::getString,  "bot.sql.password", "");

    public static final ConfigItem<Integer> POOL_SIZE = CONFIG.register(CONFIG::getInt, "bot.sql.connection_pool.size", 15);
    public static final ConfigItem<Integer> CONNECTION_TIMEOUT = CONFIG.register(CONFIG::getInt, "bot.sql.connection.timeout_ms", 30000);
    public static final ConfigItem<Integer> IDLE_TIMEOUT = CONFIG.register(CONFIG::getInt,"bot.sql.connection.idle_timeout_ms", 1800000);

    public static final ConfigItem<Integer> HEARTBEAT_INTERVAL_MS = CONFIG.register(CONFIG::getInt, "bot.sql.heartbeat.interval_ms", 30000);

    public static final ConfigItem<Boolean> SQL_STRONGLY_NEEDED = CONFIG.register(CONFIG::getBoolean, "bot.sql.strongly_need", true);
    public static final ConfigItem<Integer> MAX_FAILED_COUNT = CONFIG.register(CONFIG::getInt, "bot.sql.max_failed_count", 5);
}
