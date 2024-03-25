describe('Admin Session management spec', () => {

  const token:string  ='';

  before(() => {

  });

  it('should create session if admin', () => {

    cy.loginUI('yoga@studio.com', 'test!1234');
    // check create button exists
    cy.get('mat-card-header > button').should('exist')
    cy.get('mat-card-header > button').click()
    // check redirect after click
    cy.url().should('include', '/sessions/create');
    // check in create session form
    cy.get('mat-card-title h1').should('have.text', 'Create session');
    cy.get('input[formControlName="name"]').clear().type("New Session");
    cy.get('input[formControlName="date"]').clear().type("2024-01-01");
    cy.get('[formControlName="teacher_id"]').click();
    cy.get('#mat-option-0').click();
    cy.get('textarea[formControlName="description"]').clear().type("description");
    cy.get('button[type="submit"]').click();
    cy.url().should('include', '/sessions');
  });

  it('should show a list of session', () => {
    cy.loginUI('yoga@studio.com', 'test!1234');
    cy.get('mat-card.item').should('exist');
  });

  it('should show session details', () => {
    cy.loginUI('yoga@studio.com', 'test!1234');
    cy.get('mat-card.item').first().as('firstCard');
    cy.get('@firstCard').within(() => {
      cy.get('mat-card-actions button').first().click();
    });
    cy.url().should('include', '/detail');
  })

  it('should delete session as admin', () => {
    cy.loginUI('yoga@studio.com', 'test!1234');
    cy.get('div.items > mat-card').first().as('firstCard');
    cy.get('@firstCard').within(() => {
      cy.get('mat-card-actions button').first().click();
    });
    cy.url().should('include', '/detail');
    cy.get('mat-card').should('exist');
    cy.get('mat-card-title button span.ml1').should('have.text', 'Delete');
    cy.get('mat-card-title button span.ml1').parent().click();
    cy.wait(500);
    cy.get('.mat-simple-snack-bar-content').should('exist');
    cy.url().should('match', /^.*\/sessions$/);
  });

  it('should update session details', () => {
    cy.loginUI('yoga@studio.com', 'test!1234');
    cy.get('div.items > mat-card').first().as('firstCard');
    cy.get('@firstCard').within(() => {
      cy.get('mat-card-actions button').last().click();
    });
    cy.url().should('include', '/update');
    cy.get('form > div > button').click();
    cy.url().should('match', /^.*\/sessions$/);
  })







});
