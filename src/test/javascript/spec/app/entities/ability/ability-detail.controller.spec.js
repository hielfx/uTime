'use strict';

describe('Controller Tests', function() {

    describe('Ability Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAbility, MockTag, MockAppUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAbility = jasmine.createSpy('MockAbility');
            MockTag = jasmine.createSpy('MockTag');
            MockAppUser = jasmine.createSpy('MockAppUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Ability': MockAbility,
                'Tag': MockTag,
                'AppUser': MockAppUser
            };
            createController = function() {
                $injector.get('$controller')("AbilityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'volunteercrowdApp:abilityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
