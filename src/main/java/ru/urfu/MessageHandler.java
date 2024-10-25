package ru.urfu;

/**
 * Класс MessaseHadler предосталвяет методы для обработки сообщений(обновлений) у пользователя
 */
public class MessageHandler {

    /**
     * Метод изменяет формат сообщения в "Ваше мообщение: <сообщение пользователя>
     * @param message сообщение от пользователя
     * @return обработанное сообщение
     */
    public String changeMessage(String message){
        return "Ваше сообщение: " + message;
    }
}
