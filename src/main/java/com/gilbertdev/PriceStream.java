package com.gilbertdev;

import com.binance.connector.client.WebSocketStreamClient;
import com.binance.connector.client.impl.WebSocketStreamClientImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilbertdev.model.PriceData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PriceStream {
    private static final Logger logger = LoggerFactory.getLogger(PriceStream.class);
    private final WebSocketStreamClient webSocketStreamClient = new WebSocketStreamClientImpl();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final List<String> chatIds = new ArrayList<>();

    public void subscribeChat(String chatId) {
        chatIds.add(chatId);
    }

    public void getPriceStreamForChat(Bot bot) {
        logger.debug("Main subscribe chat method");

        webSocketStreamClient.symbolTicker("btcusdt", response -> {
            PriceData priceData = objectMapper.convertValue(response, PriceData.class);
            String message = "Response: " + response;
            for (String id : chatIds) {
                bot.sendMessage(id, message);
            }
        });
    }

    public void close() {
        webSocketStreamClient.closeAllConnections();
    }
}
