import {Livre} from './Livre';
import {Client} from './Client';

export class Reservation {

  livres: Livre[];
  client: Client;
  reference: number;
  dateReservation: Date;
  dateRetrait: Date;
  enPreparation: boolean;
}
