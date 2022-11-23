package main.cards;

import fileio.CardInput;

import java.util.ArrayList;

/**
 * Environment card. Cannot be placed on the board, but its ability can be used in that turn.
 */
public class EnvironmentCard extends AbstractCard {
    /**
     * Constructor for an environment card using the input given.
     * @param cardInput Input for the card.
     */
    public EnvironmentCard(CardInput cardInput) {
        super(cardInput);
    }

    /**
     * Copy constructor.
     * @param environmentCard Another environment card.
     */
    public EnvironmentCard(EnvironmentCard environmentCard) {
        super(environmentCard);
    }
}