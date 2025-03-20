package com.gilbertdev;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class App {
    private static final PriceStream priceStream = new PriceStream();

    public static void main( String[] args ) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        Bot bot = new Bot(priceStream);
        botsApi.registerBot(bot);
        priceStream.getPriceStreamForChat(bot);
    }
}
