
package Server.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;


public class Dictionary {
    
   // static File file= new File("C:\\words.txt");
      static File file = new File("C:\\Users\\Ma Star\\Documents\\NetBeansProjects\\h2\\words.txt");
    public Dictionary(){}
    
    public String getDictionaryLine(int totalReadLines) throws FileNotFoundException, IOException{
        BufferedReader dictionaryread= new BufferedReader(new FileReader(file));
                String word="";
                for(int i=0; i<totalReadLines;i++){
                      word=dictionaryread.readLine();    
               }
      return  word;
    }   
}
