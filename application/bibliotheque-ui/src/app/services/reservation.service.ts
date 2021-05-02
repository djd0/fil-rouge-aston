import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { DatePipe } from '@angular/common';

import { environment } from '../../environments/environment';
import { Reservation } from '../model/Reservation';
import { ReservationsPage } from '../model/ReservationsPage';

const httpOptions = {
  headers: new HttpHeaders( {'Content-Type': 'application/json'} )
};

@Injectable({
  providedIn: 'root'
})

export class ReservationService {

  apiURL: string = environment.apiUrl + '/reservations';
  reservations: Reservation[];

  constructor(private http: HttpClient) {
  }

  handleError(error: HttpErrorResponse): any {
    let errorMessage = 'Unknown error!';
    if (error.error instanceof ErrorEvent) {
      // Client-side errors
      errorMessage = `Erreur: ${error.error.message}`;
    } else {
      // Server-side errors
      errorMessage = `Erreur: ${error.error.message}`;
    }
    window.alert(errorMessage);
    return throwError(errorMessage);
  }

  getReservations(page: number, size: number): Observable<ReservationsPage> {
    const href = `${this.apiURL}`;
    const requestUrl = `${href}?page=${page}&size=${size}`;
    return this.http.get<ReservationsPage>(requestUrl);
  }

  ajouterReservation(reservation: Reservation): Observable<Reservation> {
    return this.http.post<Reservation>(this.apiURL, reservation, httpOptions);
  }

  supprimerReservation(ref: number): any {
    const url = `${this.apiURL}/${ref}`;
    return this.http.delete(url, httpOptions);
  }

  consulterReservation(reference: number): Observable<Reservation> {
      const url = `${this.apiURL}/${reference}`;
      return this.http.get<Reservation>(url);
  }

  updateReservation(reservation: Reservation): Observable<Reservation> {
    return this.http.put<Reservation>(this.apiURL + '/' + reservation.reference, reservation, httpOptions);
  }

  getUrgentReservations(page: number, size: number): Observable<ReservationsPage> {
    const href = `${this.apiURL}/daily`;
    const requestUrl = `${href}?page=${page}&size=${size}`;
    return this.http.get<ReservationsPage>(requestUrl);
  }

  preparationReservation(reference: number): any {
    const href = `${this.apiURL}/prepare/${reference}`;
    return this.http.patch(href, httpOptions).pipe(catchError(this.handleError));
  }

  validateReservation(reference: number): any {
    const href = `${this.apiURL}/validate/${reference}`;
    const pipe = new DatePipe('en-US');
    const now = Date.now();
    const mySimpleFormat = pipe.transform(now, 'dd/MM/yyyy');

    return this.http.patch(href, mySimpleFormat, httpOptions).pipe(catchError(this.handleError));
  }

  getReservationsWithFilter(page: number, size: number, selectedValue: string, filterValue: string): Observable<ReservationsPage> {
    const href = `${this.apiURL}/search`;
    const requestUrl = `${href}?filter=${selectedValue}&value=${filterValue}&page=${page}&size=${size}`;
    return this.http.get<ReservationsPage>(requestUrl);
  }
}
