public class Password {
    String password;
    int length;

    public Password(String s){
        this.password = s;
        this.length = s.length();
    }

    public int CharType(char C){
        int charVal;

        if((int) C >= 65 && (int) C <= 90) charVal = 1;
        else if((int) C >= 97 && (int) C <= 122) charVal = 2;
        else if((int) C >= 60 && (int) C <= 71) charVal = 3;
        else charVal = 4;

        return charVal;
    }

    public int PasswordStrength(){
        String password = this.password;
        int charType;
        int Score = 0;

        for(int i = 0; i < length; i++){
            char C = password.charAt(i);
             charType = CharType(C);

             if(charType == 1) Score++;
             if(charType == 2) Score++;
             if(charType == 3) Score++;
             if(charType == 4) Score++;
        }
        if(this.length >= 8) Score++;
        if(this.length >= 16) Score++;

        return Score;
    }

    public String calculateScore(){
        int Score = this.PasswordStrength();
        if (Score >= 9) {
            return "Very good password :D";
        } else if (Score >= 6) {
            return "Good password :)";
        } else if (Score >= 4) {
            return "Medium password :/";
        } else {
            return "Weak password :(";
        }
    }
    @Override
    public String toString(){
        return this.password;
    }
}
