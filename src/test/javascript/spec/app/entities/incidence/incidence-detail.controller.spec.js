'use strict';

describe('Controller Tests', function() {

    describe('Incidence Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockIncidence, MockAdministrator, MockRequest;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockIncidence = jasmine.createSpy('MockIncidence');
            MockAdministrator = jasmine.createSpy('MockAdministrator');
            MockRequest = jasmine.createSpy('MockRequest');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Incidence': MockIncidence,
                'Administrator': MockAdministrator,
                'Request': MockRequest
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
