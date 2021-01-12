package gameplay;

import entities.Player;

import java.util.HashMap;

public class TradeContext {
    private final HashMap<ResourceType, Integer> offer = new HashMap<ResourceType, Integer>();
    private final HashMap<ResourceType, Integer> request = new HashMap<ResourceType, Integer>();

    private Player tradeOwner;

    public void addToOffer(ResourceType resource, int count) {
        if(ResourceType.isResource(resource)) {
            offer.merge(resource, count, Integer::sum);
        }
    }

    public void addToRequest(ResourceType resource, int count) {
        if(ResourceType.isResource(resource)) {
            request.merge(resource, count, Integer::sum);
        }
    }

    public void removeFromOffer(ResourceType resource, int count) {
        if (offer.get(resource) != null) {
            if(offer.get(resource) - count > 0) {
                offer.merge(resource, -count, Integer::sum);
            } else if (offer.get(resource) - count == 0) {
                offer.remove(resource);
            }
        }
    }

    public void removeFromRequest(ResourceType resource, int count) {
        if (request.get(resource) != null) {
            if (request.get(resource) - count > 0) {
                request.merge(resource, -count, Integer::sum);
            } else if (request.get(resource) - count == 0) {
                request.remove(resource);
            }
        }
    }

    public HashMap<ResourceType, Integer> getOffer() { return offer; }
    public HashMap<ResourceType, Integer> getRequest() { return request; }
    public Player getTradeOwner() { return tradeOwner; }
}
