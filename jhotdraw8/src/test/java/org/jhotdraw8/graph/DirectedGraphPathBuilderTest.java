/* @(#)DirectedGraphPathBuilderTest.java
 * Copyright (c) 2017 The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.graph;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * DirectedGraphPathBuilderTest.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class DirectedGraphPathBuilderTest {

    public DirectedGraphPathBuilderTest() {
    }

    private DirectedGraph<Integer, Double> createGraph() {
        DirectedGraphBuilder<Integer, Double> builder = new DirectedGraphBuilder<>();

        // __|  1  |  2  |  3  |  4  |  5  |   6
        // 1 |       7.0   9.0               14.0
        // 2 | 7.0        10.0  15.0
        // 3 |                  11.0          2.0
        // 4 |                         6.0
        // 5 |                                9.0
        // 6 |14.0                     9.0
        //
        //

        builder.addVertex(1);
        builder.addVertex(2);
        builder.addVertex(3);
        builder.addVertex(4);
        builder.addVertex(5);
        builder.addVertex(6);
        builder.addBidiArrow(1, 2, 7.0);
        builder.addArrow(1, 3, 9.0);
        builder.addBidiArrow(1, 6, 14.0);
        builder.addArrow(2, 3, 10.0);
        builder.addArrow(2, 4, 15.0);
        builder.addArrow(3, 4, 11.0);
        builder.addArrow(3, 6, 2.0);
        builder.addArrow(4, 5, 6.0);
        builder.addBidiArrow(5, 6, 9.0);
        return builder;
    }


    @Test
    public void testCreateGraph() {
        final DirectedGraph<Integer, Double> graph = createGraph();

        final String expected
                = "1 -> 2, 3, 6.\n"
                + "2 -> 1, 3, 4.\n"
                + "3 -> 4, 6.\n"
                + "4 -> 5.\n"
                + "5 -> 6.\n"
                + "6 -> 1, 5.";

        final String actual = DumpGraphs.dumpAsAdjacencyList(graph);
        System.out.println(actual);

        assertEquals(expected, actual);
    }

    @TestFactory
    public List<DynamicTest> testFindVertexPath_3args() throws Exception {
        return Arrays.asList(
                dynamicTest("1", () -> doFindVertexPath_3args(1, 5, VertexPath.of(1, 6, 5))),
                dynamicTest("2", () -> doFindVertexPath_3args(1, 4, VertexPath.of(1, 2, 4))),
                dynamicTest("3", () -> doFindVertexPath_3args(2, 6, VertexPath.of(2, 1, 6)))
        );
    }


    /**
     * Test of findAnyVertexPath method, of class DirectedGraphPathBuilder.
     */
    public void doFindVertexPath_3args(Integer start, Integer goal, VertexPath<Integer> expected) throws Exception {
        System.out.println("doFindVertexPath_3args start:" + start + " goal:" + goal + " expResult:" + expected);
        DirectedGraph<Integer, Double> graph = createGraph();
        DirectedGraphPathBuilder<Integer, Double> instance = new DirectedGraphPathBuilder<>(graph);
        VertexPath<Integer> actual = instance.findVertexPath(start, goal::equals, Integer.MAX_VALUE);
        assertEquals(expected, actual);
    }

    @TestFactory
    public List<DynamicTest> testFindVertexPathOverWaypoints() throws Exception {
        return Arrays.asList(
                dynamicTest("1", () -> doFindVertexPathOverWaypoints(Arrays.asList(1, 5), VertexPath.of(1, 6, 5))),
                dynamicTest("2", () -> doFindVertexPathOverWaypoints(Arrays.asList(1, 4), VertexPath.of(1, 2, 4))),
                dynamicTest("3", () -> doFindVertexPathOverWaypoints(Arrays.asList(2, 6), VertexPath.of(2, 1, 6))),
                dynamicTest("4", () -> doFindVertexPathOverWaypoints(Arrays.asList(1,6,5), VertexPath.of(1, 6,5)))
        );
    }

    /**
     * Test of findAnyVertexPath method, of class DirectedGraphPathBuilder.
     */
    private void doFindVertexPathOverWaypoints(List<Integer> waypoints, VertexPath<Integer> expResult) throws Exception {
        System.out.println("doFindVertexPathOverWaypoints waypoints:" + waypoints + " expResult:" + expResult);
        DirectedGraph<Integer, Double> graph = createGraph();
        DirectedGraphPathBuilder<Integer, Double> instance = new DirectedGraphPathBuilder<>(graph);
        VertexPath<Integer> actual = instance.findVertexPathOverWaypoints(waypoints, Integer.MAX_VALUE);
        assertEquals(expResult,actual);
    }


    private DirectedGraph<Integer, Double> createGraph2() {
        // __|  1  |  2  |  3  |  4  |  5
        // 1 |       1.0   1.0
        // 2 |             1.0
        // 3 |                   1.0   1.0
        // 4 |                         1.0
        //
        //


        DirectedGraphBuilder<Integer, Double> b = new DirectedGraphBuilder<>();
        b.addVertex(1);
        b.addVertex(2);
        b.addVertex(3);
        b.addVertex(4);
        b.addVertex(5);

        b.addArrow(1, 2, 1.0);
        b.addArrow(1, 3, 1.0);
        b.addArrow(2, 3, 1.0);
        b.addArrow(3, 4, 1.0);
        b.addArrow(3, 5, 1.0);
        b.addArrow(4, 5, 1.0);
        return b;
    }

    @TestFactory
    public List<DynamicTest> testFindAllPaths() {
        DirectedGraph<Integer, Double> graph = createGraph2();

        return Arrays.asList(
                dynamicTest("1", () -> doFindAllPaths(graph, 1, 5, 5, Arrays.asList(
                        new VertexPath<>(Arrays.asList(1, 2, 3, 4, 5)),
                        new VertexPath<>(Arrays.asList(1, 2, 3, 5)),
                        new VertexPath<>(Arrays.asList(1, 3, 4, 5)),
                        new VertexPath<>(Arrays.asList(1, 3, 5))
                ))),
                dynamicTest("2", () -> doFindAllPaths(graph, 1, 5, 4, Arrays.asList(
                        new VertexPath<>(Arrays.asList(1, 2, 3, 5)),
                        new VertexPath<>(Arrays.asList(1, 3, 4, 5)),
                        new VertexPath<>(Arrays.asList(1, 3, 5))
                )))
        );
    }

    private void doFindAllPaths(DirectedGraph<Integer, Double> graph, int start, int goal, int maxDepth, List<VertexPath<Integer>> expected) {
        System.out.println("doFindAllPaths start:" + start + ", goal:" + goal + ", depth:" + maxDepth);
        DirectedGraphPathBuilder<Integer, Double> instance = new DirectedGraphPathBuilder<>(graph);
        List<VertexPath<Integer>> actual = instance.findAllVertexPaths(start,
                a -> (int) a == goal, maxDepth);
        assertEquals(expected, actual);
    }
}