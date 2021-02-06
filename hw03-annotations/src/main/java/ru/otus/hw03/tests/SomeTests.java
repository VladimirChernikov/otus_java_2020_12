package ru.otus.hw03.tests;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.lang.Exception;

import ru.otus.hw03.annotations.After;
import ru.otus.hw03.annotations.Before;
import ru.otus.hw03.annotations.Test;
import ru.otus.hw03.annotations.Title;

/**
 * SomeTests
 */
@Title("Проверка")
public class SomeTests {

    String test;
    int[] a = {1,23,4};

    @Test
    @Title("Сегодня понедельник?")
    public void todayIsMonday() 
    throws Exception
    {
        if ( !( LocalDate.now().getDayOfWeek() == DayOfWeek.MONDAY ) ) {
            throw new Exception();
        }
    }

    @Test
    @Title("Рекурсия")
    public void recursionTest() {
        while ( 1==1 )  {
            recursionTest();
        }
    }

    @Test
    @Title("Доступ к массиву")
    public void arrayTest() {
        int x = a[10];
    }

    @Test
    @Title("Конкатенация строки")
    public void concatTest() {
        while ( 1==1 ) {
            test += test;
        }
    }

    @Test
    @Title("Вывод строки")
    public void stringTest() {
        System.out.println(test);
    }

    @Before
    @Title("Перед 1")
    public void before1() {
        test = "test";
    }
    @Before
    @Title("Перед 2")
    public void before2() {
        
    }

    @After
    @Title("После 1")
    public void todayIsMonday4() {
        System.arraycopy(a,0,test,0,a.length);
    }

    @After
    @Title("После 2")
    public void todayIsMonday5() {
        test = "";
    }
}
