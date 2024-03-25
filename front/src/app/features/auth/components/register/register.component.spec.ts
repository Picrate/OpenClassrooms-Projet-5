import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import {AuthService} from "../../services/auth.service";
import {of} from "rxjs";
import {By} from "@angular/platform-browser";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  let authServiceMock!: {register: jest.Mock};

  const formData = {
    firstName : "firstName",
    lastName : "lastName",
    email : "user@email.com",
    password : "password"
  };

  beforeEach(async () => {
    authServiceMock = {
      register : jest.fn(),
    };
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        {provide: AuthService, useValue: authServiceMock}
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    authServiceMock.register.mockReturnValue(of());

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  test('submits the form successfully', () => {
    const {debugElement} = fixture;
    const form = debugElement.query(By.css('form'));
    let firstnameInput = debugElement.query(By.css('[formControlName="firstName"]'));
    let lastnameInput = debugElement.query(By.css('[formControlName="lastName"]'));
    let emailInput = debugElement.query(By.css('[formControlName="email"]'));
    let passwordInput = debugElement.query(By.css('[formControlName="password"]'));
    firstnameInput.nativeElement.value = formData.firstName;
    lastnameInput.nativeElement.value = formData.lastName;
    emailInput.nativeElement.value = formData.email;
    passwordInput.nativeElement.value =formData.password;
    firstnameInput.nativeElement.dispatchEvent(new Event('input'));
    lastnameInput.nativeElement.dispatchEvent(new Event('input'));
    emailInput.nativeElement.dispatchEvent(new Event('input'));
    passwordInput.nativeElement.dispatchEvent(new Event('input'));
    form.triggerEventHandler('submit', {});
    fixture.detectChanges();
    expect(authServiceMock.register).toHaveBeenCalledWith(formData);
    // gérer la route retour URL

  })

});
