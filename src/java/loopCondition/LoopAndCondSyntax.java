/*
 * Bug:
 * 1. isExpecting could be turned to a class so the line number error can be properly indicated.
      Wierd line number usually happen with new line and many spaces.
   2. when putting '(' in unexpected place like d() it '(' doesnt get pushed to stack so Unexpected ) error is prompted.
 */
package loopCondition;

import loopCondition.Condition;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author 
 */
public class LoopAndCondSyntax {
    
    /**
     * This stack will be used to keep track of if else and terminator pairs
     */
    public static class MyStack extends Stack{
        @Override
        public StackElement pop(){
            // ~ is a sentinel value, cant be "" cause accessing charAt(0) will cause error
            return super.isEmpty() ? new StackElement("~", -1): (StackElement)super.pop();
        }
    }
    
    public static MyStack terminatorStack = new MyStack();
    public static MyStack ifElseStack = new MyStack();
    public static MyStack conditionParenthesisStack = new MyStack();
    
    public static class StackElement{
        public String value;
        public String valueType;
        public int lineNumber;
        
        public StackElement (String string, int lineNumber){
            this.value = string;
            this.valueType = "~"; //Sentinel
            this.lineNumber = lineNumber;
        }
        
        public StackElement (Character character, String valueType, int lineNumber){
            this.value = character.toString();
            this.valueType = valueType;
            this.lineNumber = lineNumber;
        }
    }
    
    public static String loopConditionMain(){ 
        String retval = "";
        //srcCode is an array of string where each index represent one line in the source code string
        ArrayList<String> srcCode = readTxtFile("C:\\Users\\user\\Desktop\\DESKTOP STUFF\\compiler\\test.txt");
        //Used to store expected character
        Character isExpectingChar = null;
        //Used to store the value type
        String isExpectingCharValueType = "";
        //Used to check if else or else if possible
        Boolean isElseIfPossible = false;
        //Used to check non-foreach condition
        Boolean isCondition=false;
        //Used to form the non-foreach conditon
        String condition="";
        //Used to check if variable is expected, meaning non-keyword
        Boolean isExpectingVariable = false;
        //Used to check if it is for each condition
        Boolean isForeachCondition = false;
        //Used to check if keyword is expected
        String isExpectingKeyword = null;
        
        String curWord = "";
        for(int lineNumber = 0; lineNumber<srcCode.size(); lineNumber++){//For each line
            String curSrcCodeLineTxt = srcCode.get(lineNumber);
            for(int charNdx=0; charNdx<curSrcCodeLineTxt.length(); charNdx++) {//Iterate through the line
                char curChar = curSrcCodeLineTxt.charAt(charNdx);
                
//                System.out.println("--------------------");
//                System.out.println("Line Number: "+(lineNumber+1));
//                System.out.println("curWord: "+curWord);
//                System.out.println("curChar: "+curChar);
//                System.out.println("isExpecting: "+isExpecting);
//                System.out.println("--------------------");
                
                //Note: charNdx==curSrcCodeLineTxt.length()-1 is so that the last char in the line will be considered a word
                if(isWordTerminator(curChar) || charNdx==curSrcCodeLineTxt.length()-1 || isExpectingChar!=null || isCondition == true){ //Check if current character is a terminator, and do not append to curWord if still expecting something
                    //If last char, append to last word
                    if(charNdx==curSrcCodeLineTxt.length()-1 && !isWordTerminator(curChar)){
                        curWord += curChar;
                    }
                    
//                    System.out.println("curWord: "+curWord);
                    
                    //This code section is for checking foreach
                    if(isExpectingVariable == true && !curWord.equals("")){
                        //TODO: Need to add checking if curword is variable.
                        if(isKeyWord(curWord)){
                            String errorMsg = "Line number "+(lineNumber+1)+": Misplaced keyword \""+curWord+"\"";
                            System.out.println(errorMsg);
                            retval += "\n"+ errorMsg;
                            //Terminate program
                            System.exit(0);
                        }else if(isForeachCondition == true){
                            isExpectingKeyword = "as";
                            isForeachCondition = false;
                        }else{
                            if(curChar != ')'){
                                isExpectingChar = ')';
                            }
                        }
                        isExpectingVariable = false;
                    }else if(isExpectingKeyword!=null && isExpectingKeyword.equals("as")){
                        if(!curWord.equals("as")){
                            String errorMsg = "Line number "+(lineNumber+1)+": Expecting keyword \"as\" in foreach";
                            System.out.println(errorMsg);
                            retval += "\n"+ errorMsg;
                            //Terminate program
                            System.exit(0);
                        }
                        isExpectingVariable = true;
                        isExpectingKeyword = null;
                    }
                    
                    //Resetting isElseIfPossible
                    if(isElseIfPossible == true && !curWord.equals("else") && !curWord.equals("elseif") && !curWord.equals("")){
                        isElseIfPossible = false;
                    }
                    
                    //Check for conditions
                    if("if".equals(curWord) 
                        || "while".equals(curWord) 
                        || "elseif".equals(curWord)){
                            isCondition =true;                        
                    }
                    
                    //Form condition
                    if(isCondition == true){
                        if(charNdx==0){
                            condition += " ";
                        }
                        condition += curChar;
                        if(curChar=='('){
                            conditionParenthesisStack.push(new StackElement("(", lineNumber));
                        }
                    }
                    
                    if("if".equals(curWord) 
                        || "while".equals(curWord) 
                        || "foreach".equals(curWord)){
                            isExpectingChar = '(';
                            if("if".equals(curWord)){
                                ifElseStack.push(new StackElement("if", lineNumber));
                            }
                    }else if ("else".equals(curWord) || "elseif".equals(curWord)){
                        
                        if(isElseIfPossible == false){
                            String errorMsg = "Line number "+(lineNumber+1)+": Unexpected \""+curWord+"\"";
                            System.out.println(errorMsg);
                            retval += "\n"+ errorMsg;
                            //Terminate program
                            System.exit(0);
                        }
                        
                        StackElement se = ifElseStack.pop();
                        if(!se.value.equals("if") && !se.value.equals("elseif")){
                            String errorMsg = "Line number "+(lineNumber+1)+": Unexpected \"else\"";
                            System.out.println(errorMsg);
                            retval += "\n"+ errorMsg;
                            //Terminate program
                            System.exit(0);
                        }
                        
                        if("elseif".equals(curWord)){
                            ifElseStack.push(new StackElement("elseif", lineNumber));
                            isExpectingChar = '(';
                        }else{
                            isExpectingChar = '{';
                        }
                    }
                    
                    if(isExpectingChar == null){
                        if(curChar == '{'){
                            String errorMsg = "Line number "+(lineNumber+1)+": Unexpected '{'";
                            System.out.println(errorMsg);
                            retval += "\n"+ errorMsg;
                            //Terminate program
                            System.exit(0);
                        }
                        curWord = "";
                    }else{
                        //Expecting something
                        if(curChar == ' '){
                            continue;
                        }else if(isExpectingChar == '('){
                            if(curChar == '('){
                                terminatorStack.push(new StackElement('(', curWord, lineNumber));
                                
                                if(curWord.equals("foreach")){
                                    isExpectingVariable = true;
                                    isForeachCondition = true;
                                }
                                
                                isExpectingChar = null; 
                                curWord = "";
                            }else{
                                String errorMsg = "Line number "+(lineNumber+1)+": \""+curWord+"\" should be followed by '('";
                                System.out.println(errorMsg);
                                retval += "\n"+ errorMsg;
                                //Terminate program
                                System.exit(0);
                            }
                        }else  if (isExpectingChar == '{'){
                            if(curChar == '{'){
                                terminatorStack.push(new StackElement('{', isExpectingCharValueType, lineNumber));
                                isExpectingChar = null; 
                                isExpectingCharValueType = "";
                                curWord = "";
                            }else{
                                String errorMsg = "Line number "+(lineNumber+1)+": Expecting '{' but not found.";
                                System.out.println(errorMsg);
                                retval += "\n"+ errorMsg;
                                //Terminate program
                                System.exit(0);
                            }
                        }else if (isExpectingChar == ')'){ //This is for foreach
                            if(curChar != ')'){
                                String errorMsg = "Line number "+(lineNumber+1)+": Expecting ')' but not found.";
                                System.out.println(errorMsg);
                                retval += "\n"+ errorMsg;
                                //Terminate program
                                System.exit(0);
                            }
                        }
                    }
                    
                    //Closing terminators
                    if(curChar == '}'){
                        StackElement se = terminatorStack.pop();
                        if(se.value.charAt(0)!='{'){
                            String errorMsg = "Line number "+(lineNumber+1)+": Unexpected '}'";
                            System.out.println(errorMsg);
                            retval += "\n"+ errorMsg;
                            //Terminate program
                            System.exit(0);
                        }else if (se.valueType.equals("if") || se.valueType.equals("elseif")){
                            isElseIfPossible = true;
                        }
                    }else if (curChar == ')'){ 
                        if(isCondition==true && !conditionParenthesisStack.isEmpty()){
                            conditionParenthesisStack.pop();
                        }
                        //Check if conditionParenthesisStack is empty and assume condition is now finished
                        if(conditionParenthesisStack.isEmpty()){
                            StackElement se = terminatorStack.pop();
                            if(se.value.charAt(0)!='('){
                                String errorMsg = "Line number "+(lineNumber+1)+": Unexpected ')'";
                                System.out.println(errorMsg);
                                retval += "\n"+ errorMsg;
                                //Terminate program
                                System.exit(0);
                            }else{
                                if(se.valueType.equals("if")
                                    || se.valueType.equals("while")
                                    || se.valueType.equals("foreach")
                                    || se.valueType.equals("elseif")){
                                        if(!se.valueType.equals("foreach")){
                                            if(isCondition==true && Condition.checkConditionSyntax(condition) == false ){
                                                String errorMsg = "Line number "+(lineNumber+1)+": Incorrect condition syntax.";
                                                System.out.println(errorMsg);
                                                retval += "\n"+ errorMsg;
                                                //Terminate program
                                                System.exit(0);           
                                            }
                                            isCondition=false;                                    
                                            condition="";
                                        }else{
                                            //If foreach
                                            if(isExpectingVariable == true){
                                                String errorMsg = "Line number "+(lineNumber+1)+": Expecting variable after \"as\" but not found.";
                                                System.out.println(errorMsg);
                                                retval += "\n"+ errorMsg;
                                                //Terminate program
                                                System.exit(0);
                                            }
                                        }
                                        isExpectingChar = '{';
                                        isExpectingCharValueType = se.valueType;
                                }
                            }
                        }
                    }
                    
                    if(charNdx==0 && !isWordTerminator(curChar)){ //Already a new line so curWord is reset
                        curWord = "";
                        curWord += curChar;
                    }
                }else{ //Continue forming the current word
                    curWord += curChar;
                }
            }
        }//End of main loop
        
        if(!terminatorStack.isEmpty()){
            StackElement se = terminatorStack.pop();
            String errorMsg = "Line number "+(se.lineNumber+1)+": No pair found for \""+se.value+"\"";
            System.out.println(errorMsg);
            retval += "\n"+ errorMsg;
            //Terminate program
            System.exit(0);
        }
        
        if(!curWord.isEmpty()){
            System.out.println("Incomplete code. Last word is: "+curWord);
        }
        System.out.println(retval);
        return retval;
    }
    
    
    private static boolean isKeyWord(String word){
        boolean bool = false;
        switch(word){
            case "define":;
            case "if":;
            case "else":;
            case "elseif":;
            case "foreach":;
            case "as": bool=true;
        }
        return bool;
    }
    
    /**
     * This function checks if the char parameter is a terminator
     * @param c
     * @return True->terminator; False->not terminator
     */
    private static boolean isWordTerminator (char c){
        boolean bool = false;
        switch(c){
            case ';':;
            case ' ':;
            case '=':;
            case '(':;
            case ')':;
            case '{':;
            case '}': bool=true;
        }
        return bool;
    }
     /**
     * This function returns a String Arraylist where each index is one line of code
     * @param path - The path of text file to be read.
     * @return ArrayList<String>
     */
    private static ArrayList<String> readTxtFile(String path){
        ArrayList<String> ret = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                ret.add(sCurrentLine);
            }   

        } catch (IOException e) {
            System.out.println(e.toString());
        }
        
        return ret;
    }
}
