package ru.otus.hw06.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.otus.hw06.api.core.cash.RubbleBanknote;
import ru.otus.hw06.api.core.cell.CashCellFactory;

class CellTest {

    private Cell cell;
    private Banknote referenceBanknote;

    @BeforeEach
    public void setUp(){
        this.referenceBanknote = RubbleBanknote.RUB_100;
        this.cell = CashCellFactory.createInfiniteWithReferenceBanknote( this.referenceBanknote );
        this.cell.addQuantity(5);
    }

    @Test
    public void addMoney() {
        long balance = cell.getAmount();
        int qty = 3;
        long expected = balance + qty * referenceBanknote.getNominale();
        cell.addQuantity( qty );
        balance = cell.getAmount();
        assertThat( balance ).isEqualTo( expected );
    }

    @Test
    public void subtractMoney() {
        long balance = cell.getAmount();
        int qty = 3;
        long expected = balance - qty * referenceBanknote.getNominale();
        cell.subtractQuantity( qty );
        balance = cell.getAmount();
        assertThat( balance ).isEqualTo( expected );
    }
}
