/*
 * Decompiled with CFR 0.137.
 */
package rationals.transformations;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import rationals.Automaton;
import rationals.Synchronization;
import rationals.transformations.BinaryTransformation;
import rationals.transformations.Mix;

public class Shuffle
implements BinaryTransformation {
    @Override
    public Automaton transform(Automaton a, Automaton b) {
        Mix mix = new Mix(new Synchronization(){

            @Override
            public Object synchronize(Object t1, Object t2) {
                return null;
            }

            @Override
            public <T> Set<T> synchronizable(Set<T> a1, Set<T> b1) {
                return Collections.emptySet();
            }

            @Override
            public <T> Set<T> synchronizable(Collection<Set<T>> alphl) {
                return Collections.emptySet();
            }

            @Override
            public boolean synchronizeWith(Object object, Set<Object> alph) {
                return false;
            }
        });
        return mix.transform(a, b);
    }

}

