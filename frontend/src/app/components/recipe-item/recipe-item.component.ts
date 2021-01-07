import { Component, OnInit, Input } from '@angular/core';
import { RecipeService } from '../../services/recipe.service'


import { Recipe } from 'src/app/models/Recipe'

@Component({
  selector: 'app-recipe-item',
  templateUrl: './recipe-item.component.html',
  styleUrls: ['./recipe-item.component.css']
})
export class RecipeItemComponent implements OnInit {
  @Input() recipe: Recipe;

  constructor() { }

  ngOnInit(): void {
  }

}
