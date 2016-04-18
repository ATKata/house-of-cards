import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeckTest {

    @Test
    public void deckHas52Cards() throws Exception {
        Deck deck = new Deck();
        assertThat(deck.getCards()).hasSize(52);
    }

    @Test
    public void shuffle_GivenCardsInTheDeck_ShouldHaveReorganised() throws Exception {
        Deck deck = new Deck();

        deck.shuffle();


    }
}