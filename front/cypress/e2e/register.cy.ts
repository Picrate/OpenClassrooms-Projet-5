describe('Register spec', () => {
  it('Register successfull', () => {
    cy.intercept('POST', '/api/auth/register', {fixture: 'simple_user.json'});
    cy.visit('/register')
    cy.get('input[formControlName=firstName]').clear().type("register")
    cy.get('input[formControlName=lastName]').clear().type("user")
    cy.get('input[formControlName=email]').clear().type("user@email.com")
    cy.get('input[formControlName=password]').clear().type("test!1234")
    cy.get('button[type=submit]').click()
    // check registring
    cy.url().should('include', '/login')
  })




});
