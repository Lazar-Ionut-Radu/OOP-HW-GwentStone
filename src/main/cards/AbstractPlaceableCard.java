package main.cards;

import fileio.CardInput;

/**
 * A card that can be placed. Either minion or tank.
 */
abstract class AbstractPlaceableCard extends AbstractCard {
    boolean isFrozen;
    boolean hasAttacked;
    /**
     * The health of the card.
     */
    int health;

    /**
     * Constructor using the input card types.
     * @param cardInput Card input
     */
    public AbstractPlaceableCard(CardInput cardInput) {
        super(cardInput);
        this.health = cardInput.getHealth();
        this.isFrozen = false;
        this.hasAttacked = false;
    }

    /**
     * Copy constructor
     * @param placeableCard Another placeable card.
     */
    public AbstractPlaceableCard(AbstractPlaceableCard placeableCard) {
        super(placeableCard);
        this.isFrozen = false;
        this.hasAttacked = false;
        this.health = placeableCard.health;
    }

    /**
     * Return a boolean representing whether the card is frozen or not.
     * @return Is frozen parameter.
     */
    public boolean isFrozen() {
        return isFrozen;
    }

    /**
     * Does damage to this card.
     * @param attackDamage The amount of damage dealt.
     */
    public void takeDamage(int attackDamage) {
        this.health -= attackDamage;
    }

    /**
     * Returns a boolean representing whether the card has attacked this turn.
     * @return Has attacked boolean.
     */
    public boolean getHasAttacked() {
        return hasAttacked;
    }

    /**
     * Sets the has attacked field so that a card cannot attack multiple times in a turn.
     * @param hasAttacked Has attacked boolean
     */
    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    /**
     * Freezes this card.
     */
    public void freeze() {
        this.isFrozen = true;
    }

    /**
     * Unfreezes this card.
     */
    public void unFreeze() {
        this.isFrozen = false;
    }

    /**
     * @return The health of this card.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Modifies the health of this card.
     * @param health New health.
     */
    public void setHealth(int health) {
        this.health = health;
    }
}