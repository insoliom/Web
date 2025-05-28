package com.example.binance;

import com.example.protobuf.TickerUpdateOuterClass;
import com.example.ticket.Coin;
import com.example.ticket.TickersUpdateForwarder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

@Component
public class BinanceWebSocketTickerStreamClient {
    private final TickersUpdateForwarder forwarder;
    private static final ObjectMapper mapper = new ObjectMapper();

    public BinanceWebSocketTickerStreamClient(TickersUpdateForwarder forwarder) {
        this.forwarder = forwarder;
    }

    @PostConstruct
    public void start() {
        String[] coins = {"btcusdt", "ethusdt", "xrpusdt", "dogeusdt"};
        String streams = String.join("/",
                java.util.Arrays.stream(coins).map(c -> c + "@ticker").toArray(String[]::new)
        );
        String wsUrl = "wss://stream.binance.com:9443/stream?streams=" + streams;

        HttpClient.newHttpClient().newWebSocketBuilder()
                .buildAsync(URI.create(wsUrl), new WebSocket.Listener() {
                    @Override
                    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                        try {
                            JsonNode root = mapper.readTree(data.toString());
                            JsonNode ticker = root.get("data");
                            String symbol = ticker.get("s").asText();
                            double price = Double.parseDouble(ticker.get("c").asText());
                            long timestamp = ticker.get("E").asLong();
                            Coin coin = Coin.fromSymbol(symbol);
                            TickerUpdateOuterClass.Coin protoCoin = toProtoCoin(coin);
                            TickerUpdateOuterClass.TickerUpdate update = TickerUpdateOuterClass.TickerUpdate.newBuilder()
                                    .setCoin(protoCoin)
                                    .setPrice(price)
                                    .setTimestamp(timestamp)
                                    .build();
                            forwarder.forward(update);
                        } catch (Exception e) {
                            System.err.println("ERROR: " + e);
                            e.printStackTrace();
                        }
                        webSocket.request(1);
                        return null;
                    }
                });
        System.out.println("✅ Connected to Binance WS: " + wsUrl);
    }

    private static TickerUpdateOuterClass.Coin toProtoCoin(Coin coin) {
        if (coin == null) {
            System.err.println("toProtoCoin: NULL coin!!!");
            throw new IllegalArgumentException("coin is null!");
        }
        switch (coin.name()) {
            case "BTCUSDT":
                return TickerUpdateOuterClass.Coin.BTC;
            case "ETHUSDT":
                return TickerUpdateOuterClass.Coin.ETH;
            case "XRPUSDT":
                return TickerUpdateOuterClass.Coin.XRP;
            case "DOGEUSDT":
                return TickerUpdateOuterClass.Coin.DOGE;
            default:
                System.err.println("Unknown coin.name(): " + coin.name());
                return TickerUpdateOuterClass.Coin.UNRECOGNIZED;
        }
    }

}

