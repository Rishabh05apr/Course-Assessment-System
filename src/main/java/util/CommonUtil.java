package util;

import org.apache.commons.lang.StringUtils;

import java.util.Random;

/**
 * Created by pinak on 10/28/2017.
 */
public class CommonUtil {
    public static boolean isValidString(String str){
        return !(str == null || StringUtils.isEmpty(str) || StringUtils.isBlank(str));
    }
    public static boolean isValidInteger(Integer num){
        return (num!=null);
    }

    public static String formatOracleErrorMessage(String oraMessage){
        if(!isValidString(oraMessage))
            return oraMessage;
        oraMessage = oraMessage.replaceAll("ORA-[0-9]+:","").trim();
        return oraMessage;
    }


    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}
