package main.cards;
import fileio.CardInput;

import java.util.ArrayList;

public class MinionCard extends AbstractPlaceableCard {
    int attackDamage;
    private boolean isTank;

    public MinionCard(CardInput cardInput) {
        super(cardInput);
        this.attackDamage = cardInput.getAttackDamage();

        CardHandler cardHandler = new CardHandler();
        this.isTank = cardHandler.isCardTank(this.name);
    }

    public MinionCard(MinionCard minionCard) {
        super(minionCard);
        this.attackDamage = minionCard.attackDamage;
        this.isTank = minionCard.isTank;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    protected void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public boolean isTank() {
        return isTank;
    }
}