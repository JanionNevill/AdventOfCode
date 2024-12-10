package adventofcode.utilities;

public class Pair<TypeT, TypeS> {

    private TypeT first;
    private TypeS second;

    public Pair(TypeT first, TypeS second) {
        this.first = first;
        this.second = second;
    }

    public TypeT getFirst() {
        return first;
    }

    public TypeS getSecond() {
        return second;
    }

}
