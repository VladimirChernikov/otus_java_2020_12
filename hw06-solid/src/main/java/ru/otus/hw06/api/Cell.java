package ru.otus.hw06.api;

public interface Cell extends Copyable
{
    public void addQuantity(final int quantity) throws RuntimeException;
    public void subtractQuantity(final int quantity) throws RuntimeException;
    public long getAmount();
    public int getQuantity();
    public int getMaxQuantity();
    public Banknote getReferenceBanknote();
    public int getReferenceBanknoteNominale();
}


