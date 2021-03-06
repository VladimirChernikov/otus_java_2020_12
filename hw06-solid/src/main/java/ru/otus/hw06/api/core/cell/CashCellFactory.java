package ru.otus.hw06.api.core.cell;

import ru.otus.hw06.api.Money;

public class CashCellFactory
{
    public static CashCell createInfiniteWithMoney( Money money )
    {
        return new CashCell( (Money)money.copy(), Long.MAX_VALUE );
    }
}


