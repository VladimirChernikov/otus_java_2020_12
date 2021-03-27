package ru.otus.hw06.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.otus.hw06.api.core.cell.CashCellFactory;
import ru.otus.hw06.api.core.money.Cash;

class CellTest {

    private Cell cell;

    @BeforeEach
    public void setUp(){
        cell = CashCellFactory.createInfiniteWithMoney( new Cash( 100, 5 ) );
    }

    @Test
    public void addMoney() {
        double balance = cell.getAmount();
        double expected = balance + 300;
        cell.addMoney( new Cash( 100, 3 ) );
        balance = cell.getAmount();
        assertThat( balance ).isEqualTo( expected );
    }

    @Test
    public void subtractMoney() {
        double balance = cell.getAmount();
        double expected = balance - 300;
        cell.subtractMoney( new Cash( 100, 3 ) );
        balance = cell.getAmount();
        assertThat( balance ).isEqualTo( expected );
    }
}
