package NumberConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

public class Converter {
    private static final ArrayList<Character> alphaValues = new ArrayList<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'));
    private static int sourceBase;
    private static int targetBase;

    private static final MathContext precisionOfNegativeExponents = new MathContext(15);
    private  static final int precision = 5;


    /**
     * @param c character to convert
     * @return integer equivalent of c.
     */
    public static int getAlphaValue(char c) {
        return alphaValues.indexOf(Character.toUpperCase(c));
    }

    /**
     * @param sourceInput User inputted source base
     * @param targetInput User inputted target base
     */
    public static void setBases(int sourceInput, int targetInput) {
        sourceBase = sourceInput;
        targetBase = targetInput;
    }

    /**
     * @param sourceNumber User inputted number in source base to be converted
     * @return converted number as a string in target base
     */
    public static String run(String sourceNumber) {
        String result;
        if (sourceNumber.contains(".")) {
            String[] splitNumber = sourceNumber.split("\\.");
            String sourceInteger = splitNumber[0];
            String sourceFraction = splitNumber[1];

            BigInteger decimalInteger = toDecimal(sourceInteger);
            BigDecimal decimalFraction = toDecimalFraction(sourceFraction);

            String targetInteger = fromDecimal(decimalInteger);
            String targetFraction = fromDecimalFraction(decimalFraction);

            result = targetInteger + targetFraction;

        } else {
            BigInteger decimalNumber = toDecimal(sourceNumber);
            result = fromDecimal(decimalNumber);
        }
        return result;
    }


    /**
     * @param input Decimal number to convert to target base.
     * @return converted number in target base as a string.
     */
    public static String fromDecimal(BigInteger input) {
        String result = "0";
        StringBuilder convertedNumber = new StringBuilder();
        BigInteger number = input;
        if (number.compareTo(BigInteger.ZERO) != 0) {
            while (number.compareTo(BigInteger.ZERO) == 1) {
                int digit = number.mod(BigInteger.valueOf(targetBase)).intValue();
                convertedNumber.insert(0, alphaValues.get(digit));
                number = number.divide(BigInteger.valueOf(targetBase));
            }
            result = convertedNumber.toString();
        }
        return result;
    }

    /**
     * @param number number in source base to be converted.
     * @return converted number in decimal.
     */
    public static BigInteger toDecimal(String number) {
        BigInteger result;
        if ("0".equals(number)) {
            result = BigInteger.ZERO;
        } else {
            char[] numberArray = number.toCharArray();
            int numberOfPowers = numberArray.length - 1;
            BigInteger powers = BigInteger.valueOf(sourceBase).pow(numberOfPowers);
            BigInteger decimalNumber = BigInteger.ZERO;
            for (char n : numberArray) {
                decimalNumber = decimalNumber.add(BigInteger.valueOf(getAlphaValue(n)).multiply(powers));
                powers = powers.divide(BigInteger.valueOf(sourceBase));
            }
            result = decimalNumber;
        }
        return result;
    }

    /**
     * @param sourceInput fractional part of source number to be converted
     * @return fraction in decimal.
     */
    public static BigDecimal toDecimalFraction(String sourceInput) {
        char[] numberArray = sourceInput.toCharArray();
        BigDecimal decimalFraction = BigDecimal.ZERO;
        for (int i = 0; i < numberArray.length; i++) {
            int power = Math.negateExact(i + 1);
            BigDecimal fractionalPosition = BigDecimal.valueOf(sourceBase).pow(power, precisionOfNegativeExponents);
            decimalFraction = decimalFraction.add(BigDecimal.valueOf(getAlphaValue(numberArray[i])).multiply(fractionalPosition));
        }
        return decimalFraction.setScale(precision, RoundingMode.UP);
    }

    /**
     * @param decimalInput fractional number in decimal to be converted to target number.
     * @return fractional part of number converted to target base.
     */
    public static String fromDecimalFraction(BigDecimal decimalInput) {
        StringBuilder targetFraction = new StringBuilder();
        BigDecimal convertIteration = decimalInput;

        for (int i = 0; i < precision; i++) {
            BigDecimal multiplicationOfIteration = convertIteration.multiply(BigDecimal.valueOf(targetBase));
            convertIteration = multiplicationOfIteration.remainder(BigDecimal.ONE);
            int wholeNumber = multiplicationOfIteration.subtract(convertIteration).intValue();
            targetFraction.append(alphaValues.get(wholeNumber));
        }
            return "." + targetFraction;
    }
}
