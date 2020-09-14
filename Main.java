package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {

        // get the path from the command line
        File file = new File(args[0]);

        int c = 0;
        int w = 0;
        int s = 0;

        int syllables = 0;
        int polysyllables = 0; // words with at least 3 syllables

        double score = 0;

        try(Scanner scanner = new Scanner(file)){
            while(scanner.hasNextLine()){
                String text = scanner.nextLine();
                c = text.replaceAll("[ \n\t]","").split("").length;
                w = text.split("[ \n\t]").length;
                s = text.split("[.?!]").length;

                String[] sentences = text.split("[.?!]");

                for(int i = 0; i < sentences.length; i++) {
                    for(String word: sentences[i].split(" ")){
                        String newWord = word.toLowerCase().replaceAll("e\\b", "")
                                .replaceAll("[.?!]\\b", "")
                                .replaceAll("[aeiouy]{2,}", "a")
                                .replaceAll(" th "," a ")
                                .replaceAll(",","");
                                //.replaceAll("y\\b", "");

                        System.out.print(newWord + " ");

                        int syllablesInWord = 0;
                        for(int j = 0; j < newWord.length(); j++){
                            if(newWord.charAt(j) == 'a'){
                                syllablesInWord++;
                                syllables++;
                            }
                            if(newWord.charAt(j) == 'e'){
                                syllablesInWord++;
                                syllables++;
                            }
                            if(newWord.charAt(j) == 'i'){
                                syllablesInWord++;
                                syllables++;
                            }
                            if(newWord.charAt(j) == 'o'){
                                syllablesInWord++;
                                syllables++;
                            }
                            if(newWord.charAt(j) == 'u'){
                                syllablesInWord++;
                                syllables++;
                            }
                            if(newWord.charAt(j) == 'y'){
                                syllablesInWord++;
                                syllables++;
                            }
                        }
                        if(syllablesInWord == 0){
                            syllables++;
                        }
                        if(syllablesInWord >= 3){
                            polysyllables ++;
                        }
                    }
                    System.out.println();
                }
            }

            System.out.println("Words: " + w);
            System.out.println("Sentences: " + s);
            System.out.println("Characters: " + c);
            System.out.println("Syllables: " + syllables);
            System.out.println("Polysyllables: " + polysyllables);

            double ARIindex = automatedReadabilityIndex(c, w, s);
            double fkScore = fleschKincaidReadabilityTests(w,s,syllables);
            double SMOGscore = SMOG(s,polysyllables);
            double CLIndex = CL(s,w,c);


            System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): all");
//            String choice = scanner.nextLine();
            switch("all"){
                case "ARI":
                    System.out.println("Automated Readability Index: " + String.format("%.2f", ARIindex) + " (about " + getAge(ARIindex) + " year olds).");
                    break;
                case "FK":
                    System.out.println("Flesch–Kincaid readability tests: " + String.format("%.2f", fkScore) + " (about " + getAge(fkScore) + " year olds).");
                    break;
                case "SMOG":
                    System.out.println("Simple Measure of Gobbledygook: " + String.format("%.2f", SMOGscore) + " (about " + getAge(SMOGscore) + " year olds).");
                    break;
                case "CL":
                    System.out.println("Coleman–Liau index: " + String.format("%.2f", CLIndex) + " (about " + getAge(CLIndex) + " year olds).");
                    break;
                case "all":
                default:
                    System.out.println("Automated Readability Index: " + String.format("%.2f", ARIindex) + " (about " + getAge(ARIindex) + " year olds).");
                    System.out.println("Flesch–Kincaid readability tests: " + String.format("%.2f", fkScore) + " (about " + getAge(fkScore) + " year olds).");
                    System.out.println("Simple Measure of Gobbledygook: " + String.format("%.2f", SMOGscore) + " (about " + getAge(SMOGscore) + " year olds).");
                    System.out.println("Coleman–Liau index: " + String.format("%.2f", CLIndex) + " (about " + getAge(CLIndex) + " year olds).");
            }

            double avgAge = (double)(getAge(ARIindex) + getAge(fkScore) + getAge(SMOGscore) + getAge(CLIndex)) / 4.0f;

            System.out.println("This text should be understood in average by " + avgAge + " year olds.");

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }



    private static double CL(int s, int w, int c) {
        double L = (double)c / w * 100f; // L = Letters ÷ Words × 100
        double S = (double)s / w * 100f; // S = Sentences ÷ Words × 100
        return 0.0588 * L - 0.296 * S - 15.8;
    }
    private static double SMOG(int s, int polysyllables) {
        return 1.043 * Math.sqrt(polysyllables * (30/s)) + 3.1291;
    }
    private static int getAge(double score){
        int n = (int)Math.ceil(score);
        switch(n){
            case 3:
                return 9;
            case 13:
            case 14:
                return 24;
            default:
                return n + 6;
        }
    }
    private static double automatedReadabilityIndex(int c, int w, int s) {
        return 4.71 * c / w + 0.5d * w / s - 21.43d;
    }
    private static double fleschKincaidReadabilityTests(int w, int s, int syll){
        return 0.39f * w / s + 11.8f * syll / w - 15.59f;
    }


}
