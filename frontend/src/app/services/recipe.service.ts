import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from  '@angular/common/http';
import { Observable } from 'rxjs';

import { Recipe } from  '../models/Recipe';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {
  randomRecipesUrl:string = 'http://localhost:8080/recipes/random';

  constructor(private http:HttpClient) { }

  getRandomRecipes():Observable<Recipe[]> {
    return this.http.get<Recipe[]>(this.randomRecipesUrl);
  }
}
