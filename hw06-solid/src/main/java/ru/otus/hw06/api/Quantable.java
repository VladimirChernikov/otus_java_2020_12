package ru.otus.hw06.api;

public interface Quantable
{
    public double getQuant();
    public long getQuantity();
    public void setQuantity( final long newQuantity ) throws RuntimeException;

    public long getMaxQuantity();
    public long getMinQuantity();
    public void setMaxQuantity( final long newMaxQuantity ) throws RuntimeException;
    public void setMinQuantity( final long newMinQuantity ) throws RuntimeException;
}


