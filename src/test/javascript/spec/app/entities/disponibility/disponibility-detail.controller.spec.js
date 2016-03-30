'use strict';

describe('Controller Tests', function() {

    describe('Disponibility Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDisponibility, MockNeed;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDisponibility = jasmine.createSpy('MockDisponibility');
            MockNeed = jasmine.createSpy('MockNeed');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Disponibility': MockDisponibility,
                'Need': MockNeed
            };
            createController = function() {
                $injector.get('$controller')("DisponibilityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'volunteercrowdApp:disponibilityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
