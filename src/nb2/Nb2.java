/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nb2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author YH Jonathan Kwok
 */
public class Nb2 {

    /**
     * @param args the command line arguments
     */
    //source: (http://stackoverflow.com/questions/4429995/how-do-you-remove-repeated-characters-in-a-string)
    public static String removeRepeatedChars(String input, int maxRepeat)
    {
        if(input.length() == 0)return input;
        
        char[] chars = input.toCharArray();
        String b = "";
        b += chars[0];// = new StringBuilder;
        char lastChar = chars[0];
        int repeat = 0;
        for(int i=1;i<input.length();i++){
            //repeated less then twice
            if(chars[i] == lastChar && ++repeat < maxRepeat) {
                b += chars[i];
            }
            //repeated too many times
            else if(chars[i] == lastChar && ++repeat >= maxRepeat) {
                
            }
            //not repeated
            else {
                b += chars[i];
                repeat=0;
                lastChar = chars[i];
            }
        }
        return b;
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        ArrayList<String> list = new ArrayList<>();
        String line;
        
        String fileName = "C:\\Users\\YH Jonathan Kwok\\PycharmProjects\\tweepyPractice\\tweets.txt"; //testTrainTweets.txt"; //
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        
        System.out.println("Processing. . . (Reading from file)");
        while((line = br.readLine()) != null) {
            line = line.toLowerCase();
            //put lines into my collection from file
            line = removeRepeatedChars(line, 2);
            line = line.replaceAll("[^0-9a-z ]", " ");
            list.add(line);                        
        }
        
        //new dataset found (http://thinknook.com/twitter-sentiment-analysis-training-corpus-dataset-2012-09-22/)
        //idea of making new testing easy tweets (http://www.laurentluce.com/posts/twitter-sentiment-analysis-using-python-and-nltk/)
        fileName = "C:\\Users\\YH Jonathan Kwok\\PycharmProjects\\tweepyPractice\\SentimentAnalysisDataset.txt"; //testTestTweets.txt"; //
        fr = new FileReader(fileName);
        br = new BufferedReader(fr);
        int lineCounter = 0;
        while((line = br.readLine()) != null && lineCounter < 800000){ //800000){
            line = line.toLowerCase();
            line = removeRepeatedChars(line, 2);
            line = line.replaceAll("[^0-9a-z ]", " ");
            list.add(line);
            lineCounter++;
        }
        
        ArrayList<String> stopWords = new ArrayList<>();
        int stopWordsCount = 0;
        //63.77 w/ nothing
        fileName = "C:\\Users\\YH Jonathan Kwok\\PycharmProjects\\tweepyPractice\\stopCommon.txt";//65.44 only this
        fr = new FileReader(fileName);
        br = new BufferedReader(fr);
        while((line = br.readLine()) != null){
            line = line.toLowerCase();
            line = removeRepeatedChars(line, 2);
            line = line.replaceAll("[^0-9a-z ]", " ");
            stopWords.add(line);
        }
        
        fileName = "C:\\Users\\YH Jonathan Kwok\\PycharmProjects\\tweepyPractice\\stopCountryGoo.txt";//64.77 only this
        fr = new FileReader(fileName);
        br = new BufferedReader(fr);
        while((line = br.readLine()) != null){
            line = line.toLowerCase();
            line = removeRepeatedChars(line, 2);
            line = line.replaceAll("[^0-9a-z ]", " ");
            stopWords.add(line);
        }
        //65.32 sum of the above
        
        /*fileName = "C:\\Users\\YH Jonathan Kwok\\PycharmProjects\\tweepyPractice\\stopLong.txt";//64.12
        fr = new FileReader(fileName);
        br = new BufferedReader(fr);
        while((line = br.readLine()) != null){
            line = line.toLowerCase();
            line = removeRepeatedChars(line, 2);
            line = line.replaceAll("[^0-9a-z ]", " ");
            stopWords.add(line);
        }
        
        fileName = "C:\\Users\\YH Jonathan Kwok\\PycharmProjects\\tweepyPractice\\stopSQL.txt";//64.08
        fr = new FileReader(fileName);
        br = new BufferedReader(fr);
        while((line = br.readLine()) != null){
            line = line.toLowerCase();
            line = removeRepeatedChars(line, 2);
            line = line.replaceAll("[^0-9a-z ]", " ");
            stopWords.add(line);
        }*/
        //65.18
        
        System.out.println("Tweets added to list");
        
        //shuffle the list
        System.out.println("Shuffling list");
        Collections.shuffle(list);
        System.out.println("List shuffled");
        
        //split the list into testing set and training set
        //70% train, 30% testing
        System.out.println("Spliting list to training set (70%) and testing set (30%)");
        ArrayList<String> train = new ArrayList<>();
        ArrayList<String> test = new ArrayList<>();
        int listIter;
        for (listIter = 0; listIter < (list.size() * 0.7); listIter++)
            train.add(list.get(listIter));
        for ( ; listIter < list.size(); listIter++)
            test.add(list.get(listIter));
        System.out.println("List splited");
        //30 seconds
        
        System.out.println("Creating empty maps");
        Map posiMap = new HashMap();
        Map negaMap = new HashMap();
        System.out.println("Empty Maps created");
        
        System.out.println("Inserting words into map, and updating values");
        for (int i = 0; i < train.size(); i++){
            String elements[] = train.get(i).split(" ");
            if (elements[1].equals("1") || elements[1].equals("positive")){
                //do something
                for (int j = 2; j < elements.length; j++){
                    if(posiMap.containsKey(elements[j])){
                        posiMap.replace(elements[j], posiMap.get(elements[j]), ((int)posiMap.get(elements[j]) + 1));
                    }
                    else if(stopWords.contains(elements[j])){
                        stopWordsCount++;
                    }
                    //else means the word is not in yet
                    else {
                        posiMap.put(elements[j], (int)1);
                    }
                }
            }
            else if (elements[1].equals("0") || elements[1].equals("negative")){
                //do something negative
                for (int j = 2; j < elements.length; j++){
                    if(negaMap.containsKey(elements[j])){
                        negaMap.replace(elements[j], negaMap.get(elements[j]), ((int)negaMap.get(elements[j]) + 1));
                    }
                    else if(stopWords.contains(elements[j])){
                        stopWordsCount++;
                    }
                    //else means the word is not in yet
                    else {
                        negaMap.put(elements[j], (int)1);
                    }
                }
            }
            else if (!elements[1].equals("neutral")) 
                System.out.println("Refine your data" + elements[1]);
        }
        System.out.println(stopWordsCount + " stop words skipped");
        System.out.println("Maps are ready");
        
        //34 seconds
        //like extra 20 seconds to run these 2 line
        //System.out.println(posiMap.entrySet());
        //System.out.println(negaMap.values());
        int b15 = posiMap.size();
        int c15 = negaMap.size();
        int total = b15 + c15;
        double f15 = (double)b15 / total;
        double g15 = (double)c15 / total;
        System.out.println("Begin the classifying process:");
        int mCount = 0;
        for (int i = 0; i < test.size(); i++){
            String elements[] = test.get(i).split(" ");
            double posiScore = f15;
            double negaScore = g15;
            if (!elements[1].equals("neutral")) {
                String target;
                if (elements[1].equals("1") || elements[1].equals("positive"))
                    target = "positive";
                else
                    target = "negative";
                //System.out.print("Sentence: \t");
                for (int j = 2; j < elements.length; j++){
                    //System.out.print(elements[j] + " ");
                    //posiScore
                    if (posiMap.containsKey(elements[j])) {
                        int tempInt = (int)posiMap.get(elements[j]);
                        posiScore *= ((double)tempInt / b15);
                    }
                    //negaScore
                    if (negaMap.containsKey(elements[j])) {
                        int tempInt = (int)negaMap.get(elements[j]);
                        negaScore *= ((double)tempInt / c15);
                    }
                }
                //System.out.print("\n");
                
                //System.out.println("Target: \t" + target);
                //2:57
                String predict;
                if (posiScore > negaScore){
                    predict = "positive";
                    //System.out.print("Predict: \t" + predict + "\n");
                }
                else if (negaScore > posiScore){
                    predict = "negative";
                    //System.out.print("Predict: \t" + predict + "\n");
                }
                else {
                    predict = "\t\tTIE!!!";
                    //System.out.print("Predict: \t" + predict + "\n");
                }
                    
                String result;
                if (predict.equals(target)){
                    result = "matched";
                    mCount++;
                }
                else
                    result = "missed";
                
                //System.out.println("Result: \t" + result);
                //if (result.equals("missed")){
                    //System.out.println(predict + " " + posiScore + " " + negaScore);
                //}
                
            }
        }
        double accuracy = (double)mCount / test.size() * 100;
        System.out.println("Accuracy: " + accuracy + "%");
        //System.out.println("posiMap.size(): " + posiMap.size());
        //System.out.println("negaMap.size(): " + negaMap.size());
    }
    
}
