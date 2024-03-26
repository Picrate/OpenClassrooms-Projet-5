import {Session} from "../../src/app/features/sessions/interfaces/session.interface";

describe('Account Information spec', () => {

  beforeEach(() => {
    cy.intercept('GET', '/api/session',{fixture:'sessions.json'})
  })

  it('should show account informations as admin', () => {

    cy.login(true);
    cy.intercept('GET', 'api/user/**',{fixture:'adminuser.json'});

    cy.get('mat-toolbar span[routerLink="me"]').click();
    cy.get('app-me mat-card-title h1').should('have.text', 'User information');
    cy.get('mat-card-content .my2').should('have.text', 'You are admin');
  });

  it('should show user informations and delete user account', () => {
    cy.login(false);
    cy.intercept('GET', 'api/user/**',{fixture:'simple_user.json'});
    cy.intercept('DELETE', 'api/user/**',{
      statusCode: 200,
      body: {message: 'User information was deleted successfully'}
    });
    cy.get('mat-toolbar span[routerLink="me"]').click();
    cy.get('app-me mat-card-title h1').should('have.text', 'User information');
    cy.get('mat-card-content .my2 > button').should('exist');
    cy.get('mat-card-content .my2 > button').click();
    //cy.wait(500);
    cy.get('.mat-simple-snack-bar-content').should('exist');
    cy.url().should('include', '/');
  })

});
