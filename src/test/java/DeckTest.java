import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

        assertThatThrownBy(() ->
                assertThat(deck.getCards())
                        .containsExactly((Card[]) new Deck().getCards().toArray(new Card[deck.getCards().size()])))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting")
                .hasMessageContaining("to contain exactly")
                .hasMessageContaining("but some elements were not found");

    }

}