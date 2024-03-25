import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import {SessionInformation} from "../interfaces/sessionInformation.interface";

describe('SessionService', () => {
  let service: SessionService;

  const session: SessionInformation = {
    id: 1,
    lastName: "Admin",
    firstName: "Admin",
    admin:  true,
    username: "yoga@studio.com",
    type: "",
    token: ""
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SessionService]
    });
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should be logged in & logged out', () => {
    const islogged$ = service.$isLogged();
    service.logIn(session);
    islogged$.subscribe(value => {
      expect(value).toBeTruthy();
    }).unsubscribe();
    service.logOut();
    islogged$.subscribe(value => {
      expect(value).toBeFalsy();
    }).unsubscribe();
  })



});
