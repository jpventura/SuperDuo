package com.jpventura.footballscores;

public enum League {
    SERIE_A(357),
    PREMIER_LEAGUE(354),
    CHAMPIONS_LEAGUE(362),
    PRIMERA_DIVISION(358),
    BUNDESLIGA(351);

    League(int league) {
        this.league = league;
    }

    public static League valueOf(int league) {
        try {
            return League.values()[league];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public boolean equals(int league) {
        return league == this.league;
    }

    @Override
    public String toString() {
        switch (league) {
            case 351:
                return "Bundesliga";
            case 354:
                return "Premier League";
            case 357:
                return "Seria A";
            case 358:
                return "Primera Division";
            case 362:
                return "UEFA Champions League";
            default:
                return "Not known League Please report";
        }
    }

    private int league;
}
