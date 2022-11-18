package main.cards;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardHandlerTest {

    @Test
    @Order(1)
    @DisplayName("Test getCardType() for invalid card names")
    void getCardTypeForInvalidNames()
    {
        CardHandler cardHandler = new CardHandler();
        assertEquals(cardHandler.getCardType("Gibberish Name"), EnumCardType.None);
    }

    @Test
    @Order(2)
    @DisplayName("Test getCardType() for valid card names")
    void getCardTypeForValidNames()
    {
        CardHandler cardHandler = new CardHandler();
        assertEquals(cardHandler.getCardType("Firestorm"), EnumCardType.Environment, "Environment");
        assertEquals(cardHandler.getCardType("Goliath"), EnumCardType.Minion, "Minion");
        assertEquals(cardHandler.getCardType("Lord Royce"), EnumCardType.Hero, "Hero");
    }

    @Test
    @Order(3)
    @DisplayName("Test getAbility() method for invalid names")
    void getAbilityForInvalidNames()
    {
        CardHandler cardHandler = new CardHandler();
        assertNull(cardHandler.getAbility("Gibberish Name"));
    }

    @Test
    @Order(4)
    @DisplayName("Test getAbility() method for valid names")
    void getAbilityForValidNames()
    {
        CardHandler cardHandler = new CardHandler();
        assertEquals(cardHandler.getAbility("Firestorm"), EnumAbility.FirestormAbility, "Environment");
        assertEquals(cardHandler.getAbility("Goliath"), EnumAbility.None, "Minion without ability");
        assertEquals(cardHandler.getAbility("The Ripper"), EnumAbility.WeakKnees, "Minion with ability");
        assertEquals(cardHandler.getAbility("Empress Thorina"), EnumAbility.LowBlow, "Hero");
    }
}