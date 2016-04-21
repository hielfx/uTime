'use strict';

describe('Controller Tests', function() {

    describe('Availability Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAvailability, MockNeed;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAvailability = jasmine.createSpy('MockAvailability');
            MockNeed = jasmine.createSpy('MockNeed');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Availability': MockAvailability,
                'Need': MockNeed
            };
            createController = function() {
                $injector.get('$controller')("AvailabilityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'volunteercrowdApp:availabilityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
