/**
 * a factory class that creates the different player types
 */
public class PlayerFactory {

    /**
     * creates diffearent player types
     *
     * @param playerType the player type to create
     * @return a new instance of the desired player else null;
     */
    public Player buildPlayer(String playerType) {
        switch (playerType) {

            case "human":
                return new HumanPlayer();
            case "whatever":
                return new WhateverPlayer();
            case "clever":
                return new CleverPlayer();
            case "snartypamts":
                return new SnartypamtsPlayer();
            default:
                return null;

        }
    }
}
