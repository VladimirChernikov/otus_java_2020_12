package ru.otus.handler;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
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
        new ComplexProcessor(processors, (ex) -> {});

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

}
