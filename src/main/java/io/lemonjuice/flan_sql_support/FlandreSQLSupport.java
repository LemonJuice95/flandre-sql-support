package io.lemonjuice.flan_sql_support;

import com.google.common.eventbus.Subscribe;
import io.lemonjuice.flan_sql_support.config.SQLConfig;
import io.lemonjuice.flan_sql_support.config.SQLConfigChecker;
import io.lemonjuice.flan_sql_support.network.SQLCore;
import io.lemonjuice.flandre_bot.event.annotation.EventSubscriber;
import io.lemonjuice.flandre_bot.event.meta.BotStopEvent;
import io.lemonjuice.flandre_bot.event.meta.PluginLoadEvent;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.CountDownLatch;

@EventSubscriber
@Log4j2
public class FlandreSQLSupport {
    public static final CountDownLatch loaded = new CountDownLatch(1);

    @Subscribe
    public void loadPlugin(PluginLoadEvent event) {
        log.info("正在加载插件: Flandre SQL Support");
        SQLConfigChecker.check();
        SQLConfig.read();
        String url = String.format("jdbc:mysql://%s:%d/%s", SQLConfig.HOST.get(), SQLConfig.PORT.get(), SQLConfig.DB_NAME.get());
        SQLCore.connect(url, SQLConfig.USERNAME.get(), SQLConfig.PASSWORD.get());
        loaded.countDown();
    }

    @Subscribe
    public void onStop(BotStopEvent event) {
        SQLCore.close();
    }
}