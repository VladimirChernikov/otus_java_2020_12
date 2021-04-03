package ru.otus.hw06.api;

import java.util.List;

public interface Atm
{
    public void feedBanknotes( final List<Banknote> banknotes ) throws RuntimeException;
    public void feedSameBanknote( final Banknote banknote, final int quantity ) throws RuntimeException;
    public List<Banknote> takeAmount( final long amount ) throws RuntimeException;
    public long getTotalBanknoteQuantity();
    public long getBalance();
}


