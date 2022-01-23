package org.firstinspires.ftc.teamcode.drive;

public class Constants {
    public static class Vuforia {
        public static final String VUFORIA_KEY =
                "AYNvGMT/////AAABmbqx+cB66Uiuq1x4OtVVYaYqPxwETuoHASIFChwOE0FE5KyJCGATrLe9r1HPlycJfKg4" +
                        "LyYDwGfvzkJ5Afan80ksPhJFg+93fhn8xNTgV09R7cY6VtUrO61/myrjehuHoRU6UH8hAZdlV0E" +
                        "6/Q1y36TSsp0iaOWX008UCFI/jJo/UoWG7y6uZsPH5MJxGucu6jWBjERv+bS9zHvsGFDlGmIFdJi" +
                        "c2YbYP+SpUM+KK437815Iz/PxAAAK+1SUObQVGiVj/FuqB5yhSvBrkX1H1NQ2jzZDfNfzEQr5cHM" +
                        "zU68IOGhxd+yjicwx7ppxaAcFlrPE8hILKAQ90k5i6gwY1vzHwapOgLA5PSI0jsX1z/Dg";

        public static final String[] LABELS = {
                "Ball",
                "Cube",
                "Duck",
                "Marker"
        };

        public static final String TENSORFLOW_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    }

    public final static class ArmPosition {
        public final static int FEED           = 0;
        public final static int BOTTOM         = 1;
        public final static int MIDDLE         = 2;
        public final static int TOP            = 3;
        public final static int INVERSE_TOP    = 4;
        public final static int INVERSE_MIDDLE = 5;
        public final static int INVERSE_BOTTOM = 6;
        public final static int CAP_UP         = 7;
        public final static int CAP_DOWN       = 8;
        public final static int INVERSE_FEED   = 9;
    }
}
