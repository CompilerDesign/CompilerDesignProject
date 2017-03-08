package variables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jayvee_lugod
 */
public class CheckOperation {
    static List<String> operators = new ArrayList<>(Arrays.asList("+", "-", "*", "/"));
    static int checkOperation(String s){
//        System.out.println(s);
        MainClass m = new MainClass();
       
//      check if left expression is only one word
        if(!Character.isLetter(s.charAt(0)) || s.split("=")[0].replaceAll("^\\s+", "").replaceAll("\\s+$", "").split(" ").length != 1){
            return 0;
        }

//      check left expression's datatype in the arraylist of Variable class
        String dataType = "";
        for(int i = 0; i < m.var.size(); i++){
            if(m.var.get(i).getVariableName().equals(s.split("=")[0].replaceAll("^\\s+", "").replaceAll("\\s+$", ""))){
                dataType = m.var.get(i).getDataType();
                break;
            }
        }
        if(dataType != ""){
            String[] result = s.split("=")[1].split("[-+*/]");
            String rightEx = s.split("=")[1].replaceAll("^\\s+", "").replaceAll("\\s+$", "");
            
            switch(dataType){
                case "number": 
//                  check if left expression's first character is '-' and it should be followed by a number
                    if((!Character.isDigit(rightEx.charAt(0)) && rightEx.charAt(0) != '-')||
                        !Character.isDigit(rightEx.charAt(rightEx.length()-1))){
                        return 0;
                    }
//                  check if left ex a number after removing +-/* and white spaces  
                    if(result.length == 1){
                        return (result[0].replaceAll("^\\s+", "").replaceAll("\\s+$", "").split(" ").length == 1)?
                                ((result[0].replaceAll("^\\s+", "").replaceAll("\\s+$", "").matches("^[+-]?\\d+$") == true)? -1: 0): 0;
                    }else{
                        System.out.println(rightEx);
                        if(rightEx.charAt(0) == '-' && !Character.isDigit(rightEx.charAt(1))){
                            return 0;
                        }
//                        else if(rightEx.charAt(0) == '-' && !Character.isDigit(rightEx.charAt(1))){
//                            return 0;
//                       wala pa nahuman.. naglibog na ko diri nga part
                        else if(rightEx.charAt(0) == '-' && Character.isDigit(rightEx.charAt(1))){
                            rightEx = rightEx.replace("-", "");
//                            String[] nonOp = rightEx.
                        }
                        else{
                            return (result[0].replaceAll("^\\s+", "").replaceAll("\\s+$", "").matches("-?\\d+(\\.\\d+)?") && result[1].replaceAll("^\\s+", "").replaceAll("\\s+$", "").matches("-?\\d+(\\.\\d+)?"))?
                                -1: 0;
                        }
                    }
                case "character":
//                    System.out.println(result[0].replaceAll("^\\s+", "").replaceAll("\\s+$", "").split(" ").length);
                    return (result.length == 1 && result[0].replaceAll("^\\s+", "").replaceAll("\\s+$", "").split(" ").length == 1)?
                                ((result[0].replaceAll("^\\s+", "").replaceAll("\\s+$", "").charAt(0) == '\''
                                && Character.isLetter(result[0].replaceAll("^\\s+", "").replaceAll("\\s+$", "").charAt(1)) == true
                                && result[0].replaceAll("^\\s+", "").replaceAll("\\s+$", "").charAt(2) == '\'')? -1: 0): 0;
                case "string":
                    return (result.length == 1)?
                                ((result[0].replaceAll("^\\s+", "").replaceAll("\\s+$", "").startsWith("\"")
                                && result[0].replaceAll("^\\s+", "").replaceAll("\\s+$", "").endsWith("\""))? -1: 0): 0;
            }
        }
        return -1;
    }
}
