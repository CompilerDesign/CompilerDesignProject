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
 * @author jayveelugod
 */
public class CheckDefine {
    static List<String> checkDefine = new ArrayList<>(Arrays.asList("define", "as", "number", "string", "character"));
      
    static int checkIfDefine(String s){
//        System.out.println(s);
        int check = -1;
        String[] split = s.split(" ");
        MainClass m = new MainClass();

        if(split[0].equals("define") && split[(split.length)-2].equals("as")){
            s = s.replace("define", "");
            s = s.replace("as", "");
            s = s.replace(split[(split.length)-1], "");
            String dataType = split[(split.length)-1];
            s = s.trim();

            if(s.equals("") || !s.matches(".*[a-z].*")){
               return 0;
            }else if(s.toLowerCase().contains(", ") || s.toLowerCase().contains(",")){
                if(s.charAt(0) == ',' || s.endsWith(",")){
                    return 0;
                }else{
                    int commaCount = s.length() - s.replace(",", "").length();
                    String[] str = s.split(",");
                    if(commaCount != (str.length)-1){
                        return 0;
                    }
                    else{
                        for(int x = 0; x < str.length; x++){
                            String trimSpace = str[x].replaceAll("^\\s+", ""); 
                            trimSpace = trimSpace.replaceAll("\\s+$", "");
                            if(checkDefine.contains(trimSpace) || trimSpace.split(" ").length != 1 || !trimSpace.matches("[A-Za-z0-9]+")){
                                  return 0; 
                            }else{
                                Variable v = new Variable();
                                if(m.var.size() != 0){
                                   for(int i = 0; i < m.var.size(); i++){ 
                                        if(m.var.get(i).getVariableName().equals(trimSpace)){
                                            return 0;
                                         }
                                    }
                                }
                                v.setVariableName(trimSpace);
                                v.setDataType(dataType);
                                m.var.add(v);    
                            }   
                        }
                    }
                }
            }else{
                if(s.split(" ").length != 1){
                    return 0;
                }else{
                    Variable var = new Variable();
                    s = s.replace(" ", "");
                    if(m.var.size() != 0){
                        for(int i = 0; i < m.var.size(); i++){  
                            if(m.var.get(i).getVariableName().equals(s)){
                                return 0;
                            }
                        }          
                    }
                    var.setVariableName(s);
                    var.setDataType(dataType);
                    m.var.add(var);   
                }
            }
            if(split[(split.length)-1].equals("number") || split[(split.length)-1].equals("string") || split[(split.length)-1].equals("character")){
                return check;
            }else{
                check = 0;
            }
        }else{
            check = 0;
        }        
        return check;
    }
}
