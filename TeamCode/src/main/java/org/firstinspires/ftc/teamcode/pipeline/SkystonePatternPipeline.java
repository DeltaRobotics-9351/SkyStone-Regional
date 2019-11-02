package org.firstinspires.ftc.teamcode.pipeline;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import static org.opencv.core.CvType.CV_8UC1;

public class SkystonePatternPipeline extends OpenCvPipeline {
    public int left_hue;
    public int right_hue;

    public int left_br;
    public int right_br;

    public int pattern = 0;

    @Override
    public Mat processFrame(Mat input) {

        input.convertTo(input, CV_8UC1, 1, 10);

        int[] left_rect = {
                (int) (input.cols() * (13f / 32f)),
                (int) (input.rows() * (8f / 32f)),
                (int) (input.cols() * (19f / 32f)),
                (int) (input.rows() * (15f / 32f))
        };

        int[] right_rect = {
                (int) (input.cols() * (13f / 32f)),
                (int) (input.rows() * (17f / 32f)),
                (int) (input.cols() * (19f / 32f)),
                (int) (input.rows() * (24f / 32f))
        };

        Imgproc.rectangle(
                input,
                new Point(
                        left_rect[0],
                        left_rect[1]),

                new Point(
                        left_rect[2],
                        left_rect[3]),
                new Scalar(0, 255, 0), 1);

        Imgproc.rectangle(
                input,
                new Point(
                        right_rect[0],
                        right_rect[1]),

                new Point(
                        right_rect[2],
                        right_rect[3]),
                new Scalar(0, 0, 255), 1);

        Mat left_block = input.submat(left_rect[1], left_rect[3], left_rect[0], left_rect[2]);
        Mat right_block = input.submat(right_rect[1], right_rect[3], right_rect[0], right_rect[2]);

        Scalar left_mean = Core.mean(left_block);

        Scalar right_mean = Core.mean(right_block);

        left_hue = get_hue((int) left_mean.val[0], (int) left_mean.val[1], (int) left_mean.val[2]);
        right_hue = get_hue((int) right_mean.val[0], (int) right_mean.val[1], (int) right_mean.val[2]);
        left_br = get_brightness((int) left_mean.val[0], (int) left_mean.val[1], (int) left_mean.val[2]);
        right_br = get_brightness((int) right_mean.val[0], (int) right_mean.val[1], (int) right_mean.val[2]);

        if (left_br > 100 && right_br > 100) pattern = 1;
        else if (left_br > 100 && right_br < 100) pattern = 2;
        else if (left_br < 100 && right_br > 100) pattern = 3;
        else if (left_br < 100 && right_br < 100) {
            if (left_br > right_br) {
                pattern = 1;
            } else if (left_br < right_br) {
                pattern = 2;
            } else {
                pattern = 3;
            }
        }

        return input;
    }

    private int get_hue(int red, int green, int blue) {

        float min = Math.min(Math.min(red, green), blue);
        float max = Math.max(Math.max(red, green), blue);

        if (min == max) {
            return 0;
        }

        float hue = 0f;
        if (max == red) {
            hue = (green - blue) / (max - min);

        } else if (max == green) {
            hue = 2f + (blue - red) / (max - min);

        } else {
            hue = 4f + (red - green) / (max - min);
        }

        hue = hue * 60;
        if (hue < 0) hue = hue + 360;

        return Math.round(hue);
    }

    private int get_brightness(int red, int green, int blue) {
        return (int) (((double) (red + green + blue)) / 3);
    }
}