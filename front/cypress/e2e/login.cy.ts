describe('Login spec', () => {
  it('Login successfull', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {fixture: 'adminuser.json'});
    cy.intercept('GET','http://localhost:8080/api/session',{fixture: 'sessions.json'});

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.url().should('include', '/sessions')
  })

  it('logout', () => {
    cy.intercept('POST', '/api/auth/login', {fixture: 'adminuser.json'});
    cy.visit('/login')
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.url().should('include', '/sessions')
    cy.get('mat-toolbar > div > span.link').last().click();
    cy.url().should('eq', 'http://localhost:4200/');
  })



});
