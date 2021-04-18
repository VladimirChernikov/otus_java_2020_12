package ru.otus.hw06.api.core.cash;

import ru.otus.hw06.api.Banknote;

public enum RubbleBanknote implements Banknote {
    RUB_50(50),
    RUB_100(100),
    RUB_200(200),
    RUB_500(500),
    RUB_1000(1000),
    RUB_2000(2000),
    RUB_5000(5000);

    final int nominale;

    RubbleBanknote(int nominale) {
        this.nominale = nominale;
    }

    public int getNominale() {
        return this.nominale;
    }

}
