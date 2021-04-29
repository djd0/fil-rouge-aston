import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavBarComponent implements OnInit {

  public selectedVal: string;

  constructor() {

  }

  ngOnInit(): void {

    const uri: string = window.location.href.split('/').pop();

    if (uri === '') {
        this.selectedVal = 'accueil';
    }
    if (uri === 'livres') {
        this.selectedVal = 'livres';
    }
    if (uri === 'reservations') {
        this.selectedVal = 'reservations';
    }
  }

  public onValChange(val: string): void {
    this.selectedVal = val;
  }
}
