
import com.pengrad.telegrambot.*;
import com.pengrad.telegrambot.model.ChosenInlineResult;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramWebhookBot;

import java.io.IOException;
import java.util.List;

/**
 * Created by Haneko on 05.05.2017.
 */
/*
public class PengradApiBot extends TelegramWebhookBot {

    private TelegramBot bot;

    public PengradApiBot() {
        this.bot = TelegramBotAdapter.build(this.getBotToken());
    }

    public void runBot() {
        // Creating Bot
        sendMessageSync();
        sendMessageAssync();
    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        long chat_id = update.getMessage().getChatId();
        // TODO
        return null;
    }

    private void sendMessageSync(chatId) {
        // Send message: All send requests (SendMessage, SendPhoto, SendLocation...) return SendResponse object that contains Message.
        SendMessage request = new SendMessage(chatId, "text")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)
                .replyToMessageId(1)
                .replyMarkup(new ForceReply());

        SendResponse sendResponse = this.bot.execute(request);
        boolean ok = sendResponse.isOk();
        Message message = sendResponse.message();
    }

    private void sendMessageAssync() {
        // Send message: All send requests (SendMessage, SendPhoto, SendLocation...) return SendResponse object that contains Message.
        SendMessage request = new SendMessage(chatId, "text")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)
                .replyToMessageId(1)
                .replyMarkup(new ForceReply());

        this.bot.execute(request, new Callback<SendMessage, SendResponse>() {
            @Override
            public void onResponse(SendMessage request, SendResponse response) {
            }

            @Override
            public void onFailure(SendMessage request, IOException e) {
            }
        });
    }

    private void makeRequest() {
        this.bot.execute(request, new Callback() {
            @Override
            public void onResponse(BaseRequest request, BaseResponse response) {
            }

            @Override
            public void onFailure(BaseRequest request, IOException e) {
            }
        });
    }

    private void getUpdates() {
        //Building request
        GetUpdates getUpdates = new GetUpdates().limit(100).offset(0).timeout(0);

        // Executing async
        GetUpdatesResponse updatesResponse = bot.execute(getUpdates);
        updatesResponse = bot.execute(getUpdates, new Callback<GetUpdates, GetUpdatesResponse>() {
            @Override
            public void onResponse(GetUpdates request, GetUpdatesResponse response) {
                List<Update> updates = updatesResponse.updates();
            }

            @Override
            public void onFailure(GetUpdates request, IOException e) {

            }
        });
    }

    private void setUpdateListener() {
        bot.setUpdatesListener(new UpdatesListener() {
            @Override
            public int process(List<Update> updates) {

                // process updates

                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        });
    }

    private void buildWebhookRequest() {

        //Webhook Building request
        SetWebhook request = new SetWebhook()
                .url("url")
                .certificate(new byte[]{}); // byte[]
        //.certificate(new File("path")); // or file

        // async
        bot.execute(request, new Callback<SetWebhook, BaseResponse>()

        {
            @Override
            public void onResponse(SetWebhook request, BaseResponse response) {

            }

            @Override
            public void onFailure(SetWebhook request, IOException e) {

            }
        });
    }

    private void parseUpdate() {

        // Using Webhook you can parse request to Update
        String stringRequest = "";
        com.pengrad.telegrambot.model.Update update = BotUtils.parseUpdate(stringRequest); // from String
        //Update update = BotUtils.parseUpdate(reader); // or from java.io.Reader

        Message message = update.message();

        //Updates Listener
        //  You can set listener to receiving incoming updates as if using Webhook.
        //  This will trigger executing getUpdates requests in a loop.
        bot.setUpdatesListener(new UpdatesListener() {
            @Override
            public int process(List<Update> updates) {

                // process updates

                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        });
    }

    private void stopReceivingUpdates() {
        // To stop receiving updates
        bot.removeGetUpdatesListener();
    }

    private void updatingNormalMessage() {

        // Normal message:
        EditMessageText editMessageText = new EditMessageText(chatId, messageId, "new test")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .replyMarkup(new ReplyKeyboardRemove());

        BaseResponse response = bot.execute(editMessageText);
    }

    private void updatingInlineMessage() {
        EditMessageText editInlineMessageText = new EditMessageText(inlineMessageId, "new text");
        BaseResponse response = bot.execute(editInlineMessageText);
    }

    private void getInlineUpdates() {
        GetUpdatesResponse updatesResponse = bot.execute(new GetUpdates());
        List<com.pengrad.telegrambot.model.Update> updates = updatesResponse.updates();
        //...
        InlineQuery inlineQuery = update.inlineQuery();
        ChosenInlineResult chosenInlineResult = update.chosenInlineResult();
        CallbackQuery callbackQuery = update.callbackQuery();

        //If using webhook, you can parse request to InlineQuery
        Update update = BotUtils.parseUpdate(stringRequest); // from String
        //Update update = BotUtils.parseUpdate(reader); // from java.io.Reader

        InlineQuery inlineQuery = update.inlineQuery();

        //Inline query result
        InlineQueryResult r1 = new InlineQueryResultPhoto("id", "photoUrl", "thumbUrl");
        InlineQueryResult r2 = new InlineQueryResultArticle("id", "title", "message text").thumbUrl("url");
        InlineQueryResult r3 = new InlineQueryResultGif("id", "gifUrl", "thumbUrl");
        InlineQueryResult r4 = new InlineQueryResultMpeg4Gif("id", "mpeg4Url", "thumbUrl");

        InlineQueryResult r5 = new InlineQueryResultVideo(
                "id", "videoUrl", InlineQueryResultVideo.MIME_VIDEO_MP4, "message", "thumbUrl", "video title")
                .inputMessageContent(new InputLocationMessageContent(21.03f, 105.83f));

        //Answer inline query
        BaseResponse response = bot.execute(new AnswerInlineQuery(inlineQuery.id(), r1, r2, r3, r4, r5));

        // or full
        bot.execute(new AnswerInlineQuery(inlineQuery.id(), new InlineQueryResult[]{
                        r1, r2, r3, r4, r5
                })
                        .cacheTime(cacheTime)
                        .isPersonal(isPersonal)
                        .nextOffset("offset")
                        .switchPmParameter("pmParam")
                        .switchPmText("pmText")
        );
    }

    @Override
    public String getBotPath() {
        return null;
    }

    @Override
    public String getBotUsername() {
        return "PengradApiBot";
    }

    @Override
    public String getBotToken() {
        return "354202826:AAEMVYmd6KpGLvQ8OZ5tzfPBvdXrDR-LcWE";
    }

}
*/