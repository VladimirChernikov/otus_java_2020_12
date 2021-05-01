package ru.otus.hw06.api.core.money;

import ru.otus.hw06.api.core.AbstractMoney;

public class Cash extends AbstractMoney
{
    public Cash( final double quant, final long quantity )
    {
        super( quant, quantity );
    }

	@Override
	public Object copy() {
		return new Cash( this.getQuant(), this.getQuantity() );
	}
}


