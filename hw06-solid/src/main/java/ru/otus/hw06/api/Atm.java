package ru.otus.hw06.api;

import java.util.List;

public interface Atm
{
    public void feedBanknotes( final List<Banknote> banknotes );
    public void feedSameBanknote( final Banknote banknote, final int quantity );
    public List<Banknote> takeAmount( final long amount );
    public long getTotalBanknoteQuantity();
    public long getBalance();
}


