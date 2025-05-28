package com.example.ticket;

import com.example.protobuf.TickerUpdateOuterClass;
import org.springframework.stereotype.Component;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TickersUpdateForwarder {

    private final CopyOnWriteArrayList<TickerUpdateListener> listeners = new CopyOnWriteArrayList<>();

    public void registerListener(TickerUpdateListener listener) {
        listeners.add(listener);
    }

    public void forward(TickerUpdateOuterClass.TickerUpdate update) {
        for (TickerUpdateListener l : listeners) {
            l.onUpdate(update);
        }
    }
}



