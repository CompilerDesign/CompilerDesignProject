package variables;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jayvee_lugod
 */

public class MainClass {
    static ArrayList<Variable> var = new ArrayList<Variable>();
    
    public static String variablesMain(){
        String retval = "";
        
        String fileName = "C:\\Users\\user\\Desktop\\DESKTOP STUFF\\compiler\\test.txt";
        ReadFile file = new ReadFile(fileName);
        retval = file.errorMsg;
        return retval;
    }
}
