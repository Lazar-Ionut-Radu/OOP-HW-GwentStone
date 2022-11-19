package main.cards;
import fileio.CardInput;

import java.util.ArrayList;

public abstract class AbstractCard {
    String name;
    String description;
    ArrayList<String> colors;
    EnumAbility ability;
    int mana;

    public AbstractCard(CardInput cardInput) {
        this.name = cardInput.getName();
        this.description = cardInput.getDescription();
        this.colors = cardInput.getColors();
        this.mana = cardInput.getMana();

        CardHandler cardHandler = new CardHandler();
        this.ability = cardHandler.getAbility(this.name);
    }

    public AbstractCard(AbstractCard card) {
        this.name = card.name;
        this.description = card.description;
        this.colors = new ArrayList<>();
        this.colors.addAll(card.colors);
        this.mana = card.mana;
        this.ability = card.ability;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    protected void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public int getMana() {
        return mana;
    }

    protected void setMana(int mana) {
        this.mana = mana;
    }

    public EnumAbility getAbility() {
        return ability;
    }

    public void setAbility(EnumAbility ability) {
        this.ability = ability;
    }
}