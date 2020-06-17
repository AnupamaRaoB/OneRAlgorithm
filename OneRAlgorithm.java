import java.awt.List;
import java.io.BufferedReader;

import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;


public class OneRAlgorithm {

	public static void main(String[] args) {
		// TODO Auto-generated method stub	
		try {
			System.out.println("Please enter name of the data file:");
			Scanner input = new Scanner(System.in);  // Create a Scanner object
		    String fileName = input.nextLine();
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			ArffLoader.ArffReader arff = new ArffLoader.ArffReader(br);
			Map<String, ArrayList<String>> attributeMap = new HashMap<>();
			ArrayList<String> attributeValues=null;
			HashMap<String,Double> accuracyMap=new HashMap<String,Double>();
			HashMap<String,String> resultMap=new HashMap<String,String>();
			Map<String, ArrayList<Double>> errorMap = new HashMap<>();
			Instances data = arff.getData();

			System.out.println("Number of instances: "+data.numInstances());

			//Storing attribute values 

			for(int inst=0;inst<arff.getStructure().numAttributes()-1;inst++)
			{
				String attribute=arff.getStructure().attribute(inst).name();

				int length=arff.getStructure().numDistinctValues(inst);
				String[] attributeValuesArray=new String[length];
				attributeValues= new ArrayList<>();
				for(int j=0;j<arff.getStructure().numDistinctValues(inst);j++)
				{
					attributeValuesArray[j]=arff.getStructure().attribute(inst).value(j);

				}
				attributeValues.addAll(Arrays.asList(attributeValuesArray));
				attributeMap.put(attribute, attributeValues);
			}

			//storing data values

			ArrayList<String> dataList=new ArrayList<String>();
			for (int i = 0; i < data.numInstances(); i++) {
				//System.out.println(data.instance(i));
				String dataRow=data.instance(i).toString();
				dataList.add(dataRow);
			}



			Iterator<Map.Entry<String, ArrayList<String>>> itr = attributeMap.entrySet().iterator(); 

			while(itr.hasNext()) 
			{ 
				Map.Entry<String, ArrayList<String>> attributeMapEntry = itr.next(); 
				System.out.println("Attribute Key = " + attributeMapEntry.getKey()); 
				ArrayList<String> attributeMapValue = attributeMapEntry.getValue();
				ArrayList<String> lastValues =new ArrayList<String>();
				Map<String, Integer> frequenciesMap = new HashMap<String, Integer>();
				int highestValue=0;
				String highestAttributeElement=null;
				double numerator=0;
				double totaldenominator=0;
				double totalnumerator=0;
				double accuracy=0;			

				for(String aString : attributeMapValue){
					lastValues=new ArrayList<String>();
					System.out.println("Attribute Value ="+aString);
					
					for (String temp : dataList) {
						if(temp.contains(aString))
						{
							String[] dataRowValues= temp.split("\\,");
							String lastRowValue=dataRowValues[dataRowValues.length-1];
							lastValues.add(lastRowValue);
						}

					}
					frequenciesMap=countFrequencies(lastValues);
					System.out.println("Frequencies Map"+frequenciesMap);
					
					for (Map.Entry<String, Integer> val : frequenciesMap.entrySet()) { 
						
						if(val.getValue()>highestValue)
						{
							highestValue=val.getValue();
							highestAttributeElement=val.getKey();
						}
						System.out.println("Element " + val.getKey() + " "
								+ "occurs"
								+ ": " + val.getValue() + " times"); 
						
						totaldenominator=totaldenominator+val.getValue();
						
					} 
					numerator=highestValue;
					totalnumerator=totalnumerator+numerator;
					System.out.println("Highest Attribute element: "+highestAttributeElement);
					resultMap.put(aString, highestAttributeElement);
				}
				
				
				System.out.println("total numerator value: "+totalnumerator);
				System.out.println("total denominator value: "+totaldenominator);
				accuracy=totalnumerator/totaldenominator;
				System.out.println("Accuracy for atribute "+attributeMapEntry.getKey()+":"+accuracy+"\n");
				accuracyMap.put(attributeMapEntry.getKey(), accuracy);
				ArrayList<Double> resultList=new ArrayList<Double>();
				resultList.add(totalnumerator);
				resultList.add(totaldenominator);
				errorMap.put(attributeMapEntry.getKey(),resultList);
			
			}
			System.out.println("Result Map values: "+resultMap);
			System.out.println("Accuracy Map values: "+accuracyMap+"\n");
			
			double maxValueInMap=(Collections.max(accuracyMap.values())); 
			     
	        System.out.println("Printing out final results");
	        	for (Entry<String, Double> entry : accuracyMap.entrySet()) {  
	        	
	            if (entry.getValue()==maxValueInMap) {
	                System.out.println("*** Best 1-rule***"+"\n"+entry.getKey()+":");     // Print the key with max value
	                ArrayList<String> highestAttributeValues=attributeMap.get(entry.getKey()); 
	                
		            //System.out.println("Highest Atrribute Values"+highestAttributeValues);
		            
		            for(String output:highestAttributeValues)
		            {
		            	System.out.println(output +"--->"+resultMap.get(output));
		            	
		            }
		            System.out.print(errorMap);
		            double numerator=errorMap.get(entry.getKey()).get(1)-errorMap.get(entry.getKey()).get(0);
		            
		            System.out.println("Error rate:"+numerator+"/"+errorMap.get(entry.getKey()).get(1));
		              
	            }
	                    
	        }
	             
		}
		catch (IOException e) {
			e.printStackTrace();
		}	
	}
	public static HashMap<String, Integer> countFrequencies(ArrayList<String> list) 
	{ 
		Map<String, Integer> hm = new HashMap<String, Integer>(); 

		for (String i : list) { 
			Integer j = hm.get(i); 
			hm.put(i, (j == null) ? 1 : j + 1); 
		} 
		return (HashMap<String, Integer>) hm;
		
	}
}


