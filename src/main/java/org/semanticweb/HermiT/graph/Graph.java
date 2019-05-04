/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Graph<T>
implements Serializable,
Cloneable {
    private static final long serialVersionUID = 5372948202031042380L;
    protected final Set<T> m_elements = new HashSet<T>();
    protected final Map<T, Set<T>> m_successorsByNodes = new HashMap<T, Set<T>>();

    public void addEdge(T from, T to) {
        Set<T> successors = this.m_successorsByNodes.get(from);
        if (successors == null) {
            successors = new HashSet<T>();
            this.m_successorsByNodes.put(from, successors);
        }
        successors.add(to);
        this.m_elements.add(from);
        this.m_elements.add(to);
    }

    public void addEdges(T from, Set<T> to) {
        Set<T> successors = this.m_successorsByNodes.get(from);
        if (successors == null) {
            successors = new HashSet<T>();
            this.m_successorsByNodes.put(from, successors);
        }
        successors.addAll(to);
        this.m_elements.add(from);
        this.m_elements.addAll(to);
    }

    public Set<T> getElements() {
        return this.m_elements;
    }

    public Set<T> getSuccessors(T node) {
        Set<T> result = this.m_successorsByNodes.get(node);
        if (result == null) {
            result = Collections.emptySet();
        }
        return result;
    }

    public void transitivelyClose() {
        ArrayList<T> toProcess = new ArrayList<T>();
        for (Set<T> reachable : this.m_successorsByNodes.values()) {
            toProcess.clear();
            toProcess.addAll(reachable);
            while (!toProcess.isEmpty()) {
                Object elementOnPath = toProcess.remove(toProcess.size() - 1);
                Set<T> elementOnPathSuccessors = this.m_successorsByNodes.get(elementOnPath);
                if (elementOnPathSuccessors == null) continue;
                for (T elementOnPathSuccessor : elementOnPathSuccessors) {
                    if (!reachable.add(elementOnPathSuccessor)) continue;
                    toProcess.add(elementOnPathSuccessor);
                }
            }
        }
    }

    public Graph<T> getInverse() {
        Graph<T> result = new Graph<T>();
        for (Map.Entry<T, Set<T>> entry : this.m_successorsByNodes.entrySet()) {
            T from = entry.getKey();
            for (T successor : entry.getValue()) {
                result.addEdge(successor, from);
            }
        }
        return result;
    }

    public Graph<T> clone() {
        Graph<T> result = new Graph<T>();
        result.m_elements.addAll(this.m_elements);
        for (Map.Entry<T, Set<T>> entry : this.m_successorsByNodes.entrySet()) {
            T from = entry.getKey();
            for (T successor : entry.getValue()) {
                result.addEdge(from, successor);
            }
        }
        return result;
    }

    public void removeElements(Set<T> elements) {
        for (T element : elements) {
            this.m_elements.remove(element);
            this.m_successorsByNodes.remove(element);
        }
    }

    public boolean isReachableSuccessor(T fromNode, T toNode) {
        if (fromNode.equals(toNode)) {
            return true;
        }
        HashSet result = new HashSet();
        LinkedList<Object> toVisit = new LinkedList<Object>();
        toVisit.add(fromNode);
        while (!toVisit.isEmpty()) {
            Object current = toVisit.poll();
            Set successors = this.getSuccessors(current);
            if (successors.contains(toNode)) {
                return true;
            }
            if (!result.add(current)) continue;
            toVisit.addAll(successors);
        }
        return false;
    }

    public Set<T> getReachableSuccessors(T fromNode) {
        HashSet result = new HashSet();
        LinkedList<Object> toVisit = new LinkedList<Object>();
        toVisit.add(fromNode);
        while (!toVisit.isEmpty()) {
            Object current = toVisit.poll();
            if (!result.add(current)) continue;
            toVisit.addAll(this.getSuccessors(current));
        }
        return result;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (T element : this.m_elements) {
            buffer.append(element.toString());
            buffer.append(" -> { ");
            boolean firstSuccessor = true;
            Set<T> successors = this.m_successorsByNodes.get(element);
            if (successors != null) {
                for (T successor : successors) {
                    if (firstSuccessor) {
                        firstSuccessor = false;
                    } else {
                        buffer.append(", ");
                    }
                    buffer.append(successor.toString());
                }
            }
            buffer.append(" }\n");
        }
        return buffer.toString();
    }
}

