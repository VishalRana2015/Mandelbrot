import java.awt.geom.Point2D;
import java.util.ArrayList;

public class CohenSutherlandClippingAlgorithm {
    double topX, topY, bottomX, bottomY;
    private static final int INSIDE = 0;
    private static final int LEFT = 1;
    private static final int TOP = 2;
    private static final int RIGHT = 4;
    private static final int BOTTOM = 8;

    public CohenSutherlandClippingAlgorithm(int topX, int topY, int bottomX, int bottomY) {
        this.topX = topX;
        this.topY = topY;
        this.bottomX = bottomX;
        this.bottomY = bottomY;
    }

    private int computeCode(Point2D point) {
        int code = INSIDE; // initially assuming the point lies inside the rectangle
        // and then we will start verifying it.
        if (point.getX() < this.topX) {
            // the point is on the left side of the rectangle
            code = code | LEFT;
        } else if (point.getX() > this.bottomX) {
            code = code | RIGHT;
        }
        if (point.getY() < this.topY) {
            // the point is above the rectangle
            code = code | TOP;
        } else if (point.getY() > this.bottomY) {
            code = code | BOTTOM;
        }

        return code;
    }

    public ArrayList<Point2D> cohenSutherlandClip(Point2D p1, Point2D p2) {
        int code1 = computeCode(p1), code2 = computeCode(p2);
        // initially we assume that the line segment created by joining these two points doesn't intersect the rectangle.
        boolean accept = false;


        // keep on iterating, until the line segment gets accepted or rejected.
        while (true) {
            if (code1 == 0 && code2 == 0) {
                // line lies within the rectangle
                accept = true;
                break;
            } else if ((code1 & code2) != 0) {
                // both these endpoints, share at least one common region that is outside of the rectangle.
                // For example, either both are above, or below, or to the right or to the left of the rectangle.
                // In this case, line segment created by joining these endpoints doesn't intersect the rectangle.
                break;
            } else {
                // At least one point is outside the rectangle.
                //Some of the line segment lies within the rectangle.

                // find any point that lies outside the rectangle
                int code;
                Point2D point;
                if (code1 != 0) {
                    code = code1;
                    point = p1;
                } else {
                    code = code2;
                    point = p2;
                }
                double x = 0, y = 0;

                // Find the intersection point
                // Using formula y= y1 + slope(x- x1)
                // x = x1 + (1/slope) ( y - y1)
                if ((code & TOP) != 0) {
                    // this point lies above the region
                    x = p1.getX() + (p2.getX() - p1.getX()) * (topY - p1.getY()) / (p2.getY() - p1.getY());
                    y = topY;
                } else if ((code & BOTTOM) != 0) {
                    // this point lies below the region
                    x = p1.getX() + (p2.getX() - p1.getX()) * (this.bottomY - p1.getY()) / (p2.getY() - p1.getY());
                    y = bottomY;
                } else if ((code & LEFT) != 0) {
                    // this point lies left to the region
                    y = p1.getY() + (p2.getY() - p1.getY()) * (this.topX - p1.getX()) / (p2.getX() - p1.getX());
                    x = this.topX;
                } else if ((code & RIGHT) != 0) {
                    y = p1.getY() + (p2.getY() - p1.getY()) * (this.bottomX - p1.getX()) / (p2.getX() - p1.getX());
                    x = this.bottomX;
                }

                if (point == p1) {
                    p1 = new Point2D.Double(x, y);
                    code1 = computeCode(p1);
                } else {
                    p2 = new Point2D.Double(x, y);
                    code2 = computeCode(p2);
                }
            }
        }

        if (accept) {
            ArrayList<Point2D> pointList = new ArrayList<>();
            pointList.add(p1);
            pointList.add(p2);
            return pointList;
        }
        return null;
    }

    public static void main(String[] args) {
        CohenSutherlandClippingAlgorithm cohenSutherlandClippingAlgorithm = new CohenSutherlandClippingAlgorithm(0, 0, 100, 100);
//        ArrayList<Point2D> pointList = cohenSutherlandClippingAlgorithm.cohenSutherlandClip(new Point2D.Double(5,5), new Point2D.Double(7, 7));
//        printList(pointList);
        printList(cohenSutherlandClippingAlgorithm.cohenSutherlandClip(new Point2D.Double(130, 30), new Point2D.Double(50, 140)));
    }

    public static void printList(ArrayList<Point2D> pointList) {
        if (pointList == null) {
            System.out.println("empty list\n");
            return;
        }
        for (Point2D point : pointList) {
            System.out.println(point);
        }
        System.out.println();
    }
}
