'use strict';

describe('NaturalPerson e2e test', function () {

    var username = element(by.id('username'));
    var password = element(by.id('password'));
    var entityMenu = element(by.id('entity-menu'));
    var accountMenu = element(by.id('account-menu'));
    var login = element(by.id('login'));
    var logout = element(by.id('logout'));

    beforeAll(function () {
        browser.get('/');
        browser.driver.wait(protractor.until.elementIsVisible(element(by.css('h1'))));

        accountMenu.click();
        login.click();

        username.sendKeys('admin');
        password.sendKeys('admin');
        element(by.css('button[type=submit]')).click();
    });

    it('should load NaturalPeople', function () {
        entityMenu.click();
        element(by.css('[ui-sref="natural-person"]')).click().then(function() {
            expect(element.all(by.css('h2')).first().getText()).toMatch(/NaturalPeople/);
        });
    });

    it('should load create NaturalPerson dialog', function () {
        element(by.css('[ui-sref="natural-person.new"]')).click().then(function() {
            expect(element(by.css('h4.modal-title')).getText()).toMatch(/Create or edit a NaturalPerson/);
            element(by.css('button.close')).click();
        });
    });

    afterAll(function () {
        accountMenu.click();
        logout.click();
    });
});
