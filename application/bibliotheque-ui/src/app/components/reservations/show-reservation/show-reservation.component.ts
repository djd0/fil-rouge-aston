import { Component, OnInit, Inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

import {Reservation} from '../../../model/Reservation';
import {ReservationService} from '../../../services/reservation.service';

@Component({
  selector: 'app-show-reservation',
  templateUrl: './show-reservation.component.html',
  styleUrls: ['./show-reservation.component.css'],
})
export class ShowReservationComponent implements OnInit {

  displayedColumnsLivres: string[] = ['reference', 'auteur', 'titre', 'genre'];

  constructor(private router: Router,
              private reservationService: ReservationService,
              public dialogRef: MatDialogRef<ShowReservationComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit(): void {

  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
