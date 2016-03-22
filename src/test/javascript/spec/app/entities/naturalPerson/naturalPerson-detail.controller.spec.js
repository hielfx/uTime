'use strict';

describe('Controller Tests', function() {

    describe('NaturalPerson Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockNaturalPerson, MockGender, MockAppUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockNaturalPerson = jasmine.createSpy('MockNaturalPerson');
            MockGender = jasmine.createSpy('MockGender');
            MockAppUser = jasmine.createSpy('MockAppUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'NaturalPerson': MockNaturalPerson,
                'Gender': MockGender,
                'AppUser': MockAppUser
            };
            createController = function() {
                $injector.get('$controller')("NaturalPersonDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'volunteercrowdApp:naturalPersonUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
