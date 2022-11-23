

# Tema POO  - GwentStone

<div align="center"><img src="https://tenor.com/view/witcher3-gif-9340436.gif" width="500px"></div>

#### Assignment Link: [https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/tema](https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/tema)


## Skel Structure

* src/
  * checker/ - checker files
  * fileio/ - contains classes used to read data from the json files
  * main/
      * Main - the Main class runs the checker on your implementation. Add the entry point to your implementation in it. Run Main to test your implementation from the IDE or from command line.
      * Test - run the main method from Test class with the name of the input file from the command line and the result will be written
        to the out.txt file. Thus, you can compare this result with ref.
* input/ - contains the tests in JSON format
* ref/ - contains all reference output for the tests in JSON format

## Tests

1. test01_game_start - 3p
2. test02_place_card - 4p
3. test03_place_card_invalid - 4p
4. test04_use_env_card - 4p
5. test05_use_env_card_invalid - 4p
6. test06_attack_card - 4p
7. test07_attack_card_invalid - 4p
8. test08_use_card_ability - 4p
9. test09_use_card_ability_invalid -4p
10. test10_attack_hero - 4p
11. test11_attack_hero_invalid - 4p
12. test12_use_hero_ability_1 - 4p
13. test13_use_hero_ability_2 - 4p
14. test14_use_hero_ability_invalid_1 - 4p
15. test15_use_hero_ability_invalid_2 - 4p
16. test16_multiple_games_valid - 5p
17. test17_multiple_games_invalid - 6p
18. test18_big_game - 10p


<div align="center"><img src="https://tenor.com/view/homework-time-gif-24854817.gif" width="500px"></div>

## My implementation

The cards are represented by the following classes: 
1. *AbstractCard*. A generic card.
2. *AbstractPlaceableCard*. Extends AbstractCard and represents card that can be placed on the board.
3. *EnvironmentCard*. Extends AbstractCard.
4. *MinionCard*. Extends AbstractPlaceableCard and can be of tank or nontank.
5. *HeroCard*. Extends AbstractPlaceableCard. Represents a player. When a hero dies the game ends.

The *CardHandler* class implements useful method for assigning the correct ability to a card, for checking the types
of the cards given their name and for checking whether they are tanks.

The *Game* class contains all the information necessary for the implementation of a game. It handles the placing of the
cards on the board satisfying the constraints on column index and card type, it handles the passing of the turns and 
rounds, adding mana to the players and adding card to their hand and also implements the use of all the commands 
requested in the homework.

The *GamesHandler* class contains the games themselves and methods for parsing the input/output. It also contains the 
deck selections of the players.