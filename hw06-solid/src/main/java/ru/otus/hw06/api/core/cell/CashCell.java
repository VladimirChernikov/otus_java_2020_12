package ru.otus.hw06.api.core.cell;

import ru.otus.hw06.api.Money;
import ru.otus.hw06.api.core.AbstractCell;

public class CashCell extends AbstractCell
{
    public CashCell( final Money initialMoney, final long capacity )
    {
        super( initialMoney, capacity );
    }

	@Override
	public Object copy() {
		return new CashCell( (Money)this.getMoney().copy(), this.getMaxQuantity() );
	}

}


