/* @(#)DirectedGraphBuilder.java
 * Copyright © 2017 by the authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DirectedGraphBuilder.
 *
 * @author Werner Randelshofer
 * @version $Id$
 * @param <V> the vertex type
 * @param <E> the edge type
 */
public class DirectedGraphWithEdgesBuilder<V,E> extends AbstractDirectedGraphBuilder implements DirectedGraphWithEdges<V,E> {
    /**
     * Maps a vertex to a vertex index.
     */
    private final Map<V, Integer> vertexMap;
    /**
     * Maps a vertex index to a vertex object.
     */
    private final List<V> vertices;

    private final List<E> edgeData;
    
    public DirectedGraphWithEdgesBuilder() {
        this(16, 16);
    }

    public DirectedGraphWithEdgesBuilder(int vertexCapacity, int edgeCapacity) {
        super(vertexCapacity,edgeCapacity);
        this.vertexMap = new HashMap<>(vertexCapacity + vertexCapacity * 40 / 100, 0.75f);
        this.vertices = new ArrayList<>(vertexCapacity);
        this.edgeData =new ArrayList<>();
    }

    /**
     * Builder-method: adds a directed edge (arrow from va to vb).
     *
     * @param va vertex a
     * @param vb vertex b
     * @param edge the edge
     */
    public void addEdge(V va, V vb, E edge) {
        if (va == null) {
            throw new IllegalArgumentException("va=null");
        }
        if (vb == null) {
            throw new IllegalArgumentException("vb=null");
        }
        int a = vertexMap.get(va);
        int b = vertexMap.get(vb);
        buildAddEdge(a, b);

        edgeData.add(edge);
    }
    /**
     * Builder-method: adds two edges (arrow from va to vb
     * and arrow from vb to va).
     *
     * @param va vertex a
     * @param vb vertex b
     * @param edge the edge
     */
    public void addBidiEdge(V va, V vb, E edge) {
        addEdge(va,vb,edge);
        addEdge(vb,va,edge);
    }

    /**
     * Builder-method: adds a vertex.
     *
     * @param v vertex
     */
    public void addVertex(V v) {
        if (v == null) {
            throw new IllegalArgumentException("v=null");
        }
        vertexMap.computeIfAbsent(v, k -> {
            vertices.add(v);
            buildAddVertex();
            return vertices.size() - 1;
        });
    }


    @Override
    public E getEdge(int indexOfEdge) {
        return edgeData.get(indexOfEdge);
    }

    @Override
    public E getEdge(V vertex, int i) {
        int edgeId=getIndexOfEdge(getIndexOfVertex(vertex), i);
        return edgeData.get(edgeId);
    }
        @Override
    public V getVertex(int vi) {
        if (vertices.get(vi) == null) {
            System.err.println("DIrectedGraphBuilder is broken");
        }
        return vertices.get(vi);
    }
    @Override
    public int getNextCount(V v) {
        return getNextCount(getIndexOfVertex(v));
    }
    @Override
    public V getNext(V v, int i) {
        return getVertex(getNext(getIndexOfVertex(v), i));
    }
    protected int getIndexOfVertex(V v) {
        return vertexMap.get(v);
    }


    
}
