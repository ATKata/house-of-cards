package kata.houseofcards;

import java.util.ArrayList;
import java.util.Collection;
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
            if (card.getFaceValue() == 1) {
                return addCardToPile(card, foundationPileForSuit);
            } else {
                return false;
            }
        }

        if (card.getFaceValue() - foundationPileForSuit.peek().getFaceValue() == 1) {
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
        if (pile.isEmpty()) {
            if (card.getFaceValue() == 13) {
                return addCardToPile(card, pile);
            } else {
                return false;
            }
        }

        if (pile.peek().getFaceValue() == card.getFaceValue()+1) {
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

    public boolean addToTableauPile(int index, List<Card> cards) {
        cards.stream().forEach(card -> card.setFaceUp(true));
        return tableauPiles.get(index).addAll(cards);
    }

    public List<Card> takeCardsFromTableauPile(int index) {
        List<Card> sourcePile = tableauPiles.get(index);
        int indexOfFirstUpturnedCard = 0;
        for (int i = 0; i < sourcePile.size(); i++) {
            if (sourcePile.get(i).isFaceUp()) {
                indexOfFirstUpturnedCard = i;
                break;
            }
        }
        List<Card> cardsToRemove = sourcePile.subList(indexOfFirstUpturnedCard, sourcePile.size());
        List<Card> cardsToReturn = new ArrayList<>(cardsToRemove);
        cardsToRemove.clear();
        if(!sourcePile.isEmpty()) {
            sourcePile.get(sourcePile.size()-1).setFaceUp(true);
        }
        return cardsToReturn;
    }

    public boolean makeMove() {
        boolean result = tryAndMoveFoundation() || tryAndMoveTableau() || tryToTakeFromStock();
//        assert(stockPile.getCards().size()+
//                wastePile.size()+
//                foundationPiles.stream().mapToInt(Collection::size).sum()+
//                tableauPiles.stream().mapToInt((Collection::size)).sum()==52);
        return result;
    }

    private boolean tryToTakeFromStock() {
        if(!wastePile.isEmpty()){
            Card wasteCard = wastePile.peek();
            if(addToFoundationPile(wasteCard)){
                return true;
            }

            for (int i = 0; i < tableauPiles.size(); i++) {
                Stack<Card> tableauPile = tableauPiles.get(i);
                if ( addToTableauPile(i, wasteCard) ){
                    wastePile.pop();
                    return true;
                }
            }
        }
        Card dealtCard = stockPile.deal();
        wastePile.push(dealtCard);
        return false;
    }

    private boolean tryAndMoveTableau() {
        for (Stack<Card> destinationPile : tableauPiles) {
            if (!destinationPile.isEmpty()) {
                for (int i = 0; i < tableauPiles.size(); i++) {
                    Stack<Card> sourcePile = tableauPiles.get(i);
                    if (!destinationPile.equals(sourcePile)) {
                        List<Card> faceUpCards = takeCardsFromTableauPile(i);
                        if ( !faceUpCards.isEmpty() && faceUpCards.get(0).getFaceValue() == destinationPile.peek().getFaceValue() - 1) {
                            return destinationPile.addAll(faceUpCards);
                        }
                        if(!sourcePile.isEmpty()){
                            sourcePile.peek().setFaceUp(false);
                        }
                        sourcePile.addAll(faceUpCards);
                    }
                }
            }
        }
        return false;
    }

    private boolean tryAndMoveFoundation() {
        for (Stack<Card> tableauPile : tableauPiles) {
            if (!tableauPile.empty()) {
                Card card = tableauPile.peek();
                if (addToFoundationPile(card)) {
                    tableauPile.pop();
                    if(!tableauPile.isEmpty()){
                        tableauPile.peek().setFaceUp(true);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public List<Card> getFoundationPile(Suit suit) {
        return foundationPiles.get(suit.ordinal());
    }
}
