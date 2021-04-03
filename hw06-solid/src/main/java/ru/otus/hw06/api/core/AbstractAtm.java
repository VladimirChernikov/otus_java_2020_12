package ru.otus.hw06.api.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.otus.hw06.api.Atm;
import ru.otus.hw06.api.Banknote;
import ru.otus.hw06.api.Cell;

public abstract class AbstractAtm implements Atm
{

    private final Map< Banknote,  List<Cell> > cells;

    public AbstractAtm( final Map< Banknote, List<Cell> > cells )
    {
        this.cells = cells;
    }

    @Override
    public long getBalance() {
        long result = 0;
        for ( var cell : this.getCells() ) {
            result += cell.getAmount();
        }
        return result;
    }

    @Override
    public long getTotalBanknoteQuantity() {
        long result = 0;
        for ( var cell : this.getCells() ) {
            result += cell.getQuantity();
        }
        return result;
    }

    @Override
    public void feedBanknotes(List<Banknote> banknotes) throws RuntimeException {
        long processed = 0;
        for (int i = 0; i < banknotes.size(); i++) {
            var cells = this.cells.get( banknotes.get(i) );
            if ( cells != null ) {
                for (int j = 0; j < cells.size() && processed == i; j++)  {
                    var cell = cells.get(j);
                    if ( cell.getMaxQuantity() > cell.getQuantity() ) {
                        cell.addQuantity(1);
                        processed++;
                    }
                }
            }
            if ( processed-1 != i ) {
                throw new RuntimeException( String.format( "Have no capacity for banknotes starting from index %d of %s. %s", i, banknotes.toString(), this.toString() ) ) ;
            }
        }
    }

    @Override
    public void feedSameBanknote(Banknote banknote, int quantity) throws RuntimeException {
        this.feedBanknotes( Collections.nCopies( quantity, banknote ) );
    }

    @Override
    public List<Banknote> takeAmount(long amount) throws RuntimeException {
        List<Banknote> result;
        if ( amount < this.getBalance() )  {
            if ( takeAmountSequential( amount, true ).stream().mapToLong( Banknote::getNominale ).sum() == amount ) {
                result = takeAmountSequential( amount, false );
            }
            else {
                throw new RuntimeException( String.format("Required exact amount %d could not be given from cells.", amount) );
            }
        }
        else {
            throw new RuntimeException( String.format("Required amount is greater than existing: %d vs %d", amount, this.getBalance() ) );
        }
        return result;
    }

    private Collection<Cell> getCells() {
        return this.cells.values().stream().flatMap( List::stream ).collect(Collectors.toList());
    }

    private List<Banknote> takeAmountSequential( final double amount, boolean testRun ) throws RuntimeException {
        List<Banknote> result = new ArrayList<>();
        double odd = amount;
        for ( Cell cell : this.getCells() ) {
            final int quantity = (int)( odd / cell.getReferenceBanknoteNominale() );
            final int takenCapacity = Math.min( quantity, cell.getQuantity() );
            if ( takenCapacity > 0 ) {
                if ( !testRun ) {
                    cell.subtractQuantity( takenCapacity );
                }
                result.addAll( Collections.nCopies( takenCapacity, cell.getReferenceBanknote() ) );
                odd -= cell.getReferenceBanknoteNominale() * takenCapacity;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder();
        info.append( "Atm info: balance " + this.getBalance() );
        info.append( " and cells:" );
        for ( var cell : this.getCells() ) {
            info.append( " "+ cell );
        }
        return info.toString();
    }

}


