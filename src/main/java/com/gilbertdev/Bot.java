package com.gilbertdev;

import org.slf4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class Bot extends TelegramLongPollingBot {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Bot.class);

    private final PriceStream priceStream;
    private final List<String> chatIds = new ArrayList<>();

    private final double threshold = 0.3;

    public Bot(PriceStream priceStream) {
        if (priceStream == null) {
            throw new IllegalArgumentException("PriceStream cannot be null");
        }
        this.priceStream = priceStream;
    }

    @Override
    public String getBotUsername() {
        return "Bitchange Alert Bot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("tbot.apikey");
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        User user = message.getFrom();

        if ("/subscribe".equals(message.getText())) {
            subscribeChat(message.getChatId().toString());
            sendMessage(message.getChatId().toString(),
                    "¡Hola, " + user.getFirstName() + "! Te acabas de suscribir a las alertas de precio de BTC.");
        }

        logger.debug("{} wrote {}", user.getFirstName(), message.getText());
    }

    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text(message)
                .build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Error while sending message: ", e);
        }
    }

    public void startStream() {
        priceStream.getPriceStreamForChat(priceData -> {
            if (
                    Math.abs(Double.parseDouble(priceData.getPriceChangePercent())) >= threshold
            ) {
                String message = "Alerta bitcoin: Bitcoin cambio " + priceData.getPriceChangePercent() + "%";
                for (String id : chatIds) {
                    sendMessage(id, message);
                }
            }
        });
    }

    private void subscribeChat(String chatId) {
        chatIds.add(chatId);
    }
}
