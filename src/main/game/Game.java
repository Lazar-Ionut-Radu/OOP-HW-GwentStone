package main.game;

import fileio.GameInput;
import main.cards.*;

import java.util.ArrayList;
import java.util.Random;

import static java.util.Collections.max;
import static java.util.Collections.shuffle;

public class Game
{
    private ArrayList<AbstractCard> playerOneDeck;
    private ArrayList<AbstractCard> playerTwoDeck;
    private ArrayList<AbstractCard> playerOneHand;
    private ArrayList<AbstractCard> playerTwoHand;
    private HeroCard playerOneHero;
    private HeroCard playerTwoHero;
    private final int startingPlayer;
    private int activePlayer;
    private int roundNumber;
    private ArrayList<ArrayList<AbstractCard>> board;
    private int playerOneMana;
    private int playerTwoMana;


    /* Constructor */
    public Game(GameInput gameInput, ArrayList<ArrayList<AbstractCard>> playerOneDecks, ArrayList<ArrayList<AbstractCard>> playerTwoDecks)
    {
        initializeBoard();
        this.startingPlayer = gameInput.getStartGame().getStartingPlayer();
        this.activePlayer = startingPlayer;
        this.playerOneMana = 1;
        this.playerTwoMana = 1;
        this.roundNumber = 1;

        playerOneHero = new HeroCard(gameInput.getStartGame().getPlayerOneHero());
        playerTwoHero = new HeroCard(gameInput.getStartGame().getPlayerTwoHero());

        CardHandler cardHandler = new CardHandler();

        /* Initializes the decks */
        this.playerOneDeck = new ArrayList<>();
        for (int cardIdx = 0; cardIdx < playerOneDecks.get(0).size(); cardIdx++) {
            if (cardHandler.getCardType(playerOneDecks.get(gameInput.getStartGame().getPlayerOneDeckIdx()).get(cardIdx).getName()) == EnumCardType.Minion )
                this.playerOneDeck.add(cardIdx, new MinionCard((MinionCard) playerOneDecks.get(gameInput.getStartGame().getPlayerOneDeckIdx()).get(cardIdx)));
            else
                this.playerOneDeck.add(cardIdx, new EnvironmentCard((EnvironmentCard) playerOneDecks.get(gameInput.getStartGame().getPlayerOneDeckIdx()).get(cardIdx)));
        }
        this.playerTwoDeck = new ArrayList<>();
        for (int cardIdx = 0; cardIdx < playerTwoDecks.get(0).size(); cardIdx++) {
            if (cardHandler.getCardType(playerTwoDecks.get(gameInput.getStartGame().getPlayerTwoDeckIdx()).get(cardIdx).getName()) == EnumCardType.Minion )
                this.playerTwoDeck.add(cardIdx, new MinionCard((MinionCard) playerTwoDecks.get(gameInput.getStartGame().getPlayerTwoDeckIdx()).get(cardIdx)));
            else
                this.playerTwoDeck.add(cardIdx, new EnvironmentCard((EnvironmentCard) playerTwoDecks.get(gameInput.getStartGame().getPlayerTwoDeckIdx()).get(cardIdx)));
        }

        /* Shuffles the decks */
        Random rnd = new Random(gameInput.getStartGame().getShuffleSeed());
        shuffle(this.playerOneDeck, rnd);
        rnd = new Random(gameInput.getStartGame().getShuffleSeed());
        shuffle(this.playerTwoDeck, rnd);

        /* Add the first card in the decks to the players' hands */
        this.playerOneHand = new ArrayList<>();
        this.playerOneHand.add(this.playerOneDeck.get(0));
        this.playerOneDeck.remove(0);

        this.playerTwoHand = new ArrayList<>();
        this.playerTwoHand.add(this.playerTwoDeck.get(0));
        this.playerTwoDeck.remove(0);
    }

    private void initializeBoard()
    {
        board = new ArrayList<>(4);
        for (int numRow = 0; numRow < 4; numRow++) {
            board.add(new ArrayList<>(5));
        }
    }

    /* Setters and getters */
    public ArrayList<AbstractCard> getPlayerOneDeck() {
        return playerOneDeck;
    }

    public void setPlayerOneDeck(ArrayList<AbstractCard> playerOneDeck) {
        this.playerOneDeck = playerOneDeck;
    }

    public ArrayList<AbstractCard> getPlayerTwoDeck() {
        return playerTwoDeck;
    }

    public void setPlayerTwoDeck(ArrayList<AbstractCard> playerTwoDeck) {
        this.playerTwoDeck = playerTwoDeck;
    }

    public HeroCard getPlayerOneHero() {
        return playerOneHero;
    }

    public void setPlayerOneHero(HeroCard playerOneHero) {
        this.playerOneHero = playerOneHero;
    }

    public HeroCard getPlayerTwoHero() {
        return playerTwoHero;
    }

    public void setPlayerTwoHero(HeroCard playerTwoHero) {
        this.playerTwoHero = playerTwoHero;
    }

    public int getStartingPlayer() {
        return startingPlayer;
    }

    public int getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public ArrayList<ArrayList<AbstractCard>> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<ArrayList<AbstractCard>> board) {
        this.board = board;
    }

    public int getPlayerOneMana() {
        return playerOneMana;
    }

    public void setPlayerOneMana(int playerOneMana) {
        this.playerOneMana = playerOneMana;
    }

    public int getPlayerTwoMana() {
        return playerTwoMana;
    }

    public void setPlayerTwoMana(int playerTwoMana) {
        this.playerTwoMana = playerTwoMana;
    }

    public ArrayList<AbstractCard> getPlayerOneHand() {
        return playerOneHand;
    }

    public void setPlayerOneHand(ArrayList<AbstractCard> playerOneHand) {
        this.playerOneHand = playerOneHand;
    }

    public ArrayList<AbstractCard> getPlayerTwoHand() {
        return playerTwoHand;
    }

    public void setPlayerTwoHand(ArrayList<AbstractCard> playerTwoHand) {
        this.playerTwoHand = playerTwoHand;
    }

    /* Methods for playing the game */
    public void changeActivePlayer()
    {
        if (this.playerTwoHero.getHealth() <= 0 || this.playerOneHero.getHealth() <= 0)
            return;

        for (int rowIdx = 0; rowIdx < 4; rowIdx++)
            for (int cardIdx = 0; cardIdx < this.board.get(rowIdx).size(); cardIdx++)
            {
                if (this.activePlayer == 1 && (rowIdx == 0 || rowIdx == 1) )
                    continue;
                if (this.activePlayer == 2 && (rowIdx == 2 || rowIdx == 3) )
                    continue;

                ((MinionCard)(this.board.get(rowIdx).get(cardIdx))).unFreeze();

            }
        if (this.activePlayer == 1)
            this.playerOneHero.setHasAttacked(false);
        else
            this.playerTwoHero.setHasAttacked(false);

        if (this.activePlayer == 1) {
            this.activePlayer = 2;
        }
        else if (this.activePlayer == 2) {
            this.activePlayer = 1;
        }

        for (int rowIdx = 0; rowIdx < 4; rowIdx++)
            for (int cardIdx = 0; cardIdx < this.board.get(rowIdx).size(); cardIdx++)
            {
                if (this.activePlayer == 1 && (rowIdx == 0 || rowIdx == 1) )
                    continue;
                if (this.activePlayer == 2 && (rowIdx == 2 || rowIdx == 3) )
                    continue;

                ((MinionCard)(this.board.get(rowIdx).get(cardIdx))).setHasAttacked(false);
            }

        if (this.activePlayer == this.startingPlayer)
            beginRound();
    }

    private void beginRound()
    {
        if (this.playerTwoHero.getHealth() <= 0 || this.playerOneHero.getHealth() <= 0)
            return;

        this.roundNumber++;
        addManaToPlayers();
        addCardToHand();
    }

    private void addManaToPlayers()
    {
        if (this.playerTwoHero.getHealth() <= 0 || this.playerOneHero.getHealth() <= 0)
            return;

        if (this.roundNumber <= 10) {
            this.playerOneMana += this.roundNumber;
            this.playerTwoMana += this.roundNumber;
        }
        else {
            this.playerOneMana += 10;
            this.playerTwoMana += 10;
        }
    }

    private boolean isDeckEmpty(int playerIdx)
    {
        if (playerIdx == 1)
            return (this.playerOneDeck.size() == 0);
        return (this.playerTwoDeck.size() == 0);
    }

    private void addCardToHand()
    {
        if (this.playerTwoHero.getHealth() <= 0 || this.playerOneHero.getHealth() <= 0)
            return;

        if (!isDeckEmpty(1)) {
            this.playerOneHand.add(this.playerOneDeck.get(0));
            this.playerOneDeck.remove(0);
        }
        if (!isDeckEmpty(2)) {
            this.playerTwoHand.add(this.playerTwoDeck.get(0));
            this.playerTwoDeck.remove(0);
        }
    }

    public int placeCard(int handIdx)
    {
        if (this.playerTwoHero.getHealth() <= 0 || this.playerOneHero.getHealth() <= 0)
            return -100;

        CardHandler cardHandler = new CardHandler();
        AbstractCard card;
        int manaPlayer;
        if (this.activePlayer == 1) {
            if (handIdx >= this.playerOneHand.size())
                return 0;

            card = this.playerOneHand.get(handIdx);
            manaPlayer = this.playerOneMana;
        }
        else {
            if (handIdx >= this.playerTwoHand.size())
                return 0;

            card = this.playerTwoHand.get(handIdx);
            manaPlayer = this.playerTwoMana;
        }

        /* Test to see if the card is environment */
        if (cardHandler.getCardType(card.getName()) == EnumCardType.Environment)
            return -1;

        /* Test to see if the player has enough mana to play the card */
        if (card.getMana() > manaPlayer)
            return -2;

        /* Test to see if the row is full */
        int rowIdx = getRowIdxToPlaceCard(this.activePlayer, card.getName());
        if (board.get(rowIdx).size() == 5)
            return -3;

        board.get(rowIdx).add(card);
        if (this.activePlayer == 1) {
            playerOneHand.remove(handIdx);
            playerOneMana -= card.getMana();
        }
        else {
            playerTwoHand.remove(handIdx);
            playerTwoMana -= card.getMana();
        }

        return 0;
    }

    private int getRowIdxToPlaceCard(int playerIdx, String cardName)
    {
        if (cardName.equals("The Ripper") || cardName.equals("Miraj") || cardName.equals("Goliath") || cardName.equals("Warden")) {
            if (playerIdx == 1)
                return 2;
            else
                return 1;
        }
        else {
            if (playerIdx == 1)
                return 3;
            else
                return 0;
        }
    }

    public int useEnvironmentCard(int handIdx, int affectedRow)
    {
        if (this.playerTwoHero.getHealth() <= 0 || this.playerOneHero.getHealth() <= 0)
            return -100;

        CardHandler cardHandler = new CardHandler();
        AbstractCard card;
        int manaPlayer;
        if (this.activePlayer == 1) {
            if (handIdx >= this.playerOneHand.size())
                return 0;

            card = this.playerOneHand.get(handIdx);
            manaPlayer = this.playerOneMana;
        }
        else {
            if (handIdx >= this.playerTwoHand.size())
                return 0;

            card = this.playerTwoHand.get(handIdx);
            manaPlayer = this.playerTwoMana;
        }

        /* Test to see if the card is environment */
        if (cardHandler.getCardType(card.getName()) != EnumCardType.Environment)
            return -1;

        /* Test to see if the player has enough mana to play the card */
        if (card.getMana() > manaPlayer)
            return -2;

        /* Test to see if the row belongs to the enemy */
        if (this.activePlayer == 1)
            if (affectedRow == 3 || affectedRow == 2)
                return -3;
        if (this.activePlayer == 2)
            if (affectedRow == 1 || affectedRow == 0)
                return -3;

        /* Firestorm ability */
        if (card.getAbility() == EnumAbility.FirestormAbility)
        {
            for (int cardIdx = 0; cardIdx < this.board.get(affectedRow).size(); cardIdx++) {
                ((MinionCard) (this.board.get(affectedRow).get(cardIdx))).takeDamage(1);
            }

            removeDestroyedCards();
        }

        /* Winterfell ability */
        if (card.getName().equals("Winterfell"))
        {
            for (int cardIdx = 0; cardIdx < this.board.get(affectedRow).size(); cardIdx++) {
                ((MinionCard) (this.board.get(affectedRow).get(cardIdx))).freeze();
            }
        }

        /* Heart Hound ability */
        if (card.getAbility() == EnumAbility.HeartHoundAbility)
        {
            int mirroredRow = 0;
            if (affectedRow == 0)
                mirroredRow = 3;
            if (affectedRow == 1)
                mirroredRow = 2;
            if (affectedRow == 2)
                mirroredRow = 1;


            if (this.board.get(mirroredRow).size() == 5)
                return -4;

            int maxHealthMinionIdx = -1;
            int maxHealth = -1;
            for (int cardIdx = 0; cardIdx < this.board.get(affectedRow).size(); cardIdx++) {
                if (maxHealth < ((MinionCard)(this.board.get(affectedRow).get(cardIdx))).getHealth()) {
                    maxHealth = ((MinionCard)(this.board.get(affectedRow).get(cardIdx))).getHealth();
                    maxHealthMinionIdx = cardIdx;
                }
            }
            this.board.get(mirroredRow).add(this.board.get(affectedRow).get(maxHealthMinionIdx));
            this.board.get(affectedRow).remove(maxHealthMinionIdx);

        }

        /* Decrease mana */
        if (this.activePlayer == 1) {
            this.playerOneMana -= card.getMana();
        }
        else {
            this.playerTwoMana -= card.getMana();
        }

        /* Remove card from hand */
        if (activePlayer == 1) {
            this.playerOneHand.remove(handIdx);
        }
        else {
            this.playerTwoHand.remove(handIdx);
        }

        System.out.println(card.getName());
        return 0;
    }

    private void removeDestroyedCards()
    {
        for (int rowIdx = 0; rowIdx < 4; rowIdx++) {
            for (int cardIdx = 0; cardIdx < this.board.get(rowIdx).size(); cardIdx++) {
                if (((MinionCard) (this.board.get(rowIdx).get(cardIdx))).getHealth() <= 0) {
                    this.board.get(rowIdx).remove(cardIdx);
                    cardIdx--;
                }
            }
        }
    }

    private boolean hasTankCardsPlaced(int playerIdx)
    {
        for (int rowIdx = 0; rowIdx < 4; rowIdx++) {
            for (int cardIdx = 0; cardIdx < this.board.get(rowIdx).size(); cardIdx++) {
                if (playerIdx == 1 && (rowIdx == 0 || rowIdx == 1))
                    continue;
                if (playerIdx == 2 && (rowIdx == 2 || rowIdx == 3))
                    continue;

                if (((MinionCard) (this.board.get(rowIdx).get(cardIdx))).isTank())
                    return true;
            }
        }
        return false;
    }

    public int cardUseAttack(int xAttacker, int yAttacker, int xAttacked, int yAttacked)
    {
        if (this.playerTwoHero.getHealth() <= 0 || this.playerOneHero.getHealth() <= 0)
            return -100;

        if (this.activePlayer == 1 && (xAttacked == 2 || xAttacked == 3))
            return -1;
        if (this.activePlayer == 2 && (xAttacked == 1 || xAttacked == 0))
            return -1;

        if (((MinionCard)this.board.get(xAttacker).get(yAttacker)).getHasAttacked())
            return -2;

        if (((MinionCard)this.board.get(xAttacker).get(yAttacker)).isFrozen())
            return -3;

        int otherPlayer;
        if (this.activePlayer == 1)
            otherPlayer = 2;
        else
            otherPlayer = 1;

        if (hasTankCardsPlaced(otherPlayer) && !((MinionCard)this.board.get(xAttacked).get(yAttacked)).isTank())
            return -4;

        int attk = ((MinionCard) this.board.get(xAttacker).get(yAttacker)).getAttackDamage();
        ((MinionCard)this.board.get(xAttacked).get(yAttacked)).takeDamage(attk);
        ((MinionCard) this.board.get(xAttacker).get(yAttacker)).setHasAttacked(true);

        removeDestroyedCards();

        return 0;
    }

    public int cardUsesAbility(int xAttacker, int yAttacker, int xAttacked, int yAttacked)
    {

        if (this.playerTwoHero.getHealth() <= 0 || this.playerOneHero.getHealth() <= 0)
            return -100;

        if (((MinionCard)this.board.get(xAttacker).get(yAttacker)).isFrozen())
            return -1;

        if (((MinionCard)this.board.get(xAttacker).get(yAttacker)).getHasAttacked())
            return -2;

        String attackerName = this.board.get(xAttacker).get(yAttacker).getName();
        if (attackerName.equals("Disciple")) {
            if (this.activePlayer == 1 && (xAttacked == 0 || xAttacked == 1))
                return -3;
            if (this.activePlayer == 2 && (xAttacked == 2 || xAttacked == 3))
                return -3;
        }

        int otherPlayer;
        if (this.activePlayer == 1)
            otherPlayer = 2;
        else
            otherPlayer = 1;
        if (attackerName.equals("The Ripper") || attackerName.equals("Miraj") || attackerName.equals("The Cursed One")) {

            if (this.activePlayer == 1 && (xAttacked == 2 || xAttacked == 3))
                return -4;
            if (this.activePlayer == 2 && (xAttacked == 1 || xAttacked == 0))
                return -4;

            if (hasTankCardsPlaced(otherPlayer) && !((MinionCard)this.board.get(xAttacked).get(yAttacked)).isTank())
                return -5;
        }

        if (attackerName.equals("The Ripper")) {
            int newAttackDamage = ((MinionCard) board.get(xAttacked).get(yAttacked)).getAttackDamage() - 2;
            if (newAttackDamage < 0)
                newAttackDamage = 0;
            ((MinionCard)board.get(xAttacked).get(yAttacked)).setAttackDamage(newAttackDamage);
        }
        if (attackerName.equals("Miraj")) {
            int attackerNewHealth = ((MinionCard) board.get(xAttacked).get(yAttacked)).getHealth();
            int attackedNewHealth = ((MinionCard) board.get(xAttacker).get(yAttacker)).getHealth();
            ((MinionCard)board.get(xAttacker).get(yAttacker)).setHealth(attackerNewHealth);
            ((MinionCard)board.get(xAttacked).get(yAttacked)).setHealth(attackedNewHealth);
        }
        if (attackerName.equals("The Cursed One")) {
            int newAttackDamage = ((MinionCard)board.get(xAttacked).get(yAttacked)).getHealth();
            int newHealth = ((MinionCard)board.get(xAttacked).get(yAttacked)).getAttackDamage();
            ((MinionCard)board.get(xAttacked).get(yAttacked)).setHealth(newHealth);
            ((MinionCard)board.get(xAttacked).get(yAttacked)).setAttackDamage(newAttackDamage);
        }
        if (attackerName.equals("Disciple")) {
            int newHealth = ((MinionCard)board.get(xAttacked).get(yAttacked)).getHealth() + 2;
            ((MinionCard)board.get(xAttacked).get(yAttacked)).setHealth(newHealth);
        }

        ((MinionCard) this.board.get(xAttacker).get(yAttacker)).setHasAttacked(true);
        removeDestroyedCards();

        System.out.println(attackerName);
        return 0;
    }

    public int cardAttacksHero(int xAttacker, int yAttacker)
    {
        if (this.playerTwoHero.getHealth() <= 0 || this.playerOneHero.getHealth() <= 0)
            return -100;

        if (((MinionCard)this.board.get(xAttacker).get(yAttacker)).isFrozen())
            return -1;

        if (((MinionCard)this.board.get(xAttacker).get(yAttacker)).getHasAttacked())
            return -2;

        int otherPlayer;
        if (this.activePlayer == 1)
            otherPlayer = 2;
        else
            otherPlayer = 1;
        if (hasTankCardsPlaced(otherPlayer))
            return -3;

        ((MinionCard) this.board.get(xAttacker).get(yAttacker)).setHasAttacked(true);
        if (otherPlayer == 1) {

            this.playerOneHero.setHealth(this.playerOneHero.getHealth() -  ((MinionCard) this.board.get(xAttacker).get(yAttacker)).getAttackDamage());
            if (this.playerOneHero.getHealth() <= 0)
                return 2;
        }
        else {
            this.playerTwoHero.setHealth(this.playerTwoHero.getHealth() -  ((MinionCard) this.board.get(xAttacker).get(yAttacker)).getAttackDamage());
            if (this.playerTwoHero.getHealth() <= 0)
                return 1;
        }


        return 0;
    }

    public int heroUsesAbility(int affectedRow)
    {
        if (this.playerTwoHero.getHealth() <= 0 || this.playerOneHero.getHealth() <= 0)
            return -100;

        HeroCard hero;
        if (this.activePlayer == 1)
            hero = this.playerOneHero;
        else
            hero = this.playerTwoHero;


        if (this.activePlayer == 1 && hero.getMana() > this.playerOneMana) {
            System.out.println(hero.getMana() + " " + this.playerOneMana);
            return -1;

        }
        if (this.activePlayer == 2 && hero.getMana() > this.playerTwoMana) {
            System.out.println(hero.getMana() + " " + this.playerTwoMana);
            return -1;
        }

        if (this.activePlayer == 1 && this.playerOneHero.getHasAttacked())
            return -2;
        if (this.activePlayer == 2 && this.playerTwoHero.getHasAttacked())
            return -2;

        if (hero.getName().equals("Lord Royce") || hero.getName().equals("Empress Thorina")) {
            if (this.activePlayer == 1 && (affectedRow == 2 || affectedRow == 3))
                return -3;
            if (this.activePlayer == 2 && (affectedRow == 1 || affectedRow == 0))
                return -3;
        }

        if (hero.getName().equals("General Kocioraw") || hero.getName().equals("King Mudface")) {
            if (this.activePlayer == 1 && (affectedRow == 0 || affectedRow == 1))
                return -4;
            if (this.activePlayer == 2 && (affectedRow == 2 || affectedRow == 3))
                return -4;
        }

        if (hero.getName().equals("Lord Royce"))
        {
            int maxDamageMinionIdx = -1;
            int maxDamage = -1;
            for (int cardIdx = 0; cardIdx < this.board.get(affectedRow).size(); cardIdx++) {
                if (maxDamage < ((MinionCard)(this.board.get(affectedRow).get(cardIdx))).getAttackDamage()) {
                    maxDamage = ((MinionCard)(this.board.get(affectedRow).get(cardIdx))).getAttackDamage();
                    maxDamageMinionIdx = cardIdx;
                }
            }

            ((MinionCard)this.board.get(affectedRow).get(maxDamageMinionIdx)).freeze();
        }
        if (hero.getName().equals("Empress Thorina"))
        {
            int maxHealthMinionIdx = -1;
            int maxHealth = -1;
            for (int cardIdx = 0; cardIdx < this.board.get(affectedRow).size(); cardIdx++) {
                if (maxHealth < ((MinionCard)(this.board.get(affectedRow).get(cardIdx))).getHealth()) {
                    maxHealth = ((MinionCard)(this.board.get(affectedRow).get(cardIdx))).getHealth();
                    maxHealthMinionIdx = cardIdx;
                }
            }

            this.board.get(affectedRow).remove(maxHealthMinionIdx);
        }
        if (hero.getName().equals("General Kocioraw"))
        {
            for (int cardIdx = 0; cardIdx < this.board.get(affectedRow).size(); cardIdx++) {
                ((MinionCard) (this.board.get(affectedRow).get(cardIdx))).setAttackDamage(((MinionCard) (this.board.get(affectedRow).get(cardIdx))).getAttackDamage() + 1);
            }
        }
        if (hero.getName().equals("King Mudface"))
        {
            for (int cardIdx = 0; cardIdx < this.board.get(affectedRow).size(); cardIdx++) {
                ((MinionCard) (this.board.get(affectedRow).get(cardIdx))).takeDamage(-1);
            }
        }

        hero.setHasAttacked(true);
        if (this.activePlayer == 1)
            this.playerOneMana -= hero.getMana();
        else
            this.playerTwoMana -= hero.getMana();

        System.out.println(hero.getName());
        return 0;
    }
}
