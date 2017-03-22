/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import functions.HeaderAndCallSyntax;
import java.io.IOException;
import loopCondition.LoopAndCondSyntax;
import variables.MainClass;

/**
 *
 * @author user
 */
public class SyntaxChecker {
    
    public static String checkSyntax(String code) throws IOException {
        
        String retval = "";
        
        HeaderAndCallSyntax checkFunctions = new HeaderAndCallSyntax();
//        LoopAndCondSyntax checkLoopCondition = new LoopAndCondSyntax();
        MainClass checkVariables = new MainClass();
        
        System.out.println("\n======================================================================\n");
        //retval +=checkFunctions.functionMain();
        System.out.println("\n=======================FUNCTIONS DONE===============================================\n");
        //retval +=checkLoopCondition.loopConditionMain();
        System.out.println("\n=======================LOOPS CONDITIONS DONE===============================================\n");
        //retval +=checkVariables.variablesMain();
        System.out.println("\n=======================VARIABLES DONE===============================================\n");
        retval = "";
        if (retval.equals("")){
            System.out.println("NO ERRORS");
        }else{
            System.out.println("ERRORS FOUND");
        }
        
        return retval;
    }   
}
