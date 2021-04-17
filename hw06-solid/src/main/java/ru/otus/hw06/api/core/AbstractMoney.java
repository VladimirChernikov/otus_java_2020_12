package ru.otus.hw06.api.core;

import ru.otus.hw06.api.Money;

public abstract class AbstractMoney extends AbstractQuantable implements Money
{
    public AbstractMoney( final double quant, final long quantity ) {
        super( quant, quantity, 0L, Long.MAX_VALUE );
    }

    public AbstractMoney( final double quant, final long quantity, final long minQuantity, final long maxQuantity ) {
        super( quant, quantity, minQuantity, maxQuantity );
    }

    @Override
    public double getAmount() {
        return this.getQuant() * this.getQuantity();
    }

    @Override
    public void addMoney(final Money money) {
        if ( this.isQuantEquals( money ) ) {
            this.setQuantity( this.getQuantity() + money.getQuantity() );
        }
        else {
            throw new RuntimeException( "Cannot accept money " + money + ". Please, supply money with quant = " + this.getQuant() );
        }
    }

    @Override
    public Money subtractMoney(final Money money) {
        if ( this.isQuantEquals( money ) ) {
            if ( money.getQuantity() <= this.getQuantity() ) {
                this.setQuantity( this.getQuantity() - money.getQuantity() );
            }
            else {
                throw new RuntimeException( "Required amount = " + money.getAmount() + " is too much. Existing amount = " + this.getAmount() );
            }
        }
        else {
            throw new RuntimeException( "Cannot accept money " + money + ". Please, supply money with quant = " + this.getQuant() );
        }
        return (Money)money.copy();
    }

    @Override
    public String toString() {
        return super.toString() + ": amount = " + this.getAmount() + " with quant = " + this.getQuant();
    }

    private boolean isQuantEquals( final Money money ) {
        return money.getQuant() == this.getQuant();
    }

}


