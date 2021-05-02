import { Component, OnInit, Inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialogRef } from '@angular/material/dialog';

import { Livre } from '../../../model/Livre';
import { LivreService } from '../../../services/livre.service';


@Component({
  selector: 'app-add-livre',
  templateUrl: './add-livre.component.html',
  styleUrls: ['./add-livre.component.css'],

})
export class AddLivreComponent implements OnInit {

  livre = new Livre();

  constructor(private livreService: LivreService,
              private router: Router,
              private dialogRef: MatDialogRef<AddLivreComponent>) {
  }

  ngOnInit(): void {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  addLivre(livre: Livre): void {
    this.livreService.addLivre(livre).subscribe();
    this.onNoClick();
  }
}
