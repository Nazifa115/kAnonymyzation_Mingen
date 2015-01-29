package kAnonymizationAssignment2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

public class KAnonymyzation {

	HashMap<String, String> allDataTable;
	HashMap<String, Integer> hashedTable;
	int k;

	public static void main(String[] args) {
		// System.out.println("Hello World");
		KAnonymyzation kAnon = new KAnonymyzation();
		kAnon.startAnonymyzation();
	}

	public void startAnonymyzation() {
		KAnonymyzation obj = new KAnonymyzation();
		allDataTable = obj.readCSV();
		Scanner scan = new Scanner(System.in);
		// String s = scan.next();
		System.out.println("Enter value of k: ");
		k = scan.nextInt();
		hashedTable = new HashMap<String, Integer>();
		Boolean isYes = obj.isKAnonymous(allDataTable);
		System.out.println(isYes);
	}

	private Boolean isKAnonymous(HashMap<String, String> generatedDataTable) {
		hashedTable = new HashMap<String, Integer>();
		//hashedTable.clear();
		Set<Entry<String, String>> entryString = allDataTable.entrySet();
		System.out.println("Generated table size: " + generatedDataTable.size());
		Iterator<Entry<String, String>> iter = entryString.iterator();
		while (iter.hasNext()){
			Map.Entry<String, String> pair = ((Map.Entry<String, String>) iter.next());
			String key = (String) pair.getKey();
			System.out.println("Generated table key: " + key);
			String value = pair.getValue();
			System.out.println("Generated table value: " + value);
			if (hashedTable.containsKey(value)){
				int count = hashedTable.get(value);
				count++;
				hashedTable.put(value, count);
				System.out.println("Generated table count: " + count + " for value: " + value);
			}
			else {
				hashedTable.put(value, 1);
				System.out.println("Generated table count: " + "1" + " for value: " + value);
			}
			
		}
		return false;
//		Set<Entry<String, Integer>> entryInteger = hashedTable.entrySet();
//		iter = entryInteger.iterator();
//		int loopcount = 0;
//		while (iter.hasNext()) {
//			String key = (String)iter.next();
//			System.out.println("hashed table key: " + key);
//			Integer value = hashedTable.get(key);
//			System.out.println("hashed table value: " + value);
//			if (value < k) {
//				return false;
//			} 
//			loopcount++;
//		}
//		System.out.println("loopcount: " + loopcount + " hashedtable size: " + hashedTable.size());
//		if (loopcount == hashedTable.size()) {
//			return true;
//		} else {
//			return false;
//		}
	}

	public HashMap<String, String> readCSV() {
		String csvFile = "/Users/nazifakarima/Documents/Nazifa/WrightStateUni/CEG7850/adults_modified.txt";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		allDataTable = new HashMap<String, String>();
		try {
			br = new BufferedReader(new FileReader(csvFile));
			if (br != null)
				System.out.println("found file");
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] attributes = line.split(cvsSplitBy);
				//System.out.println(" [age=" + attributes[0] + ", work type="+ attributes[1] + ", gender=" + attributes[9] + "]");
				String QIDtuple = attributes[0] + attributes[1] + attributes[9];
				//System.out.println(line + " : " + s);
				allDataTable.put(line, QIDtuple);
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
