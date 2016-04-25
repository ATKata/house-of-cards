import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Klondike {
    private Deck stockPile;
    private List<Stack<Card>> tableauPiles;
    private List<Stack<Card>> foundationPiles;
    private List<Card> wastePile;

    public Klondike() {
        stockPile = new Deck();
        wastePile = new LinkedList<>();
        tableauPiles = new LinkedList<>();
        foundationPiles = new LinkedList<>();

        for (int i = 0; i < 7; i++) {
            tableauPiles.add(new Stack<>());
        }

        for (int j = 0; j < 4; j++) {
            foundationPiles.add(new Stack<>());
        }
    }

    public List<Stack<Card>> getTableauPiles() {
        return tableauPiles;
    }

    public List<Stack<Card>> getFoundationPiles() {
        return foundationPiles;
    }

    public Deck getStockPile() {
        return stockPile;
    }

    public List<Card> getWastePile() {
        return wastePile;
    }

    public void deal() {
        for (int startIndex = 0; startIndex < 7; startIndex++) {
            for (int i = startIndex; i < 7; i++) {
                tableauPiles.get(i).add(stockPile.deal());
            }
        }
    }

    public Stack<Card> getTableauPile(int index) {
        return tableauPiles.get(index);
    }
}
