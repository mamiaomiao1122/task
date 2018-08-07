

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 迷宫游戏
 *
 * @author mmm
 * @create 2018-07-29 下午 3:20
 **/
public class TestMain {
    public static String COLOR_W = "[W]";
    public static String COLOR_R = "[R]";

    public static void main(String[] arg) throws Exception {
        try {
            //初始化迷宫
            System.out.println("请输入迷宫大小：");
            BufferedReader mnBr = new BufferedReader(new InputStreamReader(System.in));
            Pot mnPot = convert2Pot(mnBr.readLine(), " ");
            Node[][] nodes = initMaze(mnPot.getX(), mnPot.getY());
            printMaze(nodes);

            //请输入路径
            System.out.println("请输入路径规则：");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String routes = br.readLine();
            String[] ss = routes.split(";");

            //节点的连通
            for (String s : ss) {
                String[] xx = s.split(" ");
                link(nodes, convert2Pot(xx[0], ","), convert2Pot(xx[1], ","));
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
                Node node = new Node(new Pot(i, j));
                if (i % 2 != 0 && j % 2 != 0) {
                    node.setColor(TestMain.COLOR_R);
                }
                nodes[i][j] = node;
            }
        }
        return nodes;
    }

    /**
     * 将相邻路径节点连通
     *
     * @param nodes 迷宫列表
     * @param pot1  路径节点坐标1
     * @param pot2  路径节点坐标2
     * @throws BizException
     */
    static void link(Node[][] nodes, Pot pot1, Pot pot2) throws BizException {
        //第一个点的坐标转换
        int x1 = pot1.getX() * 2 + 1;
        int y1 = pot1.getY() * 2 + 1;

        //第二个点的坐标转换
        int x2 = pot2.getX() * 2 + 1;
        int y2 = pot2.getY() * 2 + 1;

        //计算坐标点的差值
        int dif_x = x1 - x2;
        int dif_y = y1 - y2;

        //路径是否可以连接判断
        if (Math.abs(dif_x) != 2 && Math.abs(dif_y) != 2) {
            throw new BizException("Maze format error");
        }

        //将相连节点连通
        if (Math.abs(dif_x) == 2) {
            nodes[x2 + dif_x + 1][y1].setColor(TestMain.COLOR_R);
        }
        if (Math.abs(dif_y) == 2) {
            nodes[x1][y2 + dif_y + 1].setColor(TestMain.COLOR_R);
        }
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
     * 坐标转换
     *
     * @param str       坐标字符串
     * @param splitChar 分隔符
     * @return
     * @throws BizException
     */
    static Pot convert2Pot(String str, String splitChar) throws BizException {
        if (str == null || str.length() == 0) {
            throw new BizException("Incorrect command format");
        }
        String[] ss = str.split(splitChar);
        if (ss.length != 2) {
            throw new BizException("Incorrect command format");
        }
        Integer m, n;
        try {
            m = Integer.parseInt(ss[0]);
            n = Integer.parseInt(ss[1]);
        } catch (Exception e) {
            throw new BizException("Invalid number format");
        }
        if (m < 0 || n < 0) {
            throw new BizException("Invalid number format");
        }
        return new Pot(m, n);
    }
}

/**
 * 节点类
 */
class Node {

    /**
     * 坐标
     **/
    Pot pot;

    /**
     * 颜色 默认：墙的颜色
     **/
    private String color = TestMain.COLOR_W;

    public Node(Pot pot) {
        this.pot = pot;
    }

    public Pot getPot() {
        return pot;
    }

    public void setPot(Pot pot) {
        this.pot = pot;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

/**
 * 坐标类
 */
class Pot {

    /**
     * 坐标X
     **/
    private int x;

    /**
     * 坐标Y
     **/
    private int y;

    public Pot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

/**
 * 业务异常
 */
class BizException extends Exception {

    /**
     * 构造函数
     *
     * @param message
     */
    public BizException(String message) {
        super(message);
    }
}