package main.cards;
import fileio.CardInput;

/**
 * A minion card.
 */
public class MinionCard extends AbstractPlaceableCard {
    int attackDamage;
    private final boolean isTank;

    /**
     * Constructor using the input.
     * @param cardInput Input for a card.
     */
    public MinionCard(CardInput cardInput) {
        super(cardInput);
        this.attackDamage = cardInput.getAttackDamage();

        CardHandler cardHandler = new CardHandler();
        this.isTank = cardHandler.isCardTank(this.name);
    }

    /**
     * Copy constructor.
     * @param minionCard Minion card.
     */
    public MinionCard(MinionCard minionCard) {
        super(minionCard);
        this.attackDamage = minionCard.attackDamage;
        this.isTank = minionCard.isTank;
    }

    /**
     * @return The attack damage.
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * @param attackDamage The attack damage.
     */
    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * @return Boolean value that specifies if the minion card is tank.
     */
    public boolean isTank() {
        return isTank;
    }
}
