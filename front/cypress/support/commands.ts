// ***********************************************
// This example namespace declaration will help
// with Intellisense and code completion in your
// IDE or Text Editor.
// ***********************************************
// declare namespace Cypress {
//   interface Chainable<Subject = any> {
//     customCommand(param: any): typeof customCommand;
//   }
// }
//
// function customCommand(param: any): void {
//   console.warn(param);
// }
//
// NOTE: You can use it like so:
// Cypress.Commands.add('customCommand', customCommand);
//
// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })
Cypress.Commands.add('login', () => {
    cy.request({
      method: 'POST',
      url: 'http://localhost:8080/api/auth/login',
      body: {
        email: "yoga@studio.com",
        password: "test!1234"
      }
    }).then((response) => {
      window.localStorage.setItem('Bearer', response.body.token);
    });
});

Cypress.Commands.add('loginUI', (email: string, password: string) => {
  cy.visit('/login')
  cy.get('input[formControlName=email]').type(email)
  cy.get('input[formControlName=password]').type(`${password}{enter}{enter}`)
});

Cypress.Commands.add('register', (email: string, password: string) => {
  cy.visit('/register')
  cy.get('input[formControlName=firstName]').clear().type("register")
  cy.get('input[formControlName=lastName]').clear().type("user")
  cy.get('input[formControlName=email]').clear().type(email)
  cy.get('input[formControlName=password]').clear().type(password)
  cy.get('button[type=submit]').click()
  // check registring
  cy.url().should('include', '/login')
});

Cypress.Commands.add('createUser', (email: string, password: string) => {
  cy.request({
    method: 'POST',
    url: 'http://localhost:8080/api/auth/register',
    body: {
      firstname: 'firstName',
      lastname: 'lastName',
      email: email,
      password: password
    }
  });
})
