package com.giof71.playground.spiral;

import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class SpiralTest {

    private static int[][] array = {
            { 1, 2, 3, 4, 5 },
            { 16, 17, 18, 19, 6 },
            { 15, 24, 25, 20, 7 },
            { 14, 23, 22, 21, 8 },
            { 13, 12, 11, 10, 9 }
    };

    private static List<Integer> getExpectedResult() {
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i <= 25; ++i) {
            result.add(i);
        }
        return result;
    }

    enum Direction {
        LEFT(0, 1),
        DOWN(1, 0),
        RIGHT(0, -1),
        UP(-1, 0);

        private final int deltaX;
        private final int deltaY;

        private Direction(int deltaX, int deltaY) {
            this.deltaX = deltaX;
            this.deltaY = deltaY;
        }

        int getDeltaX() {
            return deltaX;
        }

        int getDeltaY() {
            return deltaY;
        }
    };

    static class Position {
        private final int x;
        private final int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int getX() {
            return x;
        }

        int getY() {
            return y;
        }
    }

    static class Range {
        private final int low;
        private final int high;

        Range(int low, int high) {
            this.low = low;
            this.high = high;
        }

        boolean inRange(int v) {
            return v >= low && v < high;
        }

        boolean isClosed() {
            return high == low;
        }

        int getLow() {
            return low;
        }

        int getHigh() {
            return high;
        }
    }

    static class Context {

        private final Position position;
        private final Direction direction;

        private final Range xRange;
        private final Range yRange;

        Context(Position position, Direction direction, Range xRange, Range yRange) {
            this.position = position;
            this.direction = direction;
            this.xRange = xRange;
            this.yRange = yRange;
        }

        Position getPosition() {
            return position;
        }

        Direction getDirection() {
            return direction;
        }

        Range getXrange() {
            return xRange;
        }

        Range getYrange() {
            return yRange;
        }

        boolean canMove() {
            return !finalMove();
        }

        boolean finalMove() {
            return xRange.isClosed() || yRange.isClosed();
        }
    }

    private static Context move(Context context) {
        Range xRange = context.getXrange();
        Range yRange = context.getYrange();
        Position position = context.getPosition();
        Direction direction = context.getDirection();

        int newX = direction.getDeltaX() + position.getX();
        int newY = direction.getDeltaY() + position.getY();

        boolean inX = xRange.inRange(newX);
        boolean inY = yRange.inRange(newY);

        if (inX && inY) {
            return new Context(new Position(newX, newY), direction, xRange, yRange);
        }
        int lowX = xRange.getLow();
        int highX = xRange.getHigh();
        int lowY = yRange.getLow();
        int highY = yRange.getHigh();

        if (!inX) {
            if (Direction.DOWN.equals(direction)) {
                highY -= 1;
            } else {
                lowY += 1;
            }
        } else /* (!inY) */ {
            if (Direction.LEFT.equals(direction)) {
                lowX += 1;
            } else {
                highX -= 1;
            }
        }
        Direction newDirection = Direction.values()[(direction.ordinal() + 1) % Direction.values().length];
        newX = newDirection.getDeltaX() + position.getX();
        newY = newDirection.getDeltaY() + position.getY();
        return new Context(new Position(newX, newY), newDirection, new Range(lowX, highX), new Range(lowY, highY));
    }

    private static List<Integer> printSpiral(int[][] array) {
        List<Integer> result = new ArrayList<>();
        int n = array.length;
        int m = n > 0 ? array[0].length : 0;
        Range xRange = new Range(0, n);
        Range yRange = new Range(0, m);
        Position position = new Position(0, 0);
        Direction direction = Direction.values()[0];
        Context context = new Context(position, direction, xRange, yRange);
        while (context.canMove()) {
            int currentValue = array[context.getPosition().getX()][context.getPosition().getY()];
            // uncomment this to se the values in order
            // System.out.println(currentValue);
            result.add(currentValue);
            context = move(context);
        }
        return result;
    }

    @Test
    public void printSpiral() {
        Assertions.assertArrayEquals(getExpectedResult().toArray(), printSpiral(array).toArray());
    }
}
