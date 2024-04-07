describe('Login spec', () => {
  it('should resirect to not found page', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {fixture: 'adminuser.json'});
    cy.intercept('GET','http://localhost:8080/api/session',{fixture: 'sessions.json'});

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.visit('/not-exists');
    cy.url().should('include', '404');
  })
});
