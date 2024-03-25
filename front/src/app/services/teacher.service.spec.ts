import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';

describe('TeacherService', () => {
  let service: TeacherService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ],
      providers: [TeacherService]
    });
    service = TestBed.inject(TeacherService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return two teachers', () => {
    const tearcher$ = service.all();
    tearcher$.subscribe(value => {
      expect(value.length).toEqual(2);
      expect(value[0].lastName).toEqual("DELAHAYE");
      expect(value[1].lastName).toEqual("THIERCELIN");
    }).unsubscribe();
  })

  it('should return Ms. THIERCELIN', () => {
    const tearcher$ = service.detail("2");
    tearcher$.subscribe(value => {
      expect(value.lastName).toEqual("THIERCELIN");
    }).unsubscribe();

  })


});
