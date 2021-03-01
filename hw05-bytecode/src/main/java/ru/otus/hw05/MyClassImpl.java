package ru.otus.hw05;

import ru.otus.hw05.tracer.annotations.Log;

public class MyClassImpl {

    @Log
    public Integer secureAccess(String param, int x) {
        System.out.println("secureAccess, param:" + param + " x ");
        return x+10;
    }


}
