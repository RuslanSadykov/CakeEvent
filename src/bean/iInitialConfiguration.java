package bean;

/**
 * Created by Phaenir on 29.11.2014.
 */
public interface iInitialConfiguration {
	/* in this interface we placed all static constants
	 * which used in projects in bean, model and servlet packages
	 */
	static final String MODEL_DIR="I:\\MyProjects\\JetBrains\\Java\\MyCake\\TestDir\\";
	static final String WEBSERVICE="http://localhost:8080/";
	static final String CMPL_MODEL="I:\\MyProjects\\JetBrains\\Java\\MyCake\\cmpl\\CakeEvent.cmpl";
	static final boolean PRODUCTION_SYS=false;
	static final String LOG_PROPERTY_FILENAME="log4j.properties";
}
