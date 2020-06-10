package states;

import entities.Entity;
import observers.GameObserver;
import scripts.GameManager;

public class StateRolling implements GameState {
    GameManager gameManager;

    public StateRolling(GameManager gm) {
        gameManager = gm;
    }

    /**
     * Method to replicate the roll of a die
     * @return - Returns a random number between 1 and 6
     */
    public int roll() {
        int roll = (int)(Math.random() * 6) + 1;
        gameManager.gameObserver.broadcast(GameObserver.DiceEvents.DICE_ROLLED, roll);
        return roll;
    }

    @Override
    public void handle() {

    }

    @Override
    public void onClick(Entity clicked) {

    }

    @Override
    public void onSpace() {
        int roll1 = roll();
        int roll2 = roll();

        System.out.print("You rolled: ");
        System.out.println(roll1 + roll2);

        if(roll1 + roll2 == 7) {
            gameManager.setGameState(new StateStealing());
        } else {
            // TODO: Reward the players sitting on the appropriate tiles
            gameManager.setGameState(new StateSettling());
        }
    }
}
