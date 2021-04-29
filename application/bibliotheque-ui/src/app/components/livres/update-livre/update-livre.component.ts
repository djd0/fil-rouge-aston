import { Component, OnInit, Inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { Livre } from '../../../model/Livre';
import { LivreService } from '../../../services/livre.service';


@Component({
  selector: 'app-update-livre',
  templateUrl: './update-livre.component.html',
  styleUrls: ['./update-livre.component.css'],

})
export class UpdateLivreComponent implements OnInit {

  constructor(private livreService: LivreService,
              private router: Router,
              private dialogRef: MatDialogRef<UpdateLivreComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit(): void {

  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  updateLivre(livre: Livre): any {
    this.livreService.updateLivre(livre).subscribe();
    this.onNoClick();
  }
}
