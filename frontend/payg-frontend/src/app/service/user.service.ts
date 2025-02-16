import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';


import { User } from '../model/user-model';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http:HttpClient) { }

  private apiServerUrl = environment.apiBaseUrl;


  getAllUsers(){
    const url = `${this.apiServerUrl}/private/api/users/all`;
    return this.http.get<User[]>(url);
  }

  getUserByUsername(username:string){
    const url = `${this.apiServerUrl}/private/api/users/${username}`;
    return this.http.get<User>(url);
  }

  deleteUser(user: User): Observable<User> {
    const url = `${this.apiServerUrl}/private/api/users/delete/${user.username}`;
    return this.http.delete<User>(url);
  }
  
  updateUser(user:User): Observable<User>{
    const url = `${this.apiServerUrl}/private/api/users/update/${user.username}`;

    return this.http.put<User>(url,user);
  }
}
