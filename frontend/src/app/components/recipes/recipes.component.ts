import { Component, OnInit } from '@angular/core';
import { RecipeService } from '../../services/recipe.service'

import { Recipe } from '../../models/Recipe';

@Component({
  selector: 'app-recipes',
  templateUrl: './recipes.component.html',
  styleUrls: ['./recipes.component.css']
})
export class RecipesComponent implements OnInit {
  recipes:Recipe[];

  constructor(private recipeService:RecipeService) { }

  ngOnInit(): void {
    this.recipeService.getRandomRecipes().subscribe(json => {     
      this.recipes = json.recipes
    });
  }

}
