package ui;

import entities.Player;
import objects.GameScript;
import ui.components.PlayerPortrait;

import java.util.HashMap;

public class PlayerUI extends GameScript {

    HashMap<Player, PlayerPortrait> players = new HashMap<>();

    public void trackPlayer(Player context)
    {
        PlayerPortrait portrait = new PlayerPortrait(players.size() * 200);
        players.put(context, portrait);
        getScene().getUiManager().getContainer().add(portrait, portrait.getUIConstraints());
    }

    public void setActivePlayer(Player context) {
        players.forEach((player, portrait) -> {
            portrait.setActiveBorder(false);
        });
        PlayerPortrait activePlayer = players.get(context);
        activePlayer.setActiveBorder(true);
    }

    public void updatePlayerColor(Player context) {
        PlayerPortrait selectedPlayer = players.get(context);
        if(null != selectedPlayer) {
            selectedPlayer.setColorFromResource(context.getColor());
        }
    }

    public void untrackPlayer(Player context) {
        PlayerPortrait portrait = players.get(context);
        getScene().getUiManager().getContainer().children.remove(portrait);
        players.remove(context);
    }

    @Override
    public void destroy() {

    }
}
