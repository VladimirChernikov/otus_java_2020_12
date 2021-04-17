package ru.otus.hw06.api.core.money;

public class CashFactory
{
    public static Cash createNoneWithNominal( double quant )
    {
        return new Cash( quant, 0 );
    }

    public static Cash createSingleWithNominal( double quant )
    {
        return new Cash( quant, 1 );
    }

    public static Cash createWithNominal( double quant, long quantity )
    {
        return new Cash( quant, quantity );
    }
}


