package ru.otus.hw06.api;

public interface Money extends Quantable, Copyable
{
    public double getAmount();
    public void addMoney( final Money money );
    public Money subtractMoney( final Money money );
}


