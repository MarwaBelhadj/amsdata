package com.sip.ams.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sip.ams.entities.Article;
import com.sip.ams.entities.Provider;
import com.sip.ams.repositories.ArticleRepository;
import com.sip.ams.repositories.ProviderRepository;

@Controller
@RequestMapping("/article/")
public class ArticleController {
	
	
	private ArticleRepository articleRepository ;
	private ProviderRepository providerRepository;
	
	@Autowired
	public ArticleController(ArticleRepository articleRepository, ProviderRepository providerRepository) {
		this.articleRepository = articleRepository;
		this.providerRepository = providerRepository;
	}

	public ArticleController() {
		// TODO Auto-generated constructor stub
	}
	
	@GetMapping("/list")
	public String showListArticles( Model model) {
		List<Article> la = (List<Article>) articleRepository.findAll();
		if (la.size()==0) la=null;
		model.addAttribute("articles",la );
		return"article/listArticles";		
	}
	//C'est une méthode pour afficher le formulaire vide 
		@GetMapping("/add")
		public String showAddArticleForm(Model model) {
			model.addAttribute("providers", providerRepository.findAll());
			model.addAttribute("article", new Article());
			return("article/addArticle");
			
		}
		//C'est la méthode pour remplir les champs du formulaire
		@PostMapping("/add")
		public String addProvider(@Valid Article article, BindingResult result, @RequestParam(name="providerId", required=false) long p) {
			Provider provider = providerRepository.findById(p).orElseThrow(()-> new IllegalArgumentException("Invalid article Id :" +p));
			if (result.hasErrors()) {
				return"article/addArticle";
			}
			article.setProvider(provider);
			articleRepository.save(article);
			return "redirect:list"; //c'est pour revenir à la liste 
		}
		
		@GetMapping("/delete/{id}")
		public String deleteArticle(@PathVariable("id") long id, Model model) {
			Article article = articleRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid article Id :" +id));
			articleRepository.delete(article);
			model.addAttribute("article", articleRepository.findAll()); //Est ce qu'on peut l'enlever????
			return"redirect:../list"; 
		}
		
		//C'est la méthode qui charge un article par son ID dans le model et le ramène vers la vue du formulaire pour faire la modification
		@GetMapping("/edit/{id}")
		public String editArticle(@PathVariable("id") long id, Model model) {
			Article article = articleRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid article Id :" +id));
			model.addAttribute("article", article);
			model.addAttribute("providers", providerRepository.findAll()); // Pourquoi???
			model.addAttribute("idProvider", article.getProvider().getId()); 
			return"article/updateArticle";
		}
		
		
		//C'est la méthode qui reçoit une instance ou un objet d'article et le modifie dans le formulaire
		@PostMapping("/edit/{id}")
		//Est ce que l'ordre des paramètres est bien  definit ou bien on peut les mettre comme on veut????
		public String updateArticle(@PathVariable("id") long id, @Valid Article article, Model model, BindingResult result, @RequestParam(name="providerId", required=false) long p) {
			Provider provider = providerRepository.findById(p).orElseThrow(()-> new IllegalArgumentException("Invalid article Id :" +p));
			if (result.hasErrors()) {
				article.setId(id);
				return"article/addArticle";
			}
			article.setProvider(provider);
			articleRepository.save(article);
			model.addAttribute("articles", articleRepository.findAll());
			 //c'est pour revenir à la liste 
			return "article/listArticles"; 
		}

}
