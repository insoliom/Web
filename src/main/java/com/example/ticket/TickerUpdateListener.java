package com.example.ticket;

import com.example.protobuf.TickerUpdateOuterClass;

public interface TickerUpdateListener {
    void onUpdate(TickerUpdateOuterClass.TickerUpdate update);
}



