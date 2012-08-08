/**
 * class FileHandler
 * 
 * FileHandler is the class that actually stores the data in permanent storage
 * Data  is stored in files that are XML coded
 * Filehandler is used primarily for simple reading and writing of files to XML or txt
 * 
 * @author Ankit Ganla
 * @author Vaarnan Drolia
 */

package storage;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;


public class FileHandler
{
	private static final String CURRENT_DIRECTORY = ".";
	private static Logger logger = Logger.getLogger(FileHandler.class.getName());
	/**
	 * 
	 * @param object
	 * @param filename
	 * @return boolean- true if object is written to XML, false if otherwise
	 */
	public static boolean writeObject(Object object, String filename) {
		if (logger.isDebugEnabled()) {
			logger.debug(object + "," + filename);
		}

		try {
			BufferedOutputStream xmlOut = new BufferedOutputStream(
					new FileOutputStream(filename));
			XMLEncoder writeToXml = new XMLEncoder(xmlOut);
			writeToXml.writeObject(object);
			writeToXml.close();
			logger.debug("object written to xml file");
			return true;
		} catch (FileNotFoundException e) {
			logger.error("File not found exception");
			return false;
		}
	}

	/**
	 * 
	 * @param filename
	 * @return Returns an Object read from the supplied filename
	 */
	public static Object readObject(String filename) {
		logger.debug(filename);
		try {
			BufferedInputStream xmlIn = new BufferedInputStream(
					new FileInputStream(filename));
			XMLDecoder readFromXml = new XMLDecoder(xmlIn);
			Object o = readFromXml.readObject();
			readFromXml.close();
			logger.debug("read the list from the xml file");
			return o;
		} catch (FileNotFoundException e) {
			logger.error("file not found");
			return null;
		}
	}

	/**
	 * 
	 * @return boolean- true if old file is deleted
	 *         created. false if otherwise
	 */
	public static boolean removeFile(String filename) {
		logger.debug(filename);
		boolean deleted = false;
		try {
			File newFile = new File(filename);
			if (!newFile.exists()) {
				logger.warn("no file found");
				return true;
			}

			deleted = newFile.delete();
			if(!deleted) {
				throw new IOException();
			}
		} catch (IOException exception) {
			logger.error("Could not remove the file");
		}
		return deleted;
	}

	/**
	 * 
	 * @param stringToWrite
	 * @param filename
	 * @param append to existing file or overwrite file
	 */
	public static boolean writeToFile(String stringToWrite, String filename, boolean append) {
		if (logger.isDebugEnabled()) {
			logger.debug(stringToWrite  + "," + filename  + "," + append);
		}
		try {
			BufferedWriter textWriter = new BufferedWriter(new FileWriter(
					filename, append));
			textWriter.write(stringToWrite);
			textWriter.close();
			logger.debug("Successfully written to file");
			return true;
		} catch (IOException e) {
			logger.error("Error when writing to file");
			return false;
		}
	}

	/**
	 * 
	 * @param filePath
	 * @return String- sha1 Checksum
	 */
	public static String getSha1Checksum(String filePath) {
		logger.debug(filePath);

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			logger.error("No such algorithm");
			return null;
		}
		FileInputStream fis;
		try {
			fis = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			logger.error("File not found");
			return null;
		}
		byte[] dataBytes = new byte[1024];

		int nread = 0;

		try {
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
		} catch (IOException e) {
			logger.error("IOException in reading file");
			return null;
		}

		byte[] mdbytes = md.digest();

		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("checksum generated " + sb.toString());
		}
		
		return sb.toString();
	}

	/**
	 * 
	 * @param zipFile in current directory
	 * @return boolean- true if file is unzipped, false otherwise
	 */
	public static boolean unzipFile(String zipFile) {
		if (logger.isDebugEnabled()) {
			logger.debug(zipFile);
		}
		return unzipFile(zipFile, CURRENT_DIRECTORY);
	}

	/**
	 * 
	 * @param zipFile is file to unzip
	 * @param path
	 * @return boolean- true is file is unzipped, false otherwise
	 */
	public static boolean unzipFile(String zipFile, String path) {
		if (logger.isDebugEnabled()) {
			logger.debug(zipFile + "," + path);
		}
		
		int BUFFER = 2048;
		try {
			File file = new File(zipFile);
			ZipFile zip = new ZipFile(file);
			String newPath = path;

			Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();

			logger.info("Processing each entry in zip file");
			while (zipFileEntries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
				String currentEntry = entry.getName();
				File destFile = new File(newPath, currentEntry);

				File destinationParent = destFile.getParentFile();

				if (!destinationParent.getName().equals(newPath)) {
					destinationParent.mkdirs();
					logger.debug("parent directory structure needed to be created");
				}

				if (!entry.isDirectory()) {
					BufferedInputStream is = new BufferedInputStream(
							zip.getInputStream(entry));
					int currentByte;
					byte data[] = new byte[BUFFER];

					logger.debug("writing the current file to disk");
					FileOutputStream fos = new FileOutputStream(destFile);
					BufferedOutputStream dest = new BufferedOutputStream(fos,
							BUFFER);

					while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, currentByte);
					}
					dest.flush();
					dest.close();
					is.close();
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("File not found");
			return false;
		} catch (IOException e) {
			logger.error("IOException while reading");
			return false;
		}
		return true;
	}
}
