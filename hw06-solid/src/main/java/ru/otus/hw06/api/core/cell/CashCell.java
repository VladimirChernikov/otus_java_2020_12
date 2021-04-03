package ru.otus.hw06.api.core.cell;

import ru.otus.hw06.api.Banknote;
import ru.otus.hw06.api.core.AbstractCell;

public class CashCell extends AbstractCell
{
    public CashCell( final Banknote referenceBanknote, final int maxQuantity )
    {
        super( referenceBanknote, maxQuantity );
    }

	@Override
	public Object copy() {
		return new CashCell( this.getReferenceBanknote(), this.getMaxQuantity() );
	}

}


