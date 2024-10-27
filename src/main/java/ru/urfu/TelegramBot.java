package ru.urfu;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Телеграм бот
 */
public class TelegramBot extends TelegramLongPollingBot {

    /**
     * имя тг бота
     */
    private final String telegramBotName;

    /**
     * Поле обработчика сообщений
     */
    private MessageHandler messageHandler;

    /**
     * Конструктор для создания экземпляра класса TelegramBot
     *
     * @param telegramBotName имя телеграм бота
     * @param token           токен телеграм бота
     * @param messageHandler  обработчик сообщений
     */
    public TelegramBot(String telegramBotName, String token, MessageHandler messageHandler) {
        super(token);
        this.telegramBotName = telegramBotName;
        this.messageHandler = messageHandler;
    }

    /**
     * Метод, который запуск работу бота и выводит соответствующее сообщение
     * В противном случае - выводи сообщение о неудачном старте
     */
    public void start() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            System.out.println("Telegram бот запущен");
        } catch (TelegramApiException e) {
            throw new RuntimeException("Не удалось запустить телеграм бота", e);
        }
    }

    /**
     * Метод, который обрабатывает обновление от телеграма
     *
     * @param update обновление, которое пришло от телеграма
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message updateMessage = update.getMessage();
            Long chatId = updateMessage.getChatId();
            String messageFromUser = updateMessage.getText();
            String answerMessage = messageHandler.changeMessage(messageFromUser);
            sendMessage(chatId.toString(), answerMessage);
        }
    }

    /**
     * Отправить сообщение
     *
     * @param chatId  идентификатор чата
     * @param message текст сообщения
     */
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.err.println("Не удалось отправить сообщение. " + e.getMessage());
        }
    }

    /**
     * метод, который возвращает имя телеграм бота
     *
     * @return имя тг бота
     */
    @Override
    public String getBotUsername() {
        return telegramBotName;
    }
}
