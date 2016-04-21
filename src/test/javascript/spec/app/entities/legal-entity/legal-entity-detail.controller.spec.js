'use strict';

describe('Controller Tests', function() {

    describe('LegalEntity Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLegalEntity, MockAppUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLegalEntity = jasmine.createSpy('MockLegalEntity');
            MockAppUser = jasmine.createSpy('MockAppUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LegalEntity': MockLegalEntity,
                'AppUser': MockAppUser
            };
            createController = function() {
                $injector.get('$controller')("LegalEntityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'volunteercrowdApp:legalEntityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
