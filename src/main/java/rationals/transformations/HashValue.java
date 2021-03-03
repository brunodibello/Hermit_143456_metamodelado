package rationals.transformations;

import java.util.Set;
import rationals.State;

public class HashValue {
    private final int hash;
    final Set<State> s;

    HashValue(Set<State> s) {
        this.s = s;
        this.hash = s.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HashValue)) {
            return false;
        }
        return ((HashValue)obj).hash == this.hash;
    }

    public int hashCode() {
        return this.hash;
    }

    public String toString() {
        return this.s.toString();
    }
}

