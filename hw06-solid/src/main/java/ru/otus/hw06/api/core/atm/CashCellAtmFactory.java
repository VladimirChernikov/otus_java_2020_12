package ru.otus.hw06.api.core.atm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ru.otus.hw06.api.Banknote;
import ru.otus.hw06.api.Cell;

public class CashCellAtmFactory
{
    public static CashCellAtm createWithCells( List< Cell > cells ) {
        Map< Banknote, List<Cell> > cellMap = new TreeMap<>( Collections.reverseOrder() );
        for ( Cell cell : cells )  {
            if ( !cellMap.containsKey( cell.getReferenceBanknote() ) ) {
                cellMap.put( cell.getReferenceBanknote(), new ArrayList<Cell>() );
            }
            cellMap.get( cell.getReferenceBanknote() ).add( (Cell)cell.copy() );
        }
        return new CashCellAtm( cellMap );
    }
}


