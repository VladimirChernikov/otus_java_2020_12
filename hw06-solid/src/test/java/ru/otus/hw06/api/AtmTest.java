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
import ru.otus.hw06.api.core.cash.RubbleBanknote;
import ru.otus.hw06.api.core.cell.CashCellFactory;

class AtmTest {

    // Объект класса АТМ 
    private Atm atm;
    @BeforeEach
    void setupAnEmptyAtm(){
        List<Cell> cells = CashCellFactory.createInfiniteWithEachReferenceBanknote(
                List.of( 
                     RubbleBanknote.RUB_50  
                    ,RubbleBanknote.RUB_100 
                    ,RubbleBanknote.RUB_200 
                    ,RubbleBanknote.RUB_500 
                    ,RubbleBanknote.RUB_1000
                    ,RubbleBanknote.RUB_2000
                    ,RubbleBanknote.RUB_5000
                    ) );
        atm = CashCellAtmFactory.createWithCells( cells );
    }

    @DisplayName("Принять банкноты разных номиналов")
    @Test
    void takeDifferentNominals() {
        long expectedValue = 50 + 100 + 200 + 500 + 1000 + 2000;

        List<Banknote> banknotes = new ArrayList<>( 
                List.of( 
                     RubbleBanknote.RUB_50  
                    ,RubbleBanknote.RUB_100 
                    ,RubbleBanknote.RUB_200 
                    ,RubbleBanknote.RUB_500 
                    ,RubbleBanknote.RUB_1000
                    ,RubbleBanknote.RUB_2000
                    ) );
        
        atm.feedBanknotes( banknotes );

        assertThat( expectedValue ).isEqualTo( atm.getBalance() );
    }

    @DisplayName("Выдать запрошенную сумму минимальным количеством банкнот")
    @Test
    void giveRequestedAmount() {
        double initialCount = 300 + 60 + 12 + 3 + 2;
        double expectedValue = initialCount - 2 - 3 - 3 - 1;

        atm.feedSameBanknote( RubbleBanknote.RUB_50, 300 );
        atm.feedSameBanknote( RubbleBanknote.RUB_100, 60 );
        atm.feedSameBanknote( RubbleBanknote.RUB_500, 12 );
        atm.feedSameBanknote( RubbleBanknote.RUB_1000, 3 );
        atm.feedSameBanknote( RubbleBanknote.RUB_2000, 2 );

        assertThat( initialCount ).isEqualTo( atm.getTotalBanknoteQuantity() );

        atm.takeAmount( 8550 );
        assertThat( expectedValue ).isEqualTo( atm.getTotalBanknoteQuantity() );
    }

    @DisplayName("Выдать ошибку если сумму нельзя выдать")
    @Test
    void giveRequestedAmountError() {
        atm.feedSameBanknote( RubbleBanknote.RUB_100, 12 );

        Exception exceptionGreater = assertThrows(RuntimeException.class, () -> {
            atm.takeAmount( 1300 );
        });
        assertTrue( exceptionGreater.getMessage().contains( "amount is greater than existing" ) );

        Exception exceptionUnfitted = assertThrows(RuntimeException.class, () -> {
            atm.takeAmount( 1120 );
        });
        assertThat( exceptionUnfitted.getMessage() ).containsPattern( "could not be given from cells" ) ;

    }

    @DisplayName("Выдать сумму остатка денежных средств")
    @Test
    void getBalance() {
        List<Banknote> banknotes = new ArrayList<>( 
                List.of( 
                     RubbleBanknote.RUB_100 
                    ,RubbleBanknote.RUB_200 
                    ,RubbleBanknote.RUB_2000
                    ,RubbleBanknote.RUB_5000
                    ,RubbleBanknote.RUB_5000
                    ) );
        Long expectedValue = banknotes.stream().mapToLong( Banknote::getNominale ).sum();
        atm.feedBanknotes( banknotes );
        Long balance = atm.getBalance();
        assertThat( expectedValue ).isEqualTo( balance );
    }

}
