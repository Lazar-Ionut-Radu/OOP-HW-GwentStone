package main.cards;
import fileio.CardInput;

import java.util.ArrayList;

public class HeroCard extends AbstractPlaceableCard {
    /**
     * The health of a hero card at the beginning of the game.
     */
    public final int heroInitialHealth = 30;

    /**
     * Constructor for a hero card using the input given.
     * @param cardInput Input for the card.
     */
    public HeroCard(CardInput cardInput) {
        super(cardInput);
        this.health = 30;
    }

    /**
     * Copy constructor.
     * @param heroCard Another hero card.
     */
    public HeroCard(HeroCard heroCard)
    {
        super(heroCard);
        this.health = heroInitialHealth;
    }
}