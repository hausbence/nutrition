import { Component, OnInit } from '@angular/core';
import { Food } from '../../models/Food';

@Component({
  selector: 'app-foods',
  templateUrl: './foods.component.html',
  styleUrls: ['./foods.component.css']
})
export class FoodsComponent implements OnInit {
  foods:Food[];

  constructor() { }

  ngOnInit(): void {
    this.foods = [
      {
        id: 1,
        name: "palacsinta",
        calories: 5000,
      },
      {
        id: 2,
        name: "pörkölt",
        calories: 50000,
      },
      {
        id: 3,
        name: "töltött paprika",
        calories: 1000,
      },
    ]
  }

}
