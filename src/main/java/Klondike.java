import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Klondike {
    public static final String EMPTY_PILE = "___";
    private Deck stockPile;
    private List<Stack<Card>> tableauPiles;
    private List<Stack<Card>> foundationPiles;
    private Stack<Card> wastePile;

    public Klondike() {
        stockPile = new Deck().shuffle();
        wastePile = new Stack<>();
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

    public Stack<Card> getWastePile() {
        return wastePile;
    }

    public void deal() {
        for (int startIndex = 0; startIndex < 7; startIndex++) {
            for (int i = startIndex; i < 7; i++) {
                tableauPiles.get(i).add(stockPile.deal());
            }
        }
        tableauPiles.stream().forEach(pile -> pile.peek().setFaceUp(true));
    }

    public Stack<Card> getTableauPile(int index) {
        return tableauPiles.get(index);
    }

    @Override
    public String toString(){
        String wastePileString = wastePile.size()==0 ? EMPTY_PILE : wastePile.peek().toString();

        StringBuilder foundationPilesStringBuilder = new StringBuilder("Foundation:\n");
        for(List<Card> foundationPile : foundationPiles){
            if(foundationPile.size()==0){
                foundationPilesStringBuilder.append(EMPTY_PILE).append("\n");
            } else {
                foundationPilesStringBuilder.append(
                        foundationPile.stream().map(Object::toString).collect(Collectors.joining(" "))).append("\n");
            }
        }

        StringBuilder tableauPilesStringBuilder = new StringBuilder("Tableaux:\n");
        for(List<Card> tableauPile : tableauPiles){
            if(tableauPile.size()==0){
                tableauPilesStringBuilder.append(EMPTY_PILE).append("\n");
            } else {
                tableauPilesStringBuilder.append(
                        tableauPile.stream().map(Object::toString).collect(Collectors.joining(" "))).append("\n");
            }
        }
        return String.format("Stock and Waste:\n%s %s\n\n%s\n%s", stockPile, wastePileString, foundationPilesStringBuilder, tableauPilesStringBuilder);
    }


    public Card takeCardFromStock() {
        if(stockPile.getCards().size()==0){
            stockPile.getCards().addAll(wastePile);
            //stockPile.getCards().stream().forEach(card -> card.setFaceUp(false));
        }
        Card dealtCard = stockPile.deal();
        dealtCard.setFaceUp(true);
        wastePile.push(dealtCard);
        return dealtCard;
    }
}
