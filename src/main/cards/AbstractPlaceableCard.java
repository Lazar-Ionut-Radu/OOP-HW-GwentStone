package main.cards;

import fileio.CardInput;

import java.util.ArrayList;

abstract class AbstractPlaceableCard extends AbstractCard {
    boolean isFrozen;
    boolean hasAttacked;
    int health;

    public AbstractPlaceableCard(CardInput cardInput) {
        super(cardInput);
        this.health = cardInput.getHealth();
        this.isFrozen = false;
        this.hasAttacked = false;
    }

    public AbstractPlaceableCard(AbstractPlaceableCard placeableCard) {
        super(placeableCard);
        this.isFrozen = false;
        this.hasAttacked = false;
        this.health = placeableCard.health;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    /**
     * Does damage to this card.
     * @param attackDamage The amount of damage dealt.
     */
    protected void takeDamage(int attackDamage) {
        this.health -= attackDamage;
    }

    /**
     * Freezes this card.
     */
    protected void freeze() {
        this.isFrozen = true;
    }

    /**
     * Unfreezes this card.
     */
    protected void unFreeze() {
        this.isFrozen = false;
    }
}