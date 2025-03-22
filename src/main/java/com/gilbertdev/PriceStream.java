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
import java.util.function.Consumer;

public class PriceStream {
    private static final Logger logger = LoggerFactory.getLogger(PriceStream.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final WebSocketStreamClient webSocketStreamClient = new WebSocketStreamClientImpl();

    private final String pair;


    public PriceStream(String pair) {
        this.pair = pair;
    }

    public void getPriceStreamForChat(Consumer<PriceData> consumer) {
        logger.debug("Main subscribe chat method");

        webSocketStreamClient.symbolTicker(pair, response -> {
            try {
                PriceData priceData = objectMapper.readValue(response, PriceData.class);
                consumer.accept(priceData);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void close() {
        webSocketStreamClient.closeAllConnections();
    }
}
