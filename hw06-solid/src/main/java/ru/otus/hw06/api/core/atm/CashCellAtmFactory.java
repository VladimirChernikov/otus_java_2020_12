package ru.otus.hw06.api.core.atm;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ru.otus.hw06.api.Cell;

public class CashCellAtmFactory
{
    public static CashCellAtm createWithCells( List< Cell > cells ) {
        Map< Double, Cell > cellMap = new TreeMap<>( Collections.reverseOrder() );
        for ( Cell cell : cells )  {
            if ( !cellMap.containsKey( cell.getQuant() ) ) {
                cellMap.put( cell.getQuant(), (Cell)cell.copy() );
            }
            else {
                // merge cells
                Cell existingCell = cellMap.get( cell.getQuant() );
                existingCell.setMaxQuantity( existingCell.getMaxQuantity() + cell.getMaxQuantity() );
                existingCell.setQuantity( existingCell.getQuantity() + cell.getQuantity() );
                cellMap.put( cell.getQuant(), existingCell );
            }
        }
        return new CashCellAtm( cellMap );
    }
}


