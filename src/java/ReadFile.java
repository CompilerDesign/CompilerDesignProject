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
    public ReadFile (String serverContent){
        int lineCtr = 1;
        String[] s = serverContent.split("\n");
        for(String line:s){
            new CheckLine(line, lineCtr);
            lineCtr++;
        }
    }
}
