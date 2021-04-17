package ru.otus.hw06.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.otus.hw06.api.core.money.Cash;

class MoneyTest {

    private Money cash;

    @BeforeEach
    public void setUp(){
        cash = new Cash( 100, 5 );
    }

    @Test
    public void addMoney() {
        double balance = cash.getAmount();
        double expected = balance + 300;
        cash.addMoney( new Cash( 100, 3 ) );
        balance = cash.getAmount();
        assertThat( balance ).isEqualTo( expected );
    }

    @Test
    public void subtractMoney() {
        double balance = cash.getAmount();
        double expected = balance - 300;
        cash.subtractMoney( new Cash( 100, 3 ) );
        balance = cash.getAmount();
        assertThat( balance ).isEqualTo( expected );
    }
}
