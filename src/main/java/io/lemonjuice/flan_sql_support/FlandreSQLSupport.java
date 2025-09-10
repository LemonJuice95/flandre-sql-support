package io.lemonjuice.flan_sql_support;

import com.google.common.eventbus.Subscribe;
import io.lemonjuice.flan_sql_support.config.SQLConfig;
import io.lemonjuice.flan_sql_support.config.SQLConfigChecker;
import io.lemonjuice.flan_sql_support.network.SQLCore;
import io.lemonjuice.flandre_bot_framework.event.annotation.EventSubscriber;
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

    @Subscribe
    public void registerPlugin(PluginRegisterEvent event) {
        event.register(this);
    }

    @Subscribe
    public void onStop(BotStopEvent event) {
        SQLCore.close();
    }
}