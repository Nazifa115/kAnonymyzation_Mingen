package kAnonymizationAssignment2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

public class KAnonymyzation {

	ArrayList<String> allDataTable;
	HashMap<String, Integer> generalizedHashedTable;
	HashMap<String, Integer> maxPrecisionGeneralizedTable;
	int generalizationTableSize = 42;
	int[][] generalizationTable = new int[3][generalizationTableSize];
	int k;
	double precision = 0.0;

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
				System.out.println(precision);
			}
		}
		if (precision > 0) {
			System.out.println("Anonymized table found for k = " + k + " Maximum precision: " + precision);
			Iterator<String> iter = maxPrecisionGeneralizedTable.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				int value = maxPrecisionGeneralizedTable.get(key);
				System.out.println(key + ":" + value);
				}
		}else {
			System.out.println("No anonymization was found for the value of k:" + k);
		}
		// Boolean isYes = obj.isKAnonymous(allDataTable);
		// System.out.println(isYes);
	}

	private void countPrecision(int wcLevel, int genLevel, int ageLvl) {
		double cur_precision = 1.00 - ((((wcLevel/3)+(genLevel/2)+(ageLvl/7)))/(30.162*3));
		if (cur_precision > precision){
			precision = cur_precision;
			maxPrecisionGeneralizedTable = generalizedHashedTable;
			}
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
		for (int i = 0; i < allDataTable.size(); i++) {
			String row = allDataTable.get(i);
			String cvsSplitBy = ",";
			String[] attributes = row.split(cvsSplitBy);
			String ageRange = getGenaralisedAge(ageLevel, attributes[0]);
			String workType = getGeneralisedWorkType(workClassLevel,
					attributes[1]);
			String gender = getGenaralisedGender(genderLevel, attributes[9]);
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
