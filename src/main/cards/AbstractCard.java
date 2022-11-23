package main.cards;
import fileio.CardInput;
import java.util.ArrayList;


/**
 * Represent a generic card that can be either an environment, minion or hero card.
 */
public abstract class AbstractCard {
    /**
     * The name of the card.
     */
    final String name;
    /**
     * The description of the card (basically useless).
     */
    final String description;
    /**
     * The colors on the design of the card (basically useless).
     */
    final ArrayList<String> colors;
    /**
     * The ability of the card.
     */
    final EnumAbility ability;
    /**
     * The mana cost of the mana.
     */
    int mana;

    /**
     * Constructor using the input.
     * @param cardInput The card as given in the input.
     */
    public AbstractCard(final CardInput cardInput) {
        this.name = cardInput.getName();
        this.description = cardInput.getDescription();
        this.colors = cardInput.getColors();
        this.mana = cardInput.getMana();

        CardHandler cardHandler = new CardHandler();
        this.ability = cardHandler.getAbility(this.name);
    }

    /**
     * Copy constructor.
     * @param card: Another card.
     */
    public AbstractCard(final AbstractCard card) {
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

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     * @return The mana cost of the card.
     */
    public int getMana() {
        return mana;
    }

    public EnumAbility getAbility() {
        return ability;
    }
}