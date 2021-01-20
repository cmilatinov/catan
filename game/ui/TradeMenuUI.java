package ui;

import objects.GameScript;
import ui.components.TradeMenu;

public class TradeMenuUI extends GameScript {
    TradeMenu menu;



    @Override
    public void initialize() {
        menu = new TradeMenu();
        getScene().getUiManager().getContainer().add(menu, menu.getConstraints());
    }

    public void toggleTradingMenu() {
        menu.toggle();
    }

    @Override
    public void destroy() {

    }
}
