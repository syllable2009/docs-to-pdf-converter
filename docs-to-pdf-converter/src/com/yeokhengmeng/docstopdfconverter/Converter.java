package com.yeokhengmeng.docstopdfconverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public abstract class Converter {

	
	private final String LOADING_FORMAT = "\nLoading file \"%1$s\"\n\n";
	private final String PROCESSING_FORMAT = "Load completed in %1$dms, now converting...\n\n";
	private final String SAVING_FORMAT = "Conversion to \"%1$s\" took %2$dms.\n\nTotal: %3$dms\n";
	
	private long startTime;
	private long startOfProcessTime;
	
	protected String inputFilePath;
	protected String outputFilePath;
	
	protected InputStream inStream;
	protected OutputStream outStream;
	
	protected boolean showOutputMessages = false;
	protected boolean closeStreamsWhenComplete = true;
	
	public Converter(InputStream inStream, OutputStream outStream, boolean showMessages, boolean closeStreamsWhenComplete){
		this.inStream = inStream;
		this.outStream = outStream;
		this.showOutputMessages = showMessages;
		this.closeStreamsWhenComplete = closeStreamsWhenComplete;
	}
	
//	public Converter(String inputFilePath, String outputFilePath, boolean showMessages, boolean closeStreamsWhenComplete) throws IOException {
//		inStream = getInFileStream(inputFilePath);
//		outStream = getOutFileStream(outputFilePath);	
//		this.showOutputMessages = showMessages;
//		
//		inputFilePath = inStream.toString();
//		outputFilePath = outStream.toString();
//		this.closeStreamsWhenComplete = closeStreamsWhenComplete;
//	}
	
	
	public abstract void convert() throws Exception;
	
	private void startTime(){
		startTime = System.currentTimeMillis();
		startOfProcessTime = startTime;
	}
	
	protected void loading(){
		printMessages(String.format(LOADING_FORMAT, inputFilePath));
		startTime();
	}
	
	protected void processing(){
		long currentTime = System.currentTimeMillis();
		long prevProcessTook = currentTime - startOfProcessTime;
		
		printMessages(String.format(PROCESSING_FORMAT, prevProcessTook));
		
		startOfProcessTime = System.currentTimeMillis();
		
	}
	
	protected void finished(){
		long currentTime = System.currentTimeMillis();
		long timeTaken = currentTime - startTime;
		long prevProcessTook = currentTime - startOfProcessTime;

		startOfProcessTime = System.currentTimeMillis();
		
		if(closeStreamsWhenComplete){
			try {
				inStream.close();
				outStream.close();
			} catch (IOException e) {
				//Nothing done
			}
		}
		
		printMessages(String.format(SAVING_FORMAT, outputFilePath, prevProcessTook, timeTaken));
	}
	
	protected InputStream getInFileStream(String inputFilePath) throws FileNotFoundException{
		File inFile = new File(inputFilePath);
		FileInputStream iStream = new FileInputStream(inFile);
		return iStream;
	}
	
	protected OutputStream getOutFileStream(String outputFilePath) throws IOException{
		File outFile = new File(outputFilePath);
		
		try{
			outFile.getParentFile().mkdirs();
		} catch (NullPointerException e){
			//Ignore error since it means not parent directories
		}
		
		outFile.createNewFile();
		FileOutputStream oStream = new FileOutputStream(outFile);
		return oStream;
	}
	
	protected void printMessages(String toBePrinted){
		if(showOutputMessages){
			System.out.println(toBePrinted);
		}
	}
	
	
	
	
	
	
	
	
}
