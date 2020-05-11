package com.clientui.clientui.controller;

import com.clientui.clientui.beans.CommandeBean;
import com.clientui.clientui.beans.PaiementBean;
import com.clientui.clientui.beans.ProductBean;
import com.clientui.clientui.proxies.MicroserviceCommandeProxy;
import com.clientui.clientui.proxies.MicroservicePaiementProxy;
import com.clientui.clientui.proxies.MicroserviceProduitsProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class ClientController {

    @Autowired
    MicroserviceProduitsProxy mProduitsProxy;

    @Autowired
    MicroservicePaiementProxy mPaiementProxy;

    @Autowired
    MicroserviceCommandeProxy mCommandeProxy;

    @RequestMapping("/")
    public String accueil(Model model) {

        List<ProductBean> produits = mProduitsProxy.listeDesProduits();

        model.addAttribute("produits", produits);

        return "Accueil";
    }

    @RequestMapping("/details-produit/{id}")
    public String ficheProduit(@PathVariable int id, Model model){

        ProductBean produit = mProduitsProxy.recupererUnProduit(id);

        model.addAttribute("produit", produit);

        return "FicheProduit";
    }

    @RequestMapping("/commander-produit/{produitId}/{montant}")
    public String commanderProduit(@PathVariable int produitId, @PathVariable Double montant, Model model){

        CommandeBean commande = new CommandeBean();
        commande.setProductId(produitId);
        commande.setDateCommande(new Date());
        commande.setQuantite(1);

        CommandeBean commandeAjoutee = mCommandeProxy.ajouterCommande(commande);

        model.addAttribute("commande", commandeAjoutee);
        model.addAttribute("montant", montant);

        return "Paiement";
    }

    @RequestMapping(value = "/payer-commande/{idCommande}/{montantCommande}")
    public String payerCommande(@PathVariable int idCommande, @PathVariable Double montantCommande, Model model){

        PaiementBean paiementAExcecuter = new PaiementBean();

        paiementAExcecuter.setIdCommande(idCommande);
        paiementAExcecuter.setMontant(montantCommande);
        paiementAExcecuter.setNumeroCarte(numcarte());

        ResponseEntity<PaiementBean> paiement = mPaiementProxy.payerUneCommande(paiementAExcecuter);

        Boolean paiementAccepte = Boolean.FALSE;
        if(paiement.getStatusCode() == HttpStatus.CREATED)
            paiementAccepte = Boolean.TRUE;

        model.addAttribute("paiementOk", paiementAccepte);

        return "Confirmation";
    }

    private Long numcarte() {

        return ThreadLocalRandom.current().nextLong(1000000000000000L,9000000000000000L );
    }
}
