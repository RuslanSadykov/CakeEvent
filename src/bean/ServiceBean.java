package bean;

import data.Model;
import jCMPL.*;
import xmlData.*;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Created by Phaenir on 25.11.2014.
 */
public class ServiceBean implements Serializable{
	public static final String ModelDir="I:\\MyProjects\\JetBrains\\Java\\MyCake\\TestDir\\";
	public static final String WebService="http://localhost:8080/";
	public static final String CmplModel="I:\\MyProjects\\JetBrains\\Java\\MyCake\\cmpl\\CakeEvent.cmpl";
	public static final long serialVersionUID=1L;
	public static final boolean ProduktionSyst = false;
	static Logger logger=Logger.getLogger(String.valueOf(ServiceBean.class));
	private CakeEvent modelData;
	private String modelId;
	private String user;
	public static void init(String path) throws Exception{
		Model.setBasicConfig(ServiceBean.ModelDir);
		Model.init();
		if (!ServiceBean.checkWebService())throw new Exception("Webservice not found. Please see general information in log.");
	}

	private static boolean checkWebService(){
		boolean ok = true;
		try{
			Cmpl m = new Cmpl(ServiceBean.CmplModel);
			m.connect(ServiceBean.WebService);
		}catch (CmplException e) {
			ok = false;
			logger.warning("Cmpl service nicht erreichbar " + ServiceBean.WebService);
		}
		return ok;
	}
	public ServiceBean(){
		this.user="Guest";
	}

	public void setUser(String user){this.user=user;}
	public String getUser(){return this.user;}
	public String getModelsOverview(){
		String[] ids = Model.getModelIds();
		String out = "";
		out += "<table border=\"1\">\n";
		out += "<tr><th>Id</th><th>Name</th><th colspan=\"3\">Model Management</th></tr>\n";
		for(int i=0; i<ids.length; i++){
			String refModel = "Controller?action=03_showModel&modelId="+ids[i];
			out += "<tr><td>"+ids[i]+"</td>";
			out += "<td><a href=\""+refModel+"\">"+Model.get(ids[i]).getModelData().getName()+"</a></td>";
			refModel = "Controller?action=15_removeModel&modelId="+ids[i];
			out += "<td><a href=\""+refModel+"\">delete</a></td>\n";
			refModel = "Controller?action=11_saveModel&modelId="+ids[i];
			out += "<td><a href=\""+refModel+"\">save</a></td></tr>\n";
		}
		out += "</table>\n";
		return out;
	}
	public String getModelAddView(){
		String out = "";
		out	+= "<table border=\"1\">\n";
		out += "<tr><th>Model Id</th><th>Model Name</th></tr>";
		out += "<tr>";
		out += "<td><input type=\"text\" name=\"modelId\" size=\"10\" ></td>";
		out += "<td><input type=\"text\" name=\"modelName\" size=\"10\" ></td>";
		out += "</tr>";
		out	+= "</table>\n";
		return out;
	}
	public String getHiddenModdelId(){
		String out = "<input type=\"hidden\" name=\"modelId\" value=\""+this.modelData.getId()+"\">";
		return out;
	}
	public void setModel(String modelId){
		this.modelId = modelId;
		this.modelData = this.getModel().getModelData();
	}
	public Model getModel(){
		return Model.get(this.modelId);
	}
	public String getDatum(){
		Calendar today = Calendar.getInstance();
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		return df.format(today.getTime());
	}

	public String getModelName(){
		return this.modelData.getName();
	}
	public void setModelName(String name){
		this.modelData.setName(name);
	}

	public boolean isSolved(){
		return this.modelData.getSolution().size()>0 && this.modelData.isSolved();
	}

	public String getSolutionStatus(int solId){
		return this.modelData.getSolution().get(solId).getStatus();
	}

	public int getNrZutaten(){
		return this.modelData.getIngredients().getIngredient().size();
	}

	public int getNrRezepte(){
		return this.modelData.getRecipes().getRecipe().size();
	}

	public int getNrZutaten(int rezeptId){
		return this.modelData.getRecipes().getRecipe().get(rezeptId).getRecipeIngredient().size();
	}

	public int getNrLosungen(){
		return this.modelData.getSolution().size();
	}
	public String getZutatenTableau(){
		String out = "";
		out += "<table border=\"1\">\n";
		out += "<tr><th colspan=\"5\">Zutaten</th></tr>\n";
		out += "<tr><th>Id</th><th>Name</th><th>Preis [&euro;/Einheit]</th><th>Lager</th><th>Einheit</th></tr>\n";
		for (int i=0; i< this.getNrZutaten(); i++ ){
			out += "<tr>";
			out += "<td>"+this.modelData.getIngredients().getIngredient().get(i).getId()+"</td>";
			out += "<td>"+this.modelData.getIngredients().getIngredient().get(i).getName()+"</td>";
			out += "<td>"+this.modelData.getIngredients().getIngredient().get(i).getPrice()+"</td>";
			out += "<td>"+this.modelData.getIngredients().getIngredient().get(i).getStock()+"</td>";
			out += "<td>"+this.modelData.getIngredients().getIngredient().get(i).getUnit()+"</td>";
			out += "</tr>\n";
		}
		out += "</table>\n";
		return out;
	}
	public String getRezepteTableau(){
		String out = "";
		out += "<table border=\"1\">\n";
		out += "<tr><th colspan=\"4\">Rezepte</th></tr>\n";
		for (int i=0; i< this.getNrRezepte(); i++ ){
			Recipe recipe = this.modelData.getRecipes().getRecipe().get(i);
			out += "<tr><td colspan=\"4\">";
			out += "<b>ID: </b>"+recipe.getId()+"<br/>";
			out += "<b>Name: </b>"+recipe.getName()+"<br/>";
			out += "<b>Verkaufspreis: </b>"+recipe.getSalesPrice()+" &euro;<br/>";
			out += "<b>Minimale Produktion: </b>"+recipe.getProductionLwb()+" Stck<br/>";
			out += "<b>Maximale Produktion: </b>"+recipe.getProductionUpb()+" Stck<br/>";
			out += "<b>Beschreibung:</b><br/>"+recipe.getDescription()+"<br/>";
			out += "<b>Zutaten:</b>";
			out += "</td></tr>\n";
			out += "<tr><th>Id</th><th>Name</th><th>Menge</th><th>Einheit</th></tr>\n";
			for(int j=0; j< this.getNrZutaten(i); j++){
				out += "<tr>";
				int zutatenId = recipe.getRecipeIngredient().get(j).getIngredientId();
				out += "<td>"+zutatenId+"</td>";
				out += "<td>"+this.modelData.getIngredients().getIngredient().get(zutatenId).getName()+"</td>";
				out += "<td>"+recipe.getRecipeIngredient().get(j).getAmount()+"</td>";
				out += "<td>"+this.modelData.getIngredients().getIngredient().get(zutatenId).getUnit()+"</td>";
				out += "</tr>\n";
			}
			out += "<tr><td colspan=\"4\"><b>Anleitung:</b><br/>"+recipe.getInstructions()+"</td></tr>\n";
		}
		out += "</table>\n";
		return out;
	}
	public String getSolutionTableau(){
		String out = "";
		out += "<table border=\"1\">\n";
		out += "<tr><th colspan=\"7\">L�sung</th></tr>\n";
		out += "<tr><th align=\"left\" colspan=\"5\">Solver Status: </th><td colspan=\"2\">"+this.modelData.getSolverStatus()+"</td></tr>\n";
		if(this.isSolved()){
			for (int i=0; i< this.getNrLosungen(); i++ ){
				Solution solution = this.modelData.getSolution().get(i);
				out += "<tr><th align=\"left\" colspan=\"5\">Solution Id: </th><td colspan=\"2\">"+i+"</td></tr>\n";
				out += "<tr><th align=\"left\" colspan=\"5\">Optimal: </th><td colspan=\"2\">"+solution.isOptimal()+"</td></tr>\n";
				out += "<tr><th align=\"left\" colspan=\"5\">Status: </th><td colspan=\"2\">"+solution.getStatus()+"</td></tr>\n";
				out += "<tr><th align=\"left\" colspan=\"5\">Datum der L�sung: </th><td colspan=\"2\">"+solution.getSolvedAt()+"</td></tr>\n";
				out += "<tr><th align=\"left\" colspan=\"5\">Profit:</th><td colspan=\"2\">"+solution.getProfit()+" &euro;</td></tr>\n";
				out += "<tr><th align=\"left\" colspan=\"5\">Eingesetztes Kapital:</th><td colspan=\"2\">"+solution.getUsedCapital()+" &euro;</td></tr>\n";
				out += "<tr><th align=\"left\" colspan=\"7\">Produktions-Programm:</th></tr>\n";
				out += "<tr><th colspan=\"1\">Id</th><th colspan=\"4\">Name</th><th colspan=\"2\">Menge [Stck]</th></tr>\n";
				for(int j=0; j< solution.getProduction().size(); j++){
					out += "<tr>";
					int rezeptId = solution.getProduction().get(j).getRecipeId();
					out += "<td colspan=\"1\">"+rezeptId+"</td>";
					out += "<td colspan=\"4\">"+this.modelData.getRecipes().getRecipe().get(rezeptId).getName()+"</td>";
					out += "<td colspan=\"2\">"+solution.getProduction().get(j).getQuantity()+"</td>";
					out += "</tr>\n";
				}
				out += "<tr><th align=\"left\" colspan=\"7\">Ben�tigte Zutaten:</th></tr>\n";
				out += "<tr><th>Id</th><th>Name</th><th>Lager-Menge</th><th>EK-Menge</th><th>Einheit</th><th>Kosten [&euro;]</th><th>Anteil Kosten [%]</th></tr>\n";
				for(int j=0; j< solution.getUsedIngredient().size(); j++){
					out += "<tr>";
					int zutatId = solution.getUsedIngredient().get(j).getIngredientId();
					out += "<td>"+zutatId+"</td>";
					out += "<td>"+this.modelData.getIngredients().getIngredient().get(zutatId).getName()+"</td>";
					out += "<td>"+solution.getUsedIngredient().get(j).getAmountStock()+"</td>";
					out += "<td>"+solution.getUsedIngredient().get(j).getAmountBuyed()+"</td>";
					out += "<td>"+this.modelData.getIngredients().getIngredient().get(zutatId).getUnit()+"</td>";
					out += "<td>"+solution.getUsedIngredient().get(j).getCostBuyed()+"</td>";
					out += "<td>"+solution.getUsedIngredient().get(j).getPercentOfCostBuyed()+"</td>";
					out += "</tr>\n";
				}
			}
		}
		out += "</table>\n";
		return out;
	}
	public void resetSolution(){
		this.modelData.getSolution().clear();
		this.modelData.setSolved(false);
		this.modelData.setSolverStatus("unsolved");
	}
	public void save(){
		this.getModel().printDoc();
	}
	public void solve(){

		ObjectFactory factory = new ObjectFactory();

		boolean ok;
		Cmpl m;
		CmplSet prod, ingr;
		CmplParameter sales_price, purchase_price, stock, v, capital, lwbProd, upbProd;
		String[] prodVal 			= new String[this.getNrRezepte()];
		double[] sales_priceVal 	= new double[this.getNrRezepte()];
		double[] lwbProdVal 		= new double[this.getNrRezepte()];
		double[] upbProdVal 		= new double[this.getNrRezepte()];
		for(int i=0; i<this.getNrRezepte(); i++){
			prodVal[i]			= this.modelData.getRecipes().getRecipe().get(i).getName();
			prodVal[i]			= prodVal[i].trim().replace(' ', '_');
			sales_priceVal[i]	= this.modelData.getRecipes().getRecipe().get(i).getSalesPrice();
			lwbProdVal[i]		= this.modelData.getRecipes().getRecipe().get(i).getProductionLwb();
			if(lwbProdVal[i] == Double.NEGATIVE_INFINITY) lwbProdVal[i] = -1000000.0;
			upbProdVal[i]		= this.modelData.getRecipes().getRecipe().get(i).getProductionUpb();
			if(upbProdVal[i] == Double.POSITIVE_INFINITY) upbProdVal[i] = 1000000.0;
		}

		String[] ingrVal 			= new String[this.getNrZutaten()];
		double[] purchase_priceVal 	= new double[this.getNrZutaten()];
		double[] stockVal 			= new double[this.getNrZutaten()];
		for(int j=0; j<this.getNrZutaten(); j++){
			ingrVal[j]				= this.modelData.getIngredients().getIngredient().get(j).getName();
			ingrVal[j]				= ingrVal[j].trim().replace(' ', '_');
			purchase_priceVal[j]	= this.modelData.getIngredients().getIngredient().get(j).getPrice();
			stockVal[j]				= this.modelData.getIngredients().getIngredient().get(j).getStock();
		}
		double[][] vVal 			= new double[this.getNrRezepte()][this.getNrZutaten()];
		for(int i=0; i<this.getNrRezepte(); i++){
			for(int j=0; j<this.getNrZutaten(); j++){
				vVal[i][j]	= 0.0;
			}
			for(int l=0; l<this.modelData.getRecipes().getRecipe().get(i).getRecipeIngredient().size(); l++){
				int j 		= this.modelData.getRecipes().getRecipe().get(i).getRecipeIngredient().get(l).getIngredientId();
				vVal[i][j]	= this.modelData.getRecipes().getRecipe().get(i).getRecipeIngredient().get(l).getAmount();
			}
		}
		double capitalVal 			= this.modelData.getCapital();
		try{
			m 		= new Cmpl(ServiceBean.CmplModel);

			prod	= new CmplSet("PROD");
			prod.setValues(prodVal);

			ingr	= new CmplSet("INGR");
			ingr.setValues(ingrVal);

			sales_price 	= new CmplParameter("sales_price", prod);
			sales_price.setValues(sales_priceVal);

			purchase_price 	= new CmplParameter("purchase_price", ingr);
			purchase_price.setValues(purchase_priceVal);

			stock 	= new CmplParameter("stock", ingr);
			stock.setValues(stockVal);

			v 	= new CmplParameter("v", prod, ingr);
			v.setValues(vVal);

			capital 	= new CmplParameter("capital");
			capital.setValues(capitalVal);

			lwbProd 	= new CmplParameter("lwbProd", prod);
			lwbProd.setValues(lwbProdVal);

			upbProd 	= new CmplParameter("upbProd", prod);
			upbProd.setValues(upbProdVal);

			m.setSets(prod, ingr);
			m.setParameters(sales_price, purchase_price, stock, v, capital, lwbProd, upbProd);


			m.connect(ServiceBean.WebService);
			m.solve();

			if (m.solverStatus()==Cmpl.SOLVER_OK && m.nrOfSolutions()>0) {
				ok = true;
				this.modelData.setSolved(ok);
				this.modelData.setSolverStatus(m.solverStatusText());
				this.modelData.getSolution().clear();

				CmplSolution cmplsolution = m.solution();
				Solution solution = factory.createSolution();
				solution.setSolved(ok);
				solution.setSolvedAt(this.getModel().getTime());
				solution.setOptimal(cmplsolution.status().equals("optimal"));
				solution.setStatus(cmplsolution.status());
				solution.setProfit(cmplsolution.value());
				CmplSolArray x 		= (CmplSolArray) m.getVarByName("x");
				CmplSolArray y 		= (CmplSolArray) m.getVarByName("y");
				CmplSolElement cash = (CmplSolElement) m.getVarByName("cash");

				solution.setUsedCapital(((Double)capital.values())-(Double)cash.activity());
				for(int j=0; j<this.getNrRezepte(); j++){
					Production production = factory.createProduction();
					production.setRecipeId(j);
					String recipeName = ((String[])prod.values())[j];
					production.setQuantity((Double)x.get(recipeName).activity());
					solution.getProduction().add(production);
				}
				for(int j=0; j<this.getNrZutaten(); j++){
					int k = this.getNrRezepte()+j;
					UsedIngredient usedIndegrient = factory.createUsedIngredient();
					usedIndegrient.setIngredientId(j);
					String ingredientName = ((String[])ingr.values())[j];
					usedIndegrient.setAmountBuyed((Double)y.get(ingredientName).activity());
					usedIndegrient.setAmountStock(stockVal[j]);
					double preis 	= this.modelData.getIngredients().getIngredient().get(j).getPrice();
					usedIndegrient.setCostBuyed(usedIndegrient.getAmountBuyed() * preis);
					double anteil = (100.0 * usedIndegrient.getCostBuyed()) / solution.getUsedCapital();
					usedIndegrient.setPercentOfCostBuyed(anteil);
					solution.getUsedIngredient().add(usedIndegrient);
				}
				this.modelData.getSolution().add(solution);
			}else{
				ok = false;
				this.modelData.setSolved(ok);
				this.modelData.setSolverStatus(m.solverStatusText());
				System.out.println("ErrorStatus: "+m.solverStatusText());
			}
		}catch(CmplException e){
			ok = false;
			System.out.println("CmplException  "+e.toString());
		}
	}
	public void addModel(String modelId, String ModelName){
		try{
			Model model = new Model(modelId);
			model.getModelData().setName(ModelName);
			Model.add(modelId, model);
		}catch(java.lang.NumberFormatException e){
			System.out.println("Fehler Modell konnte nicht angelegt werden");
		}
	}
	public void removeModel(String modelId){
		Model.remove(modelId);
	}
}
