package dou;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class Test {

	public static void main(String[] args){
		// TODO 自动生成的方法存根
		 Test test = new Test();
		 Instant instant = Instant.now();
		 String infile=File.separator + "Users" + File.separator + "douxu" + File.separator + "Desktop"
					+ File.separator + "datatest"+ File.separator ;
	    //输出预处理后的数据的csv文件
		  String outfile =File.separator + "Users" + File.separator + "douxu" + File.separator + "Desktop"+File.separator+"Corpus";
		  String kuozhan = ".txt";
		  FilePath.createDir(outfile);
		  System.out.println(instant);
	    // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties(); 
        props.put("annotators", "tokenize, cleanxml, ssplit, pos, lemma, ner, parse, dcoref");  //cleanxml, sentiment,
//        props.put("annotators", "tokenize, cleanxml,  ssplit,  pos"); 
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);  
        List<String> file =FilePath.getListFiles(infile,"",true);
        Iterator<String> it = file.iterator();
        while(it.hasNext()){
           String filename = it.next();
           if(new File(filename).exists()){
    	   String text = test.Context(filename);
    	   Annotation document = new Annotation(text);
    	   pipeline.annotate(document);
    	   List<CoreMap> sentences = document.get(SentencesAnnotation.class);
   		List<String> dataList = new ArrayList<String>();

    	   for(CoreMap sentence: sentences) {
    	           for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
    	        	   String word = token.get(TextAnnotation.class);
                       String pos = token.get(PartOfSpeechAnnotation.class);
                       dataList.add(word+"/"+pos+" ");
    	           } 
            }
    	   String name = getfilename(filename);
//    	   String name1 = name.replace(".x", "");
    	   String subfile =outfile+File.separator+name+kuozhan;
    	   CSVUtils.exportCsv(new File(subfile), dataList); //导出csv文件
    	   System.out.print(subfile+" ");
    	   System.out.println(instant);
    	         //    test.importCsv();  //导入csv文件
       }
        }
         /*  Tree tree = sentence.get(TreeAnnotation.class);
           // this is the Stanford dependency graph of the current sentence
         SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
         Both sentence and token offsets start at 1!
         Map<Integer, CorefChain> graph =
                  document.get(CorefChainAnnotation.class);*/
} 
		private static String getfilename(String filename) {
		// TODO Auto-generated method stub
			File fname = new File(filename.trim());
			String name = fname.getName();
		return name;
	}
		
  //从XMl文件中读取内容
	String Context(String path) {
		// TODO 自动生成的方法存根
		 StringBuilder result = new StringBuilder();
	        try{
	            BufferedReader br = new BufferedReader(new FileReader(path));//构造一个BufferedReader类来读取文件
	            String s = null;
	            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
	                result.append(System.lineSeparator()+s);
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
		return result.toString();
	}
	 //导入csv文件
    public void importCsv()  {
        List<String> dataList=CSVUtils.importCsv(new File("D:"+File.separator+"q.csv"));
        if(dataList!=null && !dataList.isEmpty()){
            for(String data : dataList){
                System.out.println(data);
            }
        }
    }
    //获取某文件夹下所有文件及子文件名称
  
	}
