package main.cards;
import java.util.ArrayList;

public class MinionCard extends AbstractPlaceableCard {
    int attackDamage;
    private boolean isTank;

    public MinionCard(MinionCard minionCard) {
        super(minionCard);
        this.attackDamage = minionCard.attackDamage;
        this.isTank = minionCard.isTank;
    }

    public MinionCard(String name, String description, ArrayList<String> colors, EnumAbility ability, int mana,
                      int health, int attackDamage, boolean isTank) {
        super(name, description, colors, ability, mana, health);
        this.attackDamage = attackDamage;
        this.isTank = isTank;
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