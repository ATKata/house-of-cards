package kata.houseofcards;

import java.util.ArrayList;
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
    public String toString() {
        String wastePileString = wastePile.size() == 0 ? EMPTY_PILE : wastePile.peek().toString();

        StringBuilder foundationPilesStringBuilder = new StringBuilder("Foundation:\n");
        for (List<Card> foundationPile : foundationPiles) {
            if (foundationPile.size() == 0) {
                foundationPilesStringBuilder.append(EMPTY_PILE).append("\n");
            } else {
                foundationPilesStringBuilder.append(
                        foundationPile.stream().map(Object::toString).collect(Collectors.joining(" "))).append("\n");
            }
        }

        StringBuilder tableauPilesStringBuilder = new StringBuilder("Tableau:\n");
        for (List<Card> tableauPile : tableauPiles) {
            if (tableauPile.size() == 0) {
                tableauPilesStringBuilder.append(EMPTY_PILE).append("\n");
            } else {
                tableauPilesStringBuilder.append(
                        tableauPile.stream().map(Object::toString).collect(Collectors.joining(" "))).append("\n");
            }
        }
        return String.format("Stock and Waste:\n%s %s\n\n%s\n%s", stockPile, wastePileString,
                foundationPilesStringBuilder, tableauPilesStringBuilder);
    }


    public Card takeCardFromStock() {
        if (stockPile.getCards().size() == 0) {
            stockPile.getCards().addAll(wastePile);
            stockPile.getCards().stream().forEach(card -> card.setFaceUp(false));
        }
        Card dealtCard = stockPile.deal();
        dealtCard.setFaceUp(true);
        wastePile.push(dealtCard);
        return dealtCard;
    }

    public boolean addToFoundationPile(Card card) {
        Stack<Card> foundationPileForSuit = foundationPiles.get(card.getSuit().ordinal());
        if (foundationPileForSuit.size() == 0) {
            if(card.getFaceValue() == 1) {
                return addCardToPile(card, foundationPileForSuit);
            } else {
                return false;
            }
        }

        if(card.getFaceValue() - foundationPileForSuit.peek().getFaceValue() == 1) {
            return addCardToPile(card, foundationPileForSuit);
        } else {
            return false;
        }

    }

    public Card peekAtFoundationPile(Suit suit) {
        return foundationPiles.get(suit.ordinal()).peek();
    }

    public boolean addToTableauPile(int index, Card card) {
        Stack<Card> pile = tableauPiles.get(index);
        if( pile.isEmpty() ){
            if( card.getFaceValue() == 13 ){
                return addCardToPile(card, pile);
            } else {
                return false;
            }
        }

        if( pile.peek().getFaceValue() > card.getFaceValue() ){
            return addCardToPile(card, pile);
        } else {
            return false;
        }
    }

    private boolean addCardToPile(Card card, Stack<Card> pile) {
        card.setFaceUp(true);
        pile.push(card);
        return true;
    }

    public boolean addToTableauPile(int index, List<Card> cards){
        cards.stream().forEach(card -> card.setFaceUp(true));
        return tableauPiles.get(index).addAll(cards);
    }

    public List<Card> takeCardsFromTableauPile(int index) {
        List<Card> selectedPile = tableauPiles.get(index);
        int indexOfFirstUpturnedCard = 0;
        for( int i = 0; i < selectedPile.size(); i++ ){
            if(selectedPile.get(i).isFaceUp()){
                indexOfFirstUpturnedCard = i;
                break;
            }
        }
        List<Card> cardsToRemove = selectedPile.subList(indexOfFirstUpturnedCard,selectedPile.size());
        List<Card> cardsToReturn = new ArrayList<>(cardsToRemove);
        cardsToRemove.clear();
        return cardsToReturn;
    }

    public void makeMove() {
        // return tryAndMoveAnAce() || tryAndMoveTableau();
        if (tryAndMoveAnAce())
            return;
        if (tryAndMoveTableau())
            return;
    }

    private boolean tryAndMoveTableau() {
        String message = "Not yet implemented!";
        System.err.println(message);
        throw new UnsupportedOperationException(message);

    }

    private boolean tryAndMoveAnAce() {
        for (Stack<Card> tableauPile : tableauPiles) {
            if( ! tableauPile.empty() ) {
                Card card = tableauPile.peek();
                if (card.getFaceValue() == 1) {
                    addToFoundationPile(tableauPile.pop());
                    return true;
                }
            }
        }
        return false;
    }
}
