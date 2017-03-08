/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author user
 */
public class HeaderAndCallSyntax {

    
    
    /**
     * @param args the command line arguments
     */
    public static String functionMain() throws IOException {
        String retval = "";
        
        String code = readFile("E:\\test\\test.txt",StandardCharsets.UTF_8);
        
        // TODO code application logic here
        
        
        //List<String> textList = parseText(readFile("C:\\Users\\user\\Desktop\\DESKTOP STUFF\\compiler\\code.txt",StandardCharsets.UTF_8));
        
        List<String> headerTextList = parseText(readFile("E:\\test\\test.txt",StandardCharsets.UTF_8));
        System.out.println("\n_______________________TESTING HEADER_______________________\n");
        //System.out.println(functionDef);
        if(checkAllFunctionHeaderSyntax(headerTextList)){
            System.out.println("HEADER SYNTAX CORRECT");
        }else{
            System.out.println("HEADER SYNTAX ERROR");
            retval += "\nHEADER SYNTAX ERROR";
        }
        
        
        
        System.out.println("\n_______________________TESTING CALL_______________________\n");
        //test check function call syntax
        List<String> callTextList = parseText(readFile("C:\\Users\\user\\Desktop\\DESKTOP STUFF\\compiler\\test.txt",StandardCharsets.UTF_8));
        //System.out.println(functionCall);
        if(checkAllFunctionCallSyntax(callTextList)){
            System.out.println("CALL SYNTAX CORRECT");
        }else{
            System.out.println("CALL SYNTAX ERROR");
            retval += "\nCALL SYNTAX ERROR";
        }
        
        return retval;
    }
    
    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
    
    public static boolean checkAllFunctionHeaderSyntax(List<String> textList) {
        boolean retval = true;
        boolean inFunction = false;
        List<String> header = new ArrayList<String>();
        
        for (String word: textList){
            System.out.println("inFunction: " + inFunction);
            if(word.equals("function") && !inFunction){
                inFunction = true;  
            }else{
                if(inFunction == true){
                    header.add(word);
                    if(word.equals(")")){
                        if(!checkFunctionHeaderSyntax(header)){
                            retval = false;
                            System.out.println("syntax error here");
                        }else{
                            System.out.println("syntax correct");
                        }
                        header.clear();
                        inFunction = false;
                        System.out.println("header checked");
                    }
                }
            }
            
        }
        
        return retval;
    }
    
    public static boolean checkAllFunctionCallSyntax(List<String> textList) {
        boolean retval = true;
        boolean inCall = false;
        List<String> call = new ArrayList<String>();
        
        for (String word: textList){
            System.out.println("inCall: " + inCall);
            if(word.equals("call") && !inCall){
                inCall = true;  
            }else{
                if(inCall == true){
                    call.add(word);
                    if(word.equals(")")){
                        if(!checkFunctionCallSyntax(call)){
                            retval = false;
                            System.out.println("syntax error here");
                        }else{
                            System.out.println("syntax correct");
                        }
                        call.clear();
                        inCall = false;
                        System.out.println("call checked");
                    }
                }
            }
            
        }
        
        return retval;
    }
    
    
    
    
    
    //parameter is a string of the function header excluding the keyword function
    //the string of the parameter is obtained using another to-be-coded function
    public static boolean checkFunctionHeaderSyntax(List<String> textList) {
        boolean retval = true;
        
        
        //LOOP for testing if parsing worked (prints each string separated by spaces)
        for (String word: textList){
            System.out.println("\""+word+"\"");
        }
        
        String state = "returnType";
        List<String> parameters = new ArrayList<String>();
        for (String word: textList){
            if(state.equalsIgnoreCase("returnType")){
                if(checkIfValidDatatype(word)){
                    state = "functionName";
                }else{
                    retval = false;
                }
            }else if(state.equalsIgnoreCase("functionName")){
                if(checkIfValidFunctionName(word)){
                    state = "openParenthesis";
                }else{
                    retval = false;
                }
            }else if(state.equalsIgnoreCase("openParenthesis")){
                if(word.equals("(")){
                    state = "parameters";
                }else{
                    retval = false;
                }
            }else if(state.equalsIgnoreCase("parameters")){
                if(word.equals(")")){
                    if(checkIfValidHeaderParameters(parameters)){
                        state = "headerEnd";
                    }else{
                        retval = false;
                    }
                }else{
                    parameters.add(word);
                }
            }else if(state.equalsIgnoreCase("headerEnd")){
                retval = false;
            }
        }
        
        
        return retval;
    }
    
    public static boolean checkIfValidDatatype(String word) {
        boolean retval = false;
        
        if(word.equals("number")||word.equals("string")){
            retval = true;
        }
        
        return retval;
    }
    
    public static boolean checkIfValidFunctionName(String word) {
        boolean retval = true;
        GlobalConstants obj = new GlobalConstants();
        char letter;
        for(int i=0; i<word.length(); i++){
            letter = word.charAt(i);
            if(!Character.isLetterOrDigit(letter)){
                retval = false;
            }
        }
        
        if(obj.keywordList.contains(word)){
            retval = false;
        }
        
        return retval;
    }
    
    public static boolean checkIfValidVariableName(String word) {
        boolean retval = true;
        
        char letter;
        for(int i=0; i<word.length(); i++){
            letter = word.charAt(i);
            if(!Character.isLetterOrDigit(letter)){
                retval = false;
            }
        }
        
        return retval;
    }
    
    public static boolean checkIfValidHeaderParameters(List<String> words) {
        boolean retval = true;
        
        GlobalConstants obj = new GlobalConstants();
        
//        if(obj.keywordList.contains(word)){
//            retval = false;
//        }
        
        String state = "datatype";
        for (String word: words){
            if(state.equalsIgnoreCase("datatype")){
                if(checkIfValidDatatype(word)){
                    state = "variableName";
                }else{
                    retval = false;
                }
            }else if(state.equalsIgnoreCase("variableName")){
                if(checkIfValidVariableName(word)){
                    state = "separator";
                }else{
                    retval = false;
                }
            }else if(state.equalsIgnoreCase("separator")){
                if(word.equals(",")){
                    state = "datatype";
                }else{
                    retval = false;
                }
            }
        }
        
        if (!state.equalsIgnoreCase("separator")){
            retval = false;
        }
        
        return retval;
    }
    
    
    public static boolean checkFunctionCallSyntax(List<String> textList) {
        boolean retval = true;
       
        
        //LOOP for testing if parsing worked (prints each string separated by spaces)
        for (String word: textList){
            System.out.println("\""+word+"\"");
        }
        
        String state = "functionName";
        List<String> parameters = new ArrayList<String>();
        for (String word: textList){
            if(state.equalsIgnoreCase("functionName")){
                if(checkIfValidFunctionName(word)){
                    state = "openParenthesis";
                }else{
                    retval = false;
                }
            }else if(state.equalsIgnoreCase("openParenthesis")){
                if(word.equals("(")){
                    state = "parameters";
                }else{
                    retval = false;
                }
            }else if(state.equalsIgnoreCase("parameters")){
                if(word.equals(")")){
                    if(checkIfValidCallParameters(parameters)){
                        state = "headerEnd";
                    }else{
                        retval = false;
                    }
                }else{
                    parameters.add(word);
                }
            }else if(state.equalsIgnoreCase("headerEnd")){
                retval = false;
            }
        }
        
        
        
        return retval;
    }

    public static boolean checkCallFunctionName() {
        boolean retval = false;
        
        return retval;
    }
    
    public static boolean checkCallParameters() {
        boolean retval = false;
        
        return retval;
    }
    
    public static boolean checkIfValidCallParameters(List<String> words) {
        boolean retval = true;
        
        String state = "variableName";
        for (String word: words){
            if(state.equalsIgnoreCase("variableName")){
                if(checkIfValidVariableName(word)){
                    state = "separator";
                }else{
                    retval = false;
                }
            }else if(state.equalsIgnoreCase("separator")){
                if(word.equals(",")){
                    state = "variableName";
                }else{
                    retval = false;
                }
            }
        }
        
        if (!state.equalsIgnoreCase("separator")){
            retval = false;
        }
        
        return retval;
    }
    
    public static List<String> parseText(String text) {
        List<String> retval = new ArrayList<String>();
        
        String currString = "";
        String currState = null;
        String lastState = null;
        
        if(text.length()==1){
            retval.add(currString+text.charAt(0));
        }else if(text.length()>1){

            for(int textIndex = 1; textIndex<text.length(); textIndex++){
                
                if(Character.isLetterOrDigit(text.charAt(textIndex-1))){
                    lastState = "character";
                }else{
                    lastState = "separator";
                }
                if(Character.isLetterOrDigit(text.charAt(textIndex))){
                    currState = "character";
                }else{
                    currState = "separator";
                }                
                
                currString+=text.charAt(textIndex-1);
                if(!currState.equalsIgnoreCase(lastState) || (currState.equalsIgnoreCase("separator") && lastState.equalsIgnoreCase("separator"))){
                    retval.add(currString);
                    currString = "";
                    //System.out.println("word added to list");
                }
    
            }
            currString+=text.charAt(text.length()-1);
            retval.add(currString);
        }
//        retval.add(currString);
//        System.out.println("word added to list");
        
        retval = removeWhiteSpace(retval);
        
        // TODO code application logic here
        return retval;
    }
    
    public static List<String> removeWhiteSpace(List<String> retval) {
        
        List<Integer> indexList = new ArrayList<Integer>();
        
        for (int i = 0; i < retval.size(); i++) {
            if(retval.get(i).matches("^\\s*$")){
                  retval.set(i, "x");
                  indexList.add(i);
            }
        }
        
        int offset = 0;
        for (Integer i: indexList){
            retval.remove(i-offset);
            offset++;
        }
        
        // TODO code application logic here
        return retval;
    }
    
    
    
}
