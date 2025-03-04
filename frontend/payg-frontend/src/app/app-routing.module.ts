import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/singup.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'signup'
  },
  { path: 'login', component: LoginComponent },
  { path: 'signup',component: SignupComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
