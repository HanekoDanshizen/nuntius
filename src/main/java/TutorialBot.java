import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Haneko on 05.05.2017.
 */
public class TutorialBot extends TelegramLongPollingBot {

    // Create the keyboard (list of keyboard rows)
    private List<KeyboardRow> keyboard;


    public TutorialBot() {
        this.keyboard = new ArrayList<>();
    }

    public void run(TelegramBotsApi botsApi) {

        // Register our bot
        try {
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        System.out.println(this.getBotUsername() + " successfully started!");
    }

    @Override
    public void onUpdateReceived(org.telegram.telegrambots.api.objects.Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String first_name = update.getMessage().getChat().getFirstName();
            String last_name = update.getMessage().getChat().getLastName();
            String username = update.getMessage().getChat().getUserName();
            long user_id = update.getMessage().getChat().getId();
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            String answer = EmojiParser.parseToUnicode(message_text);
            boolean hasText = true;

            SendMessage message = new SendMessage() // Create a message object object
                    .setChatId(chat_id);

            if (update.getMessage().getText().equals("/start")) {
                message.setText("You send /start");
            } else if (update.getMessage().getText().equals("/update_Message")) {
                updateMessageCommand(update, message);
            } else if (message_text.equals("/markup")) {
                markupCommand(message);
            } else if (message_text.equals("/hide")) {
                hideCommand(message);
            } else if (message_text.equals("/setKeyboardButton")) {
                setKeyboardbuttonCommand(message.setText(message.getText().substring(message.getText().indexOf(" "))), 0);
            } else if (message_text.startsWith("/")) {
                // Unknown command
                message.setText("Unknown command");
            } else {
                hasText = false;
            }

            if (hasText) {
                try {
                    sendMessage(message); // Sending our message object to user
                    //check(first_name, last_name, (int) user_id, username);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        } else if (update.hasCallbackQuery()) {
            // Set variables
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals("update_msg_text")) {
                String answer = "Updated message text";
                EditMessageText new_message = new EditMessageText()
                        .setChatId(chat_id)
                        .setMessageId((int) message_id)
                        .setText(answer);
                try {
                    editMessageText(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setKeyboardbuttonCommand(SendMessage message, int i) {
        //int zeile = i % 6 > 3 ? 2 : 1;
        this.keyboard.get(i).set(i, message.getText());
    }

    private void updateMessageCommand(Update update, SendMessage message) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText(update.getMessage().getText()).setCallbackData("update_msg_text"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
    }

    private void hideCommand(SendMessage message) {
        message.setText("Keyboard hidden");
        ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
        message.setReplyMarkup(keyboardMarkup);
    }

    private void log(String first_name, String last_name, String user_id, String txt, String bot_answer) {
        System.out.println("\n ----------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("Message from " + first_name + " " + last_name + ". (id = " + user_id + ") \n Text - " + txt);
        System.out.println(this.getBotUsername() + " answer: \n Text - " + bot_answer);
    }

    private void markupCommand(SendMessage message) {
        message.setText("Here is your keyboard");
        // Create ReplyKeyboardMarkup object
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // Create a keyboard row
        KeyboardRow row = new KeyboardRow();
        // Set each button, you can also use KeyboardButton objects if you need something else than text
        row.add("Button 1");
        row.add("Button 2");
        row.add("Button 3");
        // Add the first row to the keyboard
        this.keyboard.add(row);
        // Create another keyboard row
        row = new KeyboardRow();
        // Set each button for the second line
        row.add("Button 4");
        row.add("Button 5");
        row.add("Button 6");
        // Add the second row to the keyboard
        this.keyboard.add(row);
        // Set the keyboard to the markup
        keyboardMarkup.setKeyboard(this.keyboard);
        // Add it to the message
        message.setReplyMarkup(keyboardMarkup);
    }

    /*private String check(String first_name, String last_name, int user_id, String username) {
        // Set loggers
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        MongoClientURI connectionString = new MongoClientURI("mongodb://host:port");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("db_name");
        MongoCollection<Document> collection = database.getCollection("users");
        long found = collection.count(Document.parse("{id : " + Integer.toString(user_id) + "}"));
        if (found == 0) {
            Document doc = new Document("first_name", first_name)
                    .append("last_name", last_name)
                    .append("id", user_id)
                    .append("username", username);
            collection.insertOne(doc);
            mongoClient.close();
            System.out.println("User not exists in database. Written.");
            return "no_exists";
        } else {
            System.out.println("User exists in database.");
            mongoClient.close();
            return "exists";
        }
    }*/

    @Override
    public String getBotUsername() {
        return "TutorialBot";
    }

    @Override
    public String getBotToken() {
        return "354202826:AAEMVYmd6KpGLvQ8OZ5tzfPBvdXrDR-LcWE";
    }
}
