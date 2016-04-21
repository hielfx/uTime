'use strict';

describe('Controller Tests', function() {

    describe('Incidence Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockIncidence, MockAdministrator, MockRequest, MockAppUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockIncidence = jasmine.createSpy('MockIncidence');
            MockAdministrator = jasmine.createSpy('MockAdministrator');
            MockRequest = jasmine.createSpy('MockRequest');
            MockAppUser = jasmine.createSpy('MockAppUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Incidence': MockIncidence,
                'Administrator': MockAdministrator,
                'Request': MockRequest,
                'AppUser': MockAppUser
            };
            createController = function() {
                $injector.get('$controller')("IncidenceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'volunteercrowdApp:incidenceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
