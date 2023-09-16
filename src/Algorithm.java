public class Algorithm {
    public static String[][] Encrytion(String plainText, String key){

        String binaryInput = Conversions.string_to_binary(plainText);
        System.out.println(binaryInput);

        //binary to matrix
        String[][] inputMatrix = Conversions.convert_to_matrix(binaryInput);
        System.out.print("Plain Text: ");
        Main.printMatrix(inputMatrix);

        String[][] subNibbleMatrix = new String[2][2];

        // applying sub-nibbles using substitution box
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assert inputMatrix != null;
                subNibbleMatrix[i][j] = Conversions.SubNibbles(inputMatrix[i][j]);
            }
        }
        System.out.print("After 1st Sub Nibbles: ");
        Main.printMatrix(subNibbleMatrix);

        // generation of keys
        //text to binary
        String binaryKeyInput = Conversions.string_to_binary(key);

        //binary to matrix
        String[][] keyMatrix = Conversions.convert_to_matrix(binaryKeyInput);
        System.out.print("Key: ");
        Main.printMatrix(keyMatrix);

//        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < 2; j++) {
//                assert keyMatrix != null;
//                subNibbleMatrix[i][j] = Conversions.SubNibbles(keyMatrix[i][j]);
//            }
//        }
//        System.out.print("After Sub Nibbles of Master Key: ");
//        Main.printMatrix(subNibbleMatrix);

        assert keyMatrix != null;
        String[][] GenerateRoundedKeyMatrix1 = Conversions.GenerateRoundedKeys(keyMatrix, "1110");
        System.out.print("Rounded Key 1: ");
        Main.printMatrix(GenerateRoundedKeyMatrix1);

        String[][] GenerateRoundedKeyMatrix2 = Conversions.GenerateRoundedKeys(GenerateRoundedKeyMatrix1, "1010");
        System.out.print("Rounded Key 2: ");
        Main.printMatrix(GenerateRoundedKeyMatrix2);

        // two matrix xor
        String[][] XorMatrix = Conversions.TwoMatrixXOR(subNibbleMatrix, GenerateRoundedKeyMatrix1);
        System.out.print("After 1st XOR with key 1: ");
        Main.printMatrix(XorMatrix);

        // mix columns
        int [][] constantMatrix = {{1, 4}, {4, 1}};
//        String[][] mixColumnMatrix = Conversions.MixColumns(XorMatrix,constantMatrix);
        String[][] mixColumnMatrix = Conversions.MixColumns(XorMatrix);
        System.out.print("After Mix Columns: ");
        Main.printMatrix(mixColumnMatrix);

        //shift row
        String[][] shiftRowMatrix = Conversions.shiftRow(mixColumnMatrix);
        System.out.print("After 1st shift Row: ");
        Main.printMatrix(shiftRowMatrix);

        //2nd sub-nibbles
        // applying sub-nibbles 2nd time using substitution box
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assert shiftRowMatrix != null;
                subNibbleMatrix[i][j] = Conversions.SubNibbles(shiftRowMatrix[i][j]);
            }
        }
        System.out.print("After 2nd Sub Nibbles: ");
        Main.printMatrix(subNibbleMatrix);

        // xor with 2nd key
        XorMatrix = Conversions.TwoMatrixXOR(subNibbleMatrix, GenerateRoundedKeyMatrix2);
        System.out.print("After 2nd XOR with key 2: ");
        Main.printMatrix(XorMatrix);

        //2nd shift row
        shiftRowMatrix = Conversions.shiftRow(XorMatrix);
        System.out.print("After 2nd shift Row: ");
        Main.printMatrix(shiftRowMatrix);

        return shiftRowMatrix;

    }

    public static String[][] Decryption(String cipherText, String key, boolean wantOutput ){

        String binaryInput = Conversions.string_to_binary(cipherText);
        if(wantOutput) {
            System.out.println(binaryInput);
        }
        //binary to matrix
        String[][] inputMatrix = Conversions.convert_to_matrix(binaryInput);
        if(wantOutput){
            System.out.print("Cipher Text: ");
            Main.printMatrix(inputMatrix);
        }


        //shift row
        assert inputMatrix != null;
        String[][] shiftRowMatrix = Conversions.shiftRow(inputMatrix);
        if(wantOutput) {
            System.out.print("After 2nd shift Row: ");
            Main.printMatrix(shiftRowMatrix);
        }

        // generation of keys
        //text to binary
        String binaryKeyInput = Conversions.string_to_binary(key);

        //binary to matrix
        String[][] keyMatrix = Conversions.convert_to_matrix(binaryKeyInput);
        if(wantOutput) {
            System.out.print("Key: ");
            Main.printMatrix(keyMatrix);
        }


        assert keyMatrix != null;
        String[][] GenerateRoundedKeyMatrix1 = Conversions.GenerateRoundedKeys(keyMatrix, "1110");
        if(wantOutput) {
            System.out.print("Rounded Key 1: ");
            Main.printMatrix(GenerateRoundedKeyMatrix1);
        }

        String[][] GenerateRoundedKeyMatrix2 = Conversions.GenerateRoundedKeys(GenerateRoundedKeyMatrix1, "1010");
        if(wantOutput) {
            System.out.print("Rounded Key 2: ");
            Main.printMatrix(GenerateRoundedKeyMatrix2);
        }

        // xor with 2nd key
        String[][] XorMatrix = Conversions.TwoMatrixXOR(shiftRowMatrix, GenerateRoundedKeyMatrix2);
        if(wantOutput) {
            System.out.print("After 1st XOR with key 2: ");
            Main.printMatrix(XorMatrix);
        }
        //2nd sub-nibbles
        // applying sub-nibbles 2nd time using substitution box
        String[][] subNibbleMatrix = new String[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                subNibbleMatrix[i][j] = Conversions.InverseSubNibbles(XorMatrix[i][j]);
            }
        }
        if(wantOutput) {
            System.out.print("After 2nd Sub Nibbles: ");
            Main.printMatrix(subNibbleMatrix);
        }

        //2nd shift row
        shiftRowMatrix = Conversions.shiftRow(subNibbleMatrix);
        if(wantOutput) {
            System.out.print("After 1st shift Row: ");
            Main.printMatrix(shiftRowMatrix);
        }
        // mix columns
        int [][] constantMatrix = {{9, 2}, {2, 9}};
        String[][] mixColumnMatrix = Conversions.InverseMixColumns(shiftRowMatrix);
        if(wantOutput) {
            System.out.print("After Mix Columns: ");
            Main.printMatrix(mixColumnMatrix);
        }


        // two matrix xor
        XorMatrix = Conversions.TwoMatrixXOR(mixColumnMatrix, GenerateRoundedKeyMatrix1);
        if(wantOutput) {
            System.out.print("After 2nd XOR with key 1: ");
            Main.printMatrix(XorMatrix);
        }
        //2nd sub-nibbles
        // applying sub-nibbles 2nd time using substitution box
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                subNibbleMatrix[i][j] = Conversions.InverseSubNibbles(XorMatrix[i][j]);
            }
        }
        if(wantOutput) {
            System.out.print("After 1st Sub Nibbles: ");
            Main.printMatrix(subNibbleMatrix);
        }
        return subNibbleMatrix;
    }

    public static String DecrytTextFile(String cipherText, String key){
        if(cipherText == ""){
            System.out.println("Cipher Text Does not exists");
            return null;
        }

        StringBuilder plainText = new StringBuilder();
        String[][] textMatrix;
        String cipherTextSubstring = "";
        for(int i = 0; i < cipherText.length()-1; i += 5){
            cipherTextSubstring = cipherText.substring(i, i+4);
            //validation
            System.out.print(cipherTextSubstring + " : ");
            if(!Main.Validate16BitHexInput(cipherTextSubstring)){
                return null;
            }

            textMatrix = Decryption(cipherTextSubstring, key, false);

            String letter1 = convertBinaryToLetter(textMatrix[0][0] + textMatrix[1][0]);
            String letter2 = convertBinaryToLetter(textMatrix[0][1] + textMatrix[1][1]);

            plainText.append(letter1).append(letter2);

        }

        return removeTrailingNull(plainText.toString());
    }

    public static String removeTrailingNull(String input) {
        return input.replaceAll("\\x00+$", "");
    }

    private static String convertBinaryToLetter(String binary) {
        if (binary.length() != 8) {
            throw new IllegalArgumentException("Input binary string must be 8 bits long");
        }

        int decimalValue = Integer.parseInt(binary, 2);
        char asciiChar = (char) decimalValue;

        return Character.toString(asciiChar);
    }

}
