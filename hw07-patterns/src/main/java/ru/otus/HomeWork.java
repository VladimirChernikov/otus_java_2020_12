package ru.otus;

import java.util.ArrayList;
import java.util.List;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinter;
import ru.otus.listener.homework.ListenerHistory;
import ru.otus.model.InternalMessageStorage;
import ru.otus.model.Message;
import ru.otus.model.MessageStorage;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.LoggerProcessor;
import ru.otus.processor.ProcessorConcatFields;
import ru.otus.processor.ProcessorUpperField10;
import ru.otus.processor.homework.ProcessorSwapField12Field11;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяьться во время выполнения.
       4. Сделать Listener для ведения истории: старое сообщение - новое (подумайте, как сделать, чтобы сообщения не портились)
     */

    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */

        var processors = List.of(
                 new ProcessorConcatFields()
                ,new LoggerProcessor(new ProcessorUpperField10())
                ,new ProcessorSwapField12Field11()
                ,new LoggerProcessor(new ProcessorUpperField10())
                ,new ProcessorUpperField10()
                ,new LoggerProcessor(new ProcessorUpperField10())
                );

        var messageStorage = new InternalMessageStorage();
        var complexProcessor = new ComplexProcessor(processors, (ex) -> {});
        var listenerPrinter = new ListenerPrinter();
        var listenerHistory = new ListenerHistory( (MessageStorage)( messageStorage ) );
        complexProcessor.addListener(listenerPrinter);
        complexProcessor.addListener(listenerHistory);

        List<String> objectData = new ArrayList<>();
        objectData.add("a1");
        objectData.add("a2");
        objectData.add("a3");
        objectData.add("a4");

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13( new ObjectForMessage.Builder().data( objectData ).build() )
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        System.out.println( messageStorage );

        complexProcessor.removeListener(listenerPrinter);
        complexProcessor.removeListener(listenerHistory);
    }
}
