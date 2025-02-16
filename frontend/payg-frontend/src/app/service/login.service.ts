import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http'
import { Observable } from 'rxjs';
import {
  LoginUser

} from '../model/login-model';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }


  public login(username: string, password: string): Observable<LoginUser> {

    const currentHeader = new HttpHeaders().set('Content-Type', 'application/json');
    const body = { username: username, password: password };
    return this.http.post<LoginUser>(`${this.apiServerUrl}/private/api/auth/login`, body, { headers: currentHeader });
  }
}
