import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/service/login.service';
import { TokenStorageService } from 'src/app/service/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})


export class LoginComponent implements OnInit {
  isLoggedIn = false;
  success = false;
  errorMessage = "";
  hidePassword: boolean = true;

  constructor(private loginService: LoginService, private tokenStorage: TokenStorageService,private router:Router) { }

  ngOnInit(): void {
    this.isLoggedIn = false;
  }
  
  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }
  
  login(username: any, password: any) {
    this.loginService.login(username, password).subscribe({
      next: (response) => {
        if (response) {
          console.log(response);
          this.tokenStorage.saveUser(response.username);
          this.tokenStorage.saveToken(response.token);
          this.isLoggedIn = true;
          //TO DO: add navigation to home page when it is ready 
        }
      },
      error: (err) => {
        this.errorMessage = "Invalid credentials";
      }

    })

  }

}
