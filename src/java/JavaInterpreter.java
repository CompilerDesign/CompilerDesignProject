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
                    + "import java.awt.event.*;\n"
                    + "import java.text.*;\n"
                    + "import java.util.regex.*;\n"
                    + "import java.io.*;\n"
                    + "\n\npublic class "+className+ "{\n\n"
                    + "public static String main (String args[]){\n\n";
        
        String mainClose = "\n\n}\n\n";
        
        String builtInFunctions = "public static String getContent(String linkURL)\n" +
                                "{\n" +
                                "    URL url = null;\n" +
                                "    InputStream is = null;\n" +
                                "    BufferedReader reader = null;\n" +
                                "    String line = null;\n" +
                                "    String content =\"\";\n" +
                                "    try\n" +
                                "    {\n" +
                                "        System.out.println(\"Link URL: \"+linkURL);\n" +
                                "        url = new URL(linkURL);\n" +
                                "        is = url.openConnection().getInputStream();\n" +
                                "        reader = new BufferedReader( new InputStreamReader( is ));\n" +
                                "\n" +
                                "        while( ( line = reader.readLine() ) != null )  \n" +
                                "        {\n" +
                                "           content += line;\n" +
                                "        }\n" +
                                "        reader.close();\n" +
                                "    }\n" +
                                "    catch(Exception e)\n" +
                                "    {\n" +
                                "        System.err.println(e.getMessage());\n" +
                                "    }    \n" +
                                "    //System.out.println(content);\n" +
                                "    return content;\n" +
                                "}"
			+ "\n\n/*-------------------------- Sentiments Analysis ------------------------------------------------- */\n\n"
                        + "public static final String[] NEGATIVE_WORDS = {\"bad\", \"awful\"};\n" +
                            "public static final String[] POSITIVE_WORDS = {\"good\", \"awesome\"};\n" +
                            "\n" +
                            "public static String getPerception (String pageContent){\n" +
                            "    String[] pageSentences={};\n" +
                            "\n" +
                            "    String filteredSentences=\"\";\n" +
                            "\n" +
                            "    filteredSentences = filterSentences(pageContent);\n" +
                            "\n" +
                            "    int positiveWords = 0;\n" +
                            "    int negativeWords = 0;\n" +
                            "\n" +
                            "    negativeWords = countNegativePerceptionWords(filteredSentences);\n" +
                            "    positiveWords = countPositivePerceptionWords(filteredSentences);\n" +
                            "\n" +
                            "    String sentiment = getSentiment(positiveWords, negativeWords);\n" +
                            "\n" +
                            "    System.out.println(\"filteredSentences: \"+filteredSentences);\n" +
                            "    System.out.println (\"positiveWords: \"+positiveWords);\n" +
                            "    System.out.println (\"negativeWords: \"+negativeWords);\n" +
                            "\n" +
                            "    return sentiment;\n" +
                            "}\n" +
                            "\n" +
                            "public static String getSentiment (int positiveWords, int negativeWords){\n" +
                            "    String ret = \"\";\n" +
                            "\n" +
                            "    if(positiveWords == negativeWords){\n" +
                            "        ret = \"NEUTRAL\";\n" +
                            "    }else if(positiveWords > negativeWords){\n" +
                            "        ret = \"GOOD\";\n" +
                            "    }else{\n" +
                            "        ret = \"BAD\";\n" +
                            "    }\n" +
                            "\n" +
                            "    return ret;\n" +
                            "}\n" +
                            "\n" +
                            "public static int countPositivePerceptionWords(String filteredSentences)\n" +
                            "{\n" +
                            "    int positiveCount=0;\n" +
                            "    String[] wordsInTheSentence = filteredSentences.split(\" \");\n" +
                            "    for(int x = 0; x < POSITIVE_WORDS.length; x++)\n" +
                            "    {                    \n" +
                            "        for(int y = 0; y < wordsInTheSentence.length; y++)\n" +
                            "        {\n" +
                            "            if(wordsInTheSentence[y].equalsIgnoreCase(POSITIVE_WORDS[x]))\n" +
                            "            {\n" +
                            "                positiveCount++;\n" +
                            "            }\n" +
                            "        }\n" +
                            "    }\n" +
                            "    return positiveCount;\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "public static int countNegativePerceptionWords(String filteredSentences){\n" +
                            "    int negativeCount=0;\n" +
                            "    String[] wordsInTheSentence = filteredSentences.split(\" \");\n" +
                            "    for(int x = 0; x < NEGATIVE_WORDS.length; x++)\n" +
                            "    {                    \n" +
                            "        for(int y = 0; y < wordsInTheSentence.length; y++)\n" +
                            "        {\n" +
                            "            if(wordsInTheSentence[y].equalsIgnoreCase(NEGATIVE_WORDS[x]))\n" +
                            "            {\n" +
                            "                negativeCount++;\n" +
                            "            }\n" +
                            "        }\n" +
                            "    }\n" +
                            "    return negativeCount;\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "public static String filterSentences(String pageContent){\n" +
                            "    //pageContent = \"Thats.is.it.\";\n" +
                            "    //pageContent = pageContent.replaceAll(\"\\\\.\", \" \");\n" +
                            "    //System.out.println(\"1.0 This is it!\");\n" +
                            "    String filteredSentences = \"\";\n" +
                            "    String[] sentences = pageContent.split(\"\\\\.\");\n" +
                            "    int l=(sentences.length);\n" +
                            "    try{\n" +
                            "        for(int x = 0 ; x<l; x++)\n" +
                            "        {\n" +
                            "\n" +
                            "            if(sentences[x].contains(\"K12\"))\n" +
                            "            {   \n" +
                            "                filteredSentences+=\" \"+sentences[x];\n" +
                            "            }\n" +
                            "\n" +
                            "        }\n" +
                            "    }catch(ArrayIndexOutOfBoundsException ex){\n" +
                            "\n" +
                            "    }\n" +
                            "\n" +
                            "    return filteredSentences;\n" +
                            "}";
        
        String classClose = "\n\n}//End of Main class";
//        System.out.println("thisJavaCode " + javaCode);
//        javaCode = pkg + head + javaCode + mainClose + builtInFunctions+ classClose;

        javaCode = pkg + head + javaCode + mainClose + classClose;
//        builtInFunctions = builtInFunctions.replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n");
        javaCode = javaCode.replace("volunteerCode = \"\"", "volunteerCode = \""+builtInFunctions+"\"");

//        javaCode = pkg + head + javaCode + close;
        
//        System.out.println(javaCode);
        javaConverted = javaCode;
//        PrintWriter out = new PrintWriter("src/java/interpreter_output/Main.java");
//        out.println(javaCode);
//        out.close();
    }
    
    
  private static String readTxtFileLines (String path){
        String ret = "";
        
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                ret += sCurrentLine+"\n";
            }   

        } catch (IOException e) {
            System.out.println(e.toString());
        }
        
        return ret;
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
            ret = "public static String start(){";
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