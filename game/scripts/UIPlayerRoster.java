package scripts;

import entities.Player;
import objects.GameScript;
import ui.*;
import ui.constraints.AspectConstraint;
import ui.constraints.CenterConstraint;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;

import java.awt.*;
import java.util.ArrayList;

public class UIPlayerRoster extends GameScript {
    ArrayList<Player> players;

    public UIPlayerRoster(ArrayList<Player> players) {
        this.players = players;
    }

    @Override
    public void stop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void initialize() {
//        box = new UIQuad();
//        box.setColor(UIColor.GREEN);
//        UIConstraints constraints = new UIConstraints()
//                .setX(new PixelConstraint(20, UIDimensions.DIRECTION_LEFT))
//                .setY(new PixelConstraint(20, UIDimensions.DIRECTION_TOP))
//                .setWidth(new RelativeConstraint(0.2f))
//                .setHeight(new AspectConstraint(1));
//        getScene().getUiManager().getContainer().add(box, constraints);
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void destroy() {

    }
}
