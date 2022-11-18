package main.cards;

import java.util.ArrayList;

public class EnvironmentCard extends AbstractCard {
    public EnvironmentCard() {
    }

    public EnvironmentCard(EnvironmentCard environmentCard) {
        super(environmentCard);
    }

    public EnvironmentCard(String name, String description, ArrayList<String> colors, EnumAbility ability, int mana) {
        super(name, description, colors, ability, mana);
    }
}