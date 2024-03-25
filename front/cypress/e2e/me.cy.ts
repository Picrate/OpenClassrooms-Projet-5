let token: string = '';

describe('Account Information spec', () => {

  before(() => {
    cy.register("user@studio.com", "test!1234");
  })


  it('should show account informations as admin', () => {
    cy.loginUI("yoga@studio.com","test!1234");
    cy.get('mat-toolbar span[routerLink="me"]').click();
    cy.get('app-me mat-card-title h1').should('have.text', 'User information');
    cy.get('mat-card-content .my2').should('have.text', 'You are admin');
  });

  it('should show user informations and delete user account', () => {

    cy.loginUI("user@studio.com", "test!1234");
    cy.get('mat-toolbar span[routerLink="me"]').click();
    cy.get('app-me mat-card-title h1').should('have.text', 'User information');
    cy.get('mat-card-content .my2 > button').should('exist');
    cy.get('mat-card-content .my2 > button').click();
    //cy.wait(500);
    cy.get('.mat-simple-snack-bar-content').should('exist');
    cy.url().should('include', '/');
    cy.loginUI('user@studio.com', 'test!1234');
    cy.get('p.error').should('exist');
  })

});
