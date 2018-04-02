package w2.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class Deep {

    Set<String> dictionary=new HashSet<String>();

    public void Read() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:static/EnglishWords.txt");
        try {
            FileReader fr=new FileReader(file);
            BufferedReader bufr=new BufferedReader(fr);
            String s=null;
            while((s=bufr.readLine())!=null) {
                char firstletter=s.charAt(0);
                if('a'<=firstletter&&firstletter<='z') {
                    dictionary.add(s);
                }
            }
            bufr.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean search(String word) {
        if (dictionary.add(word)) {
            dictionary.remove(word);
            return false;
        }
        dictionary.remove(word);
        return true;

    }

    public static String change(String s1, int index , int length, char letter) {
        String str = null;
        if(index!=0||(index+1)!=length) {
            String str1=s1.substring(0,index);
            String str2=s1.substring(index+1);
            str=str1+letter+str2;
        }
        else if (index==0) {
            String substr=s1.substring(1);
            str=letter+substr;
        }
        else if((index+1)==length) {
            String substr=s1.substring(0, index-1);
            str=substr+letter;
        }
        return str;
    }

    public static void main(String[] args) throws FileNotFoundException {

        Deep englishwords= new Deep();
        englishwords.Read();                                        //English dictionary
        Queue<Stack<String>> queue=new LinkedList<Stack<String>>(); //store the possible word ladder

        Scanner sc=new Scanner(System.in);

        System.out.println("The first word:");
        String s1=sc.next();

        while((!englishwords.search(s1))){
            System.out.println("Maybe your first word is not in the dictionary.");
            System.out.println("The first word:");
            s1=sc.next();
        }
        englishwords.dictionary.add(s1);							//add first word to the dictionary

        System.out.println("The target:");
        String s2=sc.next();

        while((!(englishwords.search(s2)))) {
            System.out.println("Maybe your target is not in the dictionary.");
            System.out.println("The target:");
            s2=sc.next();
        };
        sc.close();													//input

        int len1=s1.length();
        int len2=s2.length();
        englishwords.dictionary.add(s2);							//add target to the dictionary
        if(len1!=len2) {
            System.out.println("The length of \""+s1+"\" is not equal to the length of \""+s2+"\".");
        }
        else {
            if(s1!=s2) {
                Stack<String> stack=new Stack<String>();
                stack.push(s1);
                queue.offer(stack);

                Loop:			while(!queue.isEmpty()) {
                    Stack<String> localstack=new Stack<String>();
                    localstack=queue.poll();
                    s1=localstack.peek();
                    for(int j=0;j<len1;j++) {
                        for(int i='a';i<='z';i++) {
                            String newString=null;
                            newString=change(s1,j,len1,(char) i);
                            if(newString.equals(s2)) {
                                localstack.push(newString);
                                stack=localstack;
                                break Loop;
                            }
                            else if(englishwords.search(newString)&&(!newString.equals(s1))) {
                                Stack<String> sclone=(Stack<String>) localstack.clone();
                                sclone.push(newString);
                                queue.offer(sclone);
                            }
                        }
                    }
                }
                if(stack.size()!=1) {
                    System.out.println("The length is "+stack.size());
                    System.out.println("The word ladder:");
                    while(!stack.isEmpty()) {
                        System.out.println(stack.pop()+'\t');
                    }
                }
                else {
                    System.out.println("I'm sorry. The word ladder isn't exist");
                }
            }
            else {
                System.out.println("The two words are the same!");
            }

        }
    }
}