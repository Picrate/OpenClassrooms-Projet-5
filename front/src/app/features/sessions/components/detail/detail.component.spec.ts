import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {RouterTestingModule,} from '@angular/router/testing';
import {expect} from '@jest/globals';
import {SessionService} from '../../../../services/session.service';

import {DetailComponent} from './detail.component';
import {SessionApiService} from "../../services/session-api.service";
import {By} from "@angular/platform-browser";
import {ActivatedRoute, convertToParamMap} from "@angular/router";
import {HttpClientModule} from "@angular/common/http";
import {of} from "rxjs";
import {Session} from "../../interfaces/session.interface";
import {SessionInformation} from "../../../../interfaces/sessionInformation.interface";


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  let service: SessionService;
  let apiService: SessionApiService;
  let activatedRoute: ActivatedRoute;

  let mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  let mockSession : Session = {
    id: 1,
    name: 'test',
    date: new Date('2024-01-03'),
    updatedAt: new Date('2024-01-02'),
    createdAt: new Date('2024-01-01'),
    description: "description",
    users: [2,3,4,5,6],
    teacher_id: 1
  };

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent],
      providers: [
        {provide: SessionService, useValue: mockSessionService},
        {provide: ActivatedRoute, useValue:{ snapshot: {paramMap: convertToParamMap({id: String(mockSessionService.sessionInformation.id)})}}},
        SessionApiService
      ],
    })
      .compileComponents();

    service = TestBed.inject(SessionService);
    apiService = TestBed.inject(SessionApiService);
    activatedRoute = TestBed.inject(ActivatedRoute);

    apiService.detail = jest.fn(() => {
      return of(mockSession);
    })

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  it('should create and get valid Session', () => {
    expect(component).toBeTruthy();
    expect(component.session).toBeDefined();
    expect(component.session).toEqual(mockSession);
  });

  test('should display Delete Button as Admin', () => {
    const {debugElement} = fixture;
    const deleteSpan = debugElement.query(By.css('mat-card-title span.ml1'));
    expect(deleteSpan).toBeDefined();
    expect(deleteSpan.nativeElement.textContent).toContain("Delete");
  });

  test('should delete session as admin', () => {
    jest.spyOn(apiService, 'delete');
    const {debugElement} = fixture;
    const deleteSpan = debugElement.query(By.css('mat-card-title span.ml1'));
    expect(deleteSpan).toBeDefined();
    expect(deleteSpan.nativeElement.textContent).toContain("Delete");
    const deleteButton = deleteSpan.parent;
    deleteButton?.nativeElement.click();
    fixture.detectChanges();
    expect(apiService.delete).toHaveBeenCalledTimes(1);
    expect(apiService.delete).toHaveBeenCalledWith('1');
  });

  test('should show participate if not subscribed to session as user', () => {
    component.isAdmin = false;
    fixture.detectChanges();
    const {debugElement} = fixture;
    const participateSpan = debugElement.query(By.css('mat-card-title span.ml1'));
    expect(participateSpan).toBeDefined();
    expect(participateSpan.nativeElement.textContent).toContain("Participate");
  });

  test('should show unParticipate after subscribing to session as user', () => {
    component.isAdmin = false;
    fixture.detectChanges();
    jest.spyOn(apiService, 'participate');
    jest.spyOn(apiService, 'detail');
    mockSession.users = [1,2,3,4,5,6];
    const {debugElement} = fixture;
    const participateButton = debugElement.query(By.css('mat-card-title span.ml1')).parent;
    expect(participateButton).toBeDefined();
    participateButton?.nativeElement.click();
    fixture.detectChanges();
    expect(apiService.participate).toHaveBeenCalledTimes(1);
    expect(apiService.participate).toHaveBeenCalledWith("1", "1");
    expect(apiService.detail).toHaveBeenCalledTimes(1);
    expect(apiService.detail).toHaveBeenCalledWith("1");
    fixture.detectChanges();
    // isParticipate = true
    expect(component.session?.users.some(u => u === service.sessionInformation!.id)).toBeTruthy();
    const unsubscribeSpan = debugElement.query(By.css('mat-card-title span.ml1'));
    //expect(unsubscribeSpan.nativeElement.textContent).toContain("Do not Participate");
  } );




});
