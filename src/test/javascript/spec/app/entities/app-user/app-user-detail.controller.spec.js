'use strict';

describe('Controller Tests', function() {

    describe('AppUser Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAppUser, MockUser, MockNaturalPerson, MockLegalEntity, MockAbility, MockRequest, MockNeed, MockAssessment, MockPayment, MockIncidence;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAppUser = jasmine.createSpy('MockAppUser');
            MockUser = jasmine.createSpy('MockUser');
            MockNaturalPerson = jasmine.createSpy('MockNaturalPerson');
            MockLegalEntity = jasmine.createSpy('MockLegalEntity');
            MockAbility = jasmine.createSpy('MockAbility');
            MockRequest = jasmine.createSpy('MockRequest');
            MockNeed = jasmine.createSpy('MockNeed');
            MockAssessment = jasmine.createSpy('MockAssessment');
            MockPayment = jasmine.createSpy('MockPayment');
            MockIncidence = jasmine.createSpy('MockIncidence');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'AppUser': MockAppUser,
                'User': MockUser,
                'NaturalPerson': MockNaturalPerson,
                'LegalEntity': MockLegalEntity,
                'Ability': MockAbility,
                'Request': MockRequest,
                'Need': MockNeed,
                'Assessment': MockAssessment,
                'Payment': MockPayment,
                'Incidence': MockIncidence
            };
            createController = function() {
                $injector.get('$controller')("AppUserDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'volunteercrowdApp:appUserUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
