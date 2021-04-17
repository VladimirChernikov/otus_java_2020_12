package ru.otus.handler;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.ProcessorThrowExceptionEvenSecond;

class HomeworkTest {

    @DisplayName("Исключение в четную секунду")
    @Test
    void throwAnExceptionEveryEvenSecondTest() {
        //given
        var message = new Message.Builder(1L).build();

        Instant startTime = Instant.now();
        boolean evenSecond = startTime.getEpochSecond() % 2 == 0;
        List<Processor> processors = new ArrayList<>();
        for ( int i = 0; i < 100; i++ )  {
            Clock clock = Clock.fixed( startTime.plusSeconds( i ), ZoneId.systemDefault() );
            processors.add( new ProcessorThrowExceptionEvenSecond( clock ) );
        }

        //when
        assertThat( processors.size() ).isNotZero();
        for ( var processor : processors )  {
            if ( evenSecond ) {
        //then
                assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> processor.process(message));
            }
            evenSecond = !evenSecond;
        }
    }

    @DisplayName("Исключение в четную секунду явно")
    @Test
    void throwAnExceptionEveryEvenSecondExplicitTest() {
        //given
        var message = new Message.Builder(1L).build();

        Instant oddSecond  = Instant.now().with(ChronoField.INSTANT_SECONDS, 11);
        Clock clockOddSecond = Clock.fixed( oddSecond, ZoneId.systemDefault() );

        Instant evenSecond = Instant.now().with(ChronoField.INSTANT_SECONDS, 12);
        Clock clockEvenSecond = Clock.fixed( evenSecond, ZoneId.systemDefault() );

        var processorOdd = new ProcessorThrowExceptionEvenSecond( clockOddSecond );
        var processorEven = new ProcessorThrowExceptionEvenSecond( clockEvenSecond );

        //when
        processorOdd.process(message);

        //then 
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> processorEven.process(message));

    }

}
