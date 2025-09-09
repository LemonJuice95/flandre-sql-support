package io.lemonjuice.flan_sql_support.network;

import com.zaxxer.hikari.HikariDataSource;
import io.lemonjuice.flan_sql_support.config.SQLConfig;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;

@Log4j2
public class SQLCore {
    @Getter
    private static volatile SQLCore instance;
    private static final Thread keepAliveThread = new Thread(new SQLKeepAlive());

    private final HikariDataSource dataSource;

    private SQLCore(String url, String username, String password) throws SQLException {
        this.dataSource = this.createDataSource(url, username, password);
    }

    public synchronized static boolean connect(String url, String username, String password) {
        try {
            instance = new SQLCore(url, username, password);
            keepAliveThread.start();
            log.info("[FlandreSQLSupport] 已连接至SQL数据库");
            return true;
        } catch (SQLException e) {
            log.error("[FlandreSQLSupport] SQL数据库连接失败！", e);
            return false;
        }
    }

    public synchronized static void close() {
        try {
            keepAliveThread.interrupt();
            instance.dataSource.close();
        } catch (NullPointerException ignored) {
            //出现该异常说明本身不存在连接，因此无需处理
        }
    }

    private HikariDataSource createDataSource(String url, String username, String password) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setMaximumPoolSize(SQLConfig.POOL_SIZE.get());
        dataSource.setConnectionTimeout(SQLConfig.CONNECTION_TIMEOUT.get());
        dataSource.setIdleTimeout(SQLConfig.IDLE_TIMEOUT.get());
        return dataSource;
    }

    /**
     * 从数据库连接池中获取一个连接
     * @return 连接实例
     * @throws SQLException
     */
    public Connection startConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}
