package tdg.teide.toRemove;

import java.util.HashSet;
import java.util.Set;

import org.apache.jena.ext.com.google.common.collect.Sets;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.tdb.TDBFactory;

public class RDFQueryMain {

	public static void main(String[] args) {
		Integer old = GlobalCounter;
		String queryString = "#Prefixes\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n" + 
				"PREFIX owl:<http://www.w3.org/2002/07/owl#>\n" + 
				"PREFIX agg:<java:tdg.link_discovery.connector.sparql.evaluator.arq.linker.aggregates.>\n" + 
				"PREFIX str:<java:tdg.link_discovery.connector.sparql.evaluator.arq.linker.string_similarities.>\n" + 
				"PREFIX trn:<java:tdg.link_discovery.connector.sparql.evaluator.arq.linker.transformations.>\n" + 
				"PREFIX text: <http://jena.apache.org/text#>"+
				"#Query\n" + 
				"SELECT DISTINCT (count(distinct ?wp1) as ?count) {\n" + 
				"	<http://www.okkam.org/oaie/restaurant2-Restaurant7> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.okkam.org/ontology_restaurant2.owl#Restaurant> . \n" + 
				"	<http://www.okkam.org/oaie/restaurant2-Restaurant7> <http://www.okkam.org/ontology_restaurant2.owl#has_address> ?wp1 . \n" + 
				"	?wp1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.okkam.org/ontology_restaurant2.owl#Address> .\n" + 
				"}";
				 
		//System.out.println("------");
		long startTime = System.nanoTime();
		execQuery(queryString, "./tdb-data/restaurants2");
		long stopTime = System.nanoTime();
	    long elapsedTime = (stopTime - startTime)/1000000;

	}
	

	public static Integer GlobalCounter = 0;
	public static Set<String> execQuery(String queryString, String repositoryName) {
		Set<String> finalResults = new HashSet<String>();
		Dataset dataset = TDBFactory.createDataset(repositoryName);
		dataset.begin(ReadWrite.READ);
	
		int counter = 0;
		Set<String> types = new HashSet<String>();
		try {
			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
			ResultSet results = qexec.execSelect();
				while(results.hasNext()){
					if(results!=null){
						QuerySolution soln = results.nextSolution();
						System.out.println(">>>>"+soln);
						 counter++;
						/*if(soln != null){
							GlobalCounter++;
							/*String iri =soln.get("?s").toString();
							if(getIrisFromGold().contains(iri)) {
								 counter++;
							}else {
								System.out.println("not contained: "+iri);
							}
						}*/
					}
				}
				qexec.close();			
				
		}catch(Exception e){
				System.out.println("Failed executing in "+repositoryName+" the query:\n"+queryString);
				e.printStackTrace();			
		} 
		dataset.end();
		
		
		for(String type:types){
			System.out.println(type);
		}
		System.out.println(counter);
		System.out.println(GlobalCounter);
		return finalResults;
	}
	
	public static Set<String> getIrisFromGold(){
		Set<String> iris = Sets.newHashSet();
		iris.add("http://www.imdb_movies.nt#work/id=3168342");
		iris.add("http://www.imdb_movies.nt#work/id=3168344");
		iris.add("http://www.imdb_movies.nt#work/id=3168341");
		iris.add("http://www.imdb_movies.nt#work/id=2835061");
		iris.add("http://www.imdb_movies.nt#work/id=2580315");
		iris.add("http://www.imdb_movies.nt#work/id=2630179");
		iris.add("http://www.imdb_movies.nt#work/id=3754518");
		iris.add("http://www.imdb_movies.nt#work/id=2703511");
		iris.add("http://www.imdb_movies.nt#work/id=281668");
		iris.add("http://www.imdb_movies.nt#work/id=281668");
		iris.add("http://www.imdb_movies.nt#work/id=2816697");
		iris.add("http://www.imdb_movies.nt#work/id=2763723");
		iris.add("http://www.imdb_movies.nt#work/id=3049217");
		iris.add("http://www.imdb_movies.nt#work/id=3049225");
		iris.add("http://www.imdb_movies.nt#work/id=3566268");
		iris.add("http://www.imdb_movies.nt#work/id=3318274");
		iris.add("http://www.imdb_movies.nt#work/id=2593902");
		iris.add("http://www.imdb_movies.nt#work/id=2593903");
		iris.add("http://www.imdb_movies.nt#work/id=2681716");
		iris.add("http://www.imdb_movies.nt#work/id=2681718");
		iris.add("http://www.imdb_movies.nt#work/id=2681719");
		iris.add("http://www.imdb_movies.nt#work/id=2681724");
		iris.add("http://www.imdb_movies.nt#work/id=2607169");
		iris.add("http://www.imdb_movies.nt#work/id=2607170");
		iris.add("http://www.imdb_movies.nt#work/id=2607171");
		iris.add("http://www.imdb_movies.nt#work/id=1785944");
		iris.add("http://www.imdb_movies.nt#work/id=2011495");
		iris.add("http://www.imdb_movies.nt#work/id=2688003");
		iris.add("http://www.imdb_movies.nt#work/id=2688005");
		iris.add("http://www.imdb_movies.nt#work/id=2688009");
		iris.add("http://www.imdb_movies.nt#work/id=184783");
		iris.add("http://www.imdb_movies.nt#work/id=1849476");
		iris.add("http://www.imdb_movies.nt#work/id=3507742");
		iris.add("http://www.imdb_movies.nt#work/id=3507743");
		iris.add("http://www.imdb_movies.nt#work/id=2654014");
		iris.add("http://www.imdb_movies.nt#work/id=2880925");
		iris.add("http://www.imdb_movies.nt#work/id=3537166");
		iris.add("http://www.imdb_movies.nt#work/id=3537171");
		iris.add("http://www.imdb_movies.nt#work/id=3494081");
		iris.add("http://www.imdb_movies.nt#work/id=3723111");
		iris.add("http://www.imdb_movies.nt#work/id=1786094");
		iris.add("http://www.imdb_movies.nt#work/id=1945093");
		iris.add("http://www.imdb_movies.nt#work/id=3301114");
		iris.add("http://www.imdb_movies.nt#work/id=3301115");
		iris.add("http://www.imdb_movies.nt#work/id=3671070");
		iris.add("http://www.imdb_movies.nt#work/id=3671074");
		iris.add("http://www.imdb_movies.nt#work/id=3031438");
		iris.add("http://www.imdb_movies.nt#work/id=3671149");
		iris.add("http://www.imdb_movies.nt#work/id=3671150");
		iris.add("http://www.imdb_movies.nt#work/id=3671153");
		iris.add("http://www.imdb_movies.nt#work/id=2739344");
		iris.add("http://www.imdb_movies.nt#work/id=3671154");
		iris.add("http://www.imdb_movies.nt#work/id=2739345");
		iris.add("http://www.imdb_movies.nt#work/id=3642781");
		iris.add("http://www.imdb_movies.nt#work/id=3422757");
		iris.add("http://www.imdb_movies.nt#work/id=3373500");
		iris.add("http://www.imdb_movies.nt#work/id=3571518");
		iris.add("http://www.imdb_movies.nt#work/id=3571519");
		return iris;
	}

}
