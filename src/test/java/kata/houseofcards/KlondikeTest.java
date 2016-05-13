package kata.houseofcards;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import static kata.houseofcards.Suit.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;

public class KlondikeTest {

    private Klondike klondike;

    @Before
    public void setUp() throws Exception {
        klondike = new Klondike();
    }

    @Test
    public void gameShouldHaveSevenTableauPiles(){
        assertThat(klondike.getTableauPiles()).hasSize(7);
    }

    @Test
    public void gameShouldHaveFourFoundationPiles(){
        assertThat(klondike.getFoundationPiles()).hasSize(4);
    }

    @Test
    public void newGameHasAShuffledStockPile(){
        assertThat(klondike.takeCardFromStock()).isNotEqualTo(new Card(1, CLUBS));
    }

    @Test
    public void gameShouldHaveAWastePile(){
        assertThat(klondike.getWastePile()).hasSize(0);
    }

    @Test
    public void dealShouldSetUpTheTableauPiles(){
        klondike.deal();

        for (int i = 0; i < 7; i++) {
            assertThat(klondike.getTableauPile(i)).hasSize(i+1);
        }
    }

    @Test
    public void generateStringRepresentation(){
        klondike.deal();

        assertThat(klondike.toString()).matches(
                "Stock and Waste:\n" +
                "XXX ___\n" +
                "\n" +
                "Foundation:\n" +
                "___\n" +
                "___\n" +
                "___\n" +
                "___\n" +
                "\n" +
                "Tableau:\n" +
                "...\n" +
                "... ...\n" +
                "... ... ...\n" +
                "... ... ... ...\n" +
                "... ... ... ... ...\n" +
                "... ... ... ... ... ...\n" +
                "... ... ... ... ... ... ...\n");
    }

    @Test
    public void takeCardFromStockAddsOneToWastePile(){
        klondike.deal();
        Card takenFromStock = klondike.takeCardFromStock();
        Card placedOnWaste = klondike.getWastePile().peek();
        assertThat(placedOnWaste).isNotNull().isEqualTo(takenFromStock);
        assertThat(placedOnWaste.isFaceUp()).isTrue();
    }

    @Test
    public void takeCardFromStockWhenEmptyRefillsFromWastePile(){
        Card firstCardDrawn = klondike.takeCardFromStock();
        Card lastCardDrawn = null;
        for (int i = 0; i < 52; i++) {
           lastCardDrawn = klondike.takeCardFromStock();
        }
        assertThat(firstCardDrawn).isEqualTo(lastCardDrawn);
    }

    @Test
    public void placeCardOnFoundationPile(){
        Card aceOfClubs = new Card(1, CLUBS);
        klondike.addToFoundationPile(aceOfClubs);
        assertThat(klondike.peekAtFoundationPile(CLUBS).isFaceUp()).isTrue();
    }

    @Test
    public void canOnlyStartAFoundationPileWithAnAce(){
        Card twoOfClubs = new Card(2, CLUBS);
        assertThat(klondike.addToFoundationPile(twoOfClubs)).isFalse();
        assertThatThrownBy(() -> klondike.peekAtFoundationPile(CLUBS)).isInstanceOf(EmptyStackException.class);
    }

    @Test
    public void foundationPilesMustBeBuiltInAscendingOrder(){
        Card aceOfClubs = new Card(1, CLUBS);
        Card twoOfClubs = new Card(2, CLUBS);
        Card fourOfClubs = new Card(4, CLUBS);
        klondike.addToFoundationPile(aceOfClubs);
        assertThat(klondike.addToFoundationPile(twoOfClubs)).isTrue();
        assertThat(klondike.addToFoundationPile(fourOfClubs)).isFalse();
    }

    @Test
    public void successfullyAddToTableauPile(){
        klondike.deal();
        Stack<Card> firstTableauPile = klondike.getTableauPile(0);
        Card cardOnTopOfPile = firstTableauPile.peek();
        Card fakeCard = new Card(cardOnTopOfPile.getFaceValue()-1,cardOnTopOfPile.getSuit());
        assertThat(klondike.addToTableauPile(0,fakeCard)).isTrue();
        assertThat(firstTableauPile).hasSize(2);
        assertThat(firstTableauPile.get(0).isFaceUp()).isTrue();
        assertThat(firstTableauPile.get(1).isFaceUp()).isTrue();
    }

    @Test
    public void tableauPileCardsMustBeInDescendingOrder(){
        klondike.deal();
        Stack<Card> firstTableauPile = klondike.getTableauPile(0);
        Card cardOnTopOfPile = firstTableauPile.peek();
        Card fakeCard = new Card(cardOnTopOfPile.getFaceValue()+1,cardOnTopOfPile.getSuit());
        assertThat(klondike.addToTableauPile(0,fakeCard)).isFalse();
        assertThat(firstTableauPile).hasSize(1);
    }

    @Test
    public void addMultipleCardsToTableauPile(){
        List<Card> cards = Arrays.asList(new Card(13,CLUBS), new Card(12,CLUBS));
        assertThat(klondike.addToTableauPile(0,cards)).isTrue();
        assertThat(klondike.getTableauPile(0)).containsExactly(new Card(13,CLUBS,true), new Card(12,CLUBS,true));
    }

    @Test
    public void cannotStartTableauPileIfNotAKing(){
        Card aceOfSpades = new Card(1,SPADES);
        assertThat(klondike.addToTableauPile(0,aceOfSpades)).isFalse();
    }

    @Test
    public void canStartTableauPileOnAKing(){
        Card kingOfSpades = new Card(13,SPADES);
        assertThat(klondike.addToTableauPile(0,kingOfSpades)).isTrue();
    }

    @Test
    public void takeFaceUpCardsFromTableauPile(){
        klondike.deal();
        assertThat(klondike.takeCardsFromTableauPile(1)).hasSize(1);
        assertThat(klondike.getTableauPile(1).get(0).isFaceUp()).isTrue();
    }

    @Test
    public void tableauPilesRemainUnchangedIfNoMoveAvailable(){
        klondike.getTableauPile(0).push(new Card(5,CLUBS));
        klondike.getTableauPile(0).push(new Card(7,HEARTS, true));
        klondike.getTableauPile(1).push(new Card(5,DIAMONDS));
        klondike.getTableauPile(1).push(new Card(7,SPADES, true));

        klondike.makeMove();

        assertThat(klondike.getTableauPile(0).get(0).isFaceUp()).isFalse();
    }

    //TODO NOT REALLY A TEST!!!
    @Test
    public void playGame(){
        klondike.deal();
        System.out.println(klondike);

        for (int turnNo=0; turnNo < 3; turnNo++) {
            klondike.makeMove();
            System.out.println("Turn: " + turnNo);
            System.out.println(klondike);
        }

    }

    @Test
    public void  makeMove_whenThereIsAnAce_moveItToFoundationPile(){
        klondike.getTableauPile(2).push(new Card(7,DIAMONDS));
        klondike.getTableauPile(2).push(new Card(1,CLUBS));

        klondike.makeMove();

        assertThat(klondike.getTableauPile(2)).hasSize(1);
        assertThat(klondike.getFoundationPile(CLUBS)).hasSize(1);
        assertThat(klondike.getTableauPile(2).peek().isFaceUp()).isTrue();
    }

    @Test
    public void  makeMove_whenThereIsNoAce_doNotMoveItToFoundationPile(){
        klondike.getTableauPile(1).push(new Card(2,CLUBS));

        klondike.makeMove();

        assertThat(klondike.getTableauPile(1)).isNotEmpty();
        assertThat(klondike.getFoundationPile(CLUBS)).isEmpty();
    }

    @Test
    public void moveACardThatIsntAnAceToTheFoundationPile(){
        klondike.addToFoundationPile(new Card(1,CLUBS));
        klondike.getTableauPile(0).push(new Card(2,CLUBS));

        klondike.makeMove();

        assertThat(klondike.getFoundationPile(CLUBS)).hasSize(2);
    }

    @Test
    public void  makeMove_whenThereIsMoreThanOneAce_shouldMakeOnlyOneMove(){
        klondike.getTableauPile(1).push(new Card(1,HEARTS));
        klondike.getTableauPile(2).push(new Card(1,CLUBS));

        klondike.makeMove();

        assertThat(klondike.getTableauPile(1)).isEmpty();
        assertThat(klondike.getTableauPile(2)).isNotEmpty();
        assertThat(klondike.getFoundationPile(HEARTS)).isNotEmpty();
    }

    @Test
    public void makeAValidTableauMoveForASingleCard(){
        klondike.addToTableauPile(0, new Card(13,CLUBS));
        klondike.getTableauPile(1).push(new Card(13,DIAMONDS));
        klondike.addToTableauPile(1, new Card(12, DIAMONDS));

        klondike.makeMove();

        assertThat(klondike.getTableauPile(0)).hasSize(2);
        assertThat(klondike.getTableauPile(1)).hasSize(1);
    }

    @Test
    public void makeAValidTableauMoveForAMultipleCards(){
        klondike.addToTableauPile(0, new Card(13,CLUBS));
        klondike.getTableauPile(1).push(new Card(7,DIAMONDS));
        klondike.addToTableauPile(1, Arrays.asList(new Card(12, DIAMONDS), new Card(11, DIAMONDS)));

        klondike.makeMove();

        assertThat(klondike.getTableauPile(0)).hasSize(3);
    }

    @Test
    public void takeFromStockPileIfCantGoAndDiscardToWasteIfCantGo(){
        klondike.getTableauPile(0).push(new Card(2,SPADES));
        klondike.getTableauPile(1).push(new Card(2,DIAMONDS));

        assertThat(klondike.makeMove()).isFalse();

        assertThat(klondike.getWastePile()).hasSize(1);
        assertThat(klondike.getTableauPile(0)).hasSize(1);
        assertThat(klondike.getTableauPile(1)).hasSize(1);
    }

    @Test
    public void takeFromWastePileIfCantGoAndPlayToTableauIfAvailable(){
        klondike.getWastePile().push(new Card(12,CLUBS));
        klondike.addToTableauPile(3, new Card(13,CLUBS));

        assertThat(klondike.makeMove()).isTrue();

        assertThat(klondike.getTableauPile(3)).hasSize(2);
    }

    @Test
    public void takeFromWastePileIfCantGoAndPlayToFoundationIfAvailable(){
        klondike.getWastePile().push(new Card(1,CLUBS));

        assertThat(klondike.makeMove()).isTrue();

        assertThat(klondike.getFoundationPile(CLUBS)).hasSize(1);
    }
}