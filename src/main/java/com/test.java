package com;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

import java.math.BigDecimal;
import java.math.MathContext;

public class test {
    public static void main(String[] args) {
        //master
        //test1
        /*double[] array = {207.650, 205.160, 210.870, 209.350, 207.250, 209.960, 207.650, 205.160, 188.170, 186.020};

        BigDecimal a = new BigDecimal(1.12, MathContext.DECIMAL32);
        System.out.println(a);
        System.out.println(a.precision());
        System.out.println(Math.max(33, 32));

        String b = "TariffFactoryUser:bpjDR4wpPU5fWHBOPPwC";
        String c = "asdв";
*/
    }

    public static void sma() {
        double[] closePrice = new double[10];
        double[] out = new double[10];
        MInteger begin = new MInteger();
        MInteger length = new MInteger();

        Core c = new Core();
        RetCode retCode = c.sma(0, closePrice.length - 1, closePrice, 5, begin, length, out);
        if (retCode == RetCode.Success) {
            System.out.println("Output Begin:" + begin.value);
            System.out.println("Output Begin:" + length.value);
            for (int i = begin.value; i < length.value; i++) {
                StringBuilder line = new StringBuilder();
                line.append("Period #");
                line.append(i + 1);
                line.append(" close= ");
                line.append(closePrice[i]);
                line.append(" mov avg=");
                line.append(out[i]);
                System.out.println(line.toString());
            }
        } else {
            System.out.println("Error");
        }
    }

    public static double[] rsi(double[] prices, int period) {
        double[] output = new double[prices.length];
        double[] tempOutPut = new double[prices.length];
        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;
        final Core core = new Core();
        retCode = core.rsi(0, prices.length - 1, prices, period, begin, length, tempOutPut);
        for (int i = 0; i < period; i++) {
            output[i] = 0;
        }
        for (int i = period; 0 < i && i < (prices.length); i++) {
            output[i] = tempOutPut[i - period];
        }
        return output;
    }
}
