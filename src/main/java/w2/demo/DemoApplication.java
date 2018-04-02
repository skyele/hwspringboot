package w2.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;


@RestController
@SpringBootApplication
public class DemoApplication {

	@RequestMapping("/")
	public String index(){
		return "Hello Spring Boot";
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

	@RequestMapping("/wordladder")
	@ResponseBody



	public String wordladder(String firstword,String target) throws FileNotFoundException {
		Deep englishwords = new Deep();
		englishwords.Read();                                        //English dictionary
		Queue<Stack<String>> queue = new LinkedList<Stack<String>>(); //store the possible word ladder



		String s1 = firstword;

		if ((!englishwords.search(s1))) {
			return("Maybe your first word is not in the dictionary.");
		}
		englishwords.dictionary.add(s1);                            //add first word to the dictionary


		String s2 = target;

		if ((!(englishwords.search(s2)))) {
			return("Maybe your target is not in the dictionary.");
		}



		int len1 = s1.length();
		int len2 = s2.length();
		englishwords.dictionary.add(s2);                            //add target to the dictionary
		if (len1 != len2) {
			return("The length of \"" + s1 + "\" is not equal to the length of \"" + s2 + "\".");
		} else {
			if (s1 != s2) {
				Stack<String> stack = new Stack<String>();
				stack.push(s1);
				queue.offer(stack);

				Loop:
				while (!queue.isEmpty()) {
					Stack<String> localstack = new Stack<String>();
					localstack = queue.poll();
					s1 = localstack.peek();
					for (int j = 0; j < len1; j++) {
						for (int i = 'a'; i <= 'z'; i++) {
							String newString = null;
							newString = change(s1, j, len1, (char) i);
							if (newString.equals(s2)) {
								localstack.push(newString);
								stack = localstack;
								break Loop;
							} else if (englishwords.search(newString) && (!newString.equals(s1))) {
								Stack<String> sclone = (Stack<String>) localstack.clone();
								sclone.push(newString);
								queue.offer(sclone);
							}
						}
					}
				}
				if (stack.size() != 1) {
					String ladder=stack.pop()+"\t";
					int i=1;
					while(!stack.isEmpty()){
						ladder+=(stack.pop()+"\t");
						i++;
					}
					return(ladder+"\n"+"The length between "+firstword+" and "+target+" is " + i);

				} else {
					return("I'm sorry. The word ladder isn't exist");
				}
			} else {
				return("The two words are the same!");
			}

		}


	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}