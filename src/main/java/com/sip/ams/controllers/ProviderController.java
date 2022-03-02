package com.sip.ams.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sip.ams.entities.Article;
import com.sip.ams.entities.Provider;
import com.sip.ams.repositories.*;

@Controller
@RequestMapping("/provider")
public class ProviderController {

	private final ProviderRepository providerRepository;

	@Autowired // C'est une annotation pour faire l'injection de dépendance provider.Repository
	public ProviderController(ProviderRepository providerRepository) {

		this.providerRepository = providerRepository;
	}

	@GetMapping("/list") // ce que je tape dans l'url
	public String listProviders(Model model) {
		List<Provider> lp = (List<Provider>) providerRepository.findAll();
		if (lp.size() == 0)
			lp = null;
		model.addAttribute("providers", lp);
		return "provider/listProviders";// retourne la vue de listproviders.html
	}

	// C'est une méthode pour afficher le formulaire vide
	@GetMapping("/add")
	public String showAddProviderForm(Model model) {

		Provider provider = new Provider();
		model.addAttribute("provider", provider);
		return "provider/addProvider";
	}

	// C'est la méthode pour remplir les champs du formulaire
	@PostMapping("/add")
	public String addProvider(@Valid Provider provider, BindingResult result) {
		if (result.hasErrors()) {
			// return "redirect:add" ; On peut faire mais ça peut causer un conflit car on a
			// une autre "add" dans GetMapping
			return "provider/addProvider";
		}
		providerRepository.save(provider);
		return "redirect:list"; // c'est pour revenir à la liste
	}

	@GetMapping("/delete/{id}")
	public String deleteProvider(@PathVariable("id") long id, Model model) {

		Provider provider = providerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invald provider Id :" + id));
		// System.out.println("suite du programme ...");
		providerRepository.delete(provider);
		return "redirect:../list";
	}

	@GetMapping("/edit/{id}")
	public String editProvider(@PathVariable("id") long id, Model model) {
		Provider provider = providerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id :" + id));
		model.addAttribute("provider", provider);
		return "provider/updateProvider";
	}

	@PostMapping("/update")
	public String updateProvider(@Valid Provider provider, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "provider/updateProvider";
		}
		providerRepository.save(provider);
		return "redirect:..list";
	}

	@GetMapping("show/{id}")
	public String showProvider(@PathVariable("id") long id, Model model) {
		Provider provider = providerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + id));
		List<Article> articles = providerRepository.findArticlesByProvider(id);
	
		for (Article a : articles)
				System.out.println("Article = " + a.getLabel());
		
		model.addAttribute("articles", articles);
		model.addAttribute("provider", provider);
				
	 return "provider/showProvider";
	 }
}