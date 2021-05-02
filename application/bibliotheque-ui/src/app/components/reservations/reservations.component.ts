import { Component, ViewChild, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';

import { merge, Observable, of as observableOf } from 'rxjs';
import { catchError, map, startWith, switchMap } from 'rxjs/operators';

import { ShowReservationComponent } from './show-reservation/show-reservation.component';

import { Reservation } from '../../model/Reservation';
import { ReservationService } from '../../services/reservation.service';

interface Filtre {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-reservations',
  templateUrl: './reservations.component.html',
  styleUrls: ['./reservations.component.css']
})
export class ReservationsComponent implements OnInit {

  isLoadingResults = true;
  isRateLimitReached = false;
  resultsLength = 5;
  currentPage = 0;
  selectedValue: string;
  filterValue: string;

  filtres: Filtre[] = [
    {value: 'reference', viewValue: 'reference'},
    {value: 'nom', viewValue: 'nom'}
  ];

  displayedColumnsReservations: string[] = ['reference', 'name', 'prenom', 'telephone', 'email', 'dateReservation', 'details', 'supprimer'];

  @ViewChild(MatPaginator) paginator: MatPaginator;

  reservations: Observable<Reservation[]>;

  constructor(private reservationService: ReservationService,
              private router: Router,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
     this.reservations = this.getReservations(0, 10);
  }

  resetPaging(): void {
    this.paginator.pageIndex = 0;
  }

  getReservations(pageIndex: number, pageSize: number): Observable<Reservation[]> {
     this.isLoadingResults = true;
     return this.reservationService.getReservations(pageIndex, pageSize)
              .pipe(map(data => {
                  this.isLoadingResults = false;
                  this.resultsLength = data.totalItems;
                  this.currentPage = data.currentPage;
                  return data.reservations;
     }));
  }

  getPaginatorReservationsData(event: PageEvent): any {
    this.getReservationsEvent(event.pageIndex, event.pageSize);
  }

  getReservationsEvent(pageIndex: number, pageSize: number): any {

      this.isLoadingResults = true;

      this.reservations = this.paginator.page
        .pipe(
                  startWith({}),
                  switchMap(() => {
                    return this.reservationService.getReservations(pageIndex, pageSize);
                  }),
                  map(data => {
                    // Flip flag to show that loading has finished.
                    this.isLoadingResults = false;
                    this.isRateLimitReached = false;
                    this.resultsLength = data.totalItems;
                    this.currentPage = data.currentPage;
                    return data.reservations;
                  }),
                  catchError(() => {
                    this.isLoadingResults = false;
                    this.isRateLimitReached = true;
                    return observableOf([]);
                  })
                );
  }

  supprimerReservation(reservation: Reservation): any {
    if (confirm('Etes-vous sûr de vouloir supprimer les reservations selectionnées?')) {
        this.reservationService.supprimerReservation(reservation.reference)
            .subscribe(
                () => {
                    this.reservations = this.getReservations(this.paginator.pageIndex, this.paginator.pageSize);
            });
    }
  }

  showReservation(reservation: any): void {
    const dialogRef = this.dialog.open(ShowReservationComponent, {
      width: '35%',
      data: {reservation}
    });

    dialogRef.afterClosed()
        .subscribe();
  }

  applyFilter() {
    this.reservations = this.reservationService.getReservationsWithFilter(this.paginator.pageIndex, this.paginator.pageSize, this.selectedValue, this.filterValue)
        .pipe(map(data => {
                           this.isLoadingResults = false;
                           this.resultsLength = data.totalItems;
                           this.currentPage = data.currentPage;
                           return data.reservations;
    }));
  }
}
