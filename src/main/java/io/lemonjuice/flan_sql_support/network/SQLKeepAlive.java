package io.lemonjuice.flan_sql_support.network;

import io.lemonjuice.flan_sql_support.config.SQLConfig;
import io.lemonjuice.flandre_bot.FlandreBot;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Log4j2
public class SQLKeepAlive implements Runnable {
    private final int intervalMs = SQLConfig.HEARTBEAT_INTERVAL_MS.get();
    private int failedCount = 0;

    @Override
    public void run() {
        while (true) {
            try (Connection connection = SQLCore.getInstance().startConnection();
                 Statement statement = connection.createStatement()) {
                statement.execute("SELECT 1");
                this.failedCount = 0;
            } catch (SQLException e) {
                this.failedCount++;
                log.warn("[FlandreSQLSupport] SQL连接疑似异常，捕获次数: {}", this.failedCount);
                if(SQLConfig.SQL_STRONGLY_NEEDED.get() && this.failedCount >= SQLConfig.MAX_FAILED_COUNT.get()) {
                    log.fatal("[FlandreSQLSupport] SQL连接无法恢复，正在停止应用...");
                    FlandreBot.stop();
                }
            }
            try {
                Thread.sleep(intervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
