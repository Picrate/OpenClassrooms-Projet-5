import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import {AuthService} from "../../services/auth.service";
import {SessionInformation} from "../../../../interfaces/sessionInformation.interface";
import {of} from "rxjs";
import {By} from "@angular/platform-browser";
import {Router} from "@angular/router";
import {LoginRequest} from "../../interfaces/loginRequest.interface";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService, AuthService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  test('submits the form successfully', () => {
    let router = TestBed.inject(Router);
    let authService = TestBed.inject(AuthService);
    const spy = jest.spyOn(authService, 'login');

    const formData  = {
      email : "user@email.com",
      password: "password"
    }
    const {debugElement} = fixture;
    const form = debugElement.query(By.css('form'));
    let emailInput = debugElement.query(By.css('[formControlName="email"]'));
    let passwordInput = debugElement.query(By.css('[formControlName="password"]'));
    emailInput.nativeElement.value = formData.email;
    passwordInput.nativeElement.value =formData.password;
    emailInput.nativeElement.dispatchEvent(new Event('input'));
    passwordInput.nativeElement.dispatchEvent(new Event('input'));
    fixture.detectChanges();
    // Check submit button enabled
    const submitButton = form.query(By.css('button[type="submit"]'));
    const submitButtonProperties: any = submitButton.properties;
    expect(submitButtonProperties.disabled).toBeFalsy();
    // Send form
    form.triggerEventHandler('submit', {});
    expect(spy).toHaveBeenCalledWith(formData);
    expect(router.url).toEqual('/');
  })

  test('should show error for required field not met', () => {
    const formData  = {
      email : "user@email.com",
      password: ""
    }
    const {debugElement} = fixture;
    const form = debugElement.query(By.css('form'));
    let emailInput = debugElement.query(By.css('[formControlName="email"]'));
    let passwordInput = debugElement.query(By.css('[formControlName="password"]'));
    emailInput.nativeElement.value =
      passwordInput.nativeElement.value =formData.password;
    emailInput.nativeElement.dispatchEvent(new Event('input'));
    passwordInput.nativeElement.dispatchEvent(new Event('input'));
    fixture.detectChanges();
    // Test submit button disabled
    const submitButton = form.query(By.css('button[type="submit"]'));
    const submitButtonProperties: any = submitButton.properties;
    expect(submitButtonProperties.disabled).toBeTruthy();
  })





});
