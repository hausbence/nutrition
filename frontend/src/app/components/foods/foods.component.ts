import { Component, OnInit } from '@angular/core';
import { FoodService } from '../../services/food.service'

import { Food } from '../../models/Food';

@Component({
  selector: 'app-foods',
  templateUrl: './foods.component.html',
  styleUrls: ['./foods.component.css']
})
export class FoodsComponent implements OnInit {
  foods:Food[];

  constructor(private foodService:FoodService) { }

  ngOnInit(): void {
    this.foodService.getRandomFoods();
  }

}
