package main.cards;
import java.util.ArrayList;

public class HeroCard extends AbstractPlaceableCard {
    public final int heroInitialHealth = 30;

    public HeroCard(HeroCard heroCard)
    {
        super(heroCard);
        this.health = heroInitialHealth;
    }
    public HeroCard(String name, String description, ArrayList<String> colors, EnumAbility ability, int mana) {
        super(name, description, colors, ability, mana);
        this.health = heroInitialHealth;
    }
}