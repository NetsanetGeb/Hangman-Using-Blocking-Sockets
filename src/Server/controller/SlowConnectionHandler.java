
package Server.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import Server.model.Dictionary;

public class SlowConnectionHandler implements Runnable {
   
    private final Socket socket;
    private static final Random rand = new Random();
    Dictionary dict = new Dictionary();
    private static int scoreCounter = 0;
    private static int attemptCounter;
    private static Character s;
    private static String enteredString;
    char[] secretWord;
    
    public SlowConnectionHandler(Socket clientsocket){
        this.socket= clientsocket;
    }
    
    @Override
    public void run(){
        try {
            String selectedWord= dict.getDictionaryLine(rand.nextInt(10000));
            System.out.println(selectedWord);
            scoreCounter = 0;
            boolean first = true;
            boolean charFound = false;
           
            
            while (true) {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                secretWord = new char[selectedWord.length()];
                attemptCounter = 6;
                System.out.println(selectedWord);
                for (int i = 0; i < selectedWord.length(); i++) {
                    secretWord[i] = '_';
                }
                PrintWriter pw = new PrintWriter(socket.getOutputStream());
                String view = new String(secretWord).concat(Integer.toString(attemptCounter)).concat(getScoreNotation(scoreCounter));
                if (first) {
                    pw.print(view);
                    pw.println();
                    pw.flush();
                }
                first = false;
                
                while (attemptCounter > 0) {
                    enteredString = br.readLine();
// if Input is character
                    if (enteredString.length() ==1) {
                            s = enteredString.charAt(0);
                            int index = 0;
                            while (index < selectedWord.length()) {
                                if (characterCompare(s, selectedWord.charAt(index)) == true) {
                                    if(secretWord[index]!=s)//handles repeated input of the same character
                                        charFound = true;
                                         System.out.println("character found");
                                    secretWord[index] = s;
                                    index++;

                                    continue;
                                }
                                index++;
                            }
                            if (!charFound) {
                                attemptCounter--;
                                System.out.println("character not found");
                            }
                            charFound = false;
                            //  if the guess of a word is corret , Increment score and select new word to Guess
                            if (String.valueOf(secretWord).compareToIgnoreCase(selectedWord) == 0) {
                                System.out.println("Guessed correctly using char");
                                scoreCounter++;
                                attemptCounter = 0;
                                selectedWord = wordSelection();
                                view = new String(wordSelection(selectedWord)).concat(Integer.toString(attemptCounter)).concat(getScoreNotation(scoreCounter));

                            } else if (attemptCounter == 0) {
                          //if the guess of a word is incorret , Decrement score and select new word to Guess
                                     System.out.println("Guessed Failed using Char");
                                    scoreCounter--;
                                    selectedWord = wordSelection();
                                    view = new String(wordSelection(selectedWord)).concat(Integer.toString(attemptCounter)).concat(getScoreNotation(scoreCounter));

                            } else {
                                System.out.println("continue guessing");
                                view = new String(secretWord).concat(Integer.toString(attemptCounter)).concat(getScoreNotation(scoreCounter));
                            }
                            pw.print(view);
                            pw.println();
                            pw.flush();

                    } else if(enteredString.length() ==0){
                         System.out.println("Forget to Insert word");
                         attemptCounter = 0;
                         selectedWord = wordSelection();
                         view = new String(wordSelection(selectedWord)).concat(Integer.toString(attemptCounter)).concat(getScoreNotation(scoreCounter));
                         pw.print(view);
                         pw.println();
                         pw.flush();
                    
                    }else{
//if input is a word
                        if (enteredString.compareToIgnoreCase(selectedWord) == 0) {
                             //if the guess of a word is corret , Increment score and select new word to Guess
                            System.out.println("Guessed correctly");
                            scoreCounter++;
                            attemptCounter = 0;
                            selectedWord = wordSelection();
                            view = new String(wordSelection(selectedWord)).concat(Integer.toString(attemptCounter)).concat(getScoreNotation(scoreCounter));
                            pw.print(view);
                            pw.println();
                            pw.flush();

                            break;
                        } else {
                            //if the guess of a word is Incorret , Decrement score and select new word to Guess                           
                            attemptCounter--;
                            if (attemptCounter == 0) {
                                scoreCounter--;
                                System.out.println("Guessed failed");
                            }
                            System.out.println("Guessed failed failed");
                            view = new String(secretWord).concat(Integer.toString(attemptCounter)).concat(getScoreNotation(scoreCounter));
                            pw.print(view);
                            pw.println();
                            pw.flush();
                        }
                    }
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(SlowConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String getScoreNotation(int x) {
        if (x == 0) {
            return "00";
        }
        if (x > 0) {
            return "+" + x;
        } else {
            return "-" + x * -1;
        }
    }
     
    private static boolean characterCompare(char c1, char c2) {
        return Character.toString(c1).compareToIgnoreCase(Character.toString(c2)) == 0;
    }
    
    
     private String wordSelection() throws IOException {
        return dict.getDictionaryLine(rand.nextInt(10000));
    }
     
    private char[] wordSelection(String str) throws IOException {
        secretWord = new char[str.length()];
        for (int i = 0; i < str.length(); i++) {
            secretWord[i] = '_';
        }
        return secretWord;
    }
    
}
