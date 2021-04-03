package ru.otus.hw06.api.core.cell;

import java.util.ArrayList;
import java.util.List;

import ru.otus.hw06.api.Banknote;
import ru.otus.hw06.api.Cell;

public class CashCellFactory
{
    public static <T extends Banknote> CashCell createInfiniteWithReferenceBanknote( T referenceBanknote )
    {
        return new CashCell( referenceBanknote, Integer.MAX_VALUE );
    }

    public static <T extends Banknote> List<Cell> createInfiniteWithEachReferenceBanknote( List<T> referenceBanknotes )
    {
        List<Cell> cashCells = new ArrayList<>();
        for ( Banknote banknote : referenceBanknotes )  {
            cashCells.add( CashCellFactory.createInfiniteWithReferenceBanknote( banknote ) );
            
        }
        return cashCells;
    }
}


