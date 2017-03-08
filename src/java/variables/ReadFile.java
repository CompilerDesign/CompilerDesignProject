package variables;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Megs
 */


public class ReadFile {
    String errorMsg = "";
    public ReadFile (String fileName){
         String line = null;
         

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            int lineCtr = 0;
            while((line = bufferedReader.readLine()) != null){
//                System.out.println(line);
                CheckLine currentLine = new CheckLine(line, lineCtr);
                errorMsg += currentLine.errorMsg;
                lineCtr++;
            }

            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");                  
        }
    }
}
