package rationals.transformations;

import java.util.Set;
import rationals.State;

class StatesCouple {
    final Set<State> sa;
    final Set<State> sb;
    final int hash;

    public StatesCouple(Set<State> sa, Set<State> sb) {
        this.sa = sa;
        this.sb = sb;
        this.hash = sa.hashCode() + sb.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StatesCouple)) {
            return false;
        }
        StatesCouple sc = (StatesCouple)obj;
        return sc.sa.equals(this.sa) && sc.sb.equals(this.sb);
    }

    public int hashCode() {
        return this.hash;
    }

    public String toString() {
        return " < " + this.sa.toString() + "," + this.sb.toString() + " >";
    }
}

