'use strict';

describe('Controller Tests', function() {

    describe('Administrator Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAdministrator, MockUser, MockIncidence;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAdministrator = jasmine.createSpy('MockAdministrator');
            MockUser = jasmine.createSpy('MockUser');
            MockIncidence = jasmine.createSpy('MockIncidence');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Administrator': MockAdministrator,
                'User': MockUser,
                'Incidence': MockIncidence
            };
            createController = function() {
                $injector.get('$controller')("AdministratorDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'volunteercrowdApp:administratorUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
