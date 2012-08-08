/**
 * Point of execution of code
 * @author Vaarnan Drolia
 */

import ui.View;
import core.Controller;

import org.apache.log4j.Logger;

public class Main
{
	private static Controller cont;
	private static View view;
	private static Logger logger = Logger.getLogger(Main.class.getName());
	
	public static void main(String args[]) {
		initialize();
	}

	public static void initialize() {
		logger.debug("Creating controller object");
		cont = new Controller();
		logger.debug("Creating view object");
		view = new View();
		logger.debug("Setting view and controller");
		cont.setView(view);
		view.setController(cont);
	}
}
