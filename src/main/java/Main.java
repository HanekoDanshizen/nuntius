import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.inlinequery.result.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.IOException;
import java.util.List;

/**
 * Created by Haneko on 27.04.2017.
 * Main-class with main-method to start program.
 */
public class Main {
    public static void main(String[] args) {

        //Variante mit dem Tutorial
        // Initialize Api Context
        ApiContextInitializer.init();

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();

        // Creating Bot
        TutorialBot bot = new TutorialBot();

        bot.run(botsApi);

        //Variante mit API von Pengrad
        //(new PengradApiBot()).runBot();
    }
}
