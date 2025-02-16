import { Component } from '@angular/core';
import { SignUpUser } from 'src/app/model/singup-model';
import { SignupService } from 'src/app/service/signup.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  errorMessage = "";
  signUpUser?: SignUpUser;
  passwordsMatch = true;
  hidePassword: boolean = true;

  constructor(private readonly signupService : SignupService, private router:Router){}

  checkPasswordsMatch() : void{
    const passwordInput = document.getElementById("password") as HTMLInputElement;
    const confirmPasswordInput = document.getElementById("confirm-password") as HTMLInputElement;
    const password=passwordInput.value;
    const confirmPassword=confirmPasswordInput.value;
    this.passwordsMatch=password==confirmPassword;
  }

  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }
  
  public signup(username: string, email: string, password: string){

    if(!this.passwordsMatch){
      return;

    }
    const signUpData = {email:email, username:username, password:password};
    this.signupService.createUser(signUpData).subscribe({
      next: (response) => {
        if(response){
          console.log("Signup");
          this.signUpUser=response;
          this.router.navigateByUrl("/login");
        }
      },
      error: (err) => {
        if (err && err.error) {
          this.errorMessage=err.error;
        }
      }
    })
  }
}
