package main.game;

import fileio.ActionsInput;
import fileio.CardInput;
import fileio.GameInput;
import main.cards.AbstractCard;
import main.cards.CardHandler;
import main.cards.HeroCard;
import java.util.ArrayList;

public class Game
{
    ArrayList<ActionsInput> actions;
    ArrayList<AbstractCard> playerOneDeck;
    ArrayList<AbstractCard> playerTwoDeck;
    HeroCard playerOneHero;
    HeroCard playerTwoHero;
    int startingPlayer;
    ArrayList<ArrayList<AbstractCard>> board;
    int playerOneMana;
    int playerTwoMana;
    int manaIncrement;

    public Game() {
    }

    public Game(GameInput gameInput, ArrayList<ArrayList<AbstractCard>> playerOneDecks, ArrayList<ArrayList<AbstractCard>> playerTwoDecks)
    {
        initializeBoard();
        this.startingPlayer = gameInput.getStartGame().getStartingPlayer();
        this.actions = gameInput.getActions();

        playerOneHero = new HeroCard(gameInput.getStartGame().getPlayerOneHero());
        playerTwoHero = new HeroCard(gameInput.getStartGame().getPlayerTwoHero());

        this.playerOneDeck = new ArrayList<>();
        for (int cardIdx = 0; cardIdx < playerOneDecks.get(0).size(); cardIdx++) {
            this.playerOneDeck.add(cardIdx, playerOneDecks.get(gameInput.getStartGame().getPlayerOneDeckIdx()).get(cardIdx));
        }
        this.playerTwoDeck = new ArrayList<>();
        for (int cardIdx = 0; cardIdx < playerTwoDecks.get(0).size(); cardIdx++) {
            this.playerTwoDeck.add(cardIdx, playerTwoDecks.get(gameInput.getStartGame().getPlayerTwoDeckIdx()).get(cardIdx));
        }

        this.playerOneMana = 0;
        this.playerTwoMana = 0;
        this.manaIncrement = 1;
    }

    private void initializeBoard()
    {
        board = new ArrayList<>(4);
        for (int numRow = 0; numRow < 4; numRow++) {
            board.add(new ArrayList<>(5));
        }
    }
}
