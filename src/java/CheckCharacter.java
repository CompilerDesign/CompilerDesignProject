import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CheckCharacter {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\USER\\Desktop\\File.txt"));
		String line;
		String[] parse, storage;
		int res;
		
		while ((line = reader.readLine()) != null) {
			parse = line.split(" ");
			if(parse[0].compareToIgnoreCase("define") != 0 && parse[1].compareToIgnoreCase("=") == 0) {
				res = checkIfCharacter(parse);
				if(res != -1){
					//error
				}
			}else{
				
			}
		}
		
		reader.close();
	}
	
	static int checkIfCharacter(String[] wordLine) {
		
		int retVal = 0;
		
		
		return retVal;
	}

}
