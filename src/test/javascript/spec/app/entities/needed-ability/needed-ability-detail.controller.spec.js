'use strict';

describe('Controller Tests', function() {

    describe('NeededAbility Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockNeededAbility, MockNeed;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockNeededAbility = jasmine.createSpy('MockNeededAbility');
            MockNeed = jasmine.createSpy('MockNeed');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'NeededAbility': MockNeededAbility,
                'Need': MockNeed
            };
            createController = function() {
                $injector.get('$controller')("NeededAbilityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'volunteercrowdApp:neededAbilityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
