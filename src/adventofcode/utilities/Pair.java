package adventofcode.utilities;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }
        Pair<?, ?> other = (Pair<?, ?>) obj;
        return Objects.equals(first, other.first) && Objects.equals(second, other.second);
    }

}
