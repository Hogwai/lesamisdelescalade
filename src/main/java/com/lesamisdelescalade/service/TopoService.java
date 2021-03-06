package com.lesamisdelescalade.service;

import java.util.List;

import com.lesamisdelescalade.model.Topo;
import com.lesamisdelescalade.model.Utilisateur;

public interface TopoService {

	List<Topo> getToposByProprietaire(Utilisateur user);

	List<Topo> getAllTopoInfos();

	Topo getById(Integer topoId);

	void updateTopo(Integer topoId, Integer topoTag);

	void addTopo(String nom, String description, Integer topoTag, Utilisateur currentUser, Integer siteId);

	List<Topo> getToposByEmprunteurStatut(Utilisateur user, Integer statut);

	void updateTopoEmprunteurStatut(Utilisateur user, Integer topoId, Integer statutTopoTag);

	List<Topo> getBookableTopos(Utilisateur user);

	List<Topo> getToposByProprietaireStatut(Utilisateur user, Integer statut);

	List<Topo> getBookedToposByProprietaire(Utilisateur user);

}
