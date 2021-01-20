package states;

import entities.Entity;
import entities.Player;
import observers.GameObserver.*;
import scripts.GameManager;

public class StateRolling implements GameState {
    /**
     * Method to replicate the roll of a die
     * @return - Returns a random number between 1 and 6
     */
    public int roll(GameManager context) {
        int roll = (int)(Math.random() * 6) + 1;
        context.gameObserver.broadcast(DiceEvents.DICE_ROLLED, roll);
        return roll;
    }

    @Override
    public GameStates getStateName() {
        return GameStates.ROLLING;
    }

    @Override
    public void onClick(GameManager context, Entity clicked, Player player) {

    }

    @Override
    public void onSpace(GameManager context) {
        int roll1 = roll(context);
        int roll2 = roll(context);

        System.out.print("You rolled: ");
        System.out.println(roll1 + roll2);

        if(roll1 + roll2 == 7) {
            context.setGameState(new StateStealing());
        } else {
            context.rewardPlayerNearTile(roll1 + roll2);
            context.setGameState(new StateSettling());
        }
    }
}
