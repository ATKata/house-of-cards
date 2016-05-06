package kata.houseofcards;

/*
 * This Java source file was auto generated by running 'gradle buildInit --type java-library'
 * by 'alec' at '18/04/16 07:07' with Gradle 2.4
 *
 * @author alec, @date 18/04/16 07:07
 */
public class Card implements Comparable<Card> {

    private final int faceValue;
    private final Suit suit;
    private boolean faceUp;

    public Card(int faceValue, Suit suit, boolean isFaceUp) {
        this(faceValue,suit);
        this.setFaceUp(isFaceUp);
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }

    public Card(int faceValue, Suit suit) {
        this.faceValue = faceValue;
        this.suit = suit;
    }

    public int getFaceValue() {
        return faceValue;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public int compareTo(Card other) {
        if(suit.compareTo(other.getSuit()) == 0) {
            return faceValue - other.getFaceValue();
        }
        return suit.compareTo(other.getSuit());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (faceValue != card.faceValue) return false;
        if (faceUp != card.faceUp) return false;
        return suit == card.suit;

    }

    @Override
    public int hashCode() {
        int result = faceValue;
        result = 31 * result + suit.hashCode();
        result = 31 * result + (faceUp ? 1 : 0);
        return result;
    }

    @Override
    public String toString(){
        if(faceUp){
            return String.format("%2d%s",faceValue,suit);
        } else {
            return "XXX";
        }
    }
}
