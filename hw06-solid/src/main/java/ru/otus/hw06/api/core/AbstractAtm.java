package ru.otus.hw06.api.core;

import java.util.Collection;
import java.util.Map;

import ru.otus.hw06.api.Atm;
import ru.otus.hw06.api.Cell;
import ru.otus.hw06.api.Money;

public abstract class AbstractAtm extends AbstractMoney implements Atm
{

    private final Map< Double,  Cell > cells;

    public AbstractAtm( final Map< Double, Cell > cells )
    {
        super(0, 0);
        this.cells = cells;
    }

    @Override
    public double getAmount() {
        double result = 0;
        for ( final Money cell : this.cells.values() ) {
            result += cell.getAmount();
        }
        return result;
    }

    @Override
    public void addMoney(final Money money) throws RuntimeException {
        this.cells.get( money.getQuant() ).addMoney( money );
    }

    @Override
    public Money subtractMoney(final Money money) throws RuntimeException {
        return this.cells.get( money.getQuant() ).subtractMoney( money );
    }

    @Override
    public Collection<Cell> getCells() {
        return this.cells.values();
    }

    @Override
    public long getTotalMoneyCount() {
        long result = 0;
        for ( var cell : this.getCells() ) {
            result += cell.getQuantity();
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder();
        info.append( "Atm info: amount " + this.getAmount() );
        info.append( " and cells:" );
        for ( var cell : this.getCells() ) {
            info.append( " "+ cell );
        }
        return info.toString();
    }

    @Override
    public void putAmount( final double amount ) throws RuntimeException {
        if ( putAmountSequential( amount, true ) == 0 ) {
            putAmountSequential( amount, false );
        }
        else {
            throw new RuntimeException( "Required exact amount " + amount + " could not be fitted into cells. " + this.toString() );
        }
    }

    @Override
    public double takeAmount( final double amount ) throws RuntimeException {
        double odd = amount;
        if ( amount < this.getAmount() )  {
            if ( takeAmountSequential( amount, true ) == 0 ) {
                takeAmountSequential( amount, false );
            }
            else {
                throw new RuntimeException( "Required exact amount " + amount + " could not be given from cells. " + this.toString() );
            }
        }
        else {
            throw new RuntimeException( "Required amount is greater than existing: " + amount + " vs " + this.getAmount() );
        }
        return amount - odd;
    }

    private double putAmountSequential( final double amount, boolean testRun ) throws RuntimeException {
        double odd = amount;
        for ( Cell cell : this.getCells() ) {
            final long quantity = (long)( odd / cell.getQuant() );
            final long remainingCapacity = Math.min( quantity, cell.getMaxQuantity() - cell.getQuantity() );
            if ( remainingCapacity > 0 ) {
                if ( !testRun ) {
                    cell.addMoney( cell.copyMoney( remainingCapacity ) );
                }
                odd -= cell.getQuant() * remainingCapacity;
            }
        }
        return odd;
    }


    private double takeAmountSequential( final double amount, boolean testRun ) throws RuntimeException {
        double odd = amount;
        for ( Cell cell : this.getCells() ) {
            final long quantity = (long)( odd / cell.getQuant() );
            final long takenCapacity = Math.min( quantity, cell.getQuantity() );
            if ( takenCapacity > 0 ) {
                if ( !testRun ) {
                    cell.subtractMoney( cell.copyMoney( takenCapacity ) );
                }
                odd -= cell.getQuant() * takenCapacity;
            }
        }
        return odd;
    }

}


