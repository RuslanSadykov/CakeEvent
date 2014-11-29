package servlet;

import bean.ServiceBean;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * Created by Phaenir on 25.11.2014.
 */
@WebServlet(name = "Controller", urlPatterns = "/controller")
public class Controller extends HttpServlet implements Servlet {
	private static final long serialVersionUID=42L;
	private static boolean error=false;
	private static Logger logger=Logger.getLogger(String.valueOf(Controller.class));

	public Controller(){
		super();
	}
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		String path=config.getServletContext().getRealPath("/");
		PropertyConfigurator.configure(path+"log4j.properties");
		Controller.error=false;
		try {
			ServiceBean.init(path);
		} catch (Exception e){
			System.out.println("Controller.init. There are some error in servlet.");
			Controller.error=true;
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doAction(request, response);
	}

	private void doAction(HttpServletRequest request, ServletResponse response) throws IOException, ServletException {
		response.setContentType("text/plain");
		PrintWriter writer = response.getWriter();
		HttpSession session = request.getSession();
		String user=null;
		ServiceBean serviceBean;
		serviceBean=(ServiceBean)session.getAttribute("sBean");
		if (serviceBean==null){
			serviceBean=new ServiceBean();
			session.setAttribute("sBean",serviceBean);
		}
		//user=request.getParameter("userName");
		if (ServiceBean.ProduktionSyst) user=request.getRemoteUser();
		else user=request.getParameter("userName");
		String action=request.getParameter("action");
		String target=null;
		RequestDispatcher dispatcher=null;
		if (!Controller.error){
			writer.println("Error or Wrong WebService");
			writer.println("Webservice:\t"+ServiceBean.WebService);
			writer.println("CmplModel:\t"+ServiceBean.CmplModel);
			writer.println("ModelDir:\t"+ServiceBean.ModelDir);
			writer.println("ProdSyst:\t"+ServiceBean.ProduktionSyst);
		} else if (action==null){
			if (user!=null) serviceBean.setUser(user);
			target="02SM.jsp";
			dispatcher=request.getRequestDispatcher(target);
			dispatcher.forward(request,response);
		} else if(action.equals("00_Abmelden")){
			session.invalidate();
			target = "index.jsp";
			dispatcher = request.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else if(action.equals("02_selectModel")){
			target = "02SM.jsp";
			dispatcher = request.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else if(action.equals("03_showModel")){
			String modelId = request.getParameter("modelId");
			serviceBean.setModel(modelId);
			target = "03show.jsp";
			dispatcher = request.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else if(action.equals("10_solveModel")){
			String modelId = request.getParameter("modelId");
			serviceBean.setModel(modelId);
			serviceBean.resetSolution();
			serviceBean.save();
			serviceBean.solve();
			serviceBean.save();
			target = "03_show.jsp";
			dispatcher = request.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else if(action.equals("11_saveModel")){
			String modelId = request.getParameter("modelId");
			serviceBean.setModel(modelId);
			serviceBean.save();
			target = "02SM.jsp";
			dispatcher = request.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else if(action.equals("14_addModel")){
			String modelId = request.getParameter("modelId");
			String modelName = request.getParameter("modelName");
			serviceBean.addModel(modelId, modelName);
			target = "02SM.jsp";
			dispatcher = request.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else if(action.equals("15_removeModel")){
			String modelId = request.getParameter("modelId");
			serviceBean.removeModel(modelId);
			target = "02SM.jsp";
			dispatcher = request.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else{
			writer.println("Error: action= "+action);
		}
		writer.println("Hier ist Controller");
		writer.close();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doAction(request,response);
	}
	private String[] getArray(HttpServletRequest req, String name, int length){
		String[] out = new String[length];
		for(int i=0; i<length; i++){
			out[i] = req.getParameter(name+"_"+i);
		}
		return out;
	}
	private String[][] getMatrix(HttpServletRequest req, String name, int lengthRow, int lengthCol){
		String[][] out = new String[lengthRow][lengthCol];
		for(int i=0; i<lengthRow; i++){
			for(int j=0; j<lengthCol; j++){
				out[i][j] = req.getParameter(name+"_"+i+"_"+j);
			}
		}
		return out;
	}
}
