
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
    
    public CheckLine(String s, int index){
        MainClass mc = new MainClass();
        if(s.equals("")){
//            do nothing
        }else if(s.split(" ")[0].equalsIgnoreCase("define") == false){
            s = s.toLowerCase();
            CheckOperation checkOp = new CheckOperation();
            int op = checkOp.checkOperation(s);
            if(op == 0){
               mc.errors.add("Error in line "+index);
               System.out.println("Error in line "+index+": "+s);
            }
        }
        else{
            s = s.toLowerCase();
            CheckDefine check = new CheckDefine();
            int c = check.checkIfDefine(s);
            if(c == 0){
                mc.errors.add("Error in line "+index);
               System.out.println("Error in line "+index+": "+s);
            }
        }
    }
    
}
