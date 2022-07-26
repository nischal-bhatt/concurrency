package com.learnjava.parallelstreams;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;
import java.util.List;
import java.util.stream.Collectors;

import com.learnjava.util.DataSet;


public class ParallelStreamsExample {

	 public List<String> stringTransform(List<String> namesList)
	 {
		return namesList.
		 //stream()
		 parallelStream()
				.map(this::addNameLengthTransform)
		 .collect(Collectors.toList());
	 }
	
	 public static void main(String[] args)
	 {
		List<String> namesList= DataSet.namesList();
		ParallelStreamsExample pse = new ParallelStreamsExample();
		
		startTimer();
		
		List<String> resultList=pse.stringTransform(namesList);
		log("resultlist : " + resultList);
		
		timeTaken();
		
	 }
	 
	 private  String addNameLengthTransform(String name) {
	        delay(500);
	        return name.length()+" - "+name ;
	    }
	 //compare time taken between stream() and parallelStream();
}
