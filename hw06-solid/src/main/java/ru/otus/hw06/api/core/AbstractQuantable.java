package ru.otus.hw06.api.core;

import ru.otus.hw06.api.Quantable;

public abstract class AbstractQuantable implements Quantable
{
    private final double quant;
    private long quantity;
    private long maxQuantity;
    private long minQuantity;

    public AbstractQuantable( final double quant, final long quantity, final long minQuantity, final long maxQuantity ) {
        this.quant = quant;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
    }

    public AbstractQuantable( final double quant, final long quantity ) {
        this( quant, quantity, Long.MIN_VALUE, Long.MAX_VALUE );
    }

    @Override
    public double getQuant() {
        return this.quant;
    }

    @Override
    public long getQuantity() {
        return this.quantity;
    }

    @Override
    public void setQuantity(final long newQuantity) {
        if ( newQuantity >= this.getMinQuantity() && newQuantity <= this.getMaxQuantity() ) {
            this.quantity = newQuantity;
        }
        else {
            throw new RuntimeException("New quantity = " + newQuantity + " is out of bounds [" + this.getMinQuantity() + "; " + this.getMaxQuantity() + "]");
        }
    }

    @Override
    public long getMaxQuantity() {
        return this.maxQuantity;
    }

    @Override
    public long getMinQuantity() {
        return this.minQuantity;
    }

    @Override
    public void setMaxQuantity(final long newMaxQuantity) {
        if ( this.getMaxQuantity() != Long.MAX_VALUE ) {
            if ( newMaxQuantity >= this.getMaxQuantity() ) {
                this.maxQuantity = newMaxQuantity;
            }
            else {
                throw new RuntimeException("New max quantity " + newMaxQuantity + " must not be less than existing " + this.getMaxQuantity() );
            }
        }
    }

    @Override
    public void setMinQuantity(final long newMinQuantity) {
        if ( this.getMinQuantity() != Long.MIN_VALUE ) {
            if ( newMinQuantity <= this.getMinQuantity() ) {
                this.minQuantity = newMinQuantity;
            }
            else {
                throw new RuntimeException("New min quantity " + newMinQuantity + " must not be greater than existing " + this.getMinQuantity() );
            }
        }
    }

}


