import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import java.text.*;
import java.util.regex.*;
import java.io.*;


public class ServerY{

public static String returnVolCode (String type){

String volunteerCode;
volunteerCode = "public static String getContent(String linkURL){ URL url = null; InputStream is = null; BufferedReader reader = null; String line = null; String content = ''; try{ System.out.println(\"Link URL: \"+linkURL); url = new URL(linkURL); is = url.openConnection().getInputStream(); reader = new BufferedReader( new InputStreamReader( is )); while( ( line = reader.readLine() ) != null ){ content += line; } reader.close(); } catch(Exception e){ System.err.println(e.getMessage()); } return content; }";
return volunteerCode;
}


}


