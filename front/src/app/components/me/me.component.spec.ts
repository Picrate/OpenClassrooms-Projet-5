import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import {expect} from '@jest/globals';
import { MeComponent } from './me.component';
import {UserService} from "../../services/user.service";
import {By} from "@angular/platform-browser";
import {of} from "rxjs";
import {User} from "../../interfaces/user.interface";
import {formatDate} from "@angular/common";
import spyOn = jest.spyOn;

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    }
  }

  let userService: UserService;

  const adminUserMock:User = {
    id: 1,
    admin: true,
    firstName: "firstname",
    lastName: "lastname",
    email: "user@user.com",
    password:"blank",
    createdAt: new Date("2024-01-01"),
    updatedAt: new Date("2024-01-02")
  }

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        UserService
      ]
    })
      .compileComponents();
    // UserServiceMock
    userService = TestBed.inject(UserService);
    userService.getById = jest.fn().mockReturnValue(of(adminUserMock));
    //
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.user).toBeDefined();
    expect(component.user).toEqual(adminUserMock);
  });

  it('should show adminUser infos in component', ()=> {
    const {debugElement} = fixture;
    const matCardElement = fixture.nativeElement.querySelector('mat-card-content');
    const divElement = matCardElement.querySelector('div');
    const nameContent = divElement.querySelector('p');
    expect(nameContent).toBeDefined();
    expect(nameContent.textContent).toContain(adminUserMock.firstName);
    expect(nameContent.textContent).toContain(adminUserMock.lastName.toUpperCase());
    const dateDivElement = divElement.querySelector('div');
    const createdDateElement = dateDivElement.querySelector('p');
    expect(createdDateElement.textContent).toContain(formatDate(adminUserMock.createdAt, 'longDate', 'EN_us'));
  });

  it('should show admin in DOM', () => {
    const { debugElement } = fixture;
    const p = debugElement.query(By.css('p.my2'));
    expect(p).toBeDefined();
  });

  it('should not show delete account button if admin', () => {
    const { debugElement } = fixture;
    const deleteButton = debugElement.query(By.css('div.my2>button'));
    expect(deleteButton).toBeNull();
  });

  it('should call userService.delete if not admin', () => {
    adminUserMock.admin = false;
    fixture.detectChanges();
    spyOn(userService, 'delete');
    const { debugElement } = fixture;
    const deleteButton = debugElement.query(By.css('div.my2>button'));
    expect(deleteButton).toBeDefined();
    deleteButton.nativeElement.click();
    fixture.detectChanges();
    expect(userService.delete).toHaveBeenCalledTimes(1);
    expect(userService.delete).toHaveBeenCalledWith("1");
  });

});
