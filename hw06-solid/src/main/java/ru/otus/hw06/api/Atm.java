package ru.otus.hw06.api;

import java.util.Collection;

public interface Atm extends Money
{
    public void putAmount( final double amount ) throws RuntimeException;
    public double takeAmount( final double amount ) throws RuntimeException;
    public Collection<Cell> getCells();
    public long getTotalMoneyCount();
}


