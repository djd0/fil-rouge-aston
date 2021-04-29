import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AccueilComponent } from './components/accueil/accueil.component';
import { LivresComponent} from './components/livres/livres.component';
import { ReservationsComponent } from './components/reservations/reservations.component';


const routes: Routes = [

  { path: '', component: AccueilComponent },
  { path: 'livres', component: LivresComponent },
  { path: 'reservations', component: ReservationsComponent }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }
