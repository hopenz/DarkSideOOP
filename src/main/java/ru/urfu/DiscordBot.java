package ru.urfu;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

/**
 * Дискорд бот
 */
public class DiscordBot {

    /**
     * токен бота
     */
    private final String token;

    /**
     * клиет для взаимодействия с дискордом
     */
    private GatewayDiscordClient client;

    /**
     * Поле обработчика сообщений
     */
    private MessageHandler messageHandler;


    /**
     * конструктор для создания экземпляра класса дискордБот
     *
     * @param token          токен бота
     * @param messageHandler обработчик сообщений
     */
    public DiscordBot(String token, MessageHandler messageHandler) {
        this.token = token;
        this.messageHandler = messageHandler;
    }

    /**
     * метод, который запускает бота и выводит соответветствующее сообщение
     * В противном случае выводит сообщение о неудачном запуске
     */
    public void start() {
        client = DiscordClient.create(token).login().block();
        if (client == null) {
            throw new RuntimeException("Ошибка при входе в Discord");
        }
        client.on(MessageCreateEvent.class)
                .doOnError(throwable -> {
                    throw new RuntimeException("Ошибка при работе Discord бота", throwable);
                })
                .subscribe(event -> {
                    Message eventMessage = event.getMessage();
                    if (eventMessage.getAuthor().map(user -> !user.isBot()).orElse(false)) {
                        String channelId = eventMessage.getChannelId().asString();
                        String messageFromUser = eventMessage.getContent();
                        String answerMessage = messageHandler.changeMessage(messageFromUser);
                        sendMessage(channelId, answerMessage);
                    }
                });
        System.out.println("Discord бот запущен");
        client.onDisconnect().block();
    }

    /**
     * Отправить сообщение
     *
     * @param chatId  идентификатор чата
     * @param message текст сообщения
     */
    public void sendMessage(String chatId, String message) {
        Snowflake channelId = Snowflake.of(chatId);
        MessageChannel channel = client.getChannelById(channelId).ofType(MessageChannel.class).block();
        if (channel != null) {
            channel.createMessage(message).block();
        } else {
            System.err.println("Канал не найден");
        }
    }
}
