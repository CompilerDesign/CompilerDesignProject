/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loopCondition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Za Espina
 */
public class Condition {
        public int startlineNumber;
        public int endlineNumber;
        public int startndx;
        public int lastndx;
        public String str="";
        
        static Pattern compReg= Pattern.compile("(<|=<|==|=>|>)");
        static Pattern logicalReg= Pattern.compile("(&&|\\|\\|)");
        static Pattern valueReg= Pattern.compile("(\\p{Alnum}*)");
        static Pattern _s =Pattern.compile("(\\s)*?");
        static Pattern simpleCondition= Pattern.compile("(\\("+_s+valueReg+_s+compReg+_s+valueReg+_s+"\\))");
        static Pattern multCondition= Pattern.compile("(("+_s+simpleCondition+_s+"("+_s+logicalReg+_s+simpleCondition+")*?)|(\\("+_s+simpleCondition+_s+"("+_s+logicalReg+_s+simpleCondition+"\\))*?))"); 
       static Pattern complexCondition= Pattern.compile("("+_s+"\\("+_s+multCondition+_s+"\\)("+_s+logicalReg+_s+"\\("+multCondition+_s+"\\))*?)|(\\("+_s+multCondition+_s+"\\)("+_s+logicalReg+_s+""+multCondition+_s+")*?)|(" + _s+multCondition+_s+"("+_s+logicalReg+_s+"\\("+multCondition+_s+")\\)*?)");
        
        public Condition(int x, int y){
            this.startlineNumber = y; 
            this.startndx=x;
        }
        
        static public boolean checkConditionSyntax(String str){
            
        
         if(multCondition.matcher(str).matches() || complexCondition.matcher(str).matches()){
             return true;
         }   
         else return false;
        }
        
        public static void main(String args[]){
            
            boolean valid= checkConditionSyntax("   ( (a==b) && (dif > 2))");
            System.out.println(valid);
            
        }
}
