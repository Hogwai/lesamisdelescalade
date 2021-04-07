package com.lesamisdelescalade.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lesamisdelescalade.enums.StatutTopoConsts;
import com.lesamisdelescalade.enums.TopoConsts;
import com.lesamisdelescalade.enums.UserInfoConsts;
import com.lesamisdelescalade.model.Topo;
import com.lesamisdelescalade.model.Utilisateur;
import com.lesamisdelescalade.service.SiteService;
import com.lesamisdelescalade.service.TopoService;

/**
 * Servlet implementation class ListeToposController
 */
@Component
@WebServlet("/listetopos")
public class ListeToposController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static final Logger LOGGER = LogManager.getLogger(ListeToposController.class);
	
	private static final String PARSE_ERROR = "Error occurred during string parsing: %s";
       
	@SuppressWarnings("unused")
	private static SiteService siteService;
	
	
	@SuppressWarnings("unused")
	private static TopoService topoService;
	
	
	public ListeToposController() {}

	@SuppressWarnings("static-access")
	@Autowired
    public ListeToposController(SiteService siteService, TopoService topoService) {
        super();
        this.siteService = siteService;
        this.topoService = topoService;
    }
    
    @SuppressWarnings("static-access")
	@Autowired
    public void setSiteService(SiteService siteService) {
    	this.siteService = siteService;
    }
    
    
    @SuppressWarnings("static-access")
	@Autowired
    public void setTopoService(TopoService topoService) {
    	this.topoService = topoService;
    }

    private void setListeToposRequest(HttpServletRequest request, Utilisateur currentUser) {
		List<Topo> toposEnAttente = topoService.getToposByEmprunteurStatut(currentUser,
				StatutTopoConsts.EN_ATTENTE);
		List<Topo> toposDisponibles = topoService.getBookableTopos(currentUser);
		List<Topo> toposReserves = topoService.getToposByEmprunteurStatut(currentUser,
				StatutTopoConsts.INDISPONIBLE);

		request.setAttribute(TopoConsts.TOPOS_EN_ATTENTE, toposEnAttente);
		request.setAttribute(TopoConsts.TOPOS_DISPONIBLES, toposDisponibles);
		request.setAttribute(TopoConsts.TOPOS_RESERVES, toposReserves);
    }
    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utilisateur currentUser = (Utilisateur) request.getSession().getAttribute(UserInfoConsts.UTILISATEUR);
		
		if (request.getParameter(TopoConsts.MODIFY) != null) {
			this.bookTopo(request, currentUser);
			this.setListeToposRequest(request, currentUser);
			this.redirectListeToposPage(request, response);
		} else if (request.getParameter(TopoConsts.CANCEL) != null) {
			this.cancelTopoBooking(request);
			this.setListeToposRequest(request, currentUser);
			this.redirectListeToposPage(request, response);
		} else {
			this.setListeToposRequest(request, currentUser);
			this.dispatchListeToposPage(request, response);
		}
	}
    
    
    /**
     * 
     * @param request
     * @param currentUser
     */
    public void cancelTopoBooking(HttpServletRequest request) {
    	Integer topoId = getTopoFromReq(request, TopoConsts.CANCEL);
    	topoService.updateTopoEmprunteurStatut(null, topoId, StatutTopoConsts.DISPONIBLE);
    }
    
    /**
     * 
     * @param request
     * @param currentUser
     */
	public void bookTopo(HttpServletRequest request, Utilisateur currentUser) {
    	Integer topoId = getTopoFromReq(request, TopoConsts.MODIFY);
		topoService.updateTopoEmprunteurStatut(currentUser, topoId, StatutTopoConsts.EN_ATTENTE);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 */
	private void dispatchListeToposPage(HttpServletRequest request, HttpServletResponse response) {
		try {
            this.getServletContext().getRequestDispatcher("/jsp/listeTopos.jsp")
                    .forward(request, response);
        } catch (ServletException | IOException e){
            LOGGER.error(String.format("Error occurred: %s", e.toString()));
        }
	}
	/**
	 * 
	 * @param request
	 * @param response
	 */
	private void redirectListeToposPage(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect(request.getContextPath() + "/listetopos");
		} catch (IOException e){
            LOGGER.error(String.format("Error occurred: %s", e.toString()));
        }
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	private Integer getTopoFromReq(HttpServletRequest request, String parameter) {
		Integer topoId = null;
    	try {
    		topoId = Integer.valueOf(request.getParameter(parameter));
		} catch (NumberFormatException e) {
			 LOGGER.error(String.format(PARSE_ERROR, e.toString()));
		}
    	return topoId;
	}
}
