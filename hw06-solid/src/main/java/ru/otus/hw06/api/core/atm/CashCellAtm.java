package ru.otus.hw06.api.core.atm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.otus.hw06.api.Cell;
import ru.otus.hw06.api.core.AbstractAtm;

public class CashCellAtm extends AbstractAtm
{

    public CashCellAtm( final Map< Double, Cell > cells )
    {
        super(cells);
    }

	@Override
	public Object copy() {
        List< Cell > newCells = new ArrayList<>();
        for ( Cell cell : this.getCells() )  {
            newCells.add( (Cell)cell.copy() );
        }
		return CashCellAtmFactory.createWithCells( newCells );
	}


}


