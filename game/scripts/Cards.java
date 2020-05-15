package scripts;

import gameplay.PlayerHand;
import gui.GUI;
import objects.GameObject;
import resources.GameResources;
import resources.Resource;

public class Cards extends GameObject {

    // Cards
    GUI[] cards = new GUI[10];

    @Override
    public void stop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void initialize() {
        PlayerHand hand = new PlayerHand(getScene().getWindow());
        for(int i = 0; i < cards.length; i++) {
            int r = (int) Math.floor(Math.random() * 5);
            switch (r) {
                case 0 -> cards[i] = new GUI(GameResources.get(Resource.TEXTURE_CARD_BRICK));
                case 1 -> cards[i] = new GUI(GameResources.get(Resource.TEXTURE_CARD_STONE));
                case 2 -> cards[i] = new GUI(GameResources.get(Resource.TEXTURE_CARD_SHEEP));
                case 3 -> cards[i] = new GUI(GameResources.get(Resource.TEXTURE_CARD_FOREST));
                case 4 -> cards[i] = new GUI(GameResources.get(Resource.TEXTURE_CARD_WHEAT));
            }
            cards[i].setSize(150);
            getScene().registerGUI(cards[i]);
        }

        for(GUI card : cards)
            hand.addCard(card);
        hand.update();
    }

    @Override
    public void update() {

    }

    @Override
    public void destroy() {

    }
}
