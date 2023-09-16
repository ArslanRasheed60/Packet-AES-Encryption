import java.util.Objects;

public class Conversions {
    public static String string_to_binary(String hexText){


        //convert to decimal
        int decimalValue = Integer.parseInt(hexText, 16);
        // Step 2: Convert the integer to binary and return

        return String.format("%16s", Integer.toBinaryString(decimalValue)).replace(' ', '0');
    }

    public static String[][] convert_to_matrix(String binaryString){
        // Check if the binary string length is not equal to 16 (2x2 matrix)
        if (binaryString.length() != 16) {
            System.out.println("Binary string should be exactly 16 bits long.");
            return null;
        }

        // Create a 2x2 matrix of hexadecimal strings
        String[][] matrix = new String[2][2];

        // Fill the matrix with 4-bit hexadecimal substrings
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                // Calculate the starting and ending indexes for the substring
                int startIndex = (i * 2 + j) * 4;
                int endIndex = startIndex + 4;

                // Get the 4-bit substring and convert it to hexadecimal
                matrix[j][i] = binaryString.substring(startIndex, endIndex);
//                int decimalValue = Integer.parseInt(binarySubstring, 2);
//                char hexValue = Integer.toHexString(decimalValue).toUpperCase().charAt(0);

                // Set the hexadecimal value in the matrix
//                matrix[j][i] = hexValue;
            }

        }
        return matrix;
    }

    public static String SubNibbles(String hexText){
        if(hexText.length() != 4){
            System.out.println("Hex length for nibbles should be 4");
            return null;
        }
        switch (hexText) {
            case "0000" -> {
                return "1010";
            }
            case "0001" -> {
                return "0000";
            }
            case "0010" -> {
                return "1001";
            }
            case "0011" -> {
                return "1110";
            }
            case "0100" -> {
                return "0110";
            }
            case "0101" -> {
                return "0011";
            }
            case "0110" -> {
                return "1111";
            }
            case "0111" -> {
                return "0101";
            }
            case "1000" -> {
                return "0001";
            }
            case "1001" -> {
                return "1101";
            }
            case "1010" -> {
                return "1100";
            }
            case "1011" -> {
                return "0111";
            }
            case "1100" -> {
                return "1011";
            }
            case "1101" -> {
                return "0100";
            }
            case "1110" -> {
                return "0010";
            }
            case "1111" -> {
                return "1000";
            }
            default -> {
                return null;
            }
        }
    }

    public static String InverseSubNibbles(String hexText){
        if(hexText.length() != 4){
            System.out.println("Hex length for nibbles should be 4");
            return null;
        }
        switch (hexText) {
            case "1010" -> {
                return "0000";
            }
            case "0000" -> {
                return "0001";
            }
            case "1001" -> {
                return "0010";
            }
            case "1110" -> {
                return "0011";
            }
            case "0110" -> {
                return "0100";
            }
            case "0011" -> {
                return "0101";
            }
            case "1111" -> {
                return "0110";
            }
            case "0101" -> {
                return "0111";
            }
            case "0001" -> {
                return "1000";
            }
            case "1101" -> {
                return "1001";
            }
            case "1100" -> {
                return "1010";
            }
            case "0111" -> {
                return "1011";
            }
            case "1011" -> {
                return "1100";
            }
            case "0100" -> {
                return "1101";
            }
            case "0010" -> {
                return "1110";
            }
            case "1000" -> {
                return "1111";
            }
            default -> {
                return null;
            }
        }
    }

    public static String[][] MixColumns(String[][] inputMatrix){

        String[][] mixColumnMatrix = new String[2][2];
        mixColumnMatrix[0][0] = Conversions.TwoStringXOR(
                Conversions.finiteFieldMultiply("0001", inputMatrix[0][0] ),
                Conversions.finiteFieldMultiply("0100", inputMatrix[1][0] ));
        mixColumnMatrix[1][0] = Conversions.TwoStringXOR(
                Conversions.finiteFieldMultiply("0100", inputMatrix[0][0] ),
                Conversions.finiteFieldMultiply("0001", inputMatrix[1][0] ));
        mixColumnMatrix[0][1] = Conversions.TwoStringXOR(
                Conversions.finiteFieldMultiply("0001", inputMatrix[0][1] ),
                Conversions.finiteFieldMultiply("0100", inputMatrix[1][1] ));
        mixColumnMatrix[1][1] = Conversions.TwoStringXOR(
                Conversions.finiteFieldMultiply("0100", inputMatrix[0][1] ),
                Conversions.finiteFieldMultiply("0001", inputMatrix[1][1] ));


        return mixColumnMatrix;
    }
    public static String[][] InverseMixColumns(String[][] inputMatrix){

        String[][] mixColumnMatrix = new String[2][2];
        mixColumnMatrix[0][0] = Conversions.TwoStringXOR(
                Conversions.finiteFieldMultiply("1001", inputMatrix[0][0] ),
                Conversions.finiteFieldMultiply("0010", inputMatrix[1][0] ));
        mixColumnMatrix[1][0] = Conversions.TwoStringXOR(
                Conversions.finiteFieldMultiply("0010", inputMatrix[0][0] ),
                Conversions.finiteFieldMultiply("1001", inputMatrix[1][0] ));
        mixColumnMatrix[0][1] = Conversions.TwoStringXOR(
                Conversions.finiteFieldMultiply("1001", inputMatrix[0][1] ),
                Conversions.finiteFieldMultiply("0010", inputMatrix[1][1] ));
        mixColumnMatrix[1][1] = Conversions.TwoStringXOR(
                Conversions.finiteFieldMultiply("0010", inputMatrix[0][1] ),
                Conversions.finiteFieldMultiply("1001", inputMatrix[1][1] ));

        return mixColumnMatrix;
    }

    private static String finiteFieldMultiply(String aString, String bString){

        int m = 0;
        int a = Integer.parseInt(aString, 2);;
        int b = Integer.parseInt(bString, 2);

        while(b > 0){
            int lsb = b & 1;
            if(lsb == 1){
                m = m ^ a;
            }
            a = a << 1;

            int forthBitSet = a & 16; //10000
            if(forthBitSet == 16){
                a = a ^ 19; //10011

            }
            b = b >> 1;
        }
        return String.format("%4s", Integer.toBinaryString(m)).replace(' ', '0');
    }

    public static String[][] shiftRow(String[][] inputMatrix){

        String[][] temp = new String[2][2];
        temp[0][0] = inputMatrix[0][1];
        temp[0][1] = inputMatrix[0][0];
        temp[1][0] = inputMatrix[1][0];
        temp[1][1] = inputMatrix[1][1];

        return temp;
    }

    public static String[][] GenerateRoundedKeys(String[][] inputMatrix, String Rcon1){
        String[][] resultMatrix = new String[2][2];

        int a = Integer.parseInt(inputMatrix[0][0], 2);
        int b = Integer.parseInt(inputMatrix[1][0], 2);
        int c = Integer.parseInt(inputMatrix[0][1], 2);
        int d = Integer.parseInt(inputMatrix[1][1], 2);
        int n = Integer.parseInt(Objects.requireNonNull(SubNibbles(inputMatrix[1][1])), 2);
        int r = Integer.parseInt(Rcon1, 2);

        int ra = a ^ n ^ r;
        resultMatrix[0][0] =    String.format("%4s", Integer.toBinaryString(ra)).replace(' ', '0');
        int rb = b ^ ra;
        resultMatrix[1][0] =    String.format("%4s", Integer.toBinaryString(rb)).replace(' ', '0');
        int rc = c ^ rb;
        resultMatrix[0][1] =    String.format("%4s", Integer.toBinaryString(rc)).replace(' ', '0');
        int rd = d ^ rc;
        resultMatrix[1][1] =    String.format("%4s", Integer.toBinaryString(rd)).replace(' ', '0');

        return  resultMatrix;
    }

    public static String[][] TwoMatrixXOR(String[][] plaintText, String[][] keyText){
        String[][] resultMatrix = new String[2][2];
        int a = Integer.parseInt(plaintText[0][0], 2) ^ Integer.parseInt(keyText[0][0], 2);
        int b = Integer.parseInt(plaintText[1][0], 2) ^ Integer.parseInt(keyText[1][0], 2);
        int c = Integer.parseInt(plaintText[0][1], 2) ^ Integer.parseInt(keyText[0][1], 2);
        int d = Integer.parseInt(plaintText[1][1], 2) ^ Integer.parseInt(keyText[1][1], 2);

        resultMatrix[0][0] = String.format("%4s", Integer.toBinaryString(a)).replace(' ', '0');
        resultMatrix[1][0] = String.format("%4s", Integer.toBinaryString(b)).replace(' ', '0');
        resultMatrix[0][1] = String.format("%4s", Integer.toBinaryString(c)).replace(' ', '0');
        resultMatrix[1][1] = String.format("%4s", Integer.toBinaryString(d)).replace(' ', '0');

        return resultMatrix;

    }

    public static String TwoStringXOR(String aText, String bText){
        int a = Integer.parseInt(aText, 2) ^ Integer.parseInt(bText, 2);
        return String.format("%4s", Integer.toBinaryString(a)).replace(' ', '0');
    }

}
