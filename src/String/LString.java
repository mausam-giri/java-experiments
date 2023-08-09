package String;

public class LString {
    private String s1;

    public LString(){}
    public LString(String s1){
        this.s1 = s1;
    }

    public String getLongestSub(String s1, String s2){
        int maxlength = 0;

        int minLen, maxLen;
        if(s1.length() > s2.length()){
            minLen = s2.length();
            maxLen = s1.length();
        }else{
            maxLen = s2.length();
            minLen = s1.length();
            String t = s1;
            s1 = s2;
            s2 = t;
        }


        int endIndex = maxLen;

        int keeper[][] = new int[maxLen + 1][minLen + 1];
        for (int i = 1; i <= maxLen; i++) {
            for (int j = 1; j <= minLen; j++) {
                if(s1.charAt(i - 1) == s2.charAt(j - 1)){
                    keeper[i][j] = keeper[i-1][j-1] + 1;
                }
                if(keeper[i][j] > maxlength){
                    maxlength = keeper[i][j];
                    endIndex = i;
                }
            }
        }
        return s1.substring(endIndex - maxlength, endIndex);
    }
}
