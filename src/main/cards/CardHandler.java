package main.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that contains useful methods for identifying cards.
 */
public final class CardHandler
{
    /**
     * List of all the environment cards.
     */
    public final ArrayList<String> environmentCardList;
    /**
     * List of all the tank minion cards.
     */
    public final ArrayList<String> tankMinionCardList;
    /**
     * List of all the non-tank minion cards.
     */
    public final ArrayList<String> nonTankMinionCardList;
    /**
     * List of all the hero cards.
     */
    public final ArrayList<String> heroCardList;
    /**
     * Hashmap that pairs the name of the card to its ability.
     */
    public final HashMap<String, EnumAbility> nameToAbilityHashMap;

    /**
     * Constructor that initiates all the lists.
     */
    public CardHandler() {
        this.environmentCardList = new ArrayList<>();
        this.environmentCardList.add("Firestorm");
        this.environmentCardList.add("Winterfell");
        this.environmentCardList.add("Heart Hound");

        this.tankMinionCardList = new ArrayList<>();
        this.tankMinionCardList.add("Goliath");
        this.tankMinionCardList.add("Warden");

        this.nonTankMinionCardList = new ArrayList<>();
        this.nonTankMinionCardList.add("Sentinel");
        this.nonTankMinionCardList.add("Berserker");
        this.nonTankMinionCardList.add("The Ripper");
        this.nonTankMinionCardList.add("Miraj");
        this.nonTankMinionCardList.add("The Cursed One");
        this.nonTankMinionCardList.add("Disciple");

        this.heroCardList = new ArrayList<>();
        this.heroCardList.add("Lord Royce");
        this.heroCardList.add("Empress Thorina");
        this.heroCardList.add("King Mudface");
        this.heroCardList.add("General Kocioraw");

        this.nameToAbilityHashMap = new HashMap<>();
        /* Minion Cards */
        this.nameToAbilityHashMap.put("Sentinel", EnumAbility.None);
        this.nameToAbilityHashMap.put("Berserker", EnumAbility.None);
        this.nameToAbilityHashMap.put("Goliath", EnumAbility.None);
        this.nameToAbilityHashMap.put("Warden", EnumAbility.None);
        this.nameToAbilityHashMap.put("The Ripper", EnumAbility.WeakKnees);
        this.nameToAbilityHashMap.put("Miraj", EnumAbility.Skyjack);
        this.nameToAbilityHashMap.put("The Cursed One", EnumAbility.Shapeshift);
        this.nameToAbilityHashMap.put("Disciple", EnumAbility.GodsPlan);
        /* Environment Cards */
        this.nameToAbilityHashMap.put("Firestorm", EnumAbility.FirestormAbility);
        this.nameToAbilityHashMap.put("Winterfell", EnumAbility.WinterfellAbility);
        this.nameToAbilityHashMap.put("Heart Hound", EnumAbility.HeartHoundAbility);
        /* Hero Cards */
        this.nameToAbilityHashMap.put("Lord Royce", EnumAbility.SubZero);
        this.nameToAbilityHashMap.put("Empress Thorina", EnumAbility.LowBlow);
        this.nameToAbilityHashMap.put("King Mudface", EnumAbility.EarthBorn);
        this.nameToAbilityHashMap.put("General Kocioraw", EnumAbility.BloodThirst);
    }

    /**
     * Returns the type of the card.
     * @param name The name of the card.
     * @return The type of the card.
     */
    public EnumCardType getCardType(String name) {
        if (this.environmentCardList.contains(name))
            return EnumCardType.Environment;
        if (this.heroCardList.contains(name))
            return EnumCardType.Hero;
        if (this.nonTankMinionCardList.contains(name) || this.tankMinionCardList.contains(name))
            return EnumCardType.Minion;
        return EnumCardType.None;
    }

    /**
     * Returns the ability of a card.
     * @param name The name of the card.
     * @return The ability of the card.
     */
    public EnumAbility getAbility(String name) {
        if (getCardType(name) == EnumCardType.None)
            return null;
        return this.nameToAbilityHashMap.get(name);
    }

    /**
     * Returns a boolean that specifies if a card is a tank.
     * @param name The name of the card.
     * @return Return a boolean that specifies if a card is a tank.
     */
    public boolean isCardTank(String name) {
        if (getCardType(name) != EnumCardType.Minion)
            return false;
        else {
            return !this.nonTankMinionCardList.contains(name);
        }
    }

    /**
     * Returns an object node representation of a card.
     * @param card A card that will be converted to object node
     * @param objectMapper Useful
     * @return An object node
     */
    public ObjectNode cardToObjectNode(AbstractCard card, ObjectMapper objectMapper) {
        ObjectNode cardObjectNode = objectMapper.createObjectNode();

        cardObjectNode.put("mana", card.getMana());

        if (getCardType(card.getName()) == EnumCardType.Minion)
            cardObjectNode.put("attackDamage", ((MinionCard)card).getAttackDamage());
        if (getCardType(card.getName()) == EnumCardType.Minion)
            cardObjectNode.put("health", ((MinionCard)card).getHealth());

        cardObjectNode.put("description", card.getDescription());

        ArrayNode colorsArrayNode = objectMapper.createArrayNode();
        for (int colorIdx = 0; colorIdx < card.getColors().size(); colorIdx++)
            colorsArrayNode.add(card.getColors().get(colorIdx));
        cardObjectNode.set("colors", colorsArrayNode);

        cardObjectNode.put("name", card.getName());
        if (getCardType(card.getName()) == EnumCardType.Hero)
            cardObjectNode.put("health", ((HeroCard)card).getHealth());

        return cardObjectNode;
    }
}
