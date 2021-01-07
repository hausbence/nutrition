import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from  '@angular/common/http';
import { Observable } from 'rxjs';

import { Food } from  '../models/Food';

@Injectable({
  providedIn: 'root'
})
export class FoodService {
  randomFoodsUrl:string = 'http://localhost:8080/recipes/random';

  constructor(private http:HttpClient) { }

  getRandomFoods():void {
    console.log(this.http.get<Food[]>(this.randomFoodsUrl).subscribe);
    ;
  }
}
