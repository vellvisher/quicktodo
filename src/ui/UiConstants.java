/**
 * This class is used to define the uiconstants in terms of colours, borders and font names.
 * @author Poornima Muthukumar
 */

package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class UiConstants
{
	public static final Color COLOR = UIManager
			.getColor("InternalFrame.activeTitleBackground");
	public static final Font FONT = new Font("Arial", Font.BOLD, 14);
	public static final int ROW_HEIGHT = 40;
	public static final int BUTTON_COLUMN_WIDTH = 30;
	public static final int EVENT_COLUMN_WIDTH = 342;
	public static Dimension NOTE_SIZE = new Dimension(420, 49);
	public static final LineBorder LINE_BORDER = new LineBorder(new Color(0, 0,
			139), 2, true);
	public static final String FONT_TYPE = "Arial";
	public static final LineBorder LABEL_BORDER = new LineBorder(new Color(0,
			0, 0), 2, true);
	public static final Font TEXT_FONT = new Font("Arial", Font.BOLD, 11);
	public static final Font COMMANDBOX_FONT = new Font("Arial", Font.BOLD, 16);
	public static final LineBorder LIGHT_BLUE_BORDER = new LineBorder(
			new Color(153, 180, 209), 2, true);
	public static final Dimension HELPSCREENSIZE = new Dimension(430, 284);
	public static final String TASKBAR_IMAGE = "images/Picture1.png";
	public static final String COMMANDBOX_IMAGE = "images/icon1.png";
	public static final Dimension PANELBOXSIZE = new Dimension(420, 49);
	public static final Point PANELBOXLOCATION = new Point(0, 0);
	public static final Rectangle COMMANDBOXBOUNDS = new Rectangle(69, 0, 351,
			48);

	public static final Dimension FRAMESIZE = new Dimension(420, 82);
	public static final Point FRAMELOCATION = new Point(112, 60);

	public static final Dimension PANELLISTSIZE = new Dimension(420, 162);
	public static final Point PANELLISTLOCATION = new Point(0,
			PANELBOXSIZE.height);

	public static final Dimension FRAMESIZEBOX = PANELBOXSIZE;
	public static final Dimension FRAMESIZEFULL = new Dimension(
			PANELBOXSIZE.width, PANELBOXSIZE.height + PANELLISTSIZE.height);
	public static final Border RAISEDBEVEL = BorderFactory
			.createRaisedBevelBorder();
	public static final Border LOWEREDBEVEL = BorderFactory
			.createLoweredBevelBorder();
	public static final Border COMPOUND = BorderFactory.createCompoundBorder(
			RAISEDBEVEL, LOWEREDBEVEL);
	public static final Rectangle TODOICON_SIZE = new Rectangle(0, 0, 77, 48);

}
