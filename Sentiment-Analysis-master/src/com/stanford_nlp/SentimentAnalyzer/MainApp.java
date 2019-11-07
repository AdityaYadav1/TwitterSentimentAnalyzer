package com.stanford_nlp.SentimentAnalyzer;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.stanford_nlp.model.SentimentResult;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class MainApp {
	
	
	public static void main(String[] args) throws IOException {

		ArrayList<String> tweets2 = new ArrayList<String>();

		int count = 0;
		Scanner in = new Scanner(System.in);
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("rWwbNiLYcAUSsUur1dcFVMl9A")
		  .setOAuthConsumerSecret("W5bG3q6Q9I7esiaVq7T0410hwuFDRDjqADTSJIg6JEv4bPxRqy")
		  .setOAuthAccessToken("423687694-PFAPIfKZnwFyXA4tkuEfFvtrizbyWgKWE0l2swAP")
		  .setOAuthAccessTokenSecret("FFBHwAqz7vlmqVUC6CVlcBr98wENmlLo9U5tynAg4fs8c");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		
        try {
        	System.out.println("Enter a search word: ");
        	String term = in.nextLine();
            Query query = new Query(term + " -filter:retweets -filter:links -filter:replies -filter:images");
            QueryResult result;
            do {
            	
				result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                   //System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
                	tweets2.add(tweet.getText());
                    count++;
                }
            } while (((query = result.nextQuery()) != null) && count < 3);
           
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
		
		
		
		
		
		
        int pos = 0 ,neg = 0, neut = 0;
		double posTotal = 0, veryPosTotal = 0, negTotal = 0, veryNegTotal = 0, neutTotal = 0;
		for(int i=0; i<tweets2.size(); i++) {
		String text = tweets2.get(i);
		
		SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
		sentimentAnalyzer.initialize();
		SentimentResult sentimentResult = sentimentAnalyzer.getSentimentResult(text);
		
		if (sentimentResult.getSentimentType().equals("Negative") || sentimentResult.getSentimentType().equals("Very Negative")) {
			neg++;
		}
		if (sentimentResult.getSentimentType().equals("Positive") || sentimentResult.getSentimentType().equals("Very Positive")) {
			pos++;
		}
		if (sentimentResult.getSentimentType().equals("Neutral")) {
			neut++;
		}
		veryPosTotal = veryPosTotal + sentimentResult.getSentimentClass().getVeryPositive();
		posTotal = posTotal + sentimentResult.getSentimentClass().getPositive();
		neutTotal = neutTotal + sentimentResult.getSentimentClass().getNeutral();
		veryNegTotal = veryNegTotal + sentimentResult.getSentimentClass().getVeryNegative();
		negTotal = negTotal + sentimentResult.getSentimentClass().getNegative();
		
		System.out.println("==========================================================================");
		System.out.println("Sentiment Score: " + sentimentResult.getSentimentScore());
		System.out.println("Sentiment Type: " + sentimentResult.getSentimentType());
		System.out.println("Very positive: " + sentimentResult.getSentimentClass().getVeryPositive()+"%");
		System.out.println("Positive: " + sentimentResult.getSentimentClass().getPositive()+"%");
		System.out.println("Neutral: " + sentimentResult.getSentimentClass().getNeutral()+"%");
		System.out.println("Negative: " + sentimentResult.getSentimentClass().getNegative()+"%");
		System.out.println("Very negative: " + sentimentResult.getSentimentClass().getVeryNegative()+"%");
		System.out.println("==========================================================================");
		
		
		
		}
		
		veryPosTotal = veryPosTotal/tweets2.size();
		posTotal = posTotal/tweets2.size();
		neutTotal = neutTotal/tweets2.size();
		veryNegTotal = veryNegTotal/tweets2.size();
		negTotal = negTotal/tweets2.size();
		if (pos > neg && pos + neg >= neut) {
			System.out.println("Sentiment Type: Positive");
		}
		if (neut > pos && neut > neg && pos + neg <= neut ) {
			System.out.println("Sentiment Type: Neutral");
		}
		if (neg + pos >= neut && neg > pos) {
			System.out.println("Sentiment Type: Negative");
		}
		System.out.println("Very positive: " + Math.round(veryPosTotal)+"%");
		System.out.println("Positive: " + Math.round(posTotal)+"%");
		System.out.println("Neutral: " + Math.round(neutTotal)+"%");
		System.out.println("Negative: " + Math.round(negTotal)+"%");
		System.out.println("Very negative: " + Math.round(veryNegTotal)+"%");
		
		
	}
}
	
