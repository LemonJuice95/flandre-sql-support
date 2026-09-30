package io.lemonjuice.flan_sql_support;

import io.lemonjuice.flan_sql_support.config.SQLConfig;
import io.lemonjuice.flan_sql_support.config.SQLConfigChecker;
import io.lemonjuice.flan_sql_support.event.SQLOpenEvent;
import io.lemonjuice.flan_sql_support.event.SQLPreCloseEvent;
import io.lemonjuice.flan_sql_support.network.SQLCore;
import io.lemonjuice.flandre_bot_framework.FlandreBot;
import io.lemonjuice.flandre_bot_framework.event.BotEventBus;
import io.lemonjuice.flandre_bot_framework.event.annotation.EventSubscriber;
import io.lemonjuice.flandre_bot_framework.event.annotation.SubscribeEvent;
import io.lemonjuice.flandre_bot_framework.event.meta.BotStopEvent;
import io.lemonjuice.flandre_bot_framework.event.meta.ConfigReloadEvent;
import io.lemonjuice.flandre_bot_framework.event.meta.PluginRegisterEvent;
import io.lemonjuice.flandre_bot_framework.plugins.BotPlugin;
import io.lemonjuice.flandre_bot_framework.plugins.PluginLoadingException;
import io.lemonjuice.flandre_bot_framework.plugins.PluginVersion;
import lombok.extern.log4j.Log4j2;

@EventSubscriber
@PluginVersion("0.11.0")
@Log4j2
public class FlandreSQLSupport implements BotPlugin {
    @Override
    public String getName() {
        return "Flandre SQL Support";
    }

    @Override
    public void load() {
        String url = String.format("jdbc:mysql://%s:%d/%s", SQLConfig.HOST.get(), SQLConfig.PORT.get(), SQLConfig.DB_NAME.get());
        if(!SQLCore.connect(url, SQLConfig.USERNAME.get(), SQLConfig.PASSWORD.get())) {
            throw new PluginLoadingException("SQL连接失败");
        } else {
            BotEventBus.post(new SQLOpenEvent());
        }
    }

    @Override
    public boolean initConfig() {
        return SQLConfig.CONFIG.load();
    }

    @SubscribeEvent
    public void registerPlugin(PluginRegisterEvent event) {
        event.register(this);
    }

    @SubscribeEvent
    public void onStop(BotStopEvent event) {
        BotEventBus.post(new SQLPreCloseEvent());
        SQLCore.close();
    }

    @SubscribeEvent
    public void onConfigReload(ConfigReloadEvent event) {
        if(event.getConfig() == SQLConfig.CONFIG) {
            String url = String.format("jdbc:mysql://%s:%d/%s", SQLConfig.HOST.get(), SQLConfig.PORT.get(), SQLConfig.DB_NAME.get());
            if(!SQLCore.restart(url, SQLConfig.USERNAME.get(), SQLConfig.PASSWORD.get()) && SQLConfig.SQL_STRONGLY_NEEDED.get()) {
                log.fatal("[FlandreSQLSupport] 无法重新连接至数据库，正在停止应用...");
                FlandreBot.stop();
            }
        }
    }
}