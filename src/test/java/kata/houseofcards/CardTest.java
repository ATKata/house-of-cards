package kata.houseofcards;

import kata.houseofcards.Card;
import kata.houseofcards.Suit;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CardTest {

    @Test
    public void compareTo_GivenCardOfSameValueAndDifferentSuits_ClubShouldComeBeforeDiamond() throws Exception {
        Card card = new Card(1, Suit.DIAMONDS);
        int comparison = card.compareTo(new Card(1, Suit.CLUBS));
        assertThat(comparison).isNotEqualTo(0);
        assertThat(comparison).isEqualTo(1);
    }

    @Test
    public void compareTo_GivenCardOfSameSuitAndDifferentValues_FourShouldComeBeforeFive() throws Exception {
        Card card = new Card(5, Suit.DIAMONDS);
        int comparison = card.compareTo(new Card(4, Suit.DIAMONDS));
        assertThat(comparison).isNotEqualTo(0);
        assertThat(comparison).isEqualTo(1);
    }
}