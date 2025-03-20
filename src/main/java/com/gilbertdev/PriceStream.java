package com.gilbertdev;

import com.binance.connector.client.WebSocketStreamClient;
import com.binance.connector.client.impl.WebSocketStreamClientImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
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
            try {
                PriceData priceData = objectMapper.readValue(response, PriceData.class);
                final double threshold = 0.3;

                if (
                        Math.abs(Double.parseDouble(priceData.getPriceChangePercent())) >= threshold
                ) {
                    String message = "Alerta bitcoin: Bitcoin cambio " + priceData.getPriceChangePercent() + "%";
                    for (String id : chatIds) {
                        bot.sendMessage(id, message);
                    }
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void close() {
        webSocketStreamClient.closeAllConnections();
    }
}
