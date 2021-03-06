package ru.otus.hw06.api.core;

import ru.otus.hw06.api.Cell;
import ru.otus.hw06.api.Money;

public abstract class AbstractCell extends AbstractMoney implements Cell
{
    private final Money money;

    public AbstractCell( final Money initialMoney, final long capacity )
    {
        super( initialMoney.getQuant(), 0, 0, capacity );
        this.money = initialMoney;
    }

    @Override
    public void addMoney(final Money money) throws RuntimeException {
        final var requiredQuantity = money.getQuantity() + this.money.getQuantity();
        if ( requiredQuantity <= this.getMaxQuantity() ) {
            this.money.addMoney(money);
        }
        else {
            throw new RuntimeException( "Required requiredQuantity = " + requiredQuantity + " is greater than cell maxQuantity = " + this.getMaxQuantity() );
        }
    }

    @Override
    public double getAmount() {
        return this.money.getAmount();
    }

    @Override
    public long getQuantity() {
        return this.money.getQuantity();
    }

    @Override
    public Money subtractMoney(final Money money) throws RuntimeException {
        return this.money.subtractMoney(money);
    }

    @Override
    public Money copyMoney(final long quantity) {
        Money result = (Money)this.money.copy();
        result.setQuantity( quantity );
        return result;
    }

    @Override
    public Money getMoney() {
        return this.money;
    }

    @Override
    public String toString() {
        return "Cell info: amount = " + this.getAmount() + " and quant = " + this.getQuant();
    }

}


