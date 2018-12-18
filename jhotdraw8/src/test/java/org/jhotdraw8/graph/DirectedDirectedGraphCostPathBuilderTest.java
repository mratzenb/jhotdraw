/* @(#)DirectedGraphPathBuilderTest.java
 * Copyright (c) 2017 The authors and contributors of JHotDraw. MIT License.
 */

package org.jhotdraw8.graph;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.List;
import java.util.function.ToDoubleFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * DirectedDirectedGraphCostPathBuilderTest.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class DirectedDirectedGraphCostPathBuilderTest {

    public DirectedDirectedGraphCostPathBuilderTest() {
    }
    
    private DirectedGraph<Integer,Double>createGraph() {
        DirectedGraphBuilder<Integer,Double> builder=new DirectedGraphBuilder<>();

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
    public List<DynamicTest> testFindShortestVertexPath() {
        return Arrays.asList(
                dynamicTest("1",()->      doFindShortestVertexPath(1,5, VertexPath.of(1,3,6,5))),
                dynamicTest("1",()->      doFindShortestVertexPath(1,4,VertexPath.of(1,3,4))),
    dynamicTest("1",()->      doFindShortestVertexPath(2,6,VertexPath.of(2,3,6)))
        );
    }
    /**
     * Test of findAnyPath method, of class DirectedGraphCostPathBuilder.
     */
    public void doFindShortestVertexPath(Integer start, Integer goal, VertexPath<Integer> expResult ) throws Exception {
        System.out.println("findShortestVertexPath start:"+start+" goal:"+goal+" expResult:"+expResult);
        DirectedGraph<Integer, Double> graph = createGraph();
        ToDoubleFunction<Double> costf = arg->arg;
        DirectedGraphCostPathBuilder<Integer,Double> instance = new DirectedGraphCostPathBuilder<>(graph::getNextEntries,costf);
        VertexPath<Integer> result = instance.findShortestVertexPath(start, goal::equals, Double.MAX_VALUE);
        assertEquals( expResult, result);
    }

    @TestFactory
    public List<DynamicTest> testFindShortestEdgeMultiGoalPath() throws Exception {
        return Arrays.asList(
                dynamicTest("1",()-> doFindShortestEdgeMultiGoalPath(1, Arrays.asList(5,6),EdgePath.of(9.0,2.0))),
                dynamicTest("2",()-> doFindShortestEdgeMultiGoalPath(1,Arrays.asList(4,5),EdgePath.of(9.0,11.0))),
                dynamicTest("3",()-> doFindShortestEdgeMultiGoalPath(2,Arrays.asList(3,6),EdgePath.of(10.0))),
                dynamicTest("4",()-> doFindShortestEdgeMultiGoalPath(1, Arrays.asList(6,5),EdgePath.of(9.0,2.0))),
                dynamicTest("5",()-> doFindShortestEdgeMultiGoalPath(1,Arrays.asList(5,4),EdgePath.of(9.0,11.0))),
                dynamicTest("6",()-> doFindShortestEdgeMultiGoalPath(2,Arrays.asList(6,3),EdgePath.of(10.0)))
        ) ;
    }

    /**
     * Test of findAnyPath method, of class DirectedGraphCostPathBuilder.
     */
    public void doFindShortestEdgeMultiGoalPath(Integer start, List<Integer> multiGoal, EdgePath<Double> expResult ) throws Exception {
        System.out.println("findShortestEdgePathMultiGoal start:"+start+" goal:"+multiGoal+" expResult:"+expResult);
        DirectedGraph<Integer, Double> graph = createGraph();
        ToDoubleFunction<Double> costf = arg->arg;
        DirectedGraphCostPathBuilder<Integer,Double> instance = new DirectedGraphCostPathBuilder<>(graph::getNextEntries,costf);

        // Find a path for each individual goal, and remember the shortest path
        EdgePath<Double> individualShortestPath=null;
        double individualShortestLength=Double.POSITIVE_INFINITY;
        for (Integer goal:multiGoal) {
            EdgePath<Double> result = instance.findShortestEdgePath(start, goal::equals, Double.POSITIVE_INFINITY);
            double resultLength = result.getEdges().stream().mapToDouble(Double::doubleValue).sum();
            if (resultLength<individualShortestLength){
                individualShortestLength=resultLength;
                individualShortestPath=result;
            }
        }

        // Find shortest path to any of the goals
        EdgePath<Double> actualShortestPath=instance.findShortestEdgePath(start,multiGoal::contains,Double.POSITIVE_INFINITY);
        double actualLength = actualShortestPath.getEdges().stream().mapToDouble(Double::doubleValue).sum();

        System.out.println("  individual shortest path: "+individualShortestPath);
        System.out.println("  actual shortest path: "+actualShortestPath);

        assertEquals(individualShortestLength,actualLength);
        assertEquals(expResult, actualShortestPath);
    }

    @TestFactory
    public List<DynamicTest> testFindShortestEdgePath() throws Exception {
        return Arrays.asList(
                dynamicTest("1",()-> doFindShortestEdgePath(1,5,EdgePath.of(9.0,2.0,9.0))),
                dynamicTest("2",()-> doFindShortestEdgePath(1,4,EdgePath.of(9.0,11.0))),
    dynamicTest("3",()-> doFindShortestEdgePath(2,6,EdgePath.of(10.0,2.0)))
        ) ;
    }
    /**
     * Test of findAnyPath method, of class DirectedGraphCostPathBuilder.
     */
    private void doFindShortestEdgePath(Integer start, Integer goal, EdgePath<Double> expResult ) throws Exception {
        System.out.println("findShortestEdgePath start:"+start+" goal:"+goal+" expResult:"+expResult);
        DirectedGraph<Integer, Double> graph = createGraph();
        ToDoubleFunction<Double> costf = arg->arg;
        DirectedGraphCostPathBuilder<Integer,Double> instance = new DirectedGraphCostPathBuilder<>(graph::getNextEntries,costf);
        EdgePath<Double> result = instance.findShortestEdgePath(start, goal::equals, Double.POSITIVE_INFINITY);
        assertEquals(result, expResult);
    }

    @TestFactory
    public List<DynamicTest> testFindAnyVertexPath_3args() throws Exception {
        return Arrays.asList(
                dynamicTest("1",()->  doFindAnyVertexPath_3args(1,5, VertexPath.of(1,6,5))),
                dynamicTest("2",()->  doFindAnyVertexPath_3args(1,4,VertexPath.of(1,2,4))),
                dynamicTest("3",()->  doFindAnyVertexPath_3args(2,6,VertexPath.of(2,1,6)))
        );
    }

    /**
     * Test of findAnyVertexPath method, of class DirectedGraphPathBuilderWithArrows.
     */
    public void doFindAnyVertexPath_3args(Integer start, Integer goal, VertexPath<Integer> expResult ) throws Exception {
        System.out.println("findAnyVertexPath start:"+start+" goal:"+goal+" expResult:"+expResult);
        DirectedGraph<Integer,Double> graph = createGraph();
        ToDoubleFunction<Double> costf = arg->arg;
        DirectedGraphCostPathBuilder<Integer,Double> instance = new DirectedGraphCostPathBuilder<>(graph::getNextEntries,costf);
        VertexPath<Integer> result = instance.findVertexPath(start, goal::equals,Double.POSITIVE_INFINITY);
        assertEquals(result, expResult);
    }

    @TestFactory
    public List<DynamicTest> testFindAnyEdgePath_3args() {
        return Arrays.asList(
                dynamicTest("1", () ->  doFindAnyEdgePath_3args(1,5, EdgePath.of(14.0,9.0))),
    dynamicTest("1", () ->  doFindAnyEdgePath_3args(1,4,EdgePath.of(7.0,15.0))),
        dynamicTest("1", () ->  doFindAnyEdgePath_3args(2,6,EdgePath.of(7.0,14.0)))

        ) ;
    }
    
    /**
     * Test of findAnyVertexPath method, of class DirectedGraphPathBuilderWithArrows.
     */
    public void doFindAnyEdgePath_3args(Integer start, Integer goal, EdgePath<Double> expResult ) throws Exception {
        System.out.println("findAnyVertexPath start:"+start+" goal:"+goal+" expResult:"+expResult);
        DirectedGraph<Integer,Double> graph = createGraph();
        ToDoubleFunction<Double> costf = arg->arg;
        DirectedGraphCostPathBuilder<Integer,Double> instance = new DirectedGraphCostPathBuilder<>(graph::getNextEntries,costf);
        EdgePath<Double> result = instance.findEdgePath(start, goal::equals,Double.POSITIVE_INFINITY);
        assertEquals(result, expResult);
    }
    private DirectedGraph<Integer,Double> createGraph2() {
        DirectedGraphBuilder<Integer, Double> b = new DirectedGraphBuilder<>();
        b.addVertex(1);
        b.addVertex(2);
        b.addVertex(3);
        b.addVertex(4);
        b.addVertex(5);

        b.addArrow(1,2,1.0);
        b.addArrow(1,3,1.0);
        b.addArrow(2,3,1.0);
        b.addArrow(3,4,1.0);
        b.addArrow(3,5,1.0);
        b.addArrow(4,5,1.0);
        return b;
    }

    @TestFactory
    public List<DynamicTest>  testFindAllPaths() {
        DirectedGraph<Integer, Double> graph = createGraph2();

        return Arrays.asList(
                dynamicTest("1",()->        doFindAllPaths(graph,1,5, 4.0, Arrays.asList(
                        new VertexPath<>(Arrays.asList(1,2,3,4,5)),
                        new VertexPath<>(Arrays.asList(1,2,3,5)),
                        new VertexPath<>(Arrays.asList(1,3,4,5)),
                        new VertexPath<>(Arrays.asList(1,3,5))
                ))),
                dynamicTest("2",()->        doFindAllPaths(graph,1,5, 3.0, Arrays.asList(
                        new VertexPath<>(Arrays.asList(1,2,3,5)),
                        new VertexPath<>(Arrays.asList(1,3,4,5)),
                        new VertexPath<>(Arrays.asList(1,3,5))
                )))
        );
    }
    public void doFindAllPaths(DirectedGraph<Integer, Double> graph, int start, int goal, double maxCost, List<VertexPath<Integer>> expected) {
        System.out.println("doTestFindAllPaths start:"+start+", goal:"+goal+", maxCost:"+maxCost);
        ToDoubleFunction<Double> costf = arg->arg;
        DirectedGraphCostPathBuilder<Integer, Double> instance = new DirectedGraphCostPathBuilder<>(graph::getNextEntries,costf);
        List<VertexPath<Integer>> actual = instance.findAllVertexPaths( start, a->a==goal, maxCost);
        assertEquals(expected,actual);
    }
}