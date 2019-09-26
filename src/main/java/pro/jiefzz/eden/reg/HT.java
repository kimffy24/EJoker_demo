package pro.jiefzz.eden.reg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HT {

	public static void main(String[] args) {
		
		String errDesc = "CODE: 1  DESC: queueId[14] is illegal, topic:[EJokerCommand] topicConfig.readQueueNums:[14] consumer:[/192.168.199.104:39836]";
		
		Pattern abs = Pattern.compile("CODE:[\\s\\S\\t\\d]*DESC:[\\s\\S\\t]+queueId\\[\\d+\\] is illegal, topic:\\[[a-zA-Z0-9-_]+\\] topicConfig\\.readQueueNums:\\[\\d+\\] consumer:");
		
		Matcher matcher = abs.matcher(errDesc);
		
		while (matcher.find()) {
			System.err.println(matcher.group());
        }
		
	}
}
