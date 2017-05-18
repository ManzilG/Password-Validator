import java.util.Scanner;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.controller.PasswordController;

public class Application {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		PasswordController passwordController = (PasswordController) context.getBean("controller");
		Scanner scanner = new Scanner(System.in);
		int input=0;
		do {
			System.out.println("Enter the password!!!!!!!!!!!");

			
			String password = scanner.nextLine();

			//Passing all requests to the controller
			Set<String> errors = passwordController.validatePassword(password);
			for (String s : errors) {
				System.out.println(s);
			}
			try{
			System.out.println("---Do you want to exit????????? Press 1 to exit or press any other number to continue--");
			input=Integer.parseInt(scanner.nextLine());
			}catch(NumberFormatException e){
				//e.printStackTrace();
				System.out.println("Enter number only");
			}
		}while(input!=1);

	}

}
