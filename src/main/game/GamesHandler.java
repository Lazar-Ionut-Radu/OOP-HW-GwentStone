package main.game;

import fileio.CardInput;
import fileio.DecksInput;
import fileio.Input;
import main.cards.*;

import java.util.ArrayList;

public class GamesHandler
{
    ArrayList<ArrayList<AbstractCard>> playerOneDecks;
    ArrayList<ArrayList<AbstractCard>> playerTwoDecks;
    ArrayList<Game> games;

    public GamesHandler(Input input)
    {
        /* Initialize the decks */
        initializeDecks(this.playerOneDecks, input.getPlayerOneDecks());
        initializeDecks(this.playerTwoDecks, input.getPlayerTwoDecks());

        /* Initialize the games */
        this.games = new ArrayList<>();
        for (int gameIdx = 0; gameIdx < input.getGames().size(); gameIdx++)
            this.games.add(gameIdx, new Game(input.getGames().get(gameIdx), this.playerOneDecks, this.playerTwoDecks));
    }

    private void initializeDecks(ArrayList<ArrayList<AbstractCard>> decks, DecksInput decksInput)
    {
        /* Instantiate the arrays */
        decks = new ArrayList<>();
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
    }
}
