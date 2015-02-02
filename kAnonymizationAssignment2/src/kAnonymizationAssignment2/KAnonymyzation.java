package kAnonymizationAssignment2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class KAnonymyzation {

	ArrayList<String> allDataTable;
	ArrayList<String> genaralisedAllDataTable, generalisedTempAllDataTable;
	HashMap<String, Integer> generalizedHashedTable;
	HashMap<String, Integer> maxPrecisionGeneralizedTable;
	int generalizationTableSize = 42;
	int[][] generalizationTable = new int[3][generalizationTableSize];
	int k;
	// BigDecimal precision = new BigDecimal(0.0);
	double precision = 0;
	BigDecimal zero = new BigDecimal(0.0);

	public static void main(String[] args) {
		// System.out.println("Hello World");
		KAnonymyzation kAnon = new KAnonymyzation();
		kAnon.startAnonymyzation();

	}

	public void startAnonymyzation() {
		KAnonymyzation obj = new KAnonymyzation();
		allDataTable = obj.readCSV();
		System.out.println("Enter value of k: ");
		Scanner scan = new Scanner(System.in);
		k = scan.nextInt();
		generateGenaralizationTable();
		// generalizedHashedTable = new HashMap<String, Integer>();
		for (int i = 0; i < generalizationTableSize; i++) {
			int wcLevel = generalizationTable[0][i];
			int genLevel = generalizationTable[1][i];
			int ageLvl = generalizationTable[2][i];
			System.out.println(wcLevel + ", " + genLevel + ", " + ageLvl);
			generalizedHashedTable = getQIDsForGeneralization(wcLevel,
					genLevel, ageLvl);
			Boolean isYes = isKAnonymous(generalizedHashedTable);
			if (isYes) {
				countPrecision(wcLevel, genLevel, ageLvl);
				//System.out.println(precision);
			}
		}
		// if (precision.compareTo(zero) == 1) {
		if (precision > 0) {
			System.out.println("Anonymized table found for k = " + k
					+ " Maximum precision: " + precision);
			writeOutputToFile();
			Iterator<String> iter = maxPrecisionGeneralizedTable.keySet()
					.iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				int value = maxPrecisionGeneralizedTable.get(key);
				System.out.println(key + ":" + value);
			}
		} else {
			System.out.println("No anonymization was found for the value of k:"
					+ k);
		}
		// Boolean isYes = obj.isKAnonymous(allDataTable);
		 System.out.println("Done. Output file is generated in parent folder.");
	}

	private void writeOutputToFile() {
		StringBuilder toBeWritten = new StringBuilder();
		for (int j = 0; j < genaralisedAllDataTable.size(); j++) {
			//System.out.println(genaralisedAllDataTable.get(j));
			toBeWritten.append(genaralisedAllDataTable.get(j));
			toBeWritten.append(System.getProperty("line.separator"));
		}
		FileOutputStream fop = null;
		File file;
		String toWriteInFile = new String(toBeWritten);
		try {
 
			file = new File("output.txt");
			fop = new FileOutputStream(file);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = toWriteInFile.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// private void countPrecision(int wcLevel, int genLevel, int ageLvl) {
	// BigDecimal deduct_numerator = new BigDecimal((wcLevel / 3)
	// + (genLevel / 2) + (ageLvl / 7));
	// BigDecimal deduct_denom = new BigDecimal(.30162 * 3);
	// BigDecimal deductable = deduct_numerator.divide(deduct_denom);
	// System.out.println(deductable + ", " + deduct_denom + ", "
	// + deduct_numerator);
	// BigDecimal one = new BigDecimal(1);
	// BigDecimal cur_precision = one.subtract(deductable);
	// if (cur_precision.compareTo(precision) == 1) {
	// precision = cur_precision;
	// maxPrecisionGeneralizedTable = generalizedHashedTable;
	// }
	// }

	private void countPrecision(int wcLevel, int genLevel, int ageLvl) {
		double deduct_numerator = (wcLevel / 3.00) + (genLevel / 2.00)
				+ (ageLvl / 7.00);

		double cur_precision = 1 - (deduct_numerator / (30162 * 3.0));
		System.out.print("current precision: " + cur_precision);
		if (cur_precision >= precision) {
			precision = cur_precision;
			System.out.print(" is maximum precision");
			maxPrecisionGeneralizedTable = generalizedHashedTable;
			storeMaxPrecisionDataTable();
		}
		System.out.println("");
	}

	private void storeMaxPrecisionDataTable() {
		genaralisedAllDataTable = new ArrayList<String>(
				generalisedTempAllDataTable);
	}

	private void generateGenaralizationTable() {
		for (int i = 0; i < generalizationTableSize; i++) {
			if (i < 14) {
				generalizationTable[0][i] = 0;
			} else if (i >= 14 && i <= 27) {
				generalizationTable[0][i] = 1;
			} else if (i > 27 && i < 42) {
				generalizationTable[0][i] = 2;
			}

			if ((i < 7) || (i > 13 && i < 21) || (i >= 28 && i < 35)) {
				generalizationTable[1][i] = 0;
			} else {
				generalizationTable[1][i] = 1;
			}

			if (i % 7 == 0) {
				generalizationTable[2][i] = 0;
			} else if (i % 7 == 1) {
				generalizationTable[2][i] = 1;
			} else if (i % 7 == 2) {
				generalizationTable[2][i] = 2;
			} else if (i % 7 == 3) {
				generalizationTable[2][i] = 3;
			} else if (i % 7 == 4) {
				generalizationTable[2][i] = 4;
			} else if (i % 7 == 5) {
				generalizationTable[2][i] = 5;
			} else if (i % 7 == 6) {
				generalizationTable[2][i] = 6;
			}
		}

		// for (int i = 0; i < generalizationTableSize; i++) {
		// System.out.println(generalizationTable[0][i] + " " +
		// generalizationTable[1][i] + " " + generalizationTable[2][i]);
		// }
	}

	private Boolean isKAnonymous(HashMap<String, Integer> generalizedMap) {
		Iterator<String> iter = generalizedMap.keySet().iterator();
		int count = 0;
		while (iter.hasNext()) {
			String key = (String) iter.next();
			int value = generalizedMap.get(key);
			// System.out.println(key + ":" + value);
			if (value < k)
				return false;
			else
				count++;
		}
		if (count == generalizedMap.size()) {
			return true;
		} else
			return false;
	}

	private HashMap<String, Integer> getQIDsForGeneralization(
			int workClassLevel, int genderLevel, int ageLevel) {
		generalizedHashedTable = new HashMap<String, Integer>();
		generalisedTempAllDataTable = new ArrayList<String>();
		for (int i = 0; i < allDataTable.size(); i++) {
			String row = allDataTable.get(i);
			String cvsSplitBy = ",";
			String[] attributes = row.split(cvsSplitBy);
			String ageRange = getGenaralisedAge(ageLevel, attributes[0]);
			String workType = getGeneralisedWorkType(workClassLevel,
					attributes[1]);
			String gender = getGenaralisedGender(genderLevel, attributes[9]);
			String newRow = ageRange + "," + workType + "," + attributes[2]
					+ "," + attributes[3] + "," + attributes[4] + ","
					+ attributes[5] + "," + attributes[6] + "," + attributes[7]
					+ "," + attributes[8] + "," + gender + "," + attributes[10]
					+ "," + attributes[11] + "," + attributes[12] + ","
					+ attributes[13] + "," + attributes[14];
			generalisedTempAllDataTable.add(newRow);
			String qIDVal = ageRange + workType + gender;
			if (generalizedHashedTable.containsKey(qIDVal)) {
				Integer count = generalizedHashedTable.get(qIDVal);
				count++;
				generalizedHashedTable.put(qIDVal, count);
			} else {
				generalizedHashedTable.put(qIDVal, 1);
			}
		}
		return generalizedHashedTable;
	}

	private String getGenaralisedGender(int genderLevel, String genderString) {
		if (genderLevel == 0)
			return genderString;
		else
			return "person";
	}

	private String getGeneralisedWorkType(int workClassLevel,
			String workTypestring) {
		if (workClassLevel == 0) {
			return workTypestring;
		} else if (workClassLevel == 1
				&& (workTypestring.equalsIgnoreCase("Self-emp-not-inc") || workTypestring
						.equalsIgnoreCase("Self-emp-inc"))) {
			return "Self_employed";
		} else if (workClassLevel == 1
				&& (workTypestring.equalsIgnoreCase("Federal-gov") || workTypestring
						.equalsIgnoreCase("State-gov"))) {
			return "Government";
		} else if (workClassLevel == 1
				&& (workTypestring.equalsIgnoreCase("Private"))) {
			return "Private";
		} else {
			return "Work_class";
		}
	}

	private String getGenaralisedAge(int ageLevel, String ageString) {
		if (ageLevel == 0) {
			return ageString;
		} else {
			int divisor = ageLevel * 5;
			int age = Integer.parseInt(ageString);
			int lowerBound = age / divisor;
			lowerBound = lowerBound * divisor;
			int upperBound = lowerBound + divisor;
			return (lowerBound + "-" + upperBound);
		}
	}

	public ArrayList<String> readCSV() {
		// String csvFile =
		// "/Users/nazifakarima/Documents/Nazifa/WrightStateUni/CEG7850/adults_modified.txt";
		String csvFile = "/Users/nazifakarima/Documents/Nazifa/WrightStateUni/CEG7850/adults.txt";
		BufferedReader br = null;
		String line = "";
		// String cvsSplitBy = ",";
		allDataTable = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader(csvFile));
			if (br != null)
				System.out.println("found file");
			while ((line = br.readLine()) != null) {
				// use comma as separator
				// String[] attributes = line.split(cvsSplitBy);
				// System.out.println(" [age=" + attributes[0] + ", work type="+
				// attributes[1] + ", gender=" + attributes[9] + "]");
				// String QIDtuple = attributes[0] + attributes[1] +
				// attributes[9];
				// System.out.println(line + " : " + s);
				allDataTable.add(line);
			}
		} catch (FileNotFoundException e) {
			// TODO: handle exception
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return allDataTable;
	}

}
