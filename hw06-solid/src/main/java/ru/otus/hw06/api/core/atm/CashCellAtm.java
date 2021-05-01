package ru.otus.hw06.api.core.atm;

import java.util.List;
import java.util.Map;

import ru.otus.hw06.api.Banknote;
import ru.otus.hw06.api.Cell;
import ru.otus.hw06.api.core.AbstractAtm;

public class CashCellAtm extends AbstractAtm
{

    public CashCellAtm( final Map< Banknote, List<Cell> > cells )
    {
        super(cells);
    }

}


