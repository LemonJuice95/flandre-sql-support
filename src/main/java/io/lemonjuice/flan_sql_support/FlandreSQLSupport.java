package io.lemonjuice.flan_sql_support;

import io.lemonjuice.flan_sql_support.config.SQLConfig;
import io.lemonjuice.flan_sql_support.config.SQLConfigChecker;
import io.lemonjuice.flan_sql_support.event.SQLPreCloseEvent;
import io.lemonjuice.flan_sql_support.network.SQLCore;
import io.lemonjuice.flandre_bot_framework.event.BotEventBus;
import io.lemonjuice.flandre_bot_framework.event.annotation.EventSubscriber;
import io.lemonjuice.flandre_bot_framework.event.annotation.SubscribeEvent;
import io.lemonjuice.flandre_bot_framework.event.meta.BotStopEvent;
import io.lemonjuice.flandre_bot_framework.event.meta.PluginRegisterEvent;
import io.lemonjuice.flandre_bot_framework.plugins.BotPlugin;

@EventSubscriber
public class FlandreSQLSupport implements BotPlugin {
    @Override
    public String getName() {
        return "Flandre SQL Support";
    }

    @Override
    public void load() {
        SQLConfigChecker.check();
        SQLConfig.read();
        String url = String.format("jdbc:mysql://%s:%d/%s", SQLConfig.HOST.get(), SQLConfig.PORT.get(), SQLConfig.DB_NAME.get());
        SQLCore.connect(url, SQLConfig.USERNAME.get(), SQLConfig.PASSWORD.get());
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
}