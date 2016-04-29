package kata.houseofcards;

import org.assertj.core.api.Condition;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeckTest {

    @Test
    public void deckHas52Cards() throws Exception {
        Deck deck = new Deck();
        assertThat(deck.getCards()).hasSize(52);
    }

    public static boolean listsArentInTheSameOrder(List<Card> list) {
        final List<Card> SORTED_CARDS = new Deck().getCards();

        for (int i = 0; i < SORTED_CARDS.size(); i++) {
            if (!SORTED_CARDS.get(i).equals(list.get(i))) {
                return true;
            }
        }
        return false;
    }

    @Test
    //N.B. this test is friable - there is a chance that the shuffle leaves the order unchanged...
    public void shuffle_shufflingTheDeck_shouldResultInADifferentOrder() throws Exception {
        Condition<List> nonSequentialOrder = new Condition<>( DeckTest::listsArentInTheSameOrder
                , "Something other than: " + new Deck().getCards());
        Deck deck = new Deck();

        deck.shuffle();

        assertThat(deck.getCards()).has(nonSequentialOrder);
    }

    @Test
    public void deal_givenANewDeckOfCards_shouldDealAceOfClubsFirst(){
        Deck deck = new Deck();
        assertThat(deck.deal()).isEqualTo(new Card(1, Suit.CLUBS));
    }

    @Test
    public void deal_givenANewDeckOfCards_removeAceOfClubsFromDeck(){
        Deck deck = new Deck();
        Card aceOfClubs = new Card(1, Suit.CLUBS);

        deck.deal();

        assertThat(deck.getCards()).doesNotContain(aceOfClubs);
    }

}