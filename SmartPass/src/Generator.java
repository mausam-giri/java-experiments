import java.util.Scanner;

public class Generator {
    Alphabet alphabet;
    public static Scanner keyboard;

    public Generator(Scanner key){
        keyboard = key;
    }

    public Generator(boolean isUpper, boolean isLower, boolean isNums, boolean isSymbol){
        alphabet = new Alphabet(isUpper, isLower, isNums, isSymbol);
    }

    public void main(){
        System.out.println("Welcome to SmartPass: Password Helper !");
        String userOpt = "-1";
        System.out.println("Select: \n\t[1] Generate Password\n\t[2] Check Password Strength\n\t[3] Quit");


        while(!userOpt.equals("3")){
            System.out.print("Enter your choice: ");
            userOpt = keyboard.next();

            switch(userOpt){
                case "1":{
                    requestPassword();
                    break;
                }
                case "2":
                    checkPassword();
                    break;
                case "3":
                    System.out.println("Closing program.");
                    break;
                default:{
                    break;
                }
            }
        }
    }

    private void checkPassword() {
        StringBuilder sb = new StringBuilder("");
        String input;
        System.out.print("Please enter you password: ");
        input = keyboard.next();
        final Password pass = new Password(input);
        System.out.println(pass.calculateScore());
    }

    private Password GeneratePassword(int length){
        StringBuilder password = new StringBuilder("");
        final int alphabetLength = alphabet.getAlphabets().length();
        int max = alphabetLength - 1;
        int min = 0;
        int range = max - min + 1;

        for(int i = 0; i < length; i++){
            int index = (int) (Math.random() * range) + min;
            password.append(alphabet.getAlphabets().charAt(index));
        }
        return new Password(password.toString());
    }

    public void requestPassword(){
        boolean isUpper = false;
        boolean isLower = false;
        boolean isNumber = false;
        boolean isSymbol = false;
        boolean correctInput;

        System.out.println("\n" + "Please help us with the Option: ");

        do{
            correctInput = false;
            System.out.println("Select choice to add: [yes/no]");
            isUpper = getUserInput("[+] Uppercase Letter: ");
            isLower = getUserInput("[+] Lowercase Letter: ");
            isNumber = getUserInput("[+] Digits: ");
            isSymbol = getUserInput("[+] Symbols: ");

            if(!isUpper && !isLower && !isNumber && !isSymbol){
                System.out.println("No valid option selected for password generation!");
                correctInput = true;
            }
        }while(correctInput);
        System.out.print("Please enter the length of password: ");
        int passLength = keyboard.nextInt();

        final Generator generator = new Generator(isUpper, isLower, isNumber, isSymbol);
        final Password password = generator.GeneratePassword(passLength);
        
        System.out.println("Generate Pass: " + password);
    }
    private boolean getUserInput(String msg){
        String input;
        do{
            System.out.print("\t"+msg);
            input = keyboard.next();
            UserInputError(input);
        }while(!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no"));
        return isInclude(input);
    }
    private boolean isInclude(String input) {
        return input.equalsIgnoreCase("yes");
    }

    private void UserInputError(String input) {
        if(!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no")) {
            System.out.println("Invalid input provided! Please try again...");
        }
    }
}
