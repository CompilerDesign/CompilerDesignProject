import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

/**
 *
 * @author jenilyn
 */
public class JavaInterpreter {
    
    public static HashMap hashMap = new HashMap();
    public String javaConverted = "";
    
    public JavaInterpreter (String srcCode, String className){
//        String srcCode = readTxtFile("SampleCode.txt");
        
        srcCode = srcCode.replace("}", " };");
        srcCode = srcCode.replace("{", " {;");
        srcCode = srcCode.replace("(", " ( ");
        srcCode = srcCode.replace(")", " ) ");
        
        srcCode = srcCode.replace("=call ", "= call ");
        srcCode = srcCode.replace(";call ", "; call ");
        srcCode = srcCode.replace(" call ", " ");
        
        //For arrays
        srcCode = srcCode.replace("[", ";[");
        
        //Print
        srcCode = srcCode.replace("print ", "return ");
            
        String javaCode = "";
        List<String> statements = Arrays.asList(srcCode.split(";+"));
        
        for(String stmt: statements){
            stmt = stmt.trim();
            if(isDeclarationStmt(stmt)){
                javaCode += convertDeclarationStmt(stmt);
            }else if (isCondition(stmt)){
                javaCode += convertConditionStmt(stmt);
            }else if (isFunctionDefinition(stmt)){
               javaCode += convertFunctionDefinitionStmt(stmt);
            }else if (isForEach(stmt)){
                javaCode += convertForEachStmt(stmt);
            }
            else if(isArray(stmt)){
                javaCode += convertArray(stmt);
            }
            else if (stmt.equals("}")){
                javaCode += "}";
            }else if(stmt.length()>0 && stmt.charAt(stmt.length()-1)=='='){
                javaCode += stmt;
            }else if(!stmt.equals("")){
                javaCode += stmt+";";
            }
            
            //Only add new line is current statement is not empty
            javaCode += stmt.equals("")? "": "\n";
        }
        
        String pkg = "";
        
        String head = "import java.util.*;\n"
                    + "import java.lang.*;\n"
                    + "import java.io.*;\n"
                    + "import java.net.*;\n"
                    + "import java.awt.*;\n"
                    + "import java.awt.event.*;\n"
                    + "import java.text.*;\n"
                    + "import java.util.regex.*;\n"
                    + "\n\npublic class "+className+"{\n\n";
        
        String close = "\n\n}//End of Main class";
        
        javaCode = pkg + head + javaCode + close;
        
        System.out.println(javaCode);
        javaConverted = javaCode;
//        PrintWriter out = new PrintWriter("src/java/interpreter_output/Main.java");
//        out.println(javaCode);
//        out.close();
    }
    
    
    private static boolean isArray(String stmt){
        stmt = stmt.trim();
        if(stmt.length()>0 && stmt.charAt(0)=='['){
            stmt = stmt.replace(" ", "");
            if(stmt.charAt((stmt.length()-1))==']'){
                return true;
            }
        }
        return false;
    }
    
    private static String convertArray(String stmt){
        stmt = stmt.replace(" ", "");
        stmt = stmt.replace("[", "");
        stmt = stmt.replace("]", "");
        String[] elements = stmt.split(",");
        
        //Get the array type
        String arrType = "new ";
        if(elements.length > 0){
            try{
                Double.parseDouble(elements[0]);
                arrType += "double[]{";
            }catch(Exception e){
                arrType += "String[]{";
            }
        }
        
        for(String elem: elements){
            arrType += elem+",";
        }
        
        arrType += "};";
        arrType = arrType.replace(",};", "};");
        return arrType;
    }
    
    /**
     * Author: Heinrich
     * @param stmt
     * @return 
     */
    private static String convertDeclarationStmt(String stmt){
        List<String> words = Arrays.asList(stmt.split(" +"));
        String variableName = words.get(1);
        String dataType = words.get(3);
        if(dataType.equals("number")){
            dataType = "double";
        }else if(dataType.equals("string")){
            dataType = "String";
        }else if(dataType.equals("numberlist")){
            dataType = "double[]";
        }else if(dataType.equals("stringlist")){
            dataType = "String[]";
        }
        
        hashMap.put(variableName, dataType);
        
        return dataType+" "+variableName+";";
    }
    
    private static boolean isDeclarationStmt(String stmt){
        List<String> words = Arrays.asList(stmt.split(" "));
        if(words.get(0).equals("define")){
            return true;
        }
        return false;
    }
    
    
    private static String readTxtFile(String path){
        String ret = "";
        
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                ret += sCurrentLine;
            }   

        } catch (IOException e) {
            System.out.println(e.toString());
        }
        
        return ret;
    }
    
    
    public static boolean isCondition(String stmt){
        List<String> words = Arrays.asList(stmt.split(" +"));
        if(words.get(0).equals("if") || words.get(0).equals("else") || words.get(0).equals("elseif")){
            return true;
        }
        return false;
    }
    
    public static String convertConditionStmt(String stmt){
        stmt = stmt.replace(";", "");
        List<String> words = Arrays.asList(stmt.split(" +"));
        if(words.get(0).equals("elseif")){
            words.set(0, "else if");
            stmt = "";
            for(String word: words){
                stmt += word+" ";
            }
        }
        
        return stmt;       
    }
    
    
    public static boolean isFunctionDefinition(String stmt){
        List<String> words = Arrays.asList(stmt.split(" +"));
        if(words.get(0).equals("function")){
            return true;
        }
        return false;
    }
    
    /**
     * TODOD: double check how a function parameter is declared.
     * @param stmt
     * @return 
     */
    public static String convertFunctionDefinitionStmt(String stmt){
        List<String> words = Arrays.asList(stmt.split(" +"));
        int flag = 0;
        String ret;
        for(String word:words){
            if(word.equals("main")){
                flag=1;
                break;
            }
        }
        if(flag == 1 ){
            ret = "public static Double start(){";
                 //public static String main ()
        } else {
            stmt = stmt.replace(";", "");
            stmt = stmt.replace("function", "public static");
            stmt = stmt.replace("numberlist", "double[]");
            stmt = stmt.replace("stringlist", "String[]");
            stmt = stmt.replace("number", "double");
            stmt = stmt.replace("string", "String");
            
            ret = stmt;
        }
        
        return ret;
    }
    
    /**
     * TODO: double check if the temporary holder has to be declared in foreach
     * @param stmt
     * @return 
     */
    public static boolean isForEach(String stmt){
        List<String> words = Arrays.asList(stmt.split(" +"));
        if(words.get(0).equals("foreach")){
            return true;
        }
        return false;
    }
    
    public static String convertForEachStmt(String stmt){
        List<String> words = Arrays.asList(stmt.split(" +"));
        String retval = "for(";
        String variableName = words.get(4);
        String listName = words.get(2);
        
        String hashDataType = (String)hashMap.get(listName);
        
//        System.out.println("listName is "+ listName);
//        System.out.println("hashDataType is "+ hashDataType);
        if(hashDataType.equals("double[]")){
            retval += "double ";
        }else if(hashDataType.equals("String[]")){
            retval += "String";
        } else {
            System.out.println(listName +" is not declared!");
            System.exit(0);
        }
        
        retval += variableName;
        retval += ": "+listName + "){";
        return retval;
    }
}
