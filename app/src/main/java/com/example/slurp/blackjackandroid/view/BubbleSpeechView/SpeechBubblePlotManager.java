package com.example.slurp.blackjackandroid.view.BubbleSpeechView;

import com.example.slurp.blackjackandroid.view.CustomPoint;

import java.util.ArrayList;
import java.util.Collections;

public class SpeechBubblePlotManager {

    public SpeechBubblePlotManager() { }

    public enum CURVE_ANGLE_ENUM {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    };

    public ArrayList<CustomPoint> plotSpeechBubble(CustomPoint originPoint, float width, float height,
                                                   int borderRadius,
                                                   int spacesBetweenPoints,
                                                   int speechTriangleWidth,
                                                   int specifiedSpacesToPlotOnLeftSide){
        CustomPoint lastPoint = null;

        ArrayList<CustomPoint> pointsArray = new ArrayList<>();

        // draw from top-left to top-right along x axis

        final float widthMinusBorderRadius = (width - borderRadius * 2);
        final float heightMinusBorderRadius = height - (borderRadius * 2);

        final float xDistance = widthMinusBorderRadius / spacesBetweenPoints;
        final float yDistance = heightMinusBorderRadius / (spacesBetweenPoints / 2);

        for(int i = 0; i <= spacesBetweenPoints ; i++){
            CustomPoint currentPoint = new CustomPoint(
                    (originPoint.getX()) + (i * xDistance),
                    originPoint.getY()
            );

            pointsArray.add(currentPoint);
            if(i == spacesBetweenPoints){
                lastPoint = currentPoint;
            }
        }

        ArrayList<CustomPoint> topRightCurve = plotAngleCurvCoords(
                new CustomPoint(lastPoint.getX(), lastPoint.getY() + borderRadius),
                borderRadius,
                4,
                CURVE_ANGLE_ENUM.TOP_RIGHT);

        joinArrayListsOfGenericType(pointsArray, topRightCurve);



        // draw from top-right to bottom-right along y axis
        for(int i = 0; i <= spacesBetweenPoints / 2; i++){
            CustomPoint currentPoint = new CustomPoint(
                    originPoint.getX() + widthMinusBorderRadius + borderRadius,
                    originPoint.getY() + borderRadius + (i * yDistance));

            pointsArray.add(currentPoint);
            if(i == spacesBetweenPoints / 2){
//                console.log("hit 2");
                lastPoint = currentPoint;
            }
        }

        ArrayList<CustomPoint> bottomRightCurve = plotAngleCurvCoords(
                new CustomPoint(lastPoint.getX() - borderRadius, lastPoint.getY()),
                borderRadius,
                4,
                CURVE_ANGLE_ENUM.BOTTOM_RIGHT);

        joinArrayListsOfGenericType(pointsArray, bottomRightCurve);

        // draw from bottom-right to bottom-left along x axis
        for(int i = spacesBetweenPoints; i >= 0; i--){
            CustomPoint currentPoint = new CustomPoint(
                    originPoint.getX() + (i * xDistance),
                    originPoint.getY() + height
            );
            pointsArray.add(currentPoint);

            if(i == 0)
                lastPoint = currentPoint;
        }

        ArrayList<CustomPoint> bottomLeftCurve = plotAngleCurvCoords(
                new CustomPoint(lastPoint.getX(), lastPoint.getY() - borderRadius),
                borderRadius,
                4,
                CURVE_ANGLE_ENUM.BOTTOM_LEFT);

//        pointsArray.push(...bottomLeftCurve);
        joinArrayListsOfGenericType(pointsArray, bottomLeftCurve);


        // draw from top-left to bottom-left along y axis


        if(specifiedSpacesToPlotOnLeftSide > 0){
            // this block of code deals with drawing the triangle on the speech bubble on the left side
            int spacesForLeft = takeNumberAndIncrementByOneIfEvenOrReturnOriginal(specifiedSpacesToPlotOnLeftSide);

            // edge case if 1
            if(spacesForLeft == 1){
                pointsArray.add(new CustomPoint(originPoint.getX() - borderRadius,
                        (originPoint.getY() + borderRadius) + heightMinusBorderRadius));

                pointsArray.add(new CustomPoint(originPoint.getX() - (borderRadius + speechTriangleWidth),
                        (originPoint.getY() + borderRadius) + (heightMinusBorderRadius / 2)));

                pointsArray.add(new CustomPoint(originPoint.getX() - borderRadius,
                        originPoint.getY() + borderRadius));

                lastPoint = new CustomPoint(originPoint.getX() - borderRadius,
                        originPoint.getY() + borderRadius);

            }else{
                for(int i = spacesForLeft; i >= 0; i--){
                    CustomPoint currentPoint = new CustomPoint(originPoint.getX() - borderRadius,
                            (originPoint.getY() + borderRadius) + (i * (heightMinusBorderRadius / spacesForLeft)));

                    if(getMiddleOfOddNumber(spacesForLeft) == i){
                        pointsArray.add(new CustomPoint(originPoint.getX() - (borderRadius + speechTriangleWidth),
                                (originPoint.getY() + borderRadius) + (i * (heightMinusBorderRadius / spacesForLeft))));
                    }else{
                        pointsArray.add(currentPoint);
                    }

                    if(i == 0)
                        lastPoint = currentPoint;
                }
            }
        }else{
            for(int i = spacesBetweenPoints / 2; i >= 0; i--){
                CustomPoint currentPoint = new CustomPoint(
                        originPoint.getX() - borderRadius,
                        originPoint.getY() + ((borderRadius) + (i * yDistance))
                );

                pointsArray.add(currentPoint);
                if(i == 0){
//                console.log("hit 2");
                    lastPoint = currentPoint;
                }
            }
        }

        ArrayList<CustomPoint> topLeftCurve = plotAngleCurvCoords(
                new CustomPoint(lastPoint.getX() + borderRadius, lastPoint.getY()),
                borderRadius,
                4,
                CURVE_ANGLE_ENUM.TOP_LEFT);

//        pointsArray.push(...topLeftCurve);
        joinArrayListsOfGenericType(pointsArray, topLeftCurve);
        return pointsArray;
    }

    private <T> void joinArrayListsOfGenericType(ArrayList<T> to, ArrayList<T> from){
        for(T t : from){
            to.add(t);
        }
    }

    private int takeNumberAndIncrementByOneIfEvenOrReturnOriginal(int n){
        if(n % 2 == 0){
            return n + 1;
        }else{
            return n;
        }
    }

    // run time exception?
    private int getMiddleOfOddNumber(int n) throws IllegalArgumentException{
        if(n % 2 == 0 && n > 3){
            throw new IllegalArgumentException("parsed number must be odd and must be greater than 3");
        }else{
            return (int) Math.ceil(n / 2);
        }
    }

    public ArrayList<CustomPoint> plotAngleCurvCoords(CustomPoint centerPoint,
                                               float radius, float nOfPoints, CURVE_ANGLE_ENUM curveAngleEnum){

        ArrayList<CustomPoint> pointsArray = new ArrayList<>();

        double curveAngleDegreesToSubtract = 0; // should this be try/catch instead?

        switch (curveAngleEnum){
            case TOP_RIGHT:
                System.out.println("hit");
                curveAngleDegreesToSubtract = degreesToRadians(90);
                break;
            case BOTTOM_RIGHT:
                curveAngleDegreesToSubtract = degreesToRadians(0);
                break;
            case BOTTOM_LEFT:
                curveAngleDegreesToSubtract = degreesToRadians(270);
                break;
            case TOP_LEFT:
                curveAngleDegreesToSubtract = degreesToRadians(180);
                break;
            default:
                // throw "curve angle not specified"; // why does this always execute?
                break;
        }

        for(int i = 1; i < nOfPoints; i++){
            double x = centerPoint.getX() + radius * Math.cos((degreesToRadians(90) * (i / nOfPoints)) - curveAngleDegreesToSubtract);
            double y = centerPoint.getY() + radius * Math.sin((degreesToRadians(90) * (i / nOfPoints)) - curveAngleDegreesToSubtract);
            pointsArray.add(new CustomPoint((float) x, (float) y));
        }

        // var x = centerPoint.x + radius * Math.cos(degreesToRadians(90) - curveAngleDegreesToSubtract);
        // var y = centerPoint.y + radius * Math.sin(degreesToRadians(90) - curveAngleDegreesToSubtract);
        // pointsArray.push(new Point(x, y));

        return pointsArray;
    }

    // return type here may need to be double or float for accuracy
    public double degreesToRadians(float degrees){
        return (degrees / 180) * Math.PI;
    }

    public ArrayList<CustomPoint> copyPlotArrAndWiggleByRange(ArrayList<CustomPoint> plotArr, float range){
        ArrayList<CustomPoint> plotArrCopy = new ArrayList<>();

        for(int i = 0; i < plotArr.size(); i++){
            CustomPoint coOrd = plotArr.get(i);
            float randXOffset = generateRandomNegOrPosNumberInRangeX(range);
            float randomYOffset = generateRandomNegOrPosNumberInRangeX(range);

            CustomPoint copyCoOrd = new CustomPoint(
                    coOrd.getX() + randXOffset,
                    coOrd.getY() + randomYOffset
            );
            plotArrCopy.add(copyCoOrd);
        }

        return plotArrCopy;
    }

    public float generateRandomNegOrPosNumberInRangeX(float x){
        float division = x / 2;
        return Math.round(Math.random() * x) - division;
    }
}
