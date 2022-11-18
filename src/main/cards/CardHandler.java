package main.cards;

import java.util.ArrayList;
import java.util.HashMap;

public final class CardHandler
{
    public final ArrayList<String> environmentCardList;
    public final ArrayList<String> tankMinionCardList;
    public final ArrayList<String> nonTankMinionCardList;
    public final ArrayList<String> heroCardList;
    public final HashMap<String, EnumAbility> nameToAbilityHashMap;

    public CardHandler()
    {
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
        /* Enviroment Cards */
        this.nameToAbilityHashMap.put("Firestorm", EnumAbility.FirestormAbility);
        this.nameToAbilityHashMap.put("Winterfall", EnumAbility.WinterfellAbility);
        this.nameToAbilityHashMap.put("Heart Hound", EnumAbility.HeartHoundAbility);
        /* Hero Cards */
        this.nameToAbilityHashMap.put("Lord Royce", EnumAbility.SubZero);
        this.nameToAbilityHashMap.put("Empress Thorina", EnumAbility.LowBlow);
        this.nameToAbilityHashMap.put("King Mudface", EnumAbility.EarthBorn);
        this.nameToAbilityHashMap.put("General Kocioraw", EnumAbility.BloodThirst);
    }

    public EnumCardType getCardType(String name)
    {
        if (this.environmentCardList.contains(name))
            return EnumCardType.Environment;
        if (this.heroCardList.contains(name))
            return EnumCardType.Hero;
        if (this.nonTankMinionCardList.contains(name) || this.tankMinionCardList.contains(name))
            return EnumCardType.Minion;
        return EnumCardType.None;
    }

    public EnumAbility getAbility(String name)
    {
        if (getCardType(name) == EnumCardType.None)
            return null;
        return this.nameToAbilityHashMap.get(name);
    }

    public boolean isCardTank(String name)
    {
        if (getCardType(name) != EnumCardType.Minion)
            return false;
        else {
            if (this.nonTankMinionCardList.contains(name))
                return false;
            return true;
        }
    }
}
