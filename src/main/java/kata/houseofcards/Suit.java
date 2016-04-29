package kata.houseofcards;

public enum Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES;

    @Override
    public String toString(){
        switch (this) {
            case CLUBS:
                return "c";
            case DIAMONDS:
                return "D";
            case HEARTS:
                return "H";
            case SPADES:
                return "s";
            default:
                return null;
        }
    }
}
