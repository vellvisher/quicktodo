/**
 * Used to test regular expressions
 * @author Vaarnan Drolia
 */
package parser;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TestHarness
{

	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		while (true) {
			System.out.print("%nEnter your regex: ");
			Pattern pattern = Pattern.compile(console.nextLine());
			System.out.print("Enter input string to search: ");
			Matcher matcher = pattern.matcher(console.nextLine());

			boolean found = false;
			while (matcher.find()) {
				System.out.printf("I found the text \"%s\" starting at "
						+ "index %d and ending at index %d.%n",
						matcher.group(), matcher.start(), matcher.end());
				found = true;
			}
			if (!found) {
				System.out.printf("No match found.%n");
			}
		}
	}
}
