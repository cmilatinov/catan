package ui;

import objects.GameScript;
import ui.components.PlayerPortrait;
import ui.components.TradeMenu;

public class TradeMenuUI extends GameScript {
    TradeMenu menu;

    @Override
    public void initialize() {
        menu = new TradeMenu();
        getScene().getUiManager().getContainer().add(menu, menu.getConstraints());
    }

    public void openTradingMenu() {
        menu.setActiveBorder(true);
    }

    public void closeTradingMenu() {
        menu.setActiveBorder(false);
    }

    @Override
    public void destroy() {

    }
}
