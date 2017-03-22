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
public class CheckLine {
    
    String errorMsg = "";
    
    public CheckLine(String s, int index){
        if(s.equals("")){
//            do nothing
        }else if(s.split(" ")[0].equalsIgnoreCase("define") == false){
            MainClass m = new MainClass();
            Boolean proceed = false;
            for(int i = 0; i < m.var.size(); i++){
                if(m.var.get(i).getVariableName().equals(s.split(" ")[0].equalsIgnoreCase("define"))){
                    proceed = true;
                    break;
                }
            }
            if(proceed){
                s = s.toLowerCase();
                CheckOperation checkOp = new CheckOperation();
                int op = checkOp.checkOperation(s);
                if(op == 0){
                   System.out.println("Error in line "+index+": "+s);
                   errorMsg += "\nError in line "+index+": "+s;
                } 
            }else{
//                do nothing
            }
            
        }
        else{
            s = s.toLowerCase();
            CheckDefine check = new CheckDefine();
            int c = check.checkIfDefine(s);
            if(c == 0){
               System.out.println("Error in line "+index+": "+s);
               errorMsg += "\nError in line "+index+": "+s;
            }
        }
    }
    
}
