import java.io.*;
import java.sql.SQLOutput;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) {

        System.out.println("CLI programs ");
        System.out.println("1: Test individual packet AES Encryption stages! ");
        System.out.println("2: Decrypting CipherText! ");
        System.out.println("3: Decrypt cipher text to plain text from/to file");

        System.out.print("Enter Valid Input: ");
        Scanner scanner = new Scanner(System.in);

        try{

            int input = scanner.nextInt();
            scanner.nextLine();

            if (input == 1){
                //take 2 inputs of 16 bits each (plain text and key)
                System.out.print("Enter 16 bit plain Text in Hex form: ");
                String plainText = scanner.nextLine();

                if(!Validate16BitHexInput(plainText)){
                    return;
                }

                //text to binary
                String binaryInput = Conversions.string_to_binary(plainText);

                //binary to matrix
                String[][] inputMatrix = Conversions.convert_to_matrix(binaryInput);

                String[][] subnibbleMatrix = new String[2][2];

                // applying sub-nibbles using substitution box
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        assert inputMatrix != null;
                        subnibbleMatrix[i][j] = Conversions.SubNibbles(inputMatrix[i][j]);
                    }
                }
                System.out.print("After Applying Subnibbles: ");
                printMatrix(subnibbleMatrix);
                System.out.println();


                String[][] mixColumnMatrix = Conversions.MixColumns(inputMatrix);
                System.out.print("After Applying MixColumns: ");
                printMatrix(mixColumnMatrix);
                System.out.println();


                String[][] shiftRowMatrix = Conversions.shiftRow(inputMatrix);
                System.out.print("After Applying ShiftRow: ");
                printMatrix(shiftRowMatrix);
                System.out.println();

                //rounded
                System.out.print("Enter 16 bit Key in Hex form: ");
                String key = scanner.nextLine();

                if(!Validate16BitHexInput(key)){
                    return;
                }

                //text to binary
                String binaryKeyInput = Conversions.string_to_binary(key);

                //binary to matrix
                String[][] keyMatrix = Conversions.convert_to_matrix(binaryKeyInput);

                String[][] GenerateRoundedKeyMatrix1 = Conversions.GenerateRoundedKeys(keyMatrix, "1110");
                System.out.println("After applying Rounded Key: ");
                System.out.print("Key 1: ");
                printMatrix(GenerateRoundedKeyMatrix1);
                System.out.println();

                String[][] GenerateRoundedKeyMatrix2 = Conversions.GenerateRoundedKeys(GenerateRoundedKeyMatrix1, "1010");
                System.out.print("Key 2: ");
                printMatrix(GenerateRoundedKeyMatrix2);
                System.out.println();

                System.out.println("------------ Applying Encryption on Above provided text and key ---------------");

                String[][] ciphertext = Algorithm.Encrytion(plainText, key);
                System.out.print("Resulted cipherText: ");
                printMatrix(ciphertext);

                System.out.println();

            }else if (input == 2){
                //input cipher text and validation
                System.out.print("Enter 16 bit Cipher Text in Hex form: ");
                String cipherText = scanner.nextLine();
                if(!Validate16BitHexInput(cipherText)){
                    return;
                }
                //input key and validation
                System.out.print("Enter 16 bit Key in Hex form: ");
                String key = scanner.nextLine();
                if(!Validate16BitHexInput(key)){
                    return;
                }
                //algorithm
                String[][] backPlainText = Algorithm.Decryption(cipherText, key, true);
                System.out.print("Resulted plain text: ");
                printMatrix(backPlainText);
            }else if(input == 3){
                //file reading for decryption
                String filePath = "C:\\Users\\arsla\\IdeaProjects\\AES Encryption\\src\\secret.txt";
                String saveFilePath = "C:\\Users\\arsla\\IdeaProjects\\AES Encryption\\src\\plain.txt";
                String fileContent = "";
                try {
                    System.out.println("Reading File.........");
                    fileContent = readFile(filePath);
                    System.out.println(fileContent);
                } catch (IOException e) {
                    System.out.println("File Not Found");
                    e.printStackTrace();
                    return;
                }

                //input key and validation
                System.out.print("Enter 16 bit Key in Hex form: ");
                String key = scanner.nextLine();
                if(!Validate16BitHexInput(key)){
                    return;
                }

                //decrypting file
                String plainTextFile = Algorithm.DecrytTextFile(fileContent, key);
                System.out.println();
                System.out.print("Plain Text From file: " + plainTextFile);

                if(plainTextFile != null){
                    //save back in txt file
                    try {
                        saveStringToFile(plainTextFile, saveFilePath);
                        System.out.println();
                        System.out.println("Text saved to " + saveFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println();
                    System.out.print("Something went wrong with input file");
                }

            }else{
                System.out.println("Invalid input");
            }


        }catch (Exception e){
            System.out.println("Exception: ");
            e.printStackTrace();
        }

        scanner.close();

    }

    public static boolean Validate16BitHexInput(String hexInput){
        String pattern = "^[0-9A-Fa-f]{0,4}$";

        // Create a Pattern object
        Pattern hexPattern = Pattern.compile(pattern);

        // Use Matcher to check if the input matches the pattern
        Matcher matcher = hexPattern.matcher(hexInput);

        if (matcher.matches()) {
            System.out.println("Valid 16-bit hexadecimal number.");
            return true;
        } else {
            System.out.println("Invalid input. Not a 16-bit hexadecimal number.");
            return false;
        }
    }

    private static void saveStringToFile(String text, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(text);
        }
    }
    private static String readFile(String filePath) throws IOException {
        StringBuilder fileContentBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileContentBuilder.append(line).append("\n");
            }
        }
        return fileContentBuilder.toString();
    }

    public static void printMatrix(String[][] matrix){
        // Print the resulting matrix
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int decimalValue = Integer.parseInt(matrix[j][i], 2);
                char hexValue = Integer.toHexString(decimalValue).toUpperCase().charAt(0);
                System.out.print(hexValue);
            }
        }
        System.out.println();
    }

}