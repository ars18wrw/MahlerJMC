/* A class which generates music from words
*  Based on Guido d'Arezzo's lookup chart for generating 
*  pitches from syllabes (ca. 1000 A.D.)
*  @author Andrew Troedson
*  
*  g2 a2 b2 c3 d3 e3 f3 g3 a3 b3 c4 d4 e4 f4 g4 a4
*  A  E  I  O  U  A  E  I  O  U  A  E  I  O  U  A
*/

import java.text.*;			//contains the StringCharacterIterator
import jm.music.data.*;		//the jMusic classes
import jm.JMC;
import jm.util.*;

public final class wordMusic implements JMC {

//the passage from which the melody will be generated
String passage;
//the name of the file produced
String fileName;

	public wordMusic() {	
	}
	
	public void setPassage(String passage) {
		this.passage = passage;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	//compose method takes two args: the passage String and the fileName String
	public void compose(String p, String f) {
	
		passage = p;
		fileName = f;
		Score score = new Score("wordMusic", 120);
		Part piano = new Part("Piano", PIANO, 0);
		Phrase phrase = new Phrase(0.0);
		Note nextNote = new Note();
		
		//set a default passage if none is given
		if (passage.length() == 0) {
			setPassage("twinkle twinkle little star,"+
								" how I wonder what you are,"+
								"	up above the world so high,"+
								" like a diamond in the sky.");
		}
		
		//set a default filename if none is given
		if (fileName.length() == 0) {
			setFileName("wordMusic.mid");
		}
		
		//change all the letters in the passage variable to 
		//Upper Case to remove case sensitivity
		passage = passage.toUpperCase();
		//assign a StringCharacterIterator to the passage
		StringCharacterIterator iter = new StringCharacterIterator(passage);

		//the variable used to count the number of non-vowels 
		//(ie consonants and symbols) between each vowel
		//this number is sent to setNoteLength() every time a 
		//new note is made	
		int nonVowelCounter = 0;
		
		//this variable is set each new note is created, and 
		//sets the Pitch of the new note
		//(this is chosen at random from the 3 or 4 options 
		//allowed for that vowel in d'Arezzo's lookup chart)
		int notePitch;
		
		//the variable used to make the random pitch selection
		int randNum;	
		
		//the main iteration loop which goes through passage 
		//and makes the notesthese notes are then added to 
		//the Phrase phrase					
		for(int i=0; i<passage.length(); i++) {
		
			iter.setIndex(i);
			char nextChar = iter.current();
			System.out.println(i + " " + nextChar);
	
 				switch (nextChar) {
				case 'A':
				
				randNum = ((int)(Math.random()*4));
				
				if (randNum == 0){
						notePitch = G2;}
				else if (randNum == 1){
						notePitch = E3;}
				else if (randNum == 2){
						notePitch = C4;}
				else {
						notePitch = a4;}	
							
						nextNote = new Note(notePitch,setNoteLength(nonVowelCounter));
						phrase.addNote(nextNote);
						nonVowelCounter = 0;
						break;
						
				case 'E':
				
				randNum = ((int)(Math.random()*3));
				
				if (randNum == 0){
						notePitch = A2;}
				else if (randNum == 1){
						notePitch = F3;}
				else {
						notePitch = D4;}
						
						nextNote = new Note(notePitch,setNoteLength(nonVowelCounter));
						phrase.addNote(nextNote);
						nonVowelCounter = 0;
						break;
						
				case 'I':
				
				randNum = ((int)(Math.random()*3));
				
				if (randNum == 0){
						notePitch = B2;}
				else if (randNum == 1){
						notePitch = G3;}
				else {
						notePitch = E4;}
						
						nextNote = new Note(notePitch,setNoteLength(nonVowelCounter));
						phrase.addNote(nextNote);
						nonVowelCounter = 0;
						break;

				case 'O':
				
				randNum = ((int)(Math.random()*3));
				
				if (randNum == 0){
						notePitch = C3;}
				else if (randNum == 1){
						notePitch = A3;}
				else {
						notePitch = F4;}
						
						nextNote = new Note(notePitch,setNoteLength(nonVowelCounter));
						phrase.addNote(nextNote);
						nonVowelCounter = 0;
						break;

				case 'U':
				
				randNum = ((int)(Math.random()*3));
				
				if (randNum == 0){
						notePitch = D3;}
				else if (randNum == 1){
						notePitch = B3;}
				else {
						notePitch = G4;}
						
						nextNote = new Note(notePitch,setNoteLength(nonVowelCounter));
						phrase.addNote(nextNote);
						nonVowelCounter = 0;
						break;
			
			//for all other letters (ie all the consonants) and syllables, 
			//no notes are created																				
				default:
						nonVowelCounter++; //add 1 to the consonant counter
						break;
				}					
		}
		
		piano.addPhrase(phrase);
		View.print(piano);
		
		//add part (instrument) to the score
		score.addPart(piano);
			
		//OK now we test SMF write and read

		Write.midi(score, fileName);
	}

	//an internal method which decides on the length of each note 
	//depending on the number of consonants (as well as spaces and 
	//other symbols) between vowels	
	static double setNoteLength(int counter){
	
			double noteLength;
			switch (counter) {
				case (0):
						noteLength = Q;
						break;
				case (1):
						noteLength = Q;
						break;							
				case (2):
						noteLength = C;
						break;
				case (3):
						noteLength = C;
						break;												
				case (4):
						noteLength = M;
						break;											
				default: //(ie more than 4 consonants)
						noteLength = MD;
						break;
			}
			return noteLength;
	}	
}