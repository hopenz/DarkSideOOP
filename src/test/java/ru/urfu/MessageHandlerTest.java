package ru.urfu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageHandlerTest {

    @Test
    public void testMessageHandler(){
        MessageHandler messageHandler = new MessageHandler();
        String testMessage = messageHandler.changeMessage("Да-да, я работаю :D");

        assertEquals("Ваше сообщение: Да-да, я работаю :D", testMessage);
    }
}
