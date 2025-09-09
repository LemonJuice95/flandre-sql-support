package io.lemonjuice.flan_sql_support.config;

import io.lemonjuice.flandre_bot_framework.FlandreBot;
import lombok.extern.log4j.Log4j2;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Log4j2
public class SQLConfigChecker {
    public static void check() {
        if(!SQLConfig.cfgFile.getParentFile().exists()) {
            SQLConfig.cfgFile.getParentFile().mkdirs();
        }

        if(!SQLConfig.cfgFile.exists()) {
            log.warn("[FlandreSQLSupport] 未检查到sql相关配置文件，即将释放");
            try (InputStream input = SQLConfigChecker.class.getClassLoader().getResourceAsStream("config/mysql.properties");
                 OutputStream output = new FileOutputStream(SQLConfig.cfgFile)) {
                output.write(input.readAllBytes());
            } catch (Exception e) {
                log.error("[FlandreSQLSupport] 释放配置文件失败！", e);
            }
            log.warn("[FlandreSQLSupport] 请先进行sql插件配置后再次启动应用");
            FlandreBot.stop();
        }
    }
}
