import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import static javafx.scene.input.KeyCode.J;

public class Klondike {
    private Deck stockPile;
    private List<Stack<Card>> tableauPiles;
    private List<Stack<Card>> foundationPiles;
    private LinkedList<Card> wastePile;

    public Klondike() {
        stockPile = new Deck().shuffle();
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

    @Override
    public String toString(){
        String wastePileString = wastePile.size()==0 ? "___" : wastePile.getLast().toString();
        StringBuilder tableauPilesStringBuilder = new StringBuilder();
        for(List<Card> list : tableauPiles){
            tableauPilesStringBuilder.append(String.format("%s\n", list.stream().map(Object::toString).collect(Collectors.joining(" "))));
        }
        return String.format("%s %s\n%s", stockPile,  wastePileString, tableauPilesStringBuilder);
    }


}
