package data;

import xmlData.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.text.DateFormat;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.logging.Logger;

/**
 * Created by Phaenir on 25.11.2014.
 */
public class Model {
	private static String basicConfig = null;
	static Hashtable<String, Model> model = null;
	private CakeEvent modelData = null;
	static Logger logger=Logger.getLogger(String.valueOf(Model.class));

	public static void setBasicConfig(String config) {
		Model.basicConfig = config;
	}

	public static void init() {
		Model.model = new Hashtable<String, Model>();
		File basic = new File(Model.basicConfig);
		if (basic.isDirectory()) {
			String[] mo = basic.list();
			File mo_file;
			if (mo != null) {
				for (int i = 0; i < mo.length; i++) {
					mo_file = new File(basic.getAbsolutePath() + "/" + mo[i]);
					if (mo_file.isFile()) {
						Model m = new Model(mo_file);
						if (m == null) System.out.println("Model.init. Error: " + mo_file.getAbsolutePath());
						Model.model.put(m.getId(), m);
					}
				}
			}
		}
	}

	public String getId() {
		return this.modelData.getId();
	}

	public Model(File modelFile) {
		this.datei = modelFile.getName();
		this.modelData = null;
		if (modelFile.isFile()) {
			try {
				this.modelData = this.unmarshalXml(new FileInputStream(modelFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private CakeEvent unmarshalXml(InputStream xmlStream) {
		CakeEvent model = null;
		try {
			Unmarshaller u = Model.jaxbContext.createUnmarshaller();
			Object paraXML = u.unmarshal(xmlStream);
			model = (CakeEvent) paraXML;
		} catch (JAXBException je) {
		}
		return model;
	}

	static JAXBContext jaxbContext;

	static {
		try {
			jaxbContext = JAXBContext.newInstance("xmlData");
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String datei = null;

	private boolean marshalXml(OutputStream xmlStream, CakeEvent model) {
		boolean out = true;
		Marshaller m;
		try {
			m = Model.jaxbContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "/Schema/CakeEvent.xsdd");
			m.marshal(model, xmlStream);
		} catch (JAXBException e) {
			out = false;
		}
		return out;
	}

	public String getTime() {
		GregorianCalendar cal = new GregorianCalendar();
		DateFormat tag = DateFormat.getDateInstance(DateFormat.MEDIUM);
		DateFormat time = DateFormat.getTimeInstance(DateFormat.MEDIUM);
		return tag.format(cal.getTime()) + "  " + time.format(cal.getTime());
	}

	/**
	 * Konstruktor zum Anlegen eines neuen Modells.
	 *
	 * @param mid ModellId
	 */
	public Model(String mid) {
		this.datei = mid + ".xml";
		ObjectFactory factory = new ObjectFactory();
		Ingredient ingredient;
		Recipe recipe;
		RecipeIngredient recipeIngredient;

		this.modelData = factory.createCakeEvent();
		this.modelData.setId(mid);
		this.modelData.setName(mid);
		this.modelData.setCreatedAt(this.getTime());
		this.modelData.setAutor("Christian M�ller");
		this.modelData.setDescription("OR Kuchen backen Aufgabe");
		this.modelData.setCapital(400.0);
		this.modelData.setSolved(false);
		this.modelData.setSolverStatus("unsolved");
		this.modelData.setIngredients(factory.createIngredients());
		this.modelData.setRecipes(factory.createRecipes());

		ingredient = factory.createIngredient();
		ingredient.setId(0);
		ingredient.setName("Mehl");
		ingredient.setUnit(Units.KG);
		ingredient.setStock(600.0);
		ingredient.setPrice(1.5);
		this.modelData.getIngredients().getIngredient().add(ingredient);
		ingredient = factory.createIngredient();
		ingredient.setId(1);
		ingredient.setName("Zucker");
		ingredient.setUnit(Units.KG);
		ingredient.setStock(250.0);
		ingredient.setPrice(2.5);
		this.modelData.getIngredients().getIngredient().add(ingredient);
		ingredient = factory.createIngredient();
		ingredient.setId(2);
		ingredient.setName("Milch");
		ingredient.setUnit(Units.LITER);
		ingredient.setStock(300.0);
		ingredient.setPrice(1.2);
		this.modelData.getIngredients().getIngredient().add(ingredient);
		ingredient = factory.createIngredient();
		ingredient.setId(3);
		ingredient.setName("Eier");
		ingredient.setUnit(Units.STCK);
		ingredient.setStock(2400.0);
		ingredient.setPrice(0.2);
		this.modelData.getIngredients().getIngredient().add(ingredient);


		recipe = factory.createRecipe();
		recipe.setId(0);
		recipe.setName("Kuchen 1");
		recipe.setDescription("Beschreibung des Kuchen 1");
		recipe.setInstructions("Anleitung zum Erstellen eines Kuchen 1");
		recipe.setSalesPrice(6.0);
		recipe.setProductionLwb(0.0);
		recipe.setProductionUpb(Double.POSITIVE_INFINITY);
		recipeIngredient = factory.createRecipeIngredient();
		recipeIngredient.setIngredientId(0);
		recipeIngredient.setAmount(2.0);
		recipe.getRecipeIngredient().add(recipeIngredient);
		recipeIngredient = factory.createRecipeIngredient();
		recipeIngredient.setIngredientId(1);
		recipeIngredient.setAmount(0.5);
		recipe.getRecipeIngredient().add(recipeIngredient);
		recipeIngredient = factory.createRecipeIngredient();
		recipeIngredient.setIngredientId(2);
		recipeIngredient.setAmount(1.0);
		recipe.getRecipeIngredient().add(recipeIngredient);
		recipeIngredient = factory.createRecipeIngredient();
		recipeIngredient.setIngredientId(3);
		recipeIngredient.setAmount(6.0);
		recipe.getRecipeIngredient().add(recipeIngredient);
		this.modelData.getRecipes().getRecipe().add(recipe);

		recipe = factory.createRecipe();
		recipe.setId(1);
		recipe.setName("Kuchen 2");
		recipe.setDescription("Beschreibung des Kuchen 2");
		recipe.setInstructions("Anleitung zum Erstellen eines Kuchen 2");
		recipe.setSalesPrice(8.0);
		recipe.setProductionLwb(0.0);
		recipe.setProductionUpb(Double.POSITIVE_INFINITY);
		recipeIngredient = factory.createRecipeIngredient();
		recipeIngredient.setIngredientId(0);
		recipeIngredient.setAmount(1.0);
		recipe.getRecipeIngredient().add(recipeIngredient);
		recipeIngredient = factory.createRecipeIngredient();
		recipeIngredient.setIngredientId(1);
		recipeIngredient.setAmount(1.0);
		recipe.getRecipeIngredient().add(recipeIngredient);
		recipeIngredient = factory.createRecipeIngredient();
		recipeIngredient.setIngredientId(2);
		recipeIngredient.setAmount(0.5);
		recipe.getRecipeIngredient().add(recipeIngredient);
		recipeIngredient = factory.createRecipeIngredient();
		recipeIngredient.setIngredientId(3);
		recipeIngredient.setAmount(9.0);
		recipe.getRecipeIngredient().add(recipeIngredient);
		this.modelData.getRecipes().getRecipe().add(recipe);
	}

	public static Model get(String modelId) {
		return Model.model.get(modelId);
	}
	public static String[] getModelIds(){
		int l = Model.model.size();
		String[] out	= new String[l];
		Enumeration<String> e = Model.model.keys();
		int i=0;
		while(e.hasMoreElements()){
			out[i++] = e.nextElement();
		}
		return out;
	}
	public CakeEvent getModelData(){
		return this.modelData;
	}
	public static Hashtable<String, Model> getModelle(){
		return Model.model;
	}
	public static void add(String modelId, Model model){
		if(! Model.model.containsKey(modelId)){
			Model.model.put(modelId, model);
			model.printDoc();
		}
	}
	public static void remove(String modelId){
		if( Model.model.containsKey(modelId)){
			Model model = Model.model.get(modelId);
			Model.model.remove(modelId);
			File f = new File(model.getDirectory()+model.datei);
			f.delete();
		}
	}
	public static String info(){
		String out = "";
		for(Enumeration e = Model.model.keys(); e.hasMoreElements(); ){
			Model model = Model.model.get(e.nextElement());
			out += model.modelData.getId()+"  "+model.modelData.getName();
			out += "\n";
		}
		return out;
	}
	public String getDirectory(){
		String out = Model.basicConfig+this.datei;
		return out.substring(0, out.lastIndexOf('/')+1);
	}
	public boolean printDoc(){
		boolean out = false;
		//System.out.println("Schreibe Problem auf: "+Model.basisPfad+this.datei);
		File file = new File(Model.basicConfig+this.datei);
		FileOutputStream f;
		try {
			f 	= 	new FileOutputStream(file);
			out =	this.marshalXml(f , modelData);
			f.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return out;
	}
}
