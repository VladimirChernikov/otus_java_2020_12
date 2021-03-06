package ru.otus.hw06.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.otus.hw06.api.core.atm.CashCellAtmFactory;
import ru.otus.hw06.api.core.cell.CashCellFactory;
import ru.otus.hw06.api.core.money.CashFactory;

class AtmTest {

    // Объект класса АТМ 
    private Atm atm;
    @BeforeEach
    void setupAnEmptyAtm(){
        List<Cell> cells = new ArrayList<>();
        cells.add( CashCellFactory.createInfiniteWithMoney( CashFactory.createNoneWithNominal(  50 ) ) );
        cells.add( CashCellFactory.createInfiniteWithMoney( CashFactory.createNoneWithNominal( 100 ) ) );
        cells.add( CashCellFactory.createInfiniteWithMoney( CashFactory.createNoneWithNominal( 200 ) ) );
        cells.add( CashCellFactory.createInfiniteWithMoney( CashFactory.createNoneWithNominal( 500 ) ) );
        cells.add( CashCellFactory.createInfiniteWithMoney( CashFactory.createNoneWithNominal( 1000 ) ) );
        cells.add( CashCellFactory.createInfiniteWithMoney( CashFactory.createNoneWithNominal( 2000 ) ) );
        atm = CashCellAtmFactory.createWithCells( cells );
    }

    @DisplayName("Принять банкноты разных номиналов")
    @Test
    void takeDifferentNominals() {
        double expectedValue = 50 + 100 + 200 + 500 + 1000 + 2000;
        
        Money cash = CashFactory.createSingleWithNominal(  50 );
        atm.addMoney( cash );
        cash.setQuantity(100);

        atm.addMoney( CashFactory.createSingleWithNominal( 100 ) );
        atm.addMoney( CashFactory.createSingleWithNominal( 200 ) );
        atm.addMoney( CashFactory.createSingleWithNominal( 500 ) );
        atm.addMoney( CashFactory.createSingleWithNominal( 1000 ) );
        atm.addMoney( CashFactory.createSingleWithNominal( 2000 ) );

        assertThat( expectedValue ).isEqualTo( atm.getAmount() );
    }

    @DisplayName("Выдать запрошенную сумму минимальным количеством банкнот")
    @Test
    void giveRequestedAmount() {
        double initialCount = 300 + 60 + 12 + 3 + 2;
        double expectedValue = initialCount - 2 - 3 - 3 - 1;

        atm.addMoney( CashFactory.createWithNominal( 50, 300 ) ) ;
        atm.addMoney( CashFactory.createWithNominal( 100, 60 ) ) ;
        atm.addMoney( CashFactory.createWithNominal( 500, 12 ) ) ;
        atm.addMoney( CashFactory.createWithNominal( 1000, 3 ) ) ;
        atm.addMoney( CashFactory.createWithNominal( 2000, 2 ) ) ;

        assertThat( initialCount ).isEqualTo( atm.getTotalMoneyCount() );

        atm.takeAmount( 8550 );
        assertThat( expectedValue ).isEqualTo( atm.getTotalMoneyCount() );
    }

    @DisplayName("Выдать ошибку если сумму нельзя выдать")
    @Test
    void giveRequestedAmountError() {
        atm.putAmount( 1200 );

        Exception exceptionGreater = assertThrows(RuntimeException.class, () -> {
            atm.takeAmount( 1300 );
        });
        assertTrue( exceptionGreater.getMessage().contains( "amount is greater than existing" ) );

        Exception exceptionUnfitted = assertThrows(RuntimeException.class, () -> {
            atm.takeAmount( 1120 );
        });
        assertTrue( exceptionUnfitted.getMessage().contains( "could not be given from cells" ) );

    }

    @DisplayName("Выдать сумму остатка денежных средств")
    @Test
    void getBalance() {
        Double expectedValue = 12300.00;
        atm.putAmount( expectedValue );
        Double balance = atm.getAmount();
        assertThat( expectedValue ).isEqualTo( balance );
    }

}
