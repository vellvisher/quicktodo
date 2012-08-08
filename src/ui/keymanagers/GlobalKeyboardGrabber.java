/**
 * 
 * Class to provide functionality for capturing 
 * global keyboard events when the program is not in focus
 * 
 * 
 * @author Vaarnan Drolia
 */

package ui.keymanagers;

import java.io.File;

import javax.swing.KeyStroke;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

public class GlobalKeyboardGrabber
{

	private static boolean isWindows;
	private static Provider keyBoardInstance;

	/**
	 * sets up the global hot key depending upon the operating system and
	 * architecture.
	 * 
	 */
	public static void setup() {
		keyBoardInstance = null;
		if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
			isWindows = true;
		} else {
			isWindows = false;
		}

		if (isWindows) {
			String libLocation = new File(".").getAbsolutePath();
			libLocation = libLocation.substring(0, libLocation.length() - 1);
			libLocation += "lib\\com\\melloware\\jintellitype\\JIntellitype.dll";

			String is64Bit = System.getenv("PROCESSOR_ARCHITECTURE");
			if (!"".equals(is64Bit)) {
				libLocation = libLocation.replace("JIntellitype.dll",
						"JIntellitype64.dll");
			}

			JIntellitype.setLibraryLocation(libLocation);

			if (JIntellitype.isJIntellitypeSupported()
					&& (!JIntellitype.checkInstanceAlreadyRunning("QuickToDo"))) {
				JIntellitype.getInstance();
				JIntellitype.getInstance().registerHotKey(1, "CTRL+SHIFT+A");
			}
		} else {
			keyBoardInstance = Provider.getCurrentProvider(true);
		}
	}

	/**
	 * Shuts down the global hot key listener
	 * 
	 */
	public static void shutdown() {
		if (isWindows) {
			JIntellitype.getInstance().cleanUp();
		} else {
			keyBoardInstance.reset();
			keyBoardInstance.stop();
		}
	}

	/**
	 * Adds a hot key listener to the respective library by implementing their
	 * own hot key listener interface
	 * 
	 * @param listener
	 */
	public static void addHotKeyListener(
			final ui.keymanagers.GlobalHotKeyListener listener) {
		if (isWindows) {
			JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {

				@Override
				public void onHotKey(int hotKey) {
					listener.onHotKey();
				}

			});
		} else {
			keyBoardInstance.register(
					KeyStroke.getKeyStroke("control shift A"),
					new HotKeyListener() {

						@Override
						public void onHotKey(HotKey hotKey) {
							listener.onHotKey();
						}

					});
		}
	}
}
