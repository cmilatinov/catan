package gameplay;

import entities.Player;

import java.util.ArrayList;

public class TradeContext {
    private final ArrayList<Integer> offer = new ArrayList<Integer>();
    private final ArrayList<Integer> request = new ArrayList<Integer>();

    private Player tradeOwner;

    public void addToOffer(int resource, int count) {
        offer.set(resource, offer.get(resource) + count);
    }

    public void addToRequest(int resource, int count) {
        request.set(resource, offer.get(resource) + count);
    }

    public void removeFromOffer(int resource, int count) {
        if (offer.get(resource) != null) {
            if(offer.get(resource) - count > 0) {
                offer.set(resource, offer.get(resource) - count);
            } else if (offer.get(resource) - count == 0) {
                offer.remove(resource);
            }
        }
    }

    public void removeFromRequest(int resource, int count) {
        if (request.get(resource) != null) {
            if (request.get(resource) - count > 0) {
                request.set(resource, offer.get(resource) - count);
            } else if (request.get(resource) - count == 0) {
                request.remove(resource);
            }
        }
    }

    public ArrayList<Integer> getOffer() { return offer; }
    public ArrayList<Integer> getRequest() { return request; }
    public Player getTradeOwner() { return tradeOwner; }
}
