package ru.otus.hw06.api.core;

import ru.otus.hw06.api.Banknote;
import ru.otus.hw06.api.Cell;

public abstract class AbstractCell implements Cell
{
    private final Banknote referenceBanknote;
    private int quantity;
    private final int maxQuantity;

    public AbstractCell( final Banknote referenceBanknote, final int maxQuantity )
    {
        this.quantity = 0;
        this.referenceBanknote = referenceBanknote;
        this.maxQuantity = maxQuantity;
    }

    @Override
    public void addQuantity(final int quantity) {
        if ( quantity >= 0 ) {
            final var requiredQuantity = this.quantity + quantity;
            if ( requiredQuantity <= this.maxQuantity ) {
                this.quantity = requiredQuantity;
            }
            else {
                throw new RuntimeException( String.format("Required requiredQuantity = %d  is greater than cell maxQuantity = %d", requiredQuantity, this.maxQuantity) );
            }
        }
        else {
            throw new RuntimeException( String.format("Quantity to add should not be negative. Supplied quantity is = %d", quantity) );
        }
    }

    @Override
    public void subtractQuantity(final int quantity) {
        if ( quantity >= 0 ) {
            final var requiredQuantity = this.quantity - quantity;
            if ( requiredQuantity >= 0 ) {
                this.quantity = requiredQuantity;
            }
            else {
                throw new RuntimeException( String.format("Required requiredQuantity = %d is greater than current cell quantity = %d", requiredQuantity, this.quantity) );
            }
        }
        else {
            throw new RuntimeException( String.format("Quantity to subtract should not be negative. Supplied quantity is = %d", quantity) );
        }
    }

    @Override
    public long getAmount() {
        return this.quantity * this.referenceBanknote.getNominale();
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public int getMaxQuantity() {
        return this.maxQuantity;
    }

    @Override
    public Banknote getReferenceBanknote() {
        return this.referenceBanknote;
    }

    @Override
    public int getReferenceBanknoteNominale() {
        return this.getReferenceBanknote().getNominale();
    }

    @Override
    public String toString() {
        return String.format("Cell info: amount = %d and nominale = %d", this.getAmount(), this.getReferenceBanknote());
    }

}


