package ru.otus.processor.homework;

import java.time.Clock;
import java.time.LocalDateTime;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorThrowExceptionEvenSecond implements Processor {

    private final Clock clock;

    public ProcessorThrowExceptionEvenSecond( Clock clock ) {
        this.clock = clock;
    }

    public ProcessorThrowExceptionEvenSecond() {
        this( Clock.systemDefaultZone() );
    }

    @Override
    public Message process(Message message) {
        if ( LocalDateTime.now(clock).getSecond() % 2 == 0 )  {
            throw new RuntimeException("Cannot process message at even second.");
        }
        else  {
            return message;
        }
    }
}
