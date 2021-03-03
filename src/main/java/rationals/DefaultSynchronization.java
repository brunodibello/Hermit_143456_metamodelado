package rationals;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DefaultSynchronization
implements Synchronization {
    @Override
    public Object synchronize(Object t1, Object t2) {
        return t1 == null ? null : (t1.equals(t2) ? t1 : null);
    }

    @Override
    public <T> Set<T> synchronizable(Set<T> a, Set<T> b) {
        HashSet<T> r = new HashSet<T>(a);
        r.retainAll(b);
        return r;
    }

    @Override
    public <T> Set<T> synchronizable(Collection<Set<T>> alphl) {
        HashSet<T> niou = new HashSet<T>();
        for (Set<T> s : alphl) {
            for (Set<T> b : alphl) {
                niou.addAll(this.synchronizable(s, b));
            }
        }
        return niou;
    }

    @Override
    public boolean synchronizeWith(Object object, Set<Object> alph) {
        return alph.contains(object);
    }
}

