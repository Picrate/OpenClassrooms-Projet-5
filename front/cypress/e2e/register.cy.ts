describe('Register spec', () => {
  it('Register successfull', () => {
    cy.visit('/register')
    cy.get('input[formControlName=firstName]').clear().type("register")
    cy.get('input[formControlName=lastName]').clear().type("user")
    cy.get('input[formControlName=email]').clear().type("user@email.com")
    cy.get('input[formControlName=password]').clear().type("test!1234")
    cy.get('button[type=submit]').click()
    // check registring
    cy.url().should('include', '/login')
  })

  it('should show user informations and delete user account', () => {
    cy.loginUI("user@email.com", "test!1234");
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
