package main.cards;
import fileio.CardInput;

import java.util.ArrayList;

public class HeroCard extends AbstractPlaceableCard {
    public final int heroInitialHealth = 30;

    public HeroCard(CardInput cardInput) {
        super(cardInput);
        this.health = 30;
    }

    public HeroCard(HeroCard heroCard)
    {
        super(heroCard);
        this.health = heroInitialHealth;
    }
}