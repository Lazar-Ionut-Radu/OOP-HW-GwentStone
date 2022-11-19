package main.cards;

import fileio.CardInput;

import java.util.ArrayList;

public class EnvironmentCard extends AbstractCard {
    public EnvironmentCard(CardInput cardInput) {
        super(cardInput);
    }

    public EnvironmentCard(EnvironmentCard environmentCard) {
        super(environmentCard);
    }
}