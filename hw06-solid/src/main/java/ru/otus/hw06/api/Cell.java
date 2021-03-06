package ru.otus.hw06.api;

public interface Cell extends Money
{
    public Money copyMoney( final long quantity );
    public Money getMoney();
}


