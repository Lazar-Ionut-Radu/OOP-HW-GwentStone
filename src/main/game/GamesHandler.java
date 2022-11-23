package main.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.DecksInput;
import fileio.Input;
import main.cards.*;

import java.util.ArrayList;
import java.util.Objects;

public class GamesHandler
{
    private final ArrayList<ArrayList<ActionsInput>> actions;
    private final ArrayList<ArrayList<AbstractCard>> playerOneDecks;
    private final ArrayList<ArrayList<AbstractCard>> playerTwoDecks;
    private ArrayList<Game> games;
    private int numberOfGamesPlayed;
    private int numberOfWinsPlayerOne;
    private int numberOfWinsPlayerTwo;
    ArrayNode output;

    /* Constructor */
    public GamesHandler(Input input, ArrayNode output)
    {
        this.numberOfGamesPlayed = 0;
        this.numberOfWinsPlayerOne = 0;
        this.numberOfWinsPlayerTwo = 0;

        /* Initialize the decks */
        this.playerOneDecks = initializeDecks(input.getPlayerOneDecks());;
        this.playerTwoDecks = initializeDecks(input.getPlayerTwoDecks());;

        /* Initialize the games */
        this.games = new ArrayList<>();
        for (int gameIdx = 0; gameIdx < input.getGames().size(); gameIdx++)
            this.games.add(gameIdx, new Game(input.getGames().get(gameIdx), this.playerOneDecks, this.playerTwoDecks));

        /* Get the actions for every game */
        this.actions = new ArrayList<>();
        for (int gameIdx = 0; gameIdx < input.getGames().size(); gameIdx++)
            this.actions.add(gameIdx, input.getGames().get(gameIdx).getActions());

        this.output = output;
    }

    private ArrayList<ArrayList<AbstractCard>> initializeDecks(DecksInput decksInput)
    {
        /* Instantiate the arrays */
        ArrayList<ArrayList<AbstractCard>> decks = new ArrayList<>();
        for (int deckIdx = 0; deckIdx < decksInput.getNrDecks(); deckIdx++)
            decks.add(deckIdx, new ArrayList<>());

        /* Add the cards in the decks */
        CardHandler cardHandler = new CardHandler();
        for (int deckIdx = 0; deckIdx < decksInput.getNrDecks(); deckIdx++) {
            for (int cardIdx = 0; cardIdx < decksInput.getNrCardsInDeck(); cardIdx++) {
                /* Information necessary for creating the card objects */
                CardInput cardInput = decksInput.getDecks().get(deckIdx).get(cardIdx);
                EnumCardType cardType = cardHandler.getCardType(cardInput.getName());
                EnumAbility cardAbility = cardHandler.getAbility(cardInput.getName());

                /* Creates the cards and adds them to the decks */
                if (cardType == EnumCardType.Minion) {
                    decks.get(deckIdx).add(cardIdx, new MinionCard(cardInput));
                }
                else if (cardType == EnumCardType.Environment) {
                    decks.get(deckIdx).add(cardIdx, new EnvironmentCard(cardInput));
                }
            }
        }

        return decks;
    }

    /* Setters and getters */
    public int getNumberOfGamesPlayed() {
        return numberOfGamesPlayed;
    }

    public void setNumberOfGamesPlayed(int numberOfGamesPlayed) {
        this.numberOfGamesPlayed = numberOfGamesPlayed;
    }

    public int getNumberOfWinsPlayerOne() {
        return numberOfWinsPlayerOne;
    }

    public void setNumberOfWinsPlayerOne(int numberOfWinsPlayerOne) {
        this.numberOfWinsPlayerOne = numberOfWinsPlayerOne;
    }

    public int getNumberOfWinsPlayerTwo() {
        return numberOfWinsPlayerTwo;
    }

    public void setNumberOfWinsPlayerTwo(int numberOfWinsPlayerTwo) {
        this.numberOfWinsPlayerTwo = numberOfWinsPlayerTwo;
    }

    /* Game loop(s) */
    public ArrayNode gameLoop()
    {
        ObjectMapper objectMapper = new ObjectMapper();

        for (int gameIdx = 0; gameIdx < this.games.size(); gameIdx++) {
            for (int actionIdx = 0; actionIdx < this.actions.get(gameIdx).size(); actionIdx++) {
                executeAction(objectMapper, this.actions.get(gameIdx).get(actionIdx), this.games.get(gameIdx));
            }
        }

        return this.output;
    }

    /* Methods for executing actions. */
    private void executeAction(ObjectMapper objectMapper, ActionsInput action, Game game)
    {
        String actionName = action.getCommand();

        ObjectNode objectNode = switch (actionName) {
            case "getPlayerDeck" -> executeGetDeckAction(objectMapper, action, game);
            case "getPlayerHero" -> executeGetHeroAction(objectMapper, action, game);
            case "getPlayerTurn" -> executeGetTurnAction(objectMapper, action, game);
            case "getCardsInHand" -> executeGetHandAction(objectMapper, action, game);
            case "getPlayerMana" -> executeGetManaAction(objectMapper, action, game);
            case "placeCard" -> executePlaceCardAction(objectMapper, action, game);
            case "endPlayerTurn" -> executeEndTurnAction(game);
            case "getCardsOnTable" -> executeGetCardsOnTableAction(objectMapper, action, game);
            case "useEnvironmentCard" -> executeUseEnvironmentCard(objectMapper, action, game);
            case "getEnvironmentCardsInHand" -> executeGetEnvironmentInHand(objectMapper, action, game);
            case "getCardAtPosition" -> executeGetCardAtPosition(objectMapper, action, game);
            case "getFrozenCardsOnTable" -> executeGetFrozenOnTableAction(objectMapper, action, game);
            case "cardUsesAttack" -> executeCardUsesAttackAction(objectMapper, action, game);
            case "cardUsesAbility" -> executeCardUsesAbilityAction(objectMapper, action, game);
            case "useAttackHero" -> executeUseAttackHeroAction(objectMapper, action, game);
            case "useHeroAbility" -> executeUseHeroAbilityAction(objectMapper, action, game);
            case "getPlayerOneWins" -> executeGetPlayerOneWinsAction(objectMapper);
            case "getPlayerTwoWins" -> executeGetPlayerTwoWinsAction(objectMapper);
            case "getTotalGamesPlayed" -> executeGetTotalGamesPlayedAction(objectMapper);
            default -> objectMapper.createObjectNode(); /* Idk how to write an empty one */
        };

        if (objectNode != null)
            this.output.add(objectNode);

    }

    private ObjectNode executeGetDeckAction(ObjectMapper objectMapper, ActionsInput action, Game game)
    {
        ArrayList<AbstractCard> deck = new ArrayList<>();
        if (action.getPlayerIdx() == 1)
            deck = game.getPlayerOneDeck();
        else if (action.getPlayerIdx() == 2)
            deck = game.getPlayerTwoDeck();

        CardHandler cardHandler = new CardHandler();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (var card: deck) {
            arrayNode.add(cardHandler.cardToObjectNode(card, objectMapper));
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getPlayerDeck");
        objectNode.put("playerIdx", action.getPlayerIdx());
        objectNode.set("output", arrayNode);

        return objectNode;
    }

    private ObjectNode executeGetHeroAction(ObjectMapper objectMapper, ActionsInput action, Game game)
    {
        HeroCard heroCard;
        if (action.getPlayerIdx() == 1)
            heroCard = game.getPlayerOneHero();
        else
            heroCard = game.getPlayerTwoHero();

        ObjectNode objectNode = objectMapper.createObjectNode();
        CardHandler cardHandler = new CardHandler();
        objectNode.put("command", "getPlayerHero");
        objectNode.put("playerIdx", action.getPlayerIdx());
        objectNode.put("output", cardHandler.cardToObjectNode(heroCard, objectMapper));
        return objectNode;
    }

    private ObjectNode executeGetTurnAction(ObjectMapper objectMapper, ActionsInput action, Game game)
    {
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "getPlayerTurn");
        objectNode.put("output", game.getActivePlayer());

        return objectNode;
    }

    private ObjectNode executeGetHandAction(ObjectMapper objectMapper, ActionsInput action, Game game)
    {
        ArrayList<AbstractCard> hand = new ArrayList<>();
        if (action.getPlayerIdx() == 1)
            hand = game.getPlayerOneHand();
        else if (action.getPlayerIdx() == 2)
            hand = game.getPlayerTwoHand();

        CardHandler cardHandler = new CardHandler();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (var card: hand)
            arrayNode.add(cardHandler.cardToObjectNode(card, objectMapper));

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getCardsInHand");
        objectNode.put("playerIdx", action.getPlayerIdx());
        objectNode.set("output", arrayNode);

        return objectNode;
    }

    private ObjectNode executeGetManaAction(ObjectMapper objectMapper, ActionsInput action, Game game)
    {
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "getPlayerMana");
        objectNode.put("playerIdx", action.getPlayerIdx());

        if (action.getPlayerIdx() == 1)
            objectNode.put("output", game.getPlayerOneMana());
        else
            objectNode.put("output", game.getPlayerTwoMana());

        return objectNode;
    }

    private ObjectNode executePlaceCardAction(ObjectMapper objectMapper, ActionsInput action, Game game)
    {
        int error = game.placeCard(action.getHandIdx());
        if (error == 0)
            return null;

        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "placeCard");
        objectNode.put("handIdx", action.getHandIdx());

        int handIdx = action.getHandIdx();
        int playerIdx = game.getActivePlayer();

        if (error == -1)
            objectNode.put("error", "Cannot place environment card on table.");
        if (error == -2)
            objectNode.put("error", "Not enough mana to place card on table.");
        if (error == -3)
            objectNode.put("error", "Cannot place card on table since row is full.");

        return objectNode;
    }

    private ObjectNode executeEndTurnAction(Game game)
    {
        game.changeActivePlayer();

        return null;
    }

    private ObjectNode executeGetCardsOnTableAction(ObjectMapper objectMapper, ActionsInput action, Game game)
    {
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command",  "getCardsOnTable");

        ArrayNode arrayNode = objectMapper.createArrayNode();
        CardHandler cardHandler = new CardHandler();
        for (int rowIdx = 0; rowIdx < 4; rowIdx++) {
            ArrayNode arrayNodeRow = objectMapper.createArrayNode();
            for (int cardIdx = 0; cardIdx < game.getBoard().get(rowIdx).size(); cardIdx++)
                arrayNodeRow.add(cardHandler.cardToObjectNode(game.getBoard().get(rowIdx).get(cardIdx), objectMapper));
            arrayNode.add(arrayNodeRow);
        }
        objectNode.set("output", arrayNode);

        return objectNode;
    }

    private ObjectNode executeUseEnvironmentCard(ObjectMapper objectMapper, ActionsInput action, Game game)
    {
        int error = game.useEnvironmentCard(action.getHandIdx(), action.getAffectedRow());
        if (error == 0)
            return null;

        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("affectedRow", action.getAffectedRow());
        objectNode.put("command", "useEnvironmentCard");


        int handIdx = action.getHandIdx();
        int playerIdx = game.getActivePlayer();

        if (error == -1)
            objectNode.put("error", "Chosen card is not of type environment.");
        if (error == -2)
            objectNode.put("error", "Not enough mana to use environment card.");
        if (error == -3)
            objectNode.put("error", "Chosen row does not belong to the enemy.");
        if (error == -4)
            objectNode.put("error", "Cannot steal enemy card since the player's row is full.");

        objectNode.put("handIdx", action.getHandIdx());
        return objectNode;
    }

    private ObjectNode executeGetEnvironmentInHand(ObjectMapper objectMapper, ActionsInput action, Game game)
    {
        ArrayList<AbstractCard> hand = new ArrayList<>();
        if (action.getPlayerIdx() == 1)
            hand = game.getPlayerOneHand();
        else if (action.getPlayerIdx() == 2)
            hand = game.getPlayerTwoHand();

        CardHandler cardHandler = new CardHandler();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (var card: hand)
            if (cardHandler.getCardType(card.getName()) == EnumCardType.Environment)
                arrayNode.add(cardHandler.cardToObjectNode(card, objectMapper));

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getEnvironmentCardsInHand");
        objectNode.put("playerIdx", action.getPlayerIdx());
        objectNode.set("output", arrayNode);

        return objectNode;
    }

    private ObjectNode executeGetCardAtPosition(ObjectMapper objectMapper, ActionsInput action, Game game)
    {
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "getCardAtPosition");
        objectNode.put("x", action.getX());
        objectNode.put("y", action.getY());

        CardHandler cardHandler = new CardHandler();
        if (game.getBoard().get(action.getX()).size() >= action.getY()) {
            objectNode.put("output", cardHandler.cardToObjectNode(game.getBoard().get(action.getX()).get(action.getY()), objectMapper));
        }
        else {
            objectNode.put("output", "No card available at that position.");
        }
        return objectNode;
    }

    private ObjectNode executeGetFrozenOnTableAction(ObjectMapper objectMapper, ActionsInput action, Game game)
    {
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command",  "getFrozenCardsOnTable");

        ArrayNode arrayNode = objectMapper.createArrayNode();
        CardHandler cardHandler = new CardHandler();
        for (int rowIdx = 0; rowIdx < 4; rowIdx++) {
            ArrayNode arrayNodeRow = objectMapper.createArrayNode();
            for (int cardIdx = 0; cardIdx < game.getBoard().get(rowIdx).size(); cardIdx++)
                if (((MinionCard)game.getBoard().get(rowIdx).get(cardIdx)).isFrozen())
                    arrayNodeRow.add(cardHandler.cardToObjectNode(game.getBoard().get(rowIdx).get(cardIdx), objectMapper));
            arrayNode.add(arrayNodeRow);
        }

        if (arrayNode.toString().equals("[[],[],[],[]]"))
            arrayNode = objectMapper.createArrayNode();

        objectNode.set("output", arrayNode);

        return objectNode;
    }

    private ObjectNode executeCardUsesAttackAction(ObjectMapper objectMapper, ActionsInput action, Game game)
    {
        int error = game.cardUseAttack(action.getCardAttacker().getX(), action.getCardAttacker().getY(),
                action.getCardAttacked().getX(), action.getCardAttacked().getY());
        if (error == 0)
            return null;

        ObjectNode objectNode = objectMapper.createObjectNode();

        if (error == 1) {
            objectNode.put("gameEnded", "Player one killed the enemy hero.");
            return objectNode;
        }
        if (error == 2) {
            objectNode.put("gameEnded", "Player two killed the enemy hero.");
        }

        int handIdx = action.getHandIdx();
        int playerIdx = game.getActivePlayer();

        objectNode.put("command", "cardUsesAttack");
        ObjectNode objectNode1 = objectMapper.createObjectNode();
        objectNode1.put("x", action.getCardAttacker().getX());
        objectNode1.put("y", action.getCardAttacker().getY());
        objectNode.put("cardAttacker", objectNode1);
        ObjectNode objectNode2 = objectMapper.createObjectNode();
        objectNode2.put("x", action.getCardAttacked().getX());
        objectNode2.put("y", action.getCardAttacked().getY());
        objectNode.put("cardAttacked", objectNode2);

        if (error == -1)
            objectNode.put("error", "Attacked card does not belong to the enemy.");
        if (error == -2)
            objectNode.put("error", "Attacker card has already attacked this turn.");
        if (error == -3)
            objectNode.put("error", "Attacker card is frozen.");
        if (error == -4)
            objectNode.put("error", "Attacked card is not of type 'Tank'.");

        return objectNode;
    }

    private ObjectNode executeCardUsesAbilityAction(ObjectMapper objectMapper, ActionsInput action, Game game)
    {
        int error = game.cardUsesAbility(action.getCardAttacker().getX(), action.getCardAttacker().getY(),
                action.getCardAttacked().getX(), action.getCardAttacked().getY());
        if (error == 0)
            return null;

        ObjectNode objectNode = objectMapper.createObjectNode();

        int handIdx = action.getHandIdx();
        int playerIdx = game.getActivePlayer();

        objectNode.put("command", "cardUsesAbility");
        ObjectNode objectNode1 = objectMapper.createObjectNode();
        objectNode1.put("x", action.getCardAttacker().getX());
        objectNode1.put("y", action.getCardAttacker().getY());
        objectNode.put("cardAttacker", objectNode1);
        ObjectNode objectNode2 = objectMapper.createObjectNode();
        objectNode2.put("x", action.getCardAttacked().getX());
        objectNode2.put("y", action.getCardAttacked().getY());
        objectNode.put("cardAttacked", objectNode2);

        if (error == -1)
            objectNode.put("error", "Attacker card is frozen.");
        if (error == -2)
            objectNode.put("error", "Attacker card has already attacked this turn.");
        if (error == -3)
            objectNode.put("error", "Attacked card does not belong to the current player.");
        if (error == -4)
            objectNode.put("error", "Attacked card does not belong to the enemy.");
        if (error == -5)
            objectNode.put("error", "Attacked card is not of type 'Tank'.");
        return objectNode;
    }

    private ObjectNode executeUseAttackHeroAction(ObjectMapper objectMapper, ActionsInput action, Game game)
    {
        int error = game.cardAttacksHero(action.getCardAttacker().getX(), action.getCardAttacker().getY());
        if (error == 0)
            return null;

        if (error == 1) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("gameEnded", "Player one killed the enemy hero.");
            this.numberOfWinsPlayerOne++;

            return objectNode;
        }
        if (error == 2) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("gameEnded", "Player two killed the enemy hero.");
            this.numberOfWinsPlayerTwo++;

            return  objectNode;
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "useAttackHero");
        ObjectNode objectNode1 = objectMapper.createObjectNode();
        objectNode1.put("x", action.getCardAttacker().getX());
        objectNode1.put("y", action.getCardAttacker().getY());
        objectNode.put("cardAttacker", objectNode1);

        if (error == -1)
            objectNode.put("error", "Attacker card is frozen.");
        if (error == -2)
            objectNode.put("error", "Attacker card has already attacked this turn.");
        if (error == -3)
            objectNode.put("error", "Attacked card is not of type 'Tank'.");

        return objectNode;
    }

    private ObjectNode executeUseHeroAbilityAction(ObjectMapper objectMapper, ActionsInput action, Game game)
    {
        int error = game.heroUsesAbility(action.getAffectedRow());
        if (error == 0)
            return null;

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "useHeroAbility");

        objectNode.put("affectedRow", action.getAffectedRow());

        if (error == -1)
            objectNode.put("error", "Not enough mana to use hero's ability.");
        if (error == -2)
            objectNode.put("error", "Hero has already attacked this turn.");
        if (error == -3)
            objectNode.put("error", "Selected row does not belong to the enemy.");
        if (error == -4)
            objectNode.put("error", "Selected row does not belong to the current player.");

        return objectNode;
    }

    private ObjectNode executeGetTotalGamesPlayedAction(ObjectMapper objectMapper)
    {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getTotalGamesPlayed");
        objectNode.put("output", this.numberOfWinsPlayerOne + this.numberOfWinsPlayerTwo);

        return objectNode;
    }

    private ObjectNode executeGetPlayerOneWinsAction(ObjectMapper objectMapper)
    {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getPlayerOneWins");
        objectNode.put("output", this.numberOfWinsPlayerOne);

        return objectNode;
    }

    private ObjectNode executeGetPlayerTwoWinsAction(ObjectMapper objectMapper)
    {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "getPlayerTwoWins");
        objectNode.put("output", this.numberOfWinsPlayerTwo);

        return objectNode;
    }
}
