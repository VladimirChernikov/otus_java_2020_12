package ru.otus.hw06.api;

public interface Quantable
{
    public double getQuant();
    public long getQuantity();
    public void setQuantity( final long newQuantity );

    public long getMaxQuantity();
    public long getMinQuantity();
    public void setMaxQuantity( final long newMaxQuantity );
    public void setMinQuantity( final long newMinQuantity );
}


