package com.ifg.Config.Licence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ifp.Action.BaseAction;
import com.ifp.exceptions.Exceptionifp;
import com.ifp.util.CommonDesc;

public class Licensemanager extends BaseAction {

	private static final long serialVersionUID = 1L;
	Exceptionifp excp = new Exceptionifp();

	String filename;
	String str[] = new String[50];
	Logger logger = Logger.getLogger(this.getClass());
	String key = "sureshnaik";

	BufferedReader br;
	String instq = null, DEPLOYMENT_TYPE = null, N_BIN = null, chckfile, sbin, sproduct, scardtype;

	// this function check the nstid deploymenttype, bincount in license file

	// LICENSE_MATCHED - Licence Matched
	// invalid - License vlaues Not Matched
	// noinstid - Inst IS not Matched
	// nodeplyid - Deploy ID not m atched
	// binexceed - BIN count Exceed
	// nofile - No Licence File matched
	// Error - Error While cheking licence file
	public String chckInstLicence(String instid, String deploymenttype, CommonDesc commondesc)
			throws Throwable {
		boolean filedeleted = false;
		// int bin_count = Integer.parseInt(bincount);
		// System.out.println("BIN COUNT RECEVIED IS "+bin_count);
		int NO_BIN = 0;
		String inst_license_chk = "invalid";
		// int bincnt=Integer.parseInt(bincount);
		// String inst_license_chk = "invalid";
		System.out
				.println("value received from function:::: instid  " + instid + "  deploymenttype ::" + deploymenttype);

		chckfile = checkfile(instid, commondesc);
		System.out.println("Check File Returns " + chckfile);
		try {

			if (chckfile == "matched") {
				filename = decrypt(instid, commondesc);
				System.out.println("FILE NAME DECRIPTY   " + filename);

				Properties properties = new Properties();

				br = new BufferedReader(new FileReader(filename));
				System.out.println("filename IS OPEN FOR READ");
				String thisLine = "";
				int j = 0;
				while ((thisLine = br.readLine()) != null) {
					// str[j] = thisLine.trim();
					// System.out.println("str["+j+"]STRING INS THE FILE
					// "+str[j]);
					// ++;

				}
				br.close();
				/*
				 * String del=deleteFile(filename); //filedeleted = .delete();
				 * System.out.println("FILE DELETED "+filedeleted);
				 * if(str[1].contains("INSTID")) {
				 * instq=str[1].substring(7).trim(); System.out.println(
				 * "INSTITU ID FROM LICENCE FILE "+instq);
				 * if(!(instid.equals(instq))) { System.out.println(
				 * " INST ID NOT MATCHED "); inst_license_chk = "noinstid";
				 * return inst_license_chk; } }
				 * /*if(str[2].contains("DEPLOYMENT_TYPE")) {
				 * DEPLOYMENT_TYPE=str[2].substring(15).trim();
				 * System.out.println(" Deployment from License is "
				 * +DEPLOYMENT_TYPE);
				 * if(!(DEPLOYMENT_TYPE.equals(deploymenttype))) {
				 * System.out.println(" DELPLOYMENT NOT MATCHED NOT MATCHED ");
				 * inst_license_chk = "nodeplyid"; return inst_license_chk; }
				 * 
				 * }
				 */
				/*
				 * if(str[3].contains("NO_OF_BIN")) {
				 * N_BIN=str[3].substring(10).trim();
				 * NO_BIN=Integer.parseInt(N_BIN); System.out.println(
				 * "No of BIN is from FILE "+NO_BIN); System.out.println(
				 * "BIN COUNT RECEVIED IS "+bin_count); if(NO_BIN < bin_count) {
				 * System.out.println("BIN COUNT EXCEED"); inst_license_chk =
				 * "binexceed"; return inst_license_chk; } }
				 */

				// System.out.println("ALL CONDITION CHECKED LICENCE MATCHED");
				inst_license_chk = "LICENSE_MATCHED";
			} else {
				inst_license_chk = "nofile";
			}

		} catch (Exception e) {
			inst_license_chk = "Error";
		}
		System.out.println("Return Value is " + inst_license_chk);
		return inst_license_chk;
	}

	public String chckBinLicence(String instid, String bin) throws Throwable {
		System.err.println("chckBinLicence started .....");
		CommonDesc commondesc = new CommonDesc();

		String bin_license_chk = "invalid";
		System.out.println("value received from function:::: instid  " + instid + "  bin ::" + bin);
		boolean valid = false;
		String chckfile = "";

		try {
			chckfile = checkfile(instid, commondesc);
		} catch (Exception e) {
			System.err.println("Exception in checkfile :: " + e);
		}
		System.out.println("chckBinLicence Check FIle " + chckfile);
		System.err.println("chckBinLicence endes .....");
		return "sucess";
	}

	public String chckBinAndProductLicence(String instid, String bin, CommonDesc commondesc) throws Throwable {
		// This Function will Return
		// invalid - no bin product match found
		// noinst - If no institution Found
		// sucess - Successfull
		// nofile - No Licence File Found
		// Error - Error

		String bin_license_chk = "invalid";
		System.out.println("value received from function:::: instid  " + instid + "  bin ::" + bin);
		boolean valid = false;
		String chckfile = checkfile(instid, commondesc);
		System.out.println("chckBinLicence Check FIle " + chckfile);
		int NO_BIN = 0;
		int iterat = 0;
		try {

			if (chckfile == "matched") {
				filename = decrypt(instid, commondesc);

				System.out.println("reades filename:::" + filename);
				Scanner sc = new Scanner(new FileReader(filename));

				System.out.println("start");
				String nobin = "";
				String binline = "";
				do {

					String next = sc.nextLine();
					binline = next.contains("NO_OF_BIN") ? next.replaceAll("NO_OF_BIN", "").trim() : "";
					if (binline != "") {
						System.out.println("iterate:::" + iterat + "--" + binline);
						nobin = binline;
					}

					iterat++;
				} while (sc.hasNextLine());
				System.out.println("nobinnobinnobinnobin" + nobin);

				sc.close();

				Scanner sc1 = new Scanner(new FileReader(filename));
				do {

					String next = sc1.nextLine();
					for (int i = 0; i < Integer.parseInt(nobin); i++) {

						System.err.println(next.contains("BIN_" + i) ? next.replaceAll("BIN_" + i, "").trim() : "");
						System.out.println(
								(next.contains("BIN_" + i) ? next.replaceAll("BIN_" + i, "").trim() : "").equals(bin));

						if ((next.contains("BIN_" + i) ? next.replaceAll("BIN_" + i, "").trim() : "").equals(bin)) {
							bin_license_chk = "sucess";
							// return bin_license_chk;
						}
					}
				} while (sc1.hasNextLine());
				sc1.close();
				System.out.println("end");

				System.out.println("filename IS OPEN FOR READ" + filename);
				br = new BufferedReader(new FileReader(filename));

				String thisLine = "";
				int i, j = 0;

				System.out.println("stttt" + str[3]);

				while ((thisLine = br.readLine()) != null) {
					str[j] = thisLine.trim();
					// System.out.println("str[j]-------->"+str[j]);
					j++;

				}
				br.close();

				/////////////

				System.out.println("1:filename:" + filename);

				///////////////////
				System.out.println("delete fiel name::" + filename);
				String del = deleteFile(filename);
				System.out.println("FILE DELETED " + del);
				System.out.println("str.length " + str.length);
				int bincounting = 0;
				for (i = 0; i < j; i++) {
					if (str[i].contains("INSTID")) {
						instq = str[i].substring(7).trim();
						// System.out.println("INSTITU ID FROM LICENCE FILE
						// "+instq);
						if (!(instid.equals(instq))) {
							System.out.println(" INST ID NOT MATCHED ");
							bin_license_chk = "noinst";
							break;
						}

					}
					/*
					 * if(str[3].contains("NO_OF_BIN")) {
					 * N_BIN=str[3].substring(10).trim();
					 * NO_BIN=Integer.parseInt(N_BIN); System.out.println(
					 * "No of BIN is from FILE "+NO_BIN); String bin_count
					 * =str[i].substring(3).trim(); System.out.println(
					 * "BIN COUNT RECEVIED IS "+bin_count);
					 * System.out.println("---"+bin_count.length());
					 * if(bin_count.length()==6) { bincounting = bincounting+1;
					 * } //if(NO_BIN < bin_count) //{ // System.out.println(
					 * "BIN COUNT EXCEED"); // bin_license_chk = "binexceed"; //
					 * return bin_license_chk; //} }
					 * System.out.println("bincounting---"+bincounting);
					 */

					if (str[i].contains("BIN")) {
						System.out.println(" BIN KEY WORD EXIST IN THE LICENSE ");
						System.out.println("sbin::::::" + str[i]);
						sbin = str[i].substring(3).trim();
						System.out.println(" PASSED BIN VALUE : " + bin);
						System.out.println(" LICENCE BIN : " + sbin);
						if (sbin.equals(bin)) {
							System.out.println("BIN IS  EQUAL ");
							bin_license_chk = "sucess";
							return bin_license_chk;
						}
					}

				}

				System.out.println("binnnnnnnnnnn:" + bincounting);
			} else {
				bin_license_chk = "nofile";
			}
		} catch (Exception e) {
			System.out.println("Exception in bin_license_chk " + e);
			bin_license_chk = "Error";
		}
		System.out.println("License Mgr bin_license_chk  " + bin_license_chk);
		return bin_license_chk;
	}

	public String check_subProd_licence(String instid, String bin, String productid, String cardtype,
			CommonDesc commondesc) throws Throwable {
		System.out.println("instid=======>" + instid);
		System.out.println("bin=======>" + bin);
		System.out.println("productid=======>" + productid);
		System.out.println("cardtype=======>" + cardtype);
		boolean valid = false;
		String subprod_license_chk = "invalid";
		try {
			String chckfile = checkfile(instid, commondesc);
			System.out.println("CHECK FILE Returns ===> " + chckfile);
			filename = decrypt(instid, commondesc);
			if (chckfile.contentEquals("matched")) {
				br = new BufferedReader(new FileReader(filename));
				System.out.println("filename IS OPEN FOR READ");
				String thisLine = "";
				int i, j = 0;
				while ((thisLine = br.readLine()) != null) {
					str[j] = thisLine.trim();
					j++;
				}
				br.close();
				String del = deleteFile(filename);
				for (i = 0; i < j; i++) {
					if (str[i].contains("INSTID")) {
						instq = str[i].substring(7).trim();
						System.out.println(instq);
						if (!(instid.equals(instq))) {
							System.out.println(" INST ID NOT MATCHED SO BREAK THE FOR ");
							subprod_license_chk = "noinst";
							break;
						}

					}
					if (str[i].contains("BIN")) {
						sbin = str[i].substring(3).trim();
						System.out.println(" BIN STRING IS  MATCHED ");
						if (sbin.equals(bin)) {
							System.out.println(" Bin Matched " + sbin);
							sproduct = str[i + 1].substring(12).trim();
							scardtype = str[i + 2].substring(10).trim();
							if (sproduct.equals(productid)) {
								System.out.println(" Sub-Product Matched " + sproduct);
								if (scardtype.contains(cardtype)) {
									System.out.println("'SBIN':::" + sbin);
									System.out.println("'sproduct':::" + sproduct);
									System.out.println("'scardtype':::" + scardtype);
									System.out.println("BIN FROM FILE ===== " + sbin + "  Matched");
									return "sucess";
								}
							}
						}
					}
				}

			} else {
				subprod_license_chk = "nofile";
			}
		} catch (Exception e) {
			subprod_license_chk = "Error";
		}

		return subprod_license_chk;

	}

	public String checkfile(String inst, CommonDesc commondesc) {
		String File_exsist = "notmatched";
		// String licence_path = getText("licencePath");
		Properties prop = commondesc.getCommonDescProperty();
		String licence_path = prop.getProperty("licencePath");
		filename = (licence_path + inst + "_enctrypted_License.txt");
		System.out.println("Inside checkfile function ");
		try {
			File file = new File(filename);
			boolean exists = file.exists();
			//System.out.println("Filename : " + filename);
			System.out.println("Checking license exist : " + exists);
			if (exists) {
				//System.out.println("file name matched");
				logger.warn("License file name matched");
				File_exsist = "matched";
			} else {
				File_exsist = "nofile";
			}

		} catch (Exception e) {
			File_exsist = "nofile";
		}
		System.out.println("Return Value is  " + File_exsist);
		return File_exsist;
	}

	public String encrypt() throws Throwable {
		trace("*************** Encrypting License File Begins **********");
		enctrace("*************** Encrypting License File Begins **********");
		String inst_id = getRequest().getParameter("inst_id").trim();
		trace("instId::" + inst_id);
		inst_id = inst_id.toUpperCase();
		HttpSession session = getRequest().getSession();
		CommonDesc commondesc = new CommonDesc();
		try {
			// String licence_path = getText("licencePath");
			Properties prop = commondesc.getCommonDescProperty();
			String licence_path = prop.getProperty("licencePath");
			String filename = licence_path + inst_id + "License.txt".trim();
			// original file
			trace("filename:::" + filename);
			FileInputStream fis = new FileInputStream(filename);
			// encrypted file
			String encoutfile = (licence_path + inst_id + "_enctrypted_License.txt");
			
			trace("encoutfile =====  "+encoutfile);
			
			FileOutputStream fos = new FileOutputStream(encoutfile);
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
			SecretKey desKey = skf.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding
														// for SunJCE
			trace(" $$$$$$$$$$$$$$$  Inside encrypt function $$$$$$$$$$$$$$$$$");
			cipher.init(Cipher.ENCRYPT_MODE, desKey);
			CipherInputStream cis = new CipherInputStream(fis, cipher);
			doCopy(cis, fos);
			session.setAttribute("curerr", "S");
			session.setAttribute("curmsg", " License Encrypted Sucessfully ");
			return "encryptSucess";
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Exception : Institution Id is invalid / File Path does not exist.. ");
			trace("Exception : Error While Encrypting License  " + e.getMessage());
			return "encryptSucess";
		}

	}

	public String decrypt(String inst_id, CommonDesc commondesc) throws Throwable {
		inst_id = inst_id.toUpperCase();
		// String licence_path = getText("licencePath");
		Properties prop = commondesc.getCommonDescProperty();
		String licence_path = prop.getProperty("licencePath");
		String filename = (licence_path + inst_id + "_enctrypted_License.txt");
		// original file
		FileInputStream fis = new FileInputStream(filename);
		// encrypted file
		String outfile = (licence_path + inst_id + "_decripted_License.txt");
		FileOutputStream fos = new FileOutputStream(outfile);
		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey desKey = skf.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for
													// SunJCE
		System.out.println(" $$$$$$$$$$$$$$$  Inside DECRYPTION function $$$$$$$$$$$$$$$$$");
		cipher.init(Cipher.DECRYPT_MODE, desKey);
		CipherInputStream cis = new CipherInputStream(fis, cipher);
		doCopy(cis, fos);
		System.out.println(" ################## Decrypt Text Copied ################### ");
		return outfile;

	}

	public static void doCopy(InputStream is, OutputStream os) throws IOException {
		System.out.println("input stream is ===    "+is +"    output stream is ====="+os);
		byte[] bytes = new byte[64];
		String filedata;
		int numBytes;
		while ((numBytes = is.read(bytes)) != -1) {
			System.out.println(" numbytes====  "+numBytes +"bytes   "+bytes);
			os.write(bytes, 0, numBytes);
		}
		System.out.println("$$$$$$$$$$$$ FILE COPIED in Docopy %%%%%%%%%%%%%%%%%%%");
		os.flush();
		os.close();
		is.close();
	}

	public String viewEncrypt() {
		trace("*************** View Encrypt Begins **********");
		enctrace("*************** View Encrypt Begins **********");

		trace("\n\n");
		enctrace("\n\n");
		return "viewEncrypt";
	}

	public String deleteFile(String fileName) {
		// System.out.println("-----------------------------------------------------------");
		// String fileName = "file.txt";
		// A File object to represent the filename
		File f = new File(fileName);

		// Make sure the file or directory exists and isn't write protected
		// System.out.println(" FIle Deleteion File Exsist ------>
		// "+f.exists());
		if (!f.exists()) {
			throw new IllegalArgumentException("Delete: no such file or directory: " + fileName);
		}
		// System.out.println(" f.canWrite() "+f.canWrite());
		if (!f.canWrite()) {
			throw new IllegalArgumentException("Delete: write protected: " + fileName);
		}

		// If it is a directory, make sure it is empty
		// System.out.println(" f.isDirectory() "+f.isDirectory());
		if (f.isDirectory()) {
			String[] files = f.list();
			if (files.length > 0)
				throw new IllegalArgumentException("Delete: directory not empty: " + fileName);
		}

		System.out.println("f exist...." + f.exists());
		// Attempt to delete it

		Boolean success = false;
		try {
			success = f.delete();
			System.out.println(" File Deleted is ----> " + success);

		} catch (Exception e) {
			System.out.println("exception.delete::" + e);
		}
		if (!success) {
			throw new IllegalArgumentException("Delete: deletion failed");
		}
		// System.out.println("-----------------------------------------------------------");
		return "delete";
	}

	public int checkLicenceDate(String instid, CommonDesc commondesc, HttpSession session) {

		chckfile = checkfile(instid, commondesc);
		trace("Check File Returns " + chckfile);
		int warningdays = 0;
		try {

			if (chckfile == "matched") {
				filename = decrypt(instid, commondesc);
				trace("FILE NAME DECRIPT   " + filename);
				br = new BufferedReader(new FileReader(filename));
				// trace("filename IS OPEN FOR READ");
				String thisLine = "";
				int j = 0;
				String expdate = "";
				while ((thisLine = br.readLine()) != null) {
					str[j] = thisLine.trim();
					// trace("john str["+j+"]STRING INS THE FILE "+str[j]);

					if (str[j].contains("LICENCE_EXPIRY")) {
						expdate = str[j].substring(14).trim();
						trace("Expiry date is " + expdate);
					}

					if (str[j].contains("WARNING_DAYS")) {
						String strwarningdays = str[j].substring(12).trim();
						warningdays = Integer.parseInt(strwarningdays);
						// trace("Warning days "+expdate);
					}

					j++;

				}
				br.close();
				String del = deleteFile(filename);
				String todaydate = commondesc.getDate("yyyy/MM/dd");
				// trace( " today date is : " + todaydate );
				long daysdiff = CommonDesc.calculateDays(todaydate, expdate);
				trace("daysdiff :" + daysdiff );
				session.setAttribute("EXPIRYWARNINGDAYS", Long.toString(daysdiff));
				if (daysdiff < warningdays) {
					if (daysdiff == 0) {
						return 1;
					}
					return 2;
				}
				// trace( " days different " + daysdiff );
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return 0;
	}

	public int chckBincountLicence(String instid, CommonDesc commondesc) throws Throwable {
		boolean filedeleted = false;

		int NO_BIN = 0;

		// int bincnt=Integer.parseInt(bincount);
		// String inst_license_chk = "invalid";
		System.out.println("value received from function:::: instid  " + instid);

		chckfile = checkfile(instid, commondesc);
		System.out.println("Check File Returns " + chckfile);
		try {

			if (chckfile == "matched") {
				filename = decrypt(instid, commondesc);
				System.out.println("FILE NAME DECRIPTY   " + filename);
				br = new BufferedReader(new FileReader(filename));
				System.out.println("filename IS OPEN FOR READ");
				String thisLine = "";
				int j = 0;
				while ((thisLine = br.readLine()) != null) {
					str[j] = thisLine.trim();
					System.out.println("str[" + j + "]STRING INS THE FILE " + str[j]);
					j++;
				}
				br.close();
				String del = deleteFile(filename);
				// filedeleted = .delete();
				System.out.println("FILE DELETED " + filedeleted);
				if (str[3].contains("NO_OF_BIN")) {
					N_BIN = str[3].substring(10).trim();
					NO_BIN = Integer.parseInt(N_BIN);
					System.out.println("No of BIN is from FILE " + NO_BIN);
				}

			}
		} catch (Exception e) {
			System.out.println("ERROR:" + e.getMessage());
		}
		return NO_BIN;
	}
}