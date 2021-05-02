import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators';

import { environment } from '../../environments/environment';
import { Livre } from '../model/Livre';
import { LivresPage } from '../model/LivresPage';

const httpOptions = {
  headers: new HttpHeaders( {'Content-Type': 'application/json'} )
};

@Injectable({
  providedIn: 'root'
})

export class LivreService {

  apiURL: string = environment.apiUrl + '/livres';
  livres: Livre[];

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

  getLivres(page: number, size: number): Observable<LivresPage> {
    const href = `${this.apiURL}`;
    const requestUrl = `${href}?page=${page}&size=${size}`;
    return this.http.get<LivresPage>(requestUrl);
  }

  addLivre(livre: Livre): any {
    return this.http.post<Livre>(this.apiURL, livre, httpOptions).pipe(catchError(this.handleError));
  }

  deleteLivre(reference: number): any {
    const url = `${this.apiURL}/${reference}`;
    return this.http.delete(url, httpOptions).pipe(catchError(this.handleError));
  }

  getLivre(reference: number): any {
    const url = `${this.apiURL}/${reference}`;
    return this.http.get<Livre>(url).pipe(catchError(this.handleError));
  }

  updateLivre(livre: Livre): any {
    return this.http.put<Livre>(this.apiURL + '/' + livre.reference, livre, httpOptions).pipe(catchError(this.handleError));
  }

  getUrgentLivres(page: number, size: number): Observable<LivresPage> {
    const href = `${this.apiURL}/commandes`;
    const requestUrl = `${href}?page=${page}&size=${size}`;
    return this.http.get<LivresPage>(requestUrl);
  }

  preparationCommande(reference: number): any {
    const href = `${this.apiURL}/prepare/${reference}`;
    return this.http.patch(href, httpOptions).pipe(catchError(this.handleError));
  }

  validateCommande(reference: number): any {
    const href = `${this.apiURL}/validate/${reference}`;
    return this.http.patch(href, httpOptions).pipe(catchError(this.handleError));
  }
}
