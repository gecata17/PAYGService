import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SignUpUser } from '../model/singup-model';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class SignupService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http:HttpClient) { }

  public createUser(user: SignUpUser) : Observable<SignUpUser>{
    const currentHeader = {headers: new HttpHeaders({'Content-Type' : 'application/json'})};
    return this.http.post<SignUpUser>(`${this.apiServerUrl}/private/api/auth/signup`,user,currentHeader)
  }
}
