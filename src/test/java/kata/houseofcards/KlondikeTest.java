package kata.houseofcards;

import org.junit.Before;
import org.junit.Test;

import java.util.EmptyStackException;

import static kata.houseofcards.Suit.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
                "Tableaux:\n" +
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

    //TODO NOT REALLY A TEST!!!
    @Test
    public void playGame(){
        klondike.deal();
        System.out.println(klondike);
    }

}