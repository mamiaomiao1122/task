/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
// package com.zdf.algorithm.maze;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zhangdefeng
 * @version $Id: Maze.java, v 0.1 2018年07月31日 上午9:50 zhangdefeng Exp $
 */
public class Maze {

    /** 墙的颜色 */
    public static String COLOR_W = "[W]";

    /** 路的颜色 */
    public static String COLOR_R = "[R]";

    public static void main(String[] arg) throws Exception {
        try {
            //初始化迷宫
            System.out.println("请输入迷宫大小：");
            BufferedReader mnBr = new BufferedReader(new InputStreamReader(System.in));
            int[] mazeSize = MazeUtils.convert2IntArray(mnBr.readLine(), " ");
            Node[][] nodes = initMaze(mazeSize[0], mazeSize[1]);
            printMaze(nodes);

            //请输入路径
            System.out.println("请输入路径规则：");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String[] ss = MazeUtils.convert2StringArrays(br.readLine(), ";");
            //连通节点
            for (String s : ss) {
                String[] xx = MazeUtils.convert2StringArray(s, " ");
                Node node1 = new Node(MazeUtils.convert2IntArray(xx[0], ",")[0],
                    MazeUtils.convert2IntArray(xx[0], ",")[1]);
                Node node2 = new Node(MazeUtils.convert2IntArray(xx[1], ",")[0],
                    MazeUtils.convert2IntArray(xx[1], ",")[1]);
                link(nodes, node1, node2);
            }

            //取出路径节点
            List<Node> rNodeList = new ArrayList<>();
            for (int i = 0; i < nodes.length; i++) {
                for (int j = 0; j < nodes[0].length; j++) {
                    if (nodes[i][j].getColor() == COLOR_R) {
                        rNodeList.add(nodes[i][j]);
                    }
                }
            }

            //定义一个联通的集合 
            List<Node> linkedNodes = new ArrayList<>();
            rNodeList.get(0).setVisited(true);
            linkedNodes.add(rNodeList.get(0));

            //遍历节点 只要节点和联通集合中任意一个联通，就放到集合中去
            for (Node signalNode : rNodeList) {
                if (!signalNode.isVisited()) {
                    for (Node linkedNode : linkedNodes) {
                        if (signalNode.isLinked(linkedNode)) {
                            signalNode.setVisited(true);
                        }
                    }
                    linkedNodes.add(signalNode);
                }
            }

            //如果两个集合的大小相等，说明所有点是联通的
            if (linkedNodes.size() == rNodeList.size()) {
                System.out.println("Maze is all connected");
            } else {
                throw new BizException("Maze not connected");
            }

            //再次打印迷宫
            printMaze(nodes);
        } catch (BizException bizException) {
            System.out.println(bizException.getMessage());
        }
    }

    /**
     * 初始化
     *
     * @param m 行数
     * @param n 列数
     * @return
     */
    static Node[][] initMaze(Integer m, Integer n) {
        Node[][] nodes = new Node[2 * m + 1][2 * n + 1];
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[0].length; j++) {
                Node node = new Node(i, j);
                if (i % 2 != 0 && j % 2 != 0) {
                    node.setColor(Maze.COLOR_R);
                }
                nodes[i][j] = node;
            }
        }
        return nodes;
    }

    /**
     * 打印迷宫
     *
     * @param nodes 迷宫节点
     */
    static void printMaze(Node[][] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[0].length; j++) {
                System.out.print(nodes[i][j].getColor());
                System.out.print("\t");
            }
            System.out.println();
        }
    }

    /**
     * 将相邻路径节点连通
     *
     * @param nodes 迷宫列表
     * @param node1  节点1
     * @param node2  节点2
     * @throws BizException
     */
    static void link(Node[][] nodes, Node node1, Node node2) throws BizException {
        //第一个点的坐标转换
        int x1 = node1.getX() * 2 + 1;
        int y1 = node1.getY() * 2 + 1;

        //第二个点的坐标转换
        int x2 = node2.getX() * 2 + 1;
        int y2 = node2.getY() * 2 + 1;

        //计算坐标点的差值
        int dif_x = x1 - x2;
        int dif_y = y1 - y2;

        //路径是否可以连接判断
        if (Math.abs(dif_x) != 2 && Math.abs(dif_y) != 2) {
            throw new BizException("Maze format error");
        }
        if (Math.abs(dif_x) == 2 && Math.abs(dif_y) == 2) {
            throw new BizException("Maze format error");
        }

        //将相连节点连通
        if (Math.abs(dif_x) == 2 && Math.abs(dif_y) == 0) {
            nodes[x2 + dif_x + 1][y1].setColor(Maze.COLOR_R);
        }
        if (Math.abs(dif_y) == 2 && Math.abs(dif_x) == 0) {
            nodes[x1][y2 + dif_y + 1].setColor(Maze.COLOR_R);
        }
    }



    static String[] convert2StringArrays(String str, String splitChar) throws BizException {
        if (str == null || str.length() == 0) {
            throw new BizException("Incorrect command format");
        }
        String[] ss = str.split(splitChar);
        if (ss.length % 2 == 0) {
            throw new BizException("Incorrect command format");
        }
        return ss;
    }

    static String[] convert2StringArray(String str, String splitChar) throws BizException {
        if (str == null || str.length() == 0) {
            throw new BizException("Incorrect command format");
        }
        String[] ss = str.split(splitChar);
        if (ss.length != 2) {
            throw new BizException("Incorrect command format");
        }
        return ss;
    }

    static int[] convert2IntArray(String str, String splitChar) throws BizException {
        if (str == null || str.length() == 0) {
            throw new BizException("Incorrect command format");
        }
        String[] ss = str.split(splitChar);
        return new int[] { convert2Int(ss[0]), convert2Int(ss[1]) };
    }

    /**
     * 字符串数字转数字
     *
     * @param strNum 字符串数字
     * @return
     * @throws BizException
     */
    static int convert2Int(String strNum) throws BizException {
        if (strNum == null || strNum.length() == 0) {
            throw new BizException("Incorrect command format");
        }
        int intNum = 0;
        try {
            intNum = Integer.parseInt(strNum);
        } catch (Exception e) {
            throw new BizException("Invalid number format");
        }
        if (intNum < 0) {
            throw new BizException("Invalid number format");
        }
        return intNum;
    }

}

/**
 * 节点类
 */
class Node {

    /** 坐标X */
    private int     x;

    /** 坐标Y */
    private int     y;

    /** 书否已经被访问过 */
    private boolean visited;

    /** 颜色 默认：墙的的颜色 */
    private String  color = Maze.COLOR_W;

    /**
     * 构造函数
     *
     * @param x 坐标x
     * @param y 坐标y
     */
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter method for property x.
     *
     * @return property value of x
     */
    public int getX() {
        return x;
    }

    /**
     * Setter method for property x.
     *
     * @param x value to be assigned to property x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Getter method for property y.
     *
     * @return property value of y
     */
    public int getY() {
        return y;
    }

    /**
     * Setter method for property y.
     *
     * @param y value to be assigned to property y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Getter method for property color.
     *
     * @return property value of color
     */
    public String getColor() {
        return color;
    }

    /**
     * Setter method for property color.
     *
     * @param color value to be assigned to property color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Getter method for property visited.
     *
     * @return property value of visited
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Setter method for property visited.
     *
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isLinked(Node t) {
        if (t == null) {
            return false;
        }
        //计算差值
        int difX = this.getX() - t.getX();
        int difY = this.getY() - t.getY();
        //X坐标相邻 连通
        if (Math.abs(difX) == 1 && difY == 0) {
            return true;
        }
        //Y坐标相邻 连通
        if (Math.abs(difY) == 1 && difX == 0) {
            return true;
        }
        return false;
    }
}

/**
 * 业务异常
 */
class BizException extends Exception {

    /**
     * 构造函数
     *
     * @param message 异常信息
     */
    public BizException(String message) {
        super(message);
    }

}